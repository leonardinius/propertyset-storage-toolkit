/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.plugins.studio.storage.toolkit.provided;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.util.dbc.Assertions;
import com.atlassian.jira.util.ofbiz.GenericValueUtils;
import com.atlassian.plugins.studio.storage.toolkit.InstanceId;
import com.atlassian.plugins.studio.storage.toolkit.Scope;
import com.atlassian.plugins.studio.storage.toolkit.StorageException;
import com.atlassian.plugins.studio.storage.toolkit.impl.AbstractDefaultScopeImpl;
import com.atlassian.plugins.studio.storage.toolkit.impl.DefaultScopeOperationsImpl;
import com.atlassian.plugins.studio.storage.toolkit.impl.ScopeOperations;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import org.ofbiz.core.entity.GenericValue;
import webwork.action.Action;

import javax.annotation.Nullable;

/**
 * User: leonidmaslov
 * Date: 12/10/10
 * Time: 12:39 AM
 */
public class Scopes {
    private static final DefaultScopeOperationsImpl DEFAULT_OPERATIONS_IMPL_BRIDGE = new DefaultScopeOperationsImpl();
    private static final long CONSTANT_ENTITY_ID = 2L;
    private static final long ACTION_ENTITY_ID = 3L;

    private Scopes() {
    }

    private static <U> Scope<U> newDefaultScope(final Function<U, InstanceId> instanceIdProvider, final Function<
            ScopeOperations, Void> deleteAllFunctions, final ScopeOperations bridge) {
        return new AbstractDefaultScopeImpl<U>(bridge) {
            @Override
            protected InstanceId getInstanceId(U context) {
                return instanceIdProvider.apply(context);
            }

            @Override
            public void removeAll() throws StorageException {
                deleteAllFunctions.apply(bridge);
            }
        };
    }

    private static <U> Scope<U> newDefaultScope(final Function<U, InstanceId> instanceIdProvider, final Function<
            ScopeOperations, Void> deleteAllFunctions) {
        return newDefaultScope(instanceIdProvider, deleteAllFunctions, DEFAULT_OPERATIONS_IMPL_BRIDGE);
    }

    public static class Builder<T> {
        Function<T, InstanceId> instanceIdProvider;
        Function<ScopeOperations, Void> deleteAllFunctions;

        public Builder(Function<T, InstanceId> instanceIdProvider, Function<ScopeOperations, Void> deleteAllFunctions) {
            this.instanceIdProvider = instanceIdProvider;
            this.deleteAllFunctions = deleteAllFunctions;
        }

        public Builder() {
        }


        public Builder gimmeId(Function<T, InstanceId> instanceIdProvider) {
            this.instanceIdProvider = instanceIdProvider;
            return this;
        }

        public Builder deleteAll(Function<ScopeOperations, Void> deleteAllFunctions) {
            this.deleteAllFunctions = deleteAllFunctions;
            return this;
        }

        public <U> Builder<U> as(final Function<U, T> transformer) {
            Function<U, InstanceId> newIdProvider = new Function<U, InstanceId>() {
                Function<T, InstanceId> oldIdProvider = Builder.this.instanceIdProvider;

                public InstanceId apply(@Nullable U from) {
                    return oldIdProvider.apply(transformer.apply(from));
                }
            };

            return new Builder<U>(newIdProvider, this.deleteAllFunctions);
        }

        public Scope<T> build() {
            Assertions.notNull("InstanceId provider is not specified", instanceIdProvider);
            Assertions.notNull("DeleteALL functionality is not provided", deleteAllFunctions);
            return newDefaultScope(instanceIdProvider, deleteAllFunctions);
        }
    }


    public static Scope<Project> projectScope(Project project) {
        Assertions.notNull("Project scope parameter is null", project);

        String entityName = "FacadeStorageProject";
        String keyPrefix = "project-";
        return new Builder<Long>(new LongIdEntity(entityName, keyPrefix), new DeleteByNamePrefix(entityName, keyPrefix))
                .as(new Function<Project, Long>() {
                    public Long apply(@Nullable Project from) {
                        return from.getId();
                    }
                }).build();
    }


    public static Scope<Issue> issueScope(Issue issue) {
        Assertions.notNull("Issue scope parameter is null", issue);

        String entityName = "FacadeStorageIssue";
        String keyPrefix = "issue-";
        return new Builder<Long>(new LongIdEntity(entityName, keyPrefix), new DeleteByNamePrefix(entityName, keyPrefix))
                .as(new Function<Issue, Long>() {
                    public Long apply(@Nullable Issue from) {
                        return from.getId();
                    }
                }).build();
    }


    public static Scope<GenericValue> gvScope(GenericValue gv) {
        Assertions.notNull("Gv scope parameter is null", gv);

        String entityName = "FacadeStorageGv";
        String keyPrefix = "gv-";
        return new Builder<GenericValue>(new GenericValueIdEntity(entityName, keyPrefix), new DeleteByNamePrefix(entityName, keyPrefix))
                .build();
    }

    public static Scope<String> constantName(String name) {
        Assertions.notNull("Constant name parameter is null", name);

        String entityName = "FacadeStorageConstant-" + name;
        String keyPrefix = "constant-";
        Long entryId = CONSTANT_ENTITY_ID;
        return new Builder<String>(new ConstantIdEntity<String>(entryId, entityName, keyPrefix),
                new DeleteByConstantId(entryId, entityName, keyPrefix))
                .build();
    }

    private static <T extends Action> Builder<String> makeActionBuilder(Class<T> actionClass) {
        String entityName = "FacadeStorageAction-" + actionClass.getName();
        String keyPrefix = "class-";
        Long entryId = ACTION_ENTITY_ID;
        return new Builder<String>(new Scopes.ConstantIdEntity<String>(entryId, entityName, keyPrefix),
                new DeleteByConstantId(entryId, entityName, keyPrefix));
    }

    public static <T extends Action> Scope<Class<T>> actionConfiguration(Class<T> actionClass) {
        Assertions.notNull("Action class parameter is null", actionClass);

        return makeActionBuilder(actionClass)
                .as(new NoOpFun<Class<T>, String>())
                .build();
    }

    public static Scope<Action> actionConfiguration(Action action) {
        Assertions.notNull("Action parameter is null", action);

        return makeActionBuilder(action.getClass())
                .as(new NoOpFun<Action, String>())
                .build();
    }

    public static class NoOpFun<F, T> implements Function<F, T> {

        public T apply(@Nullable F from) {
            return null;
        }
    }

    public static class ConstantIdEntity<U> implements Function<U, InstanceId> {
        private final Long entryId;

        private final String entityName;

        private final String keyPrefix;

        public ConstantIdEntity(Long entryId, String entityName, String keyPrefix) {
            this.entryId = entryId;
            this.entityName = entityName;
            this.keyPrefix = keyPrefix;
        }

        public InstanceId apply(@Nullable U from) {
            return new InstanceId(keyPrefix, entityName, entryId);
        }
    }

    public static class LongIdEntity implements Function<Long, InstanceId> {
        private final String entityName;

        private final String keyPrefix;

        public LongIdEntity(String name, String prefix) {
            this.entityName = name;
            this.keyPrefix = prefix;
        }

        public InstanceId apply(@Nullable Long from) {
            return new InstanceId(keyPrefix, entityName, from);
        }

    }

    public static class GenericValueIdEntity implements Function<GenericValue, InstanceId> {
        private final String entityName;

        private final String keyPrefix;

        public GenericValueIdEntity(String name, String prefix) {
            this.entityName = name;
            this.keyPrefix = prefix;
        }

        public InstanceId apply(@Nullable GenericValue from) {
            return new InstanceId(keyPrefix, entityName, GenericValueUtils.transformToLongIds(ImmutableList.of(from))[0]);
        }

    }

    public static class DeleteByNamePrefix implements Function<ScopeOperations, Void> {
        private final String entityName;

        private final String keyPrefix;

        public DeleteByNamePrefix(String name, String prefix) {
            this.entityName = name;
            this.keyPrefix = prefix;
        }

        public Void apply(@Nullable ScopeOperations bridge) {
            bridge.removeByFilter(null, entityName, keyPrefix);
            return null;
        }

    }

    public static class DeleteByConstantId implements Function<ScopeOperations, Void> {
        private final Long entryId;

        private final String entityName;

        private final String keyPrefix;

        public DeleteByConstantId(Long entryId, String entityName, String keyPrefix) {
            this.entryId = entryId;
            this.entityName = entityName;
            this.keyPrefix = keyPrefix;
        }

        public Void apply(@Nullable ScopeOperations bridge) {
            bridge.removeByFilter(entryId, entityName, keyPrefix);
            return null;
        }
    }
}

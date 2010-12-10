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

package com.atlassian.plugins.studio.storage.examples.storage.toolkit;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import org.ofbiz.core.entity.GenericValue;
import webwork.action.Action;

/**
 * User: leonidmaslov
 * Date: 12/10/10
 * Time: 4:16 AM
 */
public class Facades {
    private Facades() {
    }

    public static <U> StorageFacade storage(Scope<U> scope, U context) throws StorageException {
        return scope.load(context);
    }


    public static StorageFacade projectStorage(Project project) throws StorageException {
        return storage(Scopes.projectScope(project), project);
    }


    public static StorageFacade issueStorage(Issue issue) throws StorageException {
        return storage(Scopes.issueScope(issue), issue);
    }


    public static StorageFacade gvStorage(GenericValue gv) throws StorageException {
        return storage(Scopes.gvScope(gv), gv);
    }


    public static StorageFacade constantNameStorage(String name) throws StorageException {
        return storage(Scopes.constantName(name), name);
    }


    public static StorageFacade actionStorage(Action action) throws StorageException {
        return storage(Scopes.actionConfiguration(action), action);
    }

    public static <T extends Action> StorageFacade actionStorage(Class<T> actionClazz) throws StorageException {
        return storage(Scopes.actionConfiguration(actionClazz), actionClazz);
    }


}

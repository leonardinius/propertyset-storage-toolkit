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

package com.atlassian.plugins.studio.storage.toolkit.impl;

import com.atlassian.plugins.studio.storage.toolkit.InstanceId;
import com.atlassian.plugins.studio.storage.toolkit.StorageException;
import com.google.common.collect.ImmutableMap;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import org.apache.commons.lang.StringUtils;
import org.ofbiz.core.entity.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: leonidmaslov
 * Date: 12/9/10
 * Time: 12:28 AM
 */
public class DefaultScopeOperationsImpl implements ScopeOperations {
    private static final String STORAGE_ENTITY = "OSPropertyEntry";
    private static final String STORAGE_IMPL = "ofbiz";
    private static final String DELEGATOR_NAME = "default";


    /**
     * Provides underlying PropertySet to delegate actual work to
     *
     * @param instanceId scope instanceId
     * @return loads delegate
     * @throws com.atlassian.plugins.studio.storage.toolkit.StorageException
     *
     */
    public PropertySet loadDelegate(InstanceId instanceId) throws StorageException {
        return loadPropertySet(instanceId.getEntityName(), instanceId.getEntityId());
    }

    private PropertySet loadPropertySet(String entityName, Long entityId) {
        final Map<String, Object> props = ImmutableMap.<String, Object>of(
                "delegator.name", DELEGATOR_NAME,
                "entityName", entityName,
                "entityId", entityId);
        return PropertySetManager.getInstance(STORAGE_IMPL, props);
    }

    public void remove(PropertySet underlyingStorage) throws StorageException {
        try {

            @SuppressWarnings({"unchecked"})
            Collection<String> allKeys = underlyingStorage.getKeys("");
            if (allKeys != null) {
                for (String key : allKeys) {
                    underlyingStorage.remove(key);
                }
            }

        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void removeByFilter(Long entityId, String entityName, String keyPrefix) throws StorageException {

        try {
            final List<EntityExpr> likeExpressions = new LinkedList<EntityExpr>();

            if (entityId != null) {
                likeExpressions.add(new EntityExpr("entityId", EntityOperator.EQUALS, entityId));
            }

            if (StringUtils.isNotBlank(entityName)) {
                likeExpressions.add(new EntityExpr("entityName", EntityOperator.EQUALS, entityName));
            }

            if (StringUtils.isNotBlank(keyPrefix)) {
                likeExpressions.add(new EntityExpr("propertyKey", EntityOperator.LIKE, keyPrefix + "%"));
            }

            final EntityConditionList ecl = new EntityConditionList(likeExpressions, EntityOperator.AND);

            @SuppressWarnings({"unchecked"})
            List<GenericValue> entries = getGenericDelegator().findByCondition(STORAGE_ENTITY, ecl, null, null);

            if (entries != null) {
                for (GenericValue gv : entries) {
                    //getGenericDelegator().removeValue(gv);
                    if (gv != null) {

                        gv.remove();
                    }
                }
            }

        } catch (final GenericEntityException e) {
            throw new StorageException(e);
        }
    }

    public String serialize(Object instance) throws StorageException {
        if (instance == null) {
            return null;
        }

        try {
            return makeXstream().toXML(instance);
        } catch (XStreamException e) {
            throw new StorageException(e);
        }
    }

    public Object deserialize(String input) throws StorageException {
        if (input == null) {
            return null;
        }

        try {
            return makeXstream().fromXML(input);
        } catch (XStreamException e) {
            throw new StorageException(e);
        }
    }

    @SuppressWarnings({"WeakerAccess"})
    protected XStream makeXstream() {
        return new XStream();
    }

    private GenericDelegator getGenericDelegator() {
        return GenericDelegator.getGenericDelegator(DELEGATOR_NAME);
    }

}



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

import com.atlassian.plugins.studio.storage.toolkit.ScopeDescriptor;
import com.atlassian.plugins.studio.storage.toolkit.StorageException;
import com.google.common.collect.ImmutableMap;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.module.propertyset.PropertySetManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import org.ofbiz.core.entity.GenericDelegator;

import java.util.Collection;
import java.util.Map;

/**
 * User: leonidmaslov
 * Date: 12/9/10
 * Time: 12:28 AM
 */
public class DefaultScopeOperationsImpl implements ScopeOperations {
    private final String implementationName;

    public DefaultScopeOperationsImpl(String implementationName) {
        this.implementationName = implementationName;
    }

    public DefaultScopeOperationsImpl() {
        this("ofbiz");
    }

    /**
     * Provides underlying PropertySet to delegate actual work to
     *
     * @param descriptor scope descriptor
     * @return
     * @throws com.atlassian.plugins.studio.storage.toolkit.StorageException
     *
     */
    public PropertySet loadDelegate(ScopeDescriptor descriptor) throws StorageException {
        final Map<String, Object> props = ImmutableMap.<String, Object>of(
                "delegator.name", implementationName,
                "entityName", descriptor.getEntityName(),
                "entityId", descriptor.getEntityId());
        return PropertySetManager.getInstance(implementationName, props);
    }

    public void remove(PropertySet delegate) throws StorageException {
        @SuppressWarnings({"unchecked"})
        Collection<String> allKeys = delegate.getKeys("");
        if (allKeys != null) {
            for (String key : allKeys) {
                try {
                    delegate.remove(key);
                } catch (PropertyException e) {
                    throw new StorageException(e);
                }
            }
        }
    }

    public void removeAll(ScopeDescriptor descriptor) throws StorageException {
        remove(loadDelegate(descriptor));
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

    protected XStream makeXstream() {
        return new XStream();
    }

    private GenericDelegator getGenericDelegator() {
        return GenericDelegator.getGenericDelegator(implementationName);
    }

}

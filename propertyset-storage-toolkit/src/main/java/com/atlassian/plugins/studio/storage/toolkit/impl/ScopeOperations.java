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

import com.atlassian.plugins.studio.storage.toolkit.Scope;
import com.atlassian.plugins.studio.storage.toolkit.ScopeDescriptor;
import com.atlassian.plugins.studio.storage.toolkit.StorageException;
import com.opensymphony.module.propertyset.PropertySet;

/**
 * Scope Bridge
 * <p/>
 * User: leonidmaslov
 * Date: 12/8/10
 * Time: 3:10 AM
 */
public interface ScopeOperations<T, S extends Scope<T>> {
    /**
     * Provides underlying PropertySet to delegate actual work to
     *
     * @param descriptor scope descriptor
     * @return
     * @throws StorageException
     */
    PropertySet loadDelegate(ScopeDescriptor descriptor) throws StorageException;

    void remove(PropertySet delegate) throws StorageException;

    void removeAll(ScopeDescriptor descriptor) throws StorageException;

    String serialize(Object instance) throws StorageException;

    Object deserialize(String input) throws StorageException;
}

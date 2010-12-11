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
import com.atlassian.plugins.studio.storage.toolkit.Scope;
import com.atlassian.plugins.studio.storage.toolkit.StorageException;
import com.atlassian.plugins.studio.storage.toolkit.StorageFacade;

/**
 * User: leonidmaslov
 * Date: 12/9/10
 * Time: 11:42 PM
 */
public abstract class AbstractDefaultScopeImpl<U> implements Scope<U>
{
    private final ScopeOperations bridge;

    public AbstractDefaultScopeImpl(ScopeOperations bridge)
    {
        this.bridge = bridge;
    }

    public abstract InstanceId getInstanceId(U context);


    /**
     * Provides underlying PropertySet to delegate actual work to
     *
     * @param context scope context like Issue, Project, User
     * @return storage facade
     * @throws com.atlassian.plugins.studio.storage.toolkit.StorageException
     *
     */
    public StorageFacade load(U context) throws StorageException
    {
        InstanceId instanceId = getInstanceId(context);
        return new DefaultStorageFacadeImpl(instanceId, bridge, bridge.loadDelegate(instanceId));
    }


    public void remove(U context) throws StorageException
    {
        bridge.remove(bridge.loadDelegate(getInstanceId(context)));
    }

    /**
     * removes    all the scope instances - like all Project configurations
     * <p/>
     * Note: please use with caution
     *
     * @throws StorageException
     */
    public abstract void removeAll() throws StorageException;

}

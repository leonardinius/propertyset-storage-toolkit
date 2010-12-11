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
import com.atlassian.plugins.studio.storage.toolkit.StorageFacade;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.opensymphony.module.propertyset.PropertyException;
import com.opensymphony.module.propertyset.PropertySet;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: leonidmaslov
 * Date: 12/9/10
 * Time: 1:32 AM
 */
public class DefaultStorageFacadeImpl implements StorageFacade
{
    private final InstanceId instanceId;
    private final ScopeOperations bridge;
    private final PropertySet delegate;

    public DefaultStorageFacadeImpl(InstanceId instanceId, ScopeOperations bridge, PropertySet delegate)
    {
        this.instanceId = instanceId;
        this.bridge = bridge;
        this.delegate = delegate;
    }


    private String getKey(String key)
    {
        return new StringBuilder()
                .append(StringUtils.defaultIfEmpty(instanceId.getKeyPrefix(), ""))
                .append(key).toString();
    }


    public void setBoolean(String key, boolean value) throws StorageException
    {
        try {
            delegate.setBoolean(getKey(key), value);
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public boolean getBoolean(String key) throws StorageException
    {
        try {
            return delegate.getBoolean(getKey(key));
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setData(String key, byte[] bytes) throws StorageException
    {
        try {
            delegate.setData(getKey(key), bytes);
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public byte[] getData(String key) throws StorageException
    {
        try {
            return delegate.getData(getKey(key));
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setDate(String key, Date date) throws StorageException
    {
        try {
            delegate.setDate(getKey(key), date);
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public Date getDate(String key) throws StorageException
    {
        try {
            return delegate.getDate(getKey(key));
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setDouble(String key, BigDecimal value) throws StorageException
    {
        try {
            String entityKey = getKey(key);
            if (value != null) {
                delegate.setString(entityKey, value.toString());
            } else if (value == null && delegate.exists(entityKey)) {
                delegate.remove(entityKey);
            }
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public BigDecimal getDouble(String key) throws StorageException
    {
        try {
            String value = delegate.getString(getKey(key));

            if (value != null) {
                return new BigDecimal(value);
            }

            return null;
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public Collection<String> getKeys() throws StorageException
    {
        return cleanKeys(delegate.getKeys());
    }

    private Collection<String> cleanKeys(Collection<String> keys)
    {
        if (keys == null) return keys;

        return ImmutableList.copyOf(Iterables.transform(keys, new Function<String, String>()
        {
            public String apply(String from)
            {
                return StringUtils.removeStart(from, instanceId.getKeyPrefix());
            }
        }));
    }

    @SuppressWarnings({"unchecked"})
    public Collection<String> getKeys(String keyPrefix) throws StorageException
    {
        return cleanKeys(delegate.getKeys(keyPrefix));
    }

    public void setLong(String key, Long value) throws StorageException
    {
        try {
            String entityKey = getKey(key);
            if (value != null) {
                delegate.setLong(entityKey, value);
            } else if (value == null && delegate.exists(entityKey)) {
                delegate.remove(entityKey);
            }
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public Long getLong(String key) throws StorageException
    {
        try {
            String entryKey = getKey(key);
            if (delegate.exists(entryKey)) {
                return delegate.getLong(entryKey);
            }

            return null;
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setObject(String key, Object objectValue) throws StorageException
    {
        try {

            String entryKey = getKey(key);

            if (objectValue != null)
                delegate.setText(entryKey, bridge.serialize(objectValue));
            else
                delegate.remove(entryKey);

        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public Object getObject(String key) throws StorageException
    {
        try {

            String text = delegate.getText(getKey(key));

            if (text == null) {
                return null;
            }

            return bridge.deserialize(text);


        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setProperties(String key, Properties properties) throws StorageException
    {
        try {

            String entryKey = getKey(key);

            if (properties != null)
                //noinspection unchecked
                delegate.setText(entryKey, bridge.serialize(new HashMap(properties)));
            else
                delegate.remove(entryKey);

        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public Properties getProperties(String key) throws StorageException
    {
        try {

            String text = delegate.getText(getKey(key));

            if (text == null) {
                return null;
            }

            @SuppressWarnings({"unchecked"})
            Map<Object, Object> map = (Map) bridge.deserialize(text);

            Properties properties = new Properties();
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }

            return properties;

        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setString(String key, String string) throws StorageException
    {
        try {
            delegate.setString(getKey(key), string);
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public String getString(String key) throws StorageException
    {
        try {
            return delegate.getString(getKey(key));
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void setText(String key, String text) throws StorageException
    {
        try {
            delegate.setText(getKey(key), text);
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public String getText(String key) throws StorageException
    {
        try {
            return delegate.getText(getKey(key));
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public boolean exists(String key) throws StorageException
    {
        try {
            return delegate.exists(getKey(key));
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public boolean remove(String key) throws StorageException
    {
        try {
            String entryKey = getKey(key);
            if (delegate.exists(entryKey)) {
                delegate.remove(entryKey);
                return true;
            }
            return false;
        } catch (PropertyException e) {
            throw new StorageException(e);
        }
    }

    public void removeAll() throws StorageException
    {
        bridge.remove(delegate);
    }
}

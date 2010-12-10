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

package com.atlassian.plugins.studio.storage.toolkit;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

/**
 * The intention is to provide transparent and clear storage facade behind the
 * <code>opensymhony com.opensymphony.module.propertyset.PropertySet</code>
 * <p/>
 * User: leonidmaslov
 * Date: 12/8/10
 * Time: 2:36 AM
 */
public interface StorageFacade<T extends Scope> {

    void setBoolean(String key, boolean value) throws StorageException;

    boolean getBoolean(String key) throws StorageException;


    void setData(String key, byte[] bytes) throws StorageException;

    byte[] getData(String key) throws StorageException;


    void setDate(String key, Date date) throws StorageException;

    Date getDate(String key) throws StorageException;


    void setDouble(String key, BigDecimal value) throws StorageException;

    BigDecimal getDouble(String key) throws StorageException;


    Collection<String> getKeys() throws StorageException;

    Collection<String> getKeys(String keyPrefix) throws StorageException;


    void setLong(String key, Long value) throws StorageException;

    Long getLong(String key) throws StorageException;


    void setObject(String key, java.lang.Object objectValue) throws StorageException;

    java.lang.Object getObject(String key) throws StorageException;


    void setProperties(String key, Properties properties) throws StorageException;

    Properties getProperties(String key) throws StorageException;


    void setString(String key, String string) throws StorageException;

    java.lang.String getString(String key) throws StorageException;


    void setText(String key, String text) throws StorageException;

    java.lang.String getText(String key) throws StorageException;


    boolean exists(String key) throws StorageException;

    boolean remove(String key) throws StorageException;

    void removeAll() throws StorageException;

}

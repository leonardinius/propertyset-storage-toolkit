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

package com.atlassian.plugins.studio.storage.toolkit.test;

import com.atlassian.plugins.studio.storage.toolkit.StorageFacade;
import com.atlassian.plugins.studio.storage.toolkit.StorageService;
import com.atlassian.plugins.studio.storage.toolkit.impl.DefaultStorageServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

import static com.atlassian.jira.util.dbc.Assertions.not;
import static com.atlassian.jira.util.dbc.Assertions.stateTrue;

/**
 * User: leonidmaslov
 * Date: 12/11/10
 * Time: 11:17 AM
 */
class TestSuite
{
    private final StorageService storageService = new DefaultStorageServiceImpl();

    @ToolkitTest
    protected void shouldAlwaysBeSuccess() throws Exception
    {
        stateTrue("true is always true", true);
    }


    @ToolkitTest
    protected void emptyConstantScopeShouldNeverReturnAnyData() throws Exception
    {
        String name = getClass().getName() + "-constantTest-shouldNeverReturnData";

        StorageFacade facade = storageService.constantNameStorage(name);

        stateTrue("First string access should be null", facade.getString("string") == null);

        stateTrue("First boolean access should be false", !facade.getBoolean("boolean"));

        stateTrue("First data access should be null", facade.getData("data") == null);

        stateTrue("First date access should be null", facade.getDate("date") == null);

        stateTrue("First double access should be null", facade.getDouble("double") == null);

        stateTrue("First long access should be null", facade.getLong("long") == null);

        stateTrue("First text access should be null", facade.getText("text") == null);

        stateTrue("First properties access should be null", facade.getProperties("properties") == null);

        stateTrue("First object access should be null", facade.getObject("object") == null);

        stateTrue("Expecting keys: [], got: " + facade.getKeys(), ImmutableSet.copyOf(facade.getKeys()).equals(ImmutableSet.of()));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForString() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("string");

        stateTrue("First string access should be null", facade.getString("string") == null);
        facade.setString("string", "stringData");
        stateTrue("string key should exist now", facade.exists("string"));
        stateTrue("[string]=stringData", facade.getString("string").equals("stringData"));
        stateTrue("delete string should work", facade.remove("string"));
        not("string key should NOT exist now", facade.exists("string"));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForText() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("text");

        stateTrue("First text access should be null", facade.getText("text") == null);
        facade.setText("text", "textData");
        stateTrue("text key should exist now", facade.exists("text"));
        stateTrue("[text]=textData", facade.getText("text").equals("textData"));
        stateTrue("delete text should work", facade.remove("text"));
        not("text key should NOT exist now", facade.exists("text"));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForBoolean() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("boolean");

        stateTrue("First boolean access should be false", !facade.getBoolean("boolean"));
        facade.setBoolean("boolean", true);
        stateTrue("boolean key should exist now", facade.exists("boolean"));
        stateTrue("[boolean]=true", facade.getBoolean("boolean"));
        stateTrue("delete boolean should work", facade.remove("boolean"));
        not("boolean key should NOT exist now", facade.exists("boolean"));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForDate() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("date");

        stateTrue("First boolean access should be null", facade.getDate("date") == null);
        Calendar c = Calendar.getInstance();
        c.set(2010, 1, 2, 3, 4, 5);
        c.set(Calendar.MILLISECOND, 0);
        facade.setDate("date", c.getTime());
        stateTrue("date key should exist now", facade.exists("date"));
        Calendar c2 = Calendar.getInstance();
        c2.setTime(facade.getDate("date"));
        stateTrue("[date]=" + c.getTime() + ", actual=" + c2.getTime(), c2.getTime().equals(c.getTime()));
        stateTrue("delete date should work", facade.remove("date"));
        not("date key should NOT exist now", facade.exists("date"));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForDouble() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("double");

        stateTrue("First double access should be null", facade.getDouble("double") == null);
        facade.setDouble("double", new BigDecimal("1.05"));
        stateTrue("double key should exist now", facade.exists("double"));
        stateTrue("[double]=1.05", facade.getDouble("double").equals(new BigDecimal("1.05")));
        facade.setDouble("double", null);
        not("double key should NOT exist now", facade.exists("double"));

        facade.setDouble("double", new BigDecimal("1.05"));
        stateTrue("double key should exist now", facade.exists("double"));
        stateTrue("delete double should work", facade.remove("double"));
        not("double key should NOT exist now", facade.exists("double"));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForLong() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("long");

        stateTrue("First long access should be null", facade.getLong("long") == null);
        facade.setLong("long", 5L);
        stateTrue("long key should exist now", facade.exists("long"));
        //noinspection UnnecessaryUnboxing
        stateTrue("[long]=5", facade.getLong("long").longValue() == 5L);
        facade.setLong("long", null);
        not("long key should NOT exist now", facade.exists("long"));

        facade.setLong("long", 7L);
        stateTrue("long key should exist now", facade.exists("long"));
        stateTrue("delete long should work", facade.remove("long"));
        not("long key should NOT exist now", facade.exists("long"));
    }

    private static class MySerializable implements Serializable
    {
        private String text;
        private Long number;
        private Map<String, String> mapping;

        public MySerializable(long number, String text, Map<String, String> mapping)
        {
            this.number = number;
            this.mapping = mapping;
            this.text = text;
        }

        public String getText()
        {
            return text;
        }

        public void setText(String text)
        {
            this.text = text;
        }

        public Long getNumber()
        {
            return number;
        }

        public void setNumber(Long number)
        {
            this.number = number;
        }

        public Map<String, String> getMapping()
        {
            return mapping;
        }

        public void setMapping(Map<String, String> mapping)
        {
            this.mapping = mapping;
        }

        @Override
        public int hashCode()
        {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object o)
        {
            return EqualsBuilder.reflectionEquals(this, o);
        }

        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }


    @ToolkitTest
    protected void constantScopeSaveDeleteForObject() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("object");

        MySerializable object = new MySerializable(10L, "data data", Maps.<String, String>newHashMap(ImmutableMap.of("1", "data1", "sdsd",
                "data2")));
        stateTrue("First object access should be null", facade.getObject("object") == null);
        facade.setObject("object", object);
        stateTrue("object key should exist now", facade.exists("object"));
        stateTrue("" + object + "=" + facade.getObject("object"), facade.getObject("object").equals(object));
        facade.setObject("object", null);
        not("object key should NOT exist now", facade.exists("object"));

        facade.setObject("object", object);
        stateTrue("long key should exist now", facade.exists("object"));
        stateTrue("delete object should work", facade.remove("object"));
        not("long key should NOT exist now", facade.exists("object"));
    }

    @ToolkitTest
    protected void constantScopeSaveDeleteForProperties() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("properties");

        Properties properties = new Properties();
        properties.setProperty("this.is.on", "on");
        properties.setProperty("this.is.off", "off");

        stateTrue("First properties access should be null", facade.getProperties("properties") == null);
        facade.setProperties("properties", properties);
        stateTrue("properties key should exist now", facade.exists("properties"));
        stateTrue("[properties]=on and off", facade.getProperties("properties").equals(properties));
        facade.setProperties("properties", null);
        not("properties key should NOT exist now", facade.exists("properties"));

        facade.setProperties("properties", properties);
        stateTrue("long key should exist now", facade.exists("properties"));
        stateTrue("delete properties should work", facade.remove("properties"));
        not("long key should NOT exist now", facade.exists("properties"));
    }

    @ToolkitTest(ignore = true)  // not supported by the underlying infrastructure yet
    protected void constantScopeSaveDeleteForData() throws Exception
    {
        String name = getClass().getName() + "-saveDeleteCycle";
        StorageFacade facade = storageService.constantNameStorage(name);
        facade.remove("data");

        byte[] data = new byte[]{'3', 4, '7'};
        stateTrue("First data access should be null", facade.getText("data") == null);
        facade.setData("data", data);
        stateTrue("data key should exist now", facade.exists("data"));
        stateTrue("[data]=" + ArrayUtils.toString(data), ArrayUtils.toString(facade.getText("data")).equals(ArrayUtils.toString(data)));
        stateTrue("delete text should work", facade.remove("data"));
        not("text key should NOT exist now", facade.exists("data"));
    }
}

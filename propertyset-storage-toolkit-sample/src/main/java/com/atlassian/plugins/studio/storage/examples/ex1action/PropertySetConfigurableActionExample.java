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

package com.atlassian.plugins.studio.storage.examples.ex1action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugins.studio.storage.toolkit.StorageFacade;
import com.atlassian.plugins.studio.storage.toolkit.StorageService;
import org.apache.commons.lang.StringUtils;

/**
 * User: leonidmaslov
 * Date: 12/11/10
 * Time: 3:51 PM
 */
public class PropertySetConfigurableActionExample extends JiraWebActionSupport
{
    private static final String ADMIN_ONLY = "admin.only";
    private static final String HELLO_TEXT = "hello.text";
    private static final String WELCOME_TEXT = "welcome.text";
    private final StorageService storageService;

    private boolean adminOnly;
    private String helloText;
    private String welcomeText;

    public PropertySetConfigurableActionExample(StorageService storageService)
    {
        this.storageService = storageService;
    }

    public boolean isAdminOnly()
    {
        return adminOnly;
    }

    public void setAdminOnly(boolean adminOnly)
    {
        this.adminOnly = adminOnly;
    }

    public String getHelloText()
    {
        return helloText;
    }

    public void setHelloText(String helloText)
    {
        this.helloText = helloText;
    }

    public String getWelcomeText()
    {
        return welcomeText;
    }


    public void setWelcomeText(String welcomeText)
    {
        this.welcomeText = welcomeText;
    }

    private StorageFacade getStorage()
    {
        return storageService.actionStorage(this);
    }

    @Override
    public String doDefault() throws Exception
    {
        loadConfig(getStorage());
        return super.doDefault();
    }

    @Override
    protected String doExecute() throws Exception
    {
        saveConfig(getStorage());
        return super.doExecute();
    }


    public String doHello()
    {
        loadConfig(getStorage());
        return "sayhello";
    }

    private void loadConfig(StorageFacade storage)
    {
        setAdminOnly(storage.getBoolean(ADMIN_ONLY));
        setHelloText(StringUtils.defaultString(storage.getString(HELLO_TEXT), "Hello, "));
        setWelcomeText(StringUtils.defaultString(storage.getString(WELCOME_TEXT), "World"));
    }

    private void saveConfig(StorageFacade storage)
    {
        storage.setBoolean(ADMIN_ONLY, isAdminOnly());
        storage.setText(HELLO_TEXT, getHelloText());
        storage.setText(WELCOME_TEXT, getWelcomeText());
    }
}

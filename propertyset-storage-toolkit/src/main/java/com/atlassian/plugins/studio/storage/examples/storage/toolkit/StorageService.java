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
 * Time: 5:31 AM
 */
public interface StorageService {

    StorageFacade projectStorage(Project project) throws StorageException;


    StorageFacade issueStorage(Issue issue) throws StorageException;


    StorageFacade gvStorage(GenericValue gv) throws StorageException;


    StorageFacade constantNameStorage(String name) throws StorageException;


    StorageFacade actionStorage(Action action) throws StorageException;

    <T extends Action> StorageFacade actionStorage(Class<T> actionClazz) throws StorageException;

}

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

package com.atlassian.plugins.studio.storage.examples;

import com.atlassian.plugins.studio.storage.examples.storage.toolkit.StorageFacade;
import com.atlassian.plugins.studio.storage.examples.storage.toolkit.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: leonidmaslov
 * Date: 12/10/10
 * Time: 5:58 AM
 */
public class ExampleManagerImpl implements ExampleManager, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ExampleManagerImpl.class);
    private final StorageService storageService;

    public ExampleManagerImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void setup() {
        logger.error("setup");

        StorageFacade storageFacade = storageService.constantNameStorage("storageName");
        if (storageFacade.getBoolean("is.present")) {
            logger.error("is.present - is present");
        } else {
            logger.error("is.present - no present yet");
            storageFacade.setBoolean("is.present", true);
            logger.error("is.present db value - " + storageFacade.getBoolean("is.present"));
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        setup();
    }

    @Override
    public void destroy() throws Exception {
        logger.error("destroy");
    }
}

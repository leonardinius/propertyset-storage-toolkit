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

import com.atlassian.gzipfilter.org.apache.commons.lang.builder.ToStringBuilder;
import com.atlassian.gzipfilter.org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * storage instance id
 */
public class InstanceId {
    public InstanceId(Long entityId, String entityName, String keyPrefix) {
        this.keyPrefix = keyPrefix;
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    private final String keyPrefix;
    private final Long entityId;
    private final String entityName;


    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKeyPrefix())
                .append(getEntityName())
                .append(getEntityId())
                .toHashCode();
    }

    public boolean equals(Object o) {
        boolean result = false;

        if (this == o) {
            result = true;
        } else if (o instanceof InstanceId) {
            InstanceId other = (InstanceId) o;

            result = new EqualsBuilder()
                    .append(getKeyPrefix(), other.getKeyPrefix())
                    .append(getEntityName(), other.getEntityName())
                    .append(getEntityId(), other.getEntityId())
                    .isEquals();
        }

        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
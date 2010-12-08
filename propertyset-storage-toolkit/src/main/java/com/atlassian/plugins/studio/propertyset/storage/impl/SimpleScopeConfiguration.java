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

package com.atlassian.plugins.studio.propertyset.storage.impl;

import com.atlassian.gzipfilter.org.apache.commons.lang.builder.ToStringBuilder;
import com.atlassian.gzipfilter.org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class SimpleScopeConfiguration {
    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Long getEntityName() {
        return entityName;
    }

    private final String prefix;
    private final String suffix;
    private final Long entityId;
    private final Long entityName;

    public SimpleScopeConfiguration(String prefix, String suffix, Long entityName, Long entityId) {
        this.entityName = entityName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.entityId = entityId;
    }


    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getPrefix())
                .append(getSuffix())
                .append(getEntityName())
                .append(getEntityId())
                .toHashCode();
    }

    public boolean equals(Object o) {
        boolean result = false;

        if (this == o) {
            result = true;
        } else if (o instanceof SimpleScopeConfiguration) {
            SimpleScopeConfiguration other = (SimpleScopeConfiguration) o;

            result = new EqualsBuilder()
                    .append(getPrefix(), other.getPrefix())
                    .append(getSuffix(), other.getSuffix())
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
};
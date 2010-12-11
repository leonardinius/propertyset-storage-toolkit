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

import com.atlassian.core.ofbiz.test.mock.MockGenericValue;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugins.studio.storage.toolkit.impl.AbstractDefaultScopeImpl;
import com.google.common.collect.ImmutableMap;
import com.opensymphony.module.propertyset.PropertySet;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: leonidmaslov
 * Date: 12/10/10
 * Time: 6:20 PM
 */
public class ScopesTest {
    @Mock
    PropertySet propertySet;

    @Test
    public void testProjectScope() throws Exception {

        Project project = mock(Project.class);
        when(project.getId()).thenReturn(567L);

        InstanceId id = getId(Scopes.projectScope(project), project);
        Assert.assertThat(id, isEq(567L, "FacadeStorageProject", "project-"));
    }

    @Test
    public void testIssueScope() throws Exception {
        Issue issue = mock(Issue.class);
        when(issue.getId()).thenReturn(569L);

        InstanceId id = getId(Scopes.issueScope(issue), issue);
        Assert.assertThat(id, isEq(569L, "FacadeStorageIssue", "issue-"));
    }

    @Test
    public void testGvScope() throws Exception {
        MockGenericValue gv = new MockGenericValue("Name", ImmutableMap.of("id", 112L));

        InstanceId id = getId(Scopes.gvScope(gv), gv);
        Assert.assertThat(id, isEq(112L, "FacadeStorageGv", "gv-"));

    }

    @Test
    public void testConstantName() throws Exception {
        InstanceId id = getId(Scopes.constantName("cn.name"), "cn.name");
        Assert.assertThat(id, isEq(2L, "FacadeStorageConstant-cn.name", "constant-"));
    }

    @Test
    public void testActionConfigurationInstance() throws Exception {
        InstanceId id = getId(Scopes.actionConfiguration(new MyAction()), new MyAction());
        Assert.assertThat(id, isEq(3L, "FacadeStorageAction-com.atlassian.plugins.studio.storage.toolkit.ScopesTest$MyAction", "class-"));
    }


    @Test
    public void testActionConfigurationKlazz() throws Exception {
        InstanceId id = getId(Scopes.actionConfiguration(MyAction.class), MyAction.class);
        Assert.assertThat(id, isEq(3L, "FacadeStorageAction-com.atlassian.plugins.studio.storage.toolkit.ScopesTest$MyAction", "class-"));
    }

    private Matcher<InstanceId> isEq(Long id, String name, String prefix) {
        return CoreMatchers.equalTo(new InstanceId(id, name, prefix));
    }


    private <U> InstanceId getId(Scope<U> scope, U context) {
        return ((AbstractDefaultScopeImpl<U>) scope).getInstanceId(context);
    }


    private static class MyAction extends JiraWebActionSupport {
    }


}

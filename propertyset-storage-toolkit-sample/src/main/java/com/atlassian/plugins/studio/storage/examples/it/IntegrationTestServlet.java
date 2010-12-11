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

package com.atlassian.plugins.studio.storage.examples.it;

import com.atlassian.plugins.studio.storage.toolkit.StorageService;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.text.MessageFormat.format;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class IntegrationTestServlet extends HttpServlet

{
    private final StorageService storageService;

    private static final Logger logger = LoggerFactory.getLogger(IntegrationTestServlet.class);

    public IntegrationTestServlet(StorageService storageService)
    {
        this.storageService = storageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {

        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html");

        try {
            TestSuite suite = new TestSuite(storageService);

            doExec(res.getWriter(), suite, getTests(suite));

        } finally {
            res.getWriter().close();
        }
    }

    private enum Status
    {
        Ok
                {
                    @Override
                    public String render(int counter, String name, String stackTrace)
                    {
                        return format("<tr valign=''top'' style=''background-color : green;''><td>{3}. {0}</td><td>{1}</td><td>{2}</td></tr>",
                                name(),
                                name,
                                stackTrace, counter);
                    }
                },
        Error
                {
                    @Override
                    public String render(int counter, String name, String stackTrace)
                    {
                        return format("<tr valign=''top'' style=''background-color : red;''><td>{3}. {0}</td><td>{1}</td><td>{2}</td></tr>", name(), name, stackTrace, counter);
                    }
                };

        public abstract String render(int counter, String name, String stackTrace);
    }

    private static class Reporter
    {

        private final Writer writer;

        public Reporter(Writer writer)
        {
            this.writer = writer;
        }

        public void start() throws IOException
        {
            writer.write("" +
                    "<html>" +
                    "<head><title>PropertySet Toolkit Tets result</title></head>" +
                    "<body>" +
                    "" +
                    "<h1><a href='https://github.com/leonardinius/propertyset-storage-toolkit'>propertyset-storage-toolkit</a> test " +
                    "results</h1>" +
                    "<table>" +
                    "   <thead>" +
                    "       <tr><th>Status</th><th>Test</th><th>Error</th></tr>" +
                    "   </thead>" +
                    "<tbody>");
        }

        public void reportTest(int counter, String name, Throwable t) throws IOException
        {
            Status status = t == null ? Status.Ok : Status.Error;
            String stackTrace = t != null ? escapeHtml(ExceptionUtils.getFullStackTrace(t)) : "";

            writer.write(status.render(counter, name, stackTrace));
        }

        public void end() throws IOException
        {
            writer.write("</tbody></table>");
        }


        public void stats(int success, int total) throws IOException
        {
            writer.write(format("<b>Stats</b> Success: {0}, Failures: {1}, Total: {2}", success, total - success, total));
        }

        public void flush() throws IOException
        {
            writer.write("</body></html>");
        }
    }

    @SuppressWarnings({"ConstantConditions"})
    private void doExec(Writer writer, Object instance, Method[] tests) throws IOException
    {
        Reporter reporter = new Reporter(writer);


        reporter.start();

        int counter = 1;
        int success = 0;

        try {
            for (Method test : tests) {
                String name = getTestName(test);

                Throwable throwable = null;
                try {
                    test.invoke(instance);
                    success++;
                } catch (Exception e) {
                    throwable = (e instanceof InvocationTargetException) ? ((InvocationTargetException) (e)).getTargetException() : e;
                    logger.error("Test " + name + " failure: " + throwable.getMessage(), e);
                }

                reporter.reportTest(counter++, name, throwable);
            }
        } finally {
            reporter.end();
            reporter.stats(success, counter - 1);
            //noinspection EmptyCatchBlock
            try {
                reporter.flush();
            } catch (IOException e) {
            }
        }
    }

    private String getTestName(Method test)
    {
        return Joiner.on(' ')
                .skipNulls()
                .join(
                        Iterables.transform(Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(test.getName())), new Function<String, String>()
                        {
                            @Override
                            public String apply(@Nullable String from)
                            {
                                return StringUtils.capitalize(from);
                            }
                        })
                );
    }


    private Method[] getTests(Object instance)
    {
        return Iterables.toArray(Iterables.<Method>filter(Arrays.<Method>asList(instance.getClass().getDeclaredMethods()),
                new Predicate<Method>()
                {
                    @Override
                    public boolean apply(@Nullable Method input)
                    {
                        ToolkitTest marker = input.getAnnotation(ToolkitTest.class);
                        return marker != null && !marker.ignore();
                    }
                }), Method.class);
    }
}

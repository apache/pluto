/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pluto.core;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import junit.framework.TestCase;


public class PortletContextManagerTest extends TestCase {

    private PortletContextManager manager;
    private ServletContext context;

    public void setUp() {
        /* Java5 Required!
        context = EasyMock.createMock(ServletContext.class);
        */
        manager = PortletContextManager.getManager();
    }

    public void testComputeContextPath() throws MalformedURLException {
        /* Java5 Required!
            URL url = new URL("file://usr/local/apache-tomcat-5.1.19/webapps/my-test-context/WEB-INF/web.xml");
            EasyMock.expect(context.getResource("/WEB-INF/web.xml")).andReturn(url);
            EasyMock.replay(context);
            assertEquals("/my-test-context", manager.computeContextPath(context));
            EasyMock.verify(context);


            EasyMock.reset(context);
            url = new URL("file://usr/local/apache-tomcat-5.1.19/webapps/my-test-context.war!/WEB-INF/web.xml");
            EasyMock.expect(context.getResource("/WEB-INF/web.xml")).andReturn(url);
            EasyMock.replay(context);
            assertEquals("/my-test-context", manager.computeContextPath(context));
        */
        }
    
    /* Java5 Required!
    public void testGetPortletContext_InvalidPortletAppContextPath() throws Exception {
      try {
        PortletContextManager.getPortletContext(context, "/my-invalid-context");
        fail();
      } catch (PortletContainerException expected) {}
    }
    */
}

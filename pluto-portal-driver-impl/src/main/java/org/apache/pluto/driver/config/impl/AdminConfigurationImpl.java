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
package org.apache.pluto.driver.config.impl;

import javax.servlet.ServletContext;

import org.apache.pluto.driver.config.AdminConfiguration;
import org.apache.pluto.driver.services.portal.admin.PortletRegistryAdminService;
import org.apache.pluto.driver.services.portal.admin.RenderConfigAdminService;

/**
 *
 * @version 1.0
 * @since Nov 30, 2005
 */
public class AdminConfigurationImpl implements AdminConfiguration {

    private PortletRegistryAdminService portletRegistryAdminService;
    private RenderConfigAdminService renderConfigAdminService;

    public void init(ServletContext context) {
        
    }

    public void destroy() {

    }

    public PortletRegistryAdminService getPortletRegistryAdminService() {
        return portletRegistryAdminService;
    }

    public void setPortletRegistryAdminService(PortletRegistryAdminService portletRegistryAdminService) {
        this.portletRegistryAdminService = portletRegistryAdminService;
    }

    public RenderConfigAdminService getRenderConfigAdminService() {
        return renderConfigAdminService;
    }

    public void setRenderConfigAdminService(RenderConfigAdminService renderConfigAdminService) {
        this.renderConfigAdminService = renderConfigAdminService;
    }


}

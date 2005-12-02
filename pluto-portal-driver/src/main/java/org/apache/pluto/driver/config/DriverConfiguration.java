/*
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pluto.driver.config;

import org.apache.pluto.driver.services.portal.PortletApplicationConfig;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.services.portal.PageConfig;
import org.apache.pluto.services.PortalCallbackService;
import org.apache.pluto.services.optional.PortletPreferencesService;

import javax.servlet.ServletContext;
import java.util.Collection;

/**
 * Interface defining a means for retrieving driver services
 * based upon configuration information. Within the portal,
 * an implementation of this interface should be bound to
 * the portal's ServletContext.
 *
 *
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @since Sep 2, 2005
 *
 */
public interface DriverConfiguration {

    /**
     * Initialization method used to place the driver
     * configuration into service.
     *
     * @param context
     */
    void init(ServletContext context);

    /**
     * Retrieve the name of the portal
     * as should be returned in
     * {@link javax.portlet.PortalContext#getPortalInfo()}
     * @return the name of the portal.
     */
    String getPortalName();

    /**
     * Retrieve the version of the portal
     * as should be returned in
     * {@link javax.portlet.PortalContext#getPortalInfo()}
     * @return the portal version.
     */
    String getPortalVersion();

    /**
     * Retrieves the name of the container which
     * pluto should create and embed.
     * @return the container name.
     */
    String getContainerName();

    Collection getSupportedPortletModes();

    Collection getSupportedWindowStates();

    Collection getPortletApplications();

    PortletApplicationConfig getPortletApp(String id);

    PortletWindowConfig getPortletWindowConfig(String id);

    Collection getPages();

    PageConfig getPageConfig(String pageId);

//
// Utility methods for the container
//
    PortalCallbackService getPortalCallbackService();

    PortletPreferencesService getPortletPreferencesService();
}

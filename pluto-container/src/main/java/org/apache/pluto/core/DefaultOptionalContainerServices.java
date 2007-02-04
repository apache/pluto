/*
 * Copyright 2004 The Apache Software Foundation.
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
package org.apache.pluto.core;

import org.apache.pluto.OptionalContainerServices;
import org.apache.pluto.spi.optional.PortletPreferencesService;
import org.apache.pluto.spi.optional.PortletEnvironmentService;
import org.apache.pluto.spi.optional.PortletInvokerService;
import org.apache.pluto.spi.optional.PortletRegistryService;
import org.apache.pluto.spi.optional.PortletInfoService;
import org.apache.pluto.spi.optional.PortalAdministrationService;

/**
 * Default Optional Container Services implementation.
 *
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @version 1.0
 * @since Sep 18, 2004
 */
public class DefaultOptionalContainerServices implements OptionalContainerServices {

    private PortletPreferencesService portletPreferencesService;
    private PortletRegistryService portletRegistryService;
    private PortletInvokerService portletInvokerService;
    private PortletEnvironmentService portletEnvironmentService;
    private PortletInfoService portletInfoService;
    private PortalAdministrationService portalAdministrationService;

    
    /**
     * Constructs an instance using the default portlet preferences service
     * implementation.
     */
    public DefaultOptionalContainerServices() {
        portletPreferencesService = new DefaultPortletPreferencesService();
        portletRegistryService = PortletContextManager.getManager();
        portletInvokerService = new DefaultPortletInvokerService();
        portletEnvironmentService = new DefaultPortletEnvironmentService();
        portletInfoService = new DefaultPortletInfoService();
        portalAdministrationService = new DefaultPortalAdministrationService();
    }
    
    /**
     * Constructs an instance using specified optional container services
     * implementation. If the portlet preferences service is provided, it will
     * be used. Otherwise, the default portlet preferences service will be used.
     * @param root  the root optional container services implementation.
     */
    public DefaultOptionalContainerServices(OptionalContainerServices root) {
        this();
        if (root.getPortletPreferencesService() != null) {
            portletPreferencesService = root.getPortletPreferencesService();
        }

        if (root.getPortletRegistryService() != null) {
            portletRegistryService = root.getPortletRegistryService();
        }

        if(root.getPortletEnvironmentService() != null) {
            portletEnvironmentService = root.getPortletEnvironmentService();
        }

        if(root.getPortletInvokerService() != null) {
            portletInvokerService = root.getPortletInvokerService();
        }

        if(root.getPortletEnvironmentService() != null) {
            portletEnvironmentService = root.getPortletEnvironmentService();
        }

        if(root.getPortletInfoService() != null) {
            portletInfoService = root.getPortletInfoService();
        }

        if(root.getPortalAdministrationService() != null) {
            portalAdministrationService = root.getPortalAdministrationService();
        }

    }
    
    
    // OptionalContainerServices Impl ------------------------------------------
    
    public PortletPreferencesService getPortletPreferencesService() {
        return portletPreferencesService;
    }


    public PortletRegistryService getPortletRegistryService() {
        return portletRegistryService;
    }

    public PortletEnvironmentService getPortletEnvironmentService() {
        return portletEnvironmentService;
    }
    
    public PortletInvokerService getPortletInvokerService() {
        return portletInvokerService;
    }

    public PortletInfoService getPortletInfoService() {
        return portletInfoService;
    }

    public PortalAdministrationService getPortalAdministrationService() {
        return portalAdministrationService;
    }
}


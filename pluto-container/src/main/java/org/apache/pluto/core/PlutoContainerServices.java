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

import javax.portlet.PortalContext;

import org.apache.pluto.container.CCPPProfileService;
import org.apache.pluto.container.ContainerInvocationService;
import org.apache.pluto.container.ContainerServices;
import org.apache.pluto.container.EventCoordinationService;
import org.apache.pluto.container.NamespaceMapper;
import org.apache.pluto.container.OptionalContainerServices;
import org.apache.pluto.container.PortalCallbackService;
import org.apache.pluto.container.PortletEnvironmentService;
import org.apache.pluto.container.PortletInfoService;
import org.apache.pluto.container.PortletInvokerService;
import org.apache.pluto.container.PortletPreferencesService;
import org.apache.pluto.container.PortletRequestContextService;
import org.apache.pluto.container.RequiredContainerServices;
import org.apache.pluto.container.UserInfoService;
import org.apache.pluto.container.driver.PortalAdministrationService;
import org.apache.pluto.container.driver.PortletContextService;
import org.apache.pluto.container.driver.PortletRegistryService;


public class PlutoContainerServices implements ContainerServices
{
    /*
     * required services
     */
    private PortalContext context;
    private CCPPProfileService ccppProfileService;
    private PortalCallbackService callbackService;
    private ContainerInvocationService containerInvocation;
    
    /*
     * optional services
     */
    private PortletPreferencesService portletPreferencesService;
    private PortletRegistryService portletRegistryService;
    private PortletContextService portletContextService;
    private PortletInvokerService portletInvokerService;
    private PortletRequestContextService portletRequestContextService;
    private PortletEnvironmentService portletEnvironmentService;
    private PortletInfoService portletInfoService;
    private PortalAdministrationService portalAdministrationService;
    private UserInfoService userInfoService;
    private NamespaceMapper namespaceMapper;
    private EventCoordinationService eventCoordinationService;
        
    public PlutoContainerServices()
    {
        createDefaultRequiredServices();
        createDefaultOptionalServices();
    }
    
    public PlutoContainerServices(RequiredContainerServices required, OptionalContainerServices optional)
    {
        this(required.getPortalContext(), required.getCCPPProfileService(), required.getContainerInvocationService(), required.getPortalCallbackService(), required.getEventCoordinationService(), optional);
    }

    public PlutoContainerServices(PortalContext context,
            CCPPProfileService ccppProfileService,
            ContainerInvocationService containerInvocation,
            PortalCallbackService callbackService,
            EventCoordinationService eventCoordinationService)
    {
        this.context = context;
        this.ccppProfileService = ccppProfileService;
        this.containerInvocation = containerInvocation;
        this.callbackService = callbackService;
        this.eventCoordinationService = eventCoordinationService;
        this.createDefaultOptionalServices();
    }            
            
    public PlutoContainerServices(PortalContext context,
            CCPPProfileService ccppProfileService,
            ContainerInvocationService containerInvocation,
            PortalCallbackService callbackService,
            EventCoordinationService eventCoordinationService,
            OptionalContainerServices optionalServices)  
    {
        this.context = context;
        this.ccppProfileService = ccppProfileService;
        this.containerInvocation = containerInvocation;
        this.eventCoordinationService = eventCoordinationService;
        
        portletPreferencesService = optionalServices.getPortletPreferencesService();
        if (portletPreferencesService == null)
            portletPreferencesService =  new DefaultPortletPreferencesService();
        portletRegistryService = optionalServices.getPortletRegistryService();
        if (portletRegistryService == null)
            portletRegistryService = new PortletContextManager();
        portletContextService = optionalServices.getPortletContextService();
        if (portletContextService == null && portletRegistryService instanceof PortletContextService)
        {
            portletContextService = (PortletContextService)portletRegistryService;
        }
        portletInvokerService = optionalServices.getPortletInvokerService();
        if (portletInvokerService == null)
            portletInvokerService = new DefaultPortletInvokerService(portletContextService);
        portletRequestContextService = optionalServices.getPortletRequestContextService();
        if (portletRequestContextService == null)
            portletRequestContextService = new DefaultPortletRequestContextService();
        portletEnvironmentService = optionalServices.getPortletEnvironmentService();
        if (portletEnvironmentService == null)
            portletEnvironmentService = new DefaultPortletEnvironmentService();
        portletInfoService = optionalServices.getPortletInfoService();
        if (portletInfoService == null)
            portletInfoService = new DefaultPortletInfoService();
        portalAdministrationService = optionalServices.getPortalAdministrationService();
        if (portalAdministrationService == null)
            portalAdministrationService = new DefaultPortalAdministrationService();
        userInfoService = optionalServices.getUserInfoService();
        if (userInfoService == null)
            userInfoService = new DefaultUserInfoService();
        namespaceMapper = optionalServices.getNamespaceMapper();
        if (namespaceMapper == null)
            namespaceMapper = new DefaultNamespaceMapper();
    }
    
    protected void createDefaultOptionalServices() 
    {
        portletPreferencesService = new DefaultPortletPreferencesService();
        portletRegistryService = new PortletContextManager();
        portletContextService = (PortletContextManager)portletRegistryService;
        portletInvokerService = new DefaultPortletInvokerService(portletContextService);
        portletRequestContextService = new DefaultPortletRequestContextService();
        portletEnvironmentService = new DefaultPortletEnvironmentService();
        portletInfoService = new DefaultPortletInfoService();
        portalAdministrationService = new DefaultPortalAdministrationService();
        userInfoService = new DefaultUserInfoService();
        namespaceMapper = new DefaultNamespaceMapper();
    }

    protected void createDefaultRequiredServices() 
    {
        context = null; // TODO
        ccppProfileService = new DummyCCPPProfileServiceImpl();
        callbackService = null; // TODO
        containerInvocation = new ContainerInvocationServiceImpl();        
        eventCoordinationService = null; // TODO
    }
    
    public PortalContext getPortalContext() 
    {
        return context;
    }

    public ContainerInvocationService getContainerInvocationService()
    {
        return containerInvocation;
    }
    
    /**
     * The PortletPreferencesService provides access to the portal's
     * PortletPreference persistence mechanism.
     * @return a PortletPreferencesService instance.
     */
    public PortletPreferencesService getPortletPreferencesService() 
    {
        return this.portletPreferencesService;
    }

    /**
     * The PortalCallbackService allows the container to communicate
     * actions back to the portal.
     * @return a PortalCallbackService implementation.
     */
    public PortalCallbackService getPortalCallbackService() 
    {
        return callbackService;
    }


    /**
     * Returns null to use pluto's default
     * @return
     */
    public PortletRegistryService getPortletRegistryService() 
    {
        return this.portletRegistryService;
    }

    public PortletContextService getPortletContextService() 
    {
        return this.portletContextService;
    }

    public PortletRequestContextService getPortletRequestContextService() 
    {
        return this.portletRequestContextService;
    }

    public PortletEnvironmentService getPortletEnvironmentService() 
    {
        return this.portletEnvironmentService;
    }

    public PortletInvokerService getPortletInvokerService() 
    {
        return this.portletInvokerService;
    }

    public CCPPProfileService getCCPPProfileService() 
    {
        return ccppProfileService;
    }

    public PortletInfoService getPortletInfoService() 
    {
        return this.portletInfoService;
    }

    public PortalAdministrationService getPortalAdministrationService() 
    {
        return this.portalAdministrationService;
    }

    public UserInfoService getUserInfoService() 
    {
        return this.userInfoService;
    }
    
    public NamespaceMapper getNamespaceMapper() 
    {
        return this.namespaceMapper;
    }

    public CCPPProfileService getCcppProfileService()
    {
        return ccppProfileService;
    }

    public PortalCallbackService getCallbackService()
    {
        return callbackService;
    }

    public ContainerInvocationService getContainerInvocation()
    {
        return containerInvocation;
    }

    public EventCoordinationService getEventCoordinationService()
    {
        return eventCoordinationService;
    }
}

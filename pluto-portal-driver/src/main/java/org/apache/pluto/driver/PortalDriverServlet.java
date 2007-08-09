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
package org.apache.pluto.driver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.portlet.PortletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.PortletContainerException;
import org.apache.pluto.PortletWindow;
import org.apache.pluto.descriptors.portlet.EventDD;
import org.apache.pluto.descriptors.portlet.EventDefinitionDD;
import org.apache.pluto.descriptors.portlet.PortletAppDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.pluto.descriptors.portlet.PublicRenderParamDD;
import org.apache.pluto.driver.config.DriverConfiguration;
import org.apache.pluto.driver.core.PortalRequestContext;
import org.apache.pluto.driver.core.PortletWindowImpl;
import org.apache.pluto.driver.services.portal.PageConfig;
import org.apache.pluto.driver.services.portal.PortletApplicationConfig;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.services.portal.SupportedModesService;
import org.apache.pluto.driver.url.PortalURL;
import org.apache.pluto.internal.InternalPortletWindow;
import org.apache.pluto.spi.EventProvider;
import org.apache.pluto.spi.PublicRenderParameterProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
/**
 * The controller servlet used to drive the Portal Driver. All requests mapped
 * to this servlet will be processed as Portal Requests.
 * 
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @author <a href="mailto:esm@apache.org">Elliot Metsger</a>
 * @author <a href="mailto:chrisra@cs.uni-jena.de">Christian Raschka</a>
 * @version 1.0
 * @since Sep 22, 2004
 */
public class PortalDriverServlet extends HttpServlet {

    /** Internal Logger. */
    private static final Log LOG = LogFactory.getLog(PortalDriverServlet.class);    
    
    /** The Portal Driver sServlet Context */
    private ServletContext servletContext = null;
    
    /** Is the SupportedModesService initialized? */
    private boolean isSupportedModesServiceInitialized = false;
        
    protected static final String DEFAULT_PAGE_URI =
    		"/WEB-INF/themes/pluto-default-theme.jsp";
    
    /** The portlet container to which we will forward all portlet requests. */
    protected PortletContainer container = null;
    
    
    
    
    // HttpServlet Impl --------------------------------------------------------
    
    public String getServletInfo() {
        return "Pluto Portal Driver Servlet";
    }
    
    /**
     * Initialize the Portal Driver. This method retrieves the portlet container
     * instance from the servlet context scope.
     * @see PortletContainer
     */
    public void init() {
        servletContext = getServletContext();
        container = (PortletContainer) servletContext.getAttribute(
        		AttributeKeys.PORTLET_CONTAINER);        
    }
    

    /**
     * Handle all requests. All POST requests are passed to this method.
     * @param request  the incoming HttpServletRequest.
     * @param response  the incoming HttpServletResponse.
     * @throws ServletException  if an internal error occurs.
     * @throws IOException  if an error occurs writing to the response.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        initSupportedModesService();
        
        PortalRequestContext portalRequestContext =
            new PortalRequestContext(getServletContext(), request, response);

        PortalURL portalURL = portalRequestContext.getRequestedPortalURL();
        String actionWindowId = portalURL.getActionWindow();
        String resourceWindowId = portalURL.getResourceWindow();
                
        PortletWindowConfig resourceWindowConfig = null;
        PortletWindowConfig actionWindowConfig = null;
        request.setAttribute("filter-manager",
        		container.getRequiredContainerServices().
        		getPortalCallbackService().
        		getFilterManager());
        if (resourceWindowId != null){
               resourceWindowConfig = getDriverConfiguration()
                               .getPortletWindowConfig(resourceWindowId);
        }
        else{
               actionWindowConfig = getDriverConfiguration()
               .getPortletWindowConfig(actionWindowId);
 
        }

        // Action window config will only exist if there is an action request.
        if (actionWindowConfig != null) {
            PortletWindowImpl portletWindow = new PortletWindowImpl(
            		actionWindowConfig, portalURL);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing action request for window: "
                		+ portletWindow.getId().getStringId());
            }
            try {
                container.doAction(portletWindow, request, response);
            } catch (PortletContainerException ex) {
                throw new ServletException(ex);
            } catch (PortletException ex) {
                throw new ServletException(ex);
            }
            if (LOG.isDebugEnabled()) {
            	LOG.debug("Action request processed.\n\n");
            }
        }
        else if (resourceWindowConfig != null) {
               PortletWindowImpl portletWindow = new PortletWindowImpl(
                               resourceWindowConfig, portalURL);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Processing resource Serving request for window: "
                               + portletWindow.getId().getStringId());
            }
            try {
                container.doServeResource(portletWindow, request, response);
            } catch (PortletContainerException ex) {
                throw new ServletException(ex);
            } catch (PortletException ex) {
                throw new ServletException(ex);
            }
            if (LOG.isDebugEnabled()) {
               LOG.debug("Action request processed.\n\n");
            }
        }
        // Otherwise (actionWindowConfig == null), handle the render request.
        else {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("Processing render request.");
        	}
            PageConfig pageConfig = portalURL.getPageConfig(servletContext);
            if (pageConfig == null)
            {
                LOG.error("PageConfig for render path [" + portalURL.getRenderPath() + "] could not be found.");
            }
            
            try {
				registerPortlets(pageConfig, portalURL);
			} catch (PortletContainerException e) {
				throw new ServletException(e);
			}
            
            request.setAttribute(AttributeKeys.CURRENT_PAGE, pageConfig);
            String uri = (pageConfig.getUri() != null)
            		? pageConfig.getUri() : DEFAULT_PAGE_URI;
            if (LOG.isDebugEnabled()) {
            	LOG.debug("Dispatching to: " + uri);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
            dispatcher.forward(request, response);
            if (LOG.isDebugEnabled()) {
            	LOG.debug("Render request processed.\n\n");
            }
        }
    }

    /**
     * Pass all POST requests to {@link #doGet(HttpServletRequest, HttpServletResponse)}.
     * @param request  the incoming servlet request.
     * @param response  the incoming servlet response.
     * @throws ServletException if an exception occurs.
     * @throws IOException if an exception occurs writing to the response.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
    
    
    // Private Methods ---------------------------------------------------------
    
    private void registerPortlets(PageConfig pageConfig, PortalURL portalURL) throws ServletException, PortletContainerException {
		
		// iterate all portlets on the page
        for (Object object : pageConfig.getPortletIds()) {
        	String portletId = (String) object;
        	DriverConfiguration driverConfig = (DriverConfiguration)
				getServletContext().getAttribute(AttributeKeys.DRIVER_CONFIG);
        	PortletAppDD portletAppDD = container.getPortletApplicationDescriptor(
        		PortletWindowConfig.parseContextPath(portletId));
        	
        	PortletWindowConfig windowConfig = driverConfig
        			.getPortletWindowConfig(portletId);
            PortletWindow window = new PortletWindowImpl(windowConfig, portalURL);
            InternalPortletWindow win = new org.apache.pluto.internal.impl.PortletWindowImpl(
            		getServletContext(),window);
            PortletDD portletDD = win.getPortletEntity().getPortletDefinition();
            
//            registerEvents(window, portletDD, portletAppDD);
            
//            registerEventDefinitions(portletAppDD);
            
            registerPublicRenderParams(window, portletDD,portletAppDD);
		}
	}

	/**
	 * registers the public render params at the PublicRenderParameterProvider
	 * @param window
	 * @param portletDD
	 * @param portletAppDD
	 */
	private void registerPublicRenderParams(PortletWindow window, PortletDD portletDD, PortletAppDD portletAppDD) {
		PublicRenderParameterProvider renderProvider = container.getRequiredContainerServices()
			.getPortalCallbackService().getPublicRenderParameterProvider();
		List<String> render = portletDD.getPublicRenderParameter();
		if (render != null){ 
			for (String renderDD : render){
				List<PublicRenderParamDD> publicRenderParameterList = portletAppDD.getRender();
				if (publicRenderParameterList!= null){
					for (PublicRenderParamDD renderportletAppDD : publicRenderParameterList){
						if (renderportletAppDD.getIdentifier().equals(renderDD)){
							renderDD = renderportletAppDD.getName().toString();
							break;
						}
					}
					renderProvider.registerPublicRenderParameter(window.getId().getStringId(), renderDD);
					if (LOG.isDebugEnabled()){
						LOG.debug(renderDD+ " successfully registered!");
					}
				}
			}
		}
	}

	/**
	 * registers the Events of the given portletDD at the (static) EventProvider
	 * @param window The PortletWindow to store the event
	 * @param portletDD The PortletDD, where the event is declared
	 */
//	private void registerEvents(PortletWindow window, PortletDD portletDD, 
//			PortletAppDD portletAppDD) {
//		EventProvider eventProvider = container.getRequiredContainerServices()
//			.getPortalCallbackService().getEventProvider();
//		List<QName> events = portletDD.getProcessingEvents();
//		if (events != null) {
//			for (QName event : events) {
//				EventDefinitionDD eventDD = getEvent(event, portletAppDD);
//				if (eventDD != null) {
//
//					// register event only if it is declared as "processing-event"
//					List<QName> processingEvents = portletDD.getProcessingEvents();
//					for (QName name : processingEvents) {
//						if (name.toString().equals(eventDD.getName().toString())) {
//							eventProvider.registerEvent(event.toString(),window.getId().getStringId(), eventDD);
//							if (LOG.isDebugEnabled()){
//								LOG.debug(event+" registered successfully!");
//							}
//						}
//					}				
//				}
//			}
//		}
//	}
	
	/**
	 * Register event definitions.
	 * 
	 * @param portletAppDD the portlet app DD
	 */
//	private void registerEventDefinitions(PortletAppDD portletAppDD) {
//		EventProvider eventProvider = container.getRequiredContainerServices()
//		.getPortalCallbackService().getEventProvider();
//		List<EventDefinitionDD> defs = portletAppDD.getEvents();
//		if (defs != null){
//			for (EventDefinitionDD definitionDD : defs) {
//				eventProvider.registerEventDefinitionDD(definitionDD);
//			}
//		}
//	}
//
//    
//    private EventDefinitionDD getEvent(QName event, PortletAppDD portletAppDD) {
//		List<EventDefinitionDD> events = portletAppDD.getEvents();
//		for (EventDefinitionDD definitionDD : events) {
//			if (definitionDD.getName().equals(event))
//				return definitionDD;
//		}
//		// return null, if not found
//		return null;
//	}

	/**
     * Returns the portal driver configuration object.
     * @return the portal driver configuration object.
     */
    private DriverConfiguration getDriverConfiguration() {
        return (DriverConfiguration) getServletContext().getAttribute(
        		AttributeKeys.DRIVER_CONFIG);
    }    
    
    private void initSupportedModesService()
    {
        if (isSupportedModesServiceInitialized)
        {
            return;
        }

        ApplicationContext springContext = (ApplicationContext)servletContext.getAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        SupportedModesService service = (SupportedModesService)springContext.getBean("SupportedModesService");
        service.init(servletContext);
        
        isSupportedModesServiceInitialized = true;
    }
    
}


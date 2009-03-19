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
package org.apache.pluto.container.driver;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventPortlet;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceServingPortlet;
import javax.portlet.UnavailableException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pluto.container.FilterManager;
import org.apache.pluto.container.PortletContainerException;
import org.apache.pluto.container.PortletInvokerService;
import org.apache.pluto.container.PortletRequestContext;
import org.apache.pluto.container.PortletResponseContext;
import org.apache.pluto.container.PortletWindow;
import org.apache.pluto.container.om.portlet.PortletDefinition;

/**
 * Portlet Invocation Servlet. This servlet recieves cross context requests from
 * the the container and services the portlet request for the specified method.
 *
 * @version 1.1
 * @since 09/22/2004
 */
public class PortletServlet extends HttpServlet
{
    private static final long serialVersionUID = -5096339022539360365L;
    
    static class NullPortlet implements EventPortlet, ResourceServingPortlet, Portlet
    {
        public void processEvent(EventRequest arg0, EventResponse arg1)
        throws PortletException, IOException
        {
        }
        
        public void serveResource(ResourceRequest arg0, ResourceResponse arg1)
        throws PortletException, IOException
        {
        }

        public void destroy()
        {
        }

        public void init(PortletConfig arg0) throws PortletException
        {
        }

        public void processAction(ActionRequest arg0, ActionResponse arg1)
        throws PortletException, IOException
        {
        }

        public void render(RenderRequest arg0, RenderResponse arg1)
        throws PortletException, IOException
        {
        }
    }    

    // Private Member Variables ------------------------------------------------
    /**
     * The portlet name as defined in the portlet app descriptor.
     */
    private String portletName;

    /**
     * The portlet instance wrapped by this servlet.
     */
    private Portlet portlet;

    /**
     * The internal portlet context instance.
     */
    private DriverPortletContext portletContext;

    /**
     * The internal portlet config instance.
     */
    private DriverPortletConfig portletConfig;

    /**
     * The Event Portlet instance (the same object as portlet) wrapped by this
     * servlet.
     */
    private EventPortlet eventPortlet = null;

    /** The resource serving portlet instance wrapped by this servlet. */
    private ResourceServingPortlet resourceServingPortlet = null;

    private PortletContextService contextService;

    private boolean started = false;
    Timer   startTimer = null;

    // HttpServlet Impl --------------------------------------------------------

    public String getServletInfo()
    {
        return "Pluto PortletServlet [" + portletName + "]";
    }

    /**
     * Initialize the portlet invocation servlet.
     *
     * @throws ServletException
     *             if an error occurs while loading portlet.
     */
    public void init(ServletConfig config) throws ServletException
    {

        // Call the super initialization method.
        super.init(config);

        // Retrieve portlet name as defined as an initialization parameter.
        portletName = getInitParameter("portlet-name");

        started = false;

        startTimer = new Timer(true);
        final ServletContext servletContext = getServletContext();
        final ClassLoader paClassLoader = Thread.currentThread().getContextClassLoader();
        startTimer.schedule(new TimerTask()
        {
            public void run()
            {
                synchronized(servletContext)
                {
                    if (startTimer != null)
                    {
                        if (attemptRegistration(servletContext, paClassLoader ))
                        {
                            startTimer.cancel();
                            startTimer = null;
                        }
                    }
                }
            }
        }, 1, 10000);
    }

    protected boolean attemptRegistration(ServletContext context, ClassLoader paClassLoader)
    {
        if (PlutoServices.getServices() != null)
        {
            contextService = PlutoServices.getServices().getPortletContextService();
            try
            {
                ServletConfig sConfig = getServletConfig();
                if (sConfig == null)
                {
                    String msg = "Problem obtaining servlet configuration(getServletConfig() returns null).";
                    context.log(msg);
                    return true;
                }

                String applicationName = contextService.register(sConfig);
                started = true;
                portletContext = contextService.getPortletContext(applicationName);
                portletConfig = contextService.getPortletConfig(applicationName, portletName);

            }
            catch (PortletContainerException ex)
            {
                context.log(ex.getMessage(),ex);
                return true;
            }

            PortletDefinition portletDD = portletConfig.getPortletDefinition();

//          Create and initialize the portlet wrapped in the servlet.
            try
            {
                Class<?> clazz = paClassLoader.loadClass((portletDD.getPortletClass()));
                portlet = (Portlet) clazz.newInstance();
                portlet.init(portletConfig);
                initializeEventPortlet();
                initializeResourceServingPortlet();
                return true;
            }
            catch (Exception ex)
            {
                context.log(ex.getMessage(),ex);
                // take out of service
                portlet = null;
                portletConfig = null;
                return true;
            }
        }
        return false;
    }

    public void destroy()
    {
        synchronized(getServletContext())
        {
            if ( startTimer != null )
            {
              startTimer.cancel();
              startTimer = null;
            }
            else if ( started && portletContext != null)
            {
              started = false;
              contextService.unregister(portletContext);
              if (portlet != null)
              {
                  try
                  {
                      portlet.destroy();
                  }
                  catch (Exception e)
                  {
                      // ignore
                  }
                  portlet = null;
              }
            }
            super.destroy();
        }
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        dispatch(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        dispatch(request, response);
    }

    protected void doPut(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        dispatch(request, response);
    }

    // Private Methods ---------------------------------------------------------

    /**
     * Dispatch the request to the appropriate portlet methods. This method
     * assumes that the following attributes are set in the servlet request
     * scope:
     * <ul>
     * <li>METHOD_ID: indicating which method to dispatch.</li>
     * <li>PORTLET_REQUEST: the internal portlet request.</li>
     * <li>PORTLET_RESPONSE: the internal portlet response.</li>
     * </ul>
     *
     * @param request
     *            the servlet request.
     * @param response
     *            the servlet response.
     * @throws ServletException
     * @throws IOException
     */
    private void dispatch(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        if (portlet == null)
        {
            throw new javax.servlet.UnavailableException("Portlet "+portletName+" unavailable");
        }

        // Retrieve attributes from the servlet request.
        Integer methodId = (Integer) request.getAttribute(PortletInvokerService.METHOD_ID);

        final PortletRequest portletRequest = (PortletRequest)request.getAttribute(PortletInvokerService.PORTLET_REQUEST);

        final PortletResponse portletResponse = (PortletResponse)request.getAttribute(PortletInvokerService.PORTLET_RESPONSE);
        
        final PortletRequestContext requestContext = (PortletRequestContext)portletRequest.getAttribute(PortletInvokerService.REQUEST_CONTEXT);
        final PortletResponseContext responseContext = (PortletResponseContext)portletRequest.getAttribute(PortletInvokerService.RESPONSE_CONTEXT);
        
        final FilterManager filterManager = (FilterManager)request.getAttribute(PortletInvokerService.FILTER_MANAGER);
        
        request.removeAttribute(PortletInvokerService.METHOD_ID);
        request.removeAttribute(PortletInvokerService.PORTLET_REQUEST);
        request.removeAttribute(PortletInvokerService.PORTLET_RESPONSE);
        request.removeAttribute(PortletInvokerService.FILTER_MANAGER);

        requestContext.init(portletConfig, getServletContext(), request, response);
        responseContext.init(request, response);

        PortletWindow window = requestContext.getPortletWindow();

        PortletInvocationEvent event = new PortletInvocationEvent(portletRequest, window, methodId.intValue());

        notify(event, true, null);

        // Init the classloader for the filter and get the Service for
        // processing the filters.
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // FilterManager filtermanager = (FilterManager) request.getAttribute(
        // "filter-manager");

        try
        {

            // The requested method is RENDER: call Portlet.render(..)
            if (methodId == PortletInvokerService.METHOD_RENDER)
            {
                RenderRequest renderRequest = (RenderRequest) portletRequest;
                RenderResponse renderResponse = (RenderResponse) portletResponse;
                filterManager.processFilter(renderRequest, renderResponse,
                        loader, portlet, portletContext);
            }

            // The requested method is RESOURCE: call
            // ResourceServingPortlet.serveResource(..)
            else if (methodId == PortletInvokerService.METHOD_RESOURCE)
            {
                ResourceRequest resourceRequest = (ResourceRequest) portletRequest;
                ResourceResponse resourceResponse = (ResourceResponse) portletResponse;
                filterManager.processFilter(resourceRequest, resourceResponse,
                        loader, resourceServingPortlet, portletContext);
            }

            // The requested method is ACTION: call Portlet.processAction(..)
            else if (methodId == PortletInvokerService.METHOD_ACTION)
            {
                ActionRequest actionRequest = (ActionRequest) portletRequest;
                ActionResponse actionResponse = (ActionResponse) portletResponse;
                filterManager.processFilter(actionRequest, actionResponse,
                        loader, portlet, portletContext);
            }

            // The request methode is Event: call Portlet.processEvent(..)
            else if (methodId == PortletInvokerService.METHOD_EVENT)
            {
                EventRequest eventRequest = (EventRequest) portletRequest;
                EventResponse eventResponse = (EventResponse) portletResponse;
                filterManager.processFilter(eventRequest, eventResponse,
                        loader, eventPortlet, portletContext);
            }
            // The requested method is ADMIN: call handlers.
            else if (methodId == PortletInvokerService.METHOD_ADMIN)
            {
                PortalAdministrationService pas = PlutoServices.getServices().getPortalAdministrationService();

                for (AdministrativeRequestListener l : pas.getAdministrativeRequestListeners())
                {
                    l.administer(portletRequest, portletResponse);
                }
            }

            // The requested method is NOOP: do nothing.
            else if (methodId == PortletInvokerService.METHOD_NOOP)
            {
                // Do nothing.
            }

            notify(event, false, null);

        }
        catch (UnavailableException ex)
        {
            System.err.println(ex.getMessage());
            /*
             * if (e.isPermanent()) { throw new
             * UnavailableException(e.getMessage()); } else { throw new
             * UnavailableException(e.getMessage(), e.getUnavailableSeconds());
             * }
             */

            // Portlet.destroy() isn't called by Tomcat, so we have to fix it.
            try
            {
                portlet.destroy();
            }
            catch (Throwable th)
            {
                System.err.println(ex.getMessage());
                // Don't care for Exception
            }
            // take portlet out of service
            portlet = null;

            // TODO: Handle everything as permanently for now.
            throw new javax.servlet.UnavailableException(ex.getMessage());

        }
        catch (PortletException ex)
        {
            notify(event, false, ex);
            System.err.println(ex.getMessage());
            throw new ServletException(ex);

        }
    }

    protected void notify(PortletInvocationEvent event, boolean pre, Throwable e)
    {
        PortalAdministrationService pas = PlutoServices.getServices().getPortalAdministrationService();

        for (PortletInvocationListener listener : pas.getPortletInvocationListeners())
        {
            if (pre)
            {
                listener.onBegin(event);
            }
            else if (e == null)
            {
                listener.onEnd(event);
            }
            else
            {
                listener.onError(event, e);
            }
        }
    }

    private void initializeEventPortlet()
    {
        if (portlet instanceof EventPortlet)
        {
            eventPortlet = (EventPortlet) portlet;
        }
        else
        {
            eventPortlet = new NullPortlet();
        }
    }

    private void initializeResourceServingPortlet()
    {
        if (portlet instanceof ResourceServingPortlet)
        {
            resourceServingPortlet = (ResourceServingPortlet) portlet;
        }
        else
        {
            resourceServingPortlet = new NullPortlet();
        }
    }
}

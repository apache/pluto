/*
 * Copyright 2003,2004 The Apache Software Foundation.
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
package org.apache.pluto;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pluto.descriptors.portlet.PortletAppDD;

/**
 * The publicized entry point into Pluto. The base functionality of the portlet
 * container can be enhanced or even modified by PortletContainerServices.
 * <p/>
 * <P> The methods of this class have to be called in the following order:
 * <TABLE> <TR><TH>Method</TH><TH>Description</TH><TH>Constraints</TH></TR>
 * <TR><TD>{@link #init(javax.servlet.ServletContext)}</TD> <TD>Initialized the
 * portlet container.</TD> <TD>Performed only once per container
 * lifecycle.</TD></TR>
 * <p/>
 * <TR><TD>{@link #doAction(org.apache.pluto.PortletWindow,
    * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}</TD>
 * <TD>Perform the action for the targeted portlet</TD> <TD>Optionally performed
 * for a single portlet per request</TD></TR>
 * <p/>
 * <TR><TD>{@link #doRender(org.apache.pluto.PortletWindow,
    * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}</TD>
 * <TD>Render the portlet</TD> <TD>Performed once for each portlet per
 * request.</TD></TR>
 * <p/>
 * <TR><TD>{@link #destroy()}</TD> <TD>Destroy and remove container from
 * service.</TD> <TD>Performed only once per container lifecylce</TD></TR>
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:esm@apache.org">Elliot Metsger</a>
 * @version $Id: PortletContainer.java 36010 2004-07-30 14:16:06Z ddewolf $
 */
public interface PortletContainer {

    /**
     * Initializes the container for use within the given servlet context.
     * @param servletContext  the servlet context.
     * @throws PortletContainerException if an error occurs.
     */
    public void init(ServletContext servletContext)
    throws PortletContainerException;

    /**
     * Shuts down the container. After calling this method it is no longer valid
     * to call any method on the portlet container.
     * @throws PortletContainerException if an error occurs while shutting down
     *                                   the container
     */
    public void destroy() throws PortletContainerException;

    /**
     * Calls the render method of the given portlet window.
     * @param internalPortletWindow the portlet Window
     * @param request               the servlet request
     * @param response              the servlet response
     * @throws PortletException          if one portlet has trouble fulfilling
     *                                   the request
     * @throws IOException               if the streaming causes an I/O problem
     * @throws PortletContainerException if the portlet container implementation
     *                                   has trouble fulfilling the request
     */
    public void doRender(PortletWindow internalPortletWindow,
                         HttpServletRequest request,
                         HttpServletResponse response)
        throws PortletException, IOException, PortletContainerException;


    /**
     * Indicates that a portlet action occured in the current request and calls
     * the processAction method of this portlet.
     * @param internalPortletWindow the portlet Window
     * @param request               the servlet request
     * @param response              the servlet response
     * @throws PortletException          if one portlet has trouble fulfilling
     *                                   the request
     * @throws PortletContainerException if the portlet container implementation
     *                                   has trouble fulfilling the request
     */
    public void doAction(PortletWindow internalPortletWindow,
                         HttpServletRequest request,
                         HttpServletResponse response)
        throws PortletException, IOException, PortletContainerException;

    /**
     * Indicates that the portlet must be initialized
     * @param internalPortletWindow the portlet Window
     * @param servletRequest        the servlet request
     * @param servletResponse       the servlet response
     * @throws PortletException          if one portlet has trouble fulfilling
     *                                   the request
     * @throws PortletContainerException if the portlet container implementation
     *                                   has trouble fulfilling the request
     */
    public void doLoad(PortletWindow internalPortletWindow,
                       HttpServletRequest servletRequest,
                       HttpServletResponse servletResponse)
        throws PortletException, IOException, PortletContainerException;

    /**
     * Returns whether the container is already initialized or not.
     * @return <code>true</code> if the container is initialized
     */
    public boolean isInitialized();

    /**
     * Retrieve the unique container name
     * @return the container name.
     */
    public String getName();

    /**
     * Retreive the required container services associated with this container.
     * @return the required container services associated with this container.
     */
    public RequiredContainerServices getRequiredContainerServices();

    /**
     * Retrieve the optional container services associated with this contianer.
     * @return the container services provided by either the portal or the defaults.
     */
    public OptionalContainerServices getOptionalContainerServices();
    
    /**
     * Retrieve the {@link PortletAppDD} for the portlet
     * located at the supplied context.
     * 
     * Must not return null.
     * 
     * @param context the context of the portlet
     * @return the portlet application descriptor
     * @throws PortletContainerException if the container has trouble obtaining
     *                                   the context of the portlet, or retrieving
     *                                   the <code>PortletAppDD</code>
     */    
    public PortletAppDD getPortletApplicationDescriptor(String context)
        throws PortletContainerException;
    
}

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
package org.apache.pluto.driver.url.impl;

import org.apache.pluto.driver.url.PortalURL;
import org.apache.pluto.driver.url.PortalURLParameter;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The portal URL.
 * @since 1.0
 * @deprecated replaced by {@link RelativePortalURLImpl}
 */
public class PortalURLImpl implements PortalURL {

	/** Server URI contains protocol, host name, and (optional) port. */
    private String serverURI;

    private String servletPath;
    private String renderPath;
    private String actionWindow;

    /** The window states: key is the window ID, value is WindowState. */
    private Map windowStates = new HashMap();

    private Map portletModes = new HashMap();

    /** Parameters of the portlet windows. */
    private Map parameters = new HashMap();


    // Constructors ------------------------------------------------------------

    /**
     * Constructs a PortalURLImpl instance using default port.
     * @param protocol  the protocol.
     * @param hostName  the host name.
     * @param contextPath  the servlet context path.
     * @param servletName  the servlet name.
     */
    public PortalURLImpl(String protocol,
                         String hostName,
                         String contextPath,
                         String servletName) {
    	this(protocol, hostName, -1, contextPath, servletName);
    }

    /**
     * Constructs a PortalURLImpl instance using customized port.
     * @param protocol  the protocol.
     * @param hostName  the host name.
     * @param port  the port number: 0 or negative means using default port.
     * @param contextPath  the servlet context path.
     * @param servletName  the servlet name.
     */
    public PortalURLImpl(String protocol,
                         String hostName,
                         int port,
                         String contextPath,
                         String servletName) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(protocol);
    	buffer.append(hostName);
    	if (port > 0) {
    		buffer.append(":").append(port);
    	}
    	serverURI = buffer.toString();

    	buffer = new StringBuffer();
    	buffer.append(contextPath);
    	buffer.append(servletName);
        servletPath = buffer.toString();
    }

    /**
     * Internal private constructor used by method <code>clone()</code>.
     * @see #clone()
     */
    private PortalURLImpl() {
    	// Do nothing.
    }

    // Public Methods ----------------------------------------------------------

    public void setRenderPath(String renderPath) {
        this.renderPath = renderPath;
    }

    public String getRenderPath() {
        return renderPath;
    }

    public void addParameter(PortalURLParameter param) {
        parameters.put(param.getWindowId() + param.getName(), param);
    }

    public Collection getParameters() {
        return parameters.values();
    }

    public void setActionWindow(String actionWindow) {
        this.actionWindow = actionWindow;
    }

    public String getActionWindow() {
        return actionWindow;
    }

    public Map getPortletModes() {
        return Collections.unmodifiableMap(portletModes);
    }

    public PortletMode getPortletMode(String windowId) {
        PortletMode mode = (PortletMode) portletModes.get(windowId);
        if (mode == null) {
            mode = PortletMode.VIEW;
        }
        return mode;
    }

    public void setPortletMode(String windowId, PortletMode portletMode) {
        portletModes.put(windowId, portletMode);
    }

    public Map getWindowStates() {
        return Collections.unmodifiableMap(windowStates);
    }

    /**
     * Returns the window state of the specified window.
     * @param windowId  the window ID.
     * @return the window state. Default to NORMAL.
     */
    public WindowState getWindowState(String windowId) {
        WindowState state = (WindowState) windowStates.get(windowId);
        if (state == null) {
            state = WindowState.NORMAL;
        }
        return state;
    }

    /**
     * Sets the window state of the specified window.
     * @param windowId  the window ID.
     * @param windowState  the window state.
     */
    public void setWindowState(String windowId, WindowState windowState) {
        this.windowStates.put(windowId, windowState);
    }

    /**
     * Clear parameters of the specified window.
     * @param windowId  the window ID.
     */
    public void clearParameters(String windowId) {
    	for (Iterator it = parameters.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            PortalURLParameter param = (PortalURLParameter) entry.getValue();
            if (param.getWindowId().equals(windowId)) {
            	it.remove();
            }
        }
    }

    /**
     * Converts to a string representing the portal URL.
     * @return a string representing the portal URL.
     * @see org.apache.pluto.driver.url.impl.PortalURLParserImpl#toString(PortalURL)
     */
    public String toString() {
        return PortalURLParserImpl.getParser().toString(this);
    }


    /**
     * Returns the server URI (protocol, name, port).
     * @return the server URI portion of the portal URL.
     */
    public String getServerURI() {
        return serverURI;
    }

    /**
     * Returns the servlet path (context path + servlet name).
     * @return the servlet path.
     */
    public String getServletPath() {
        return servletPath;
    }

    /**
     * Clone a copy of itself.
     * @return a copy of itself.
     */
    public Object clone() {
    	PortalURLImpl portalURL = new PortalURLImpl();
    	portalURL.serverURI = this.serverURI;
    	portalURL.servletPath = this.servletPath;
    	portalURL.parameters = new HashMap(parameters);
    	portalURL.portletModes = new HashMap(portletModes);
    	portalURL.windowStates = new HashMap(windowStates);
    	portalURL.renderPath = renderPath;
    	portalURL.actionWindow = actionWindow;
        return portalURL;
    }
}

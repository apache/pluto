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

package org.apache.pluto.driver.services.container;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pluto.container.PortletContainer;
import org.apache.pluto.container.PortletResponseContext;
import org.apache.pluto.container.PortletWindow;
import org.apache.pluto.container.ResourceURLProvider;
import org.apache.pluto.driver.core.PortalRequestContext;
import org.apache.pluto.driver.url.PortalURL;
import org.w3c.dom.Element;

/**
 * @version $Id$
 *
 */
public abstract class PortletResponseContextImpl implements PortletResponseContext
{
    private PortletContainer container;
    private HttpServletRequest containerRequest;
    private HttpServletResponse containerResponse;
    private HttpServletRequest servletRequest;
    private HttpServletResponse servletResponse;
    private PortletWindow window;
    private PortalURL portalURL;
    private boolean closed;
    private boolean released;
    
    public PortletResponseContextImpl(PortletContainer container, HttpServletRequest containerRequest,
                                      HttpServletResponse containerResponse, PortletWindow window)
    {
        this.container = container;
        this.containerRequest = containerRequest;
        this.containerResponse = containerResponse;
        this.window = window;
        this.portalURL = PortalRequestContext.getContext(containerRequest).createPortalURL();
    }
    
    protected PortalURL getPortalURL()
    {
        return portalURL;
    }

    protected boolean isClosed()
    {
        return closed;
    }
    
    protected boolean isReleased()
    {
        return released;
    }

    public void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
    {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }
    
    public void addProperty(Cookie cookie)
    {
        if (!isClosed())
        {
            servletResponse.addCookie(cookie);
        }
    }

    public void addProperty(String key, Element element)
    {
        // not supported 
    }

    public void addProperty(String key, String value)
    {
        // not supported
    }

    public void close()
    {
        closed = true;
    }

    public PortletContainer getContainer()
    {
        return container;
    }

    public PortletWindow getPortletWindow()
    {
        return window;
    }

    public HttpServletRequest getContainerRequest()
    {
        return containerRequest;
    }

    public HttpServletResponse getContainerResponse()
    {
        return containerResponse;
    }

    public HttpServletRequest getServletRequest()
    {
        return servletRequest;
    }

    public HttpServletResponse getServletResponse()
    {
        return servletResponse;
    }

    public void release()
    {
        closed = true;
        released = true;
        container = null;
        servletRequest = null;
        servletResponse = null;
        window = null;
    }

    public void setProperty(String key, String value)
    {
        // not supported
    }

    public ResourceURLProvider getResourceURLProvider()
    {
        return isReleased() ? null : new ResourceURLProviderImpl(servletRequest,window);
    }
}

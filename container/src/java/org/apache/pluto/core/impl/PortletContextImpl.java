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
/* 

 */

package org.apache.pluto.core.impl;

import java.net.MalformedURLException;

import javax.portlet.PortletContext;

import org.apache.pluto.core.InternalPortletContext;
import org.apache.pluto.om.portlet.PortletApplicationDefinition;
import org.apache.pluto.Environment;

public class PortletContextImpl implements PortletContext, InternalPortletContext
{
    private PortletApplicationDefinition portletApplicationDefinition;
    private javax.servlet.ServletContext servletContext;

    public PortletContextImpl(javax.servlet.ServletContext servletContext,
                              PortletApplicationDefinition portletApplicationDefinition)
    {
        this.servletContext = servletContext;
        this.portletApplicationDefinition = portletApplicationDefinition;
    }

    // javax.portlet.PortletContext implementation ------------------------------------------------
    public String getServerInfo()
    {
        return Environment.getServerInfo();
    }

    public javax.portlet.PortletRequestDispatcher getRequestDispatcher(String path)
    {
		try {
	        javax.servlet.RequestDispatcher rd = servletContext.getRequestDispatcher(path);
    	    return new PortletRequestDispatcherImpl(rd);
        } catch (Exception e) {
    		// need to catch exception because of tomcat 4.x bug
    		// tomcat throws an exception instead of return null
    		// if the path was not found
    		return null;
    	}

    }

    public javax.portlet.PortletRequestDispatcher getNamedDispatcher(String name)
    {
       	javax.servlet.RequestDispatcher rd = servletContext.getNamedDispatcher(name);
       	return rd != null ? new PortletRequestDispatcherImpl(rd)
           	              : null;
    }

    public java.io.InputStream getResourceAsStream(String path)
    {
        return servletContext.getResourceAsStream(path);
    }
    
    public int getMajorVersion()
    {
        return 1;
    }

    public int getMinorVersion()
    {
        return 0;
    }

    public String getMimeType(String file)
    {
        return servletContext.getMimeType(file);
    }

    public String getRealPath(String path)
    {
        return servletContext.getRealPath(path);
    }

    public java.util.Set getResourcePaths(String path)
    {
        return servletContext.getResourcePaths(path);
    }

    public java.net.URL getResource(String path) throws java.net.MalformedURLException
    {
        if (path == null || !path.startsWith("/"))
        {
            throw new MalformedURLException("path must start with a '/'");
        }
        return servletContext.getResource(path);
    }

    public java.lang.Object getAttribute(java.lang.String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Attribute name == null");
        }

        return servletContext.getAttribute(name);
    }

    public java.util.Enumeration getAttributeNames()
    {
        return servletContext.getAttributeNames();
    }

    public java.lang.String getInitParameter(java.lang.String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Parameter name == null");
        }

        return servletContext.getInitParameter(name);
    }

    public java.util.Enumeration getInitParameterNames()
    {
        return servletContext.getInitParameterNames();
    }

    public void log(java.lang.String msg)
    {
        servletContext.log(msg);
    }

    public void log(java.lang.String message, java.lang.Throwable throwable)
    {
        servletContext.log(message, throwable);
    }

    public void removeAttribute(java.lang.String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Attribute name == null");
        }

        servletContext.removeAttribute(name);
    }

    public void setAttribute(java.lang.String name, java.lang.Object object)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Attribute name == null");
        }

        servletContext.setAttribute(name, object);
    }

    public String getPortletContextName()
    {
        return servletContext.getServletContextName();
    }
    // --------------------------------------------------------------------------------------------

    // org.apache.pluto.core.InternalPortletContext implementation --------------------------------
    public javax.servlet.ServletContext getServletContext()
    {
        return servletContext;
    }

    public PortletApplicationDefinition getInternalPortletApplicationDefinition()
    {
        return portletApplicationDefinition;
    }
    // --------------------------------------------------------------------------------------------
}


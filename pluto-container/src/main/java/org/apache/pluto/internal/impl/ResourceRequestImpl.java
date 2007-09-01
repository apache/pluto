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
package org.apache.pluto.internal.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.Constants;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.internal.InternalPortletWindow;
import org.apache.pluto.internal.InternalResourceRequest;
import org.apache.pluto.spi.PortletURLProvider;
import org.apache.pluto.spi.PublicRenderParameterProvider;
import org.apache.pluto.util.ArgumentUtility;
import org.apache.pluto.util.StringUtils;

public class ResourceRequestImpl extends PortletRequestImpl
implements ResourceRequest, InternalResourceRequest {

	/** Logger. */
    private static final Log LOG = LogFactory.getLog(ResourceRequestImpl.class);
    
    
    // Private Member Variables ------------------------------------------------
    
    /** FIXME: The portlet preferences. */
    private PortletPreferences portletPreferences = null;
    
    
    // Constructor -------------------------------------------------------------
    
    public ResourceRequestImpl(PortletContainer container,
                             InternalPortletWindow internalPortletWindow,
                             HttpServletRequest servletRequest) {
        super(container, internalPortletWindow, servletRequest);
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Created action request for: " + internalPortletWindow);
        }
    }

    // ResourceRequest impl ------------------------------------------------------
    
    /* (non-Javadoc)
     * FIXME: should we set the bodyAccessed flag?
     * @see org.apache.pluto.core.InternalActionResponse#getPortletInputStream()
     */
    public InputStream getPortletInputStream() throws IOException {
        HttpServletRequest servletRequest = (HttpServletRequest) getRequest();
        if (servletRequest.getMethod().equals("POST")) {
            String contentType = servletRequest.getContentType();
            if (contentType == null ||
                contentType.equals("application/x-www-form-urlencoded")) {
                throw new IllegalStateException(
                		"User request HTTP POST data is of type "
                		+ "application/x-www-form-urlencoded. "
                		+ "This data has been already processed "
                		+ "by the portal/portlet-container and is available "
                		+ "as request parameters.");
            }
        }
        return servletRequest.getInputStream();
    }

    public String[] getParameterValues(String name) {
    	ArgumentUtility.validateNotNull("parameterName", name);
    	String values1[] = super.getParameterValues(name);
    	PortletURLProvider urlProvider = container.getRequiredContainerServices().getPortalCallbackService().getPortletURLProvider(getHttpServletRequest(), internalPortletWindow);
    	String values2[] = urlProvider.getPrivateRenderParameters(name);
    	String values[] = null;
    	int length1 = 0;
    	int length2 = 0;
    	if (values1 != null)
    		length1 = values1.length;
    	if (values2 != null){
    		length2 += values2.length;
    		values = new String[length1+length2];
    		System.arraycopy(values2, 0, values, length1, length2);
    	}
    	else if (length1>0){
    		values = new String[length1];
    	}
    	
    	if (length1>0){
    		System.arraycopy(values1, 0, values, 0, length1);
    	}
    	if ((length1+length2) == 0){
    		values = null;
    	}
        if (values != null) {
            values = StringUtils.copy(values);
        }
        return values;
    }
    
    public String getParameter(String name) {
    	
    	ArgumentUtility.validateNotNull("parameterName", name);
    	String value = super.getParameter(name);
    	if (value == null){
    		PortletURLProvider urlProvider = container.getRequiredContainerServices().getPortalCallbackService().getPortletURLProvider(getHttpServletRequest(), internalPortletWindow);
        	String[] values1 = urlProvider.getPrivateRenderParameters(name);
        	if (values1!= null){
        		if (values1.length>0){
        			value = values1[0];
        		}
        	}
    	}
		return value;
    }
    
    // PortletRequestImpl impl -------------------------------------------------
    
    /**
     * FIXME: 
     */
    public PortletPreferences getPreferences() {
        if (portletPreferences == null) {
            portletPreferences = new PortletPreferencesImpl(
            		getPortletContainer(),
            		getInternalPortletWindow(),
            		this,
            		Constants.METHOD_ACTION);
        }
        return portletPreferences;
    }

	public String getETag() {
		// TODO Auto-generated method stub
//		return null;
		throw new UnsupportedOperationException("This method needs to be implemented");
	}

	public String getLifecyclePhase() {
		return RESOURCE_PHASE;
	}

	public String getResourceID() {
		return getParameter("resourceID");
	}

	public Cookie[] getCookieProperties() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method needs to be implemented.");
	}
	
}
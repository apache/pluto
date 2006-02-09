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

import java.util.Enumeration;
import java.util.Vector;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.portlet.PortletSessionUtil;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import org.apache.pluto.core.InternalPortletWindow;
import org.apache.pluto.util.ArgumentUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the <code>javax.portlet.PortletSession</code> interface.
 * 
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 */
public class PortletSessionImpl implements PortletSession, HttpSession {
	
	/** Logger. */
    private static final Log LOG = LogFactory.getLog(PortletSessionImpl.class);
    
    /** The default scope (<code>PORTLET_SCOPE</code>) for storing objects. */
    private static final int DEFAULT_SCOPE = PortletSession.PORTLET_SCOPE;
    
    /** The portlet scope namespace as defined in PLT. 15.3. */
    private static final String PORTLET_SCOPE_NAMESPACE = "javax.portlet.p.";
    
    /** The portlet window ID / attribute name separator as defined in PLT. 15.3. */
    private static final char ID_NAME_SEPARATOR = '?';

    
    // Private Member Variables ------------------------------------------------
    
    /** The wrapped HttpSession object. */
    private HttpSession httpSession = null;
    
    /** The portlet context. */
    private PortletContext portletContext = null;
    
    /** The internal portlet window. */
    private InternalPortletWindow internalPortletWindow = null;
    
    
    // Constructor -------------------------------------------------------------
    
    /**
     * Constructs an instance.
     */
    public PortletSessionImpl(PortletContext portletContext,
                              InternalPortletWindow internalPortletWindow,
                              HttpSession httpSession) {
        this.portletContext = portletContext;
        this.internalPortletWindow = internalPortletWindow;
        this.httpSession = httpSession;
    }
    
    
    // PortletSession Impl: Attributes -----------------------------------------
    
    public Object getAttribute(String name) {
        return getAttribute(name, DEFAULT_SCOPE);
    }
    
    /**
     * Returns the attribute of the specified name under the given scope.
     * 
     * @param name  the attribute name.
     * @param scope  the scope under which the attribute object is stored.
     * @return the attribute object.
     */
    public Object getAttribute(String name, int scope) {
    	ArgumentUtility.validateNotNull("attributeName", name);
    	String key = (scope == PortletSession.APPLICATION_SCOPE)
    			? name : createPortletScopedId(name);
    	return httpSession.getAttribute(key);
    }
    
    public Enumeration getAttributeNames() {
        return getAttributeNames(DEFAULT_SCOPE);
    }
    
    public Enumeration getAttributeNames(int scope) {
    	// Return all attribute names in the nested HttpSession object.
        if (scope == PortletSession.APPLICATION_SCOPE) {
            return httpSession.getAttributeNames();
        }
        // Return attribute names with the portlet-scoped prefix.
        else {
            Vector portletScopedNames = new Vector();
            for (Enumeration en = httpSession.getAttributeNames();
            		en.hasMoreElements(); ) {
            	String name = (String) en.nextElement();
                if (isInCurrentPortletScope(name)) {
                	portletScopedNames.add(
                			PortletSessionUtil.decodeAttributeName(name));
                }
           }
            return portletScopedNames.elements();
        }
    }
    
    public void removeAttribute(String name) {
        removeAttribute(name, DEFAULT_SCOPE);
    }

    public void removeAttribute(String name, int scope) {
    	ArgumentUtility.validateNotNull("attributeName", name);
    	if (scope == PortletSession.APPLICATION_SCOPE) {
    		httpSession.removeAttribute(name);
    	} else {
    		httpSession.removeAttribute(createPortletScopedId(name));
    	}
    }
    
    public void setAttribute(String name, Object value) {
        setAttribute(name, value, DEFAULT_SCOPE);
    }

    public void setAttribute(String name, Object value, int scope) {
    	ArgumentUtility.validateNotNull("attributeName", name);
    	if (scope == PortletSession.APPLICATION_SCOPE) {
    		httpSession.setAttribute(name, value);
    	} else {
    		httpSession.setAttribute(createPortletScopedId(name),  value);
    	}
    }

    
    // PortletSession Impl -----------------------------------------------------
    
    public PortletContext getPortletContext() {
        return portletContext;
    }

    public long getCreationTime() {
        return httpSession.getCreationTime();
    }

    public String getId() {
        return httpSession.getId();
    }

    public long getLastAccessedTime() {
        return httpSession.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return httpSession.getMaxInactiveInterval();
    }

    public void invalidate() throws IllegalStateException {
        httpSession.invalidate();
    }

    public boolean isNew() throws IllegalStateException {
        return httpSession.isNew();
    }

    public void setMaxInactiveInterval(int interval) {
        httpSession.setMaxInactiveInterval(interval);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Session timeout set to: " + interval);
        }
    }
    
    
    // Private Methods ---------------------------------------------------------
    
    /**
     * Creates portlet-scoped ID for the specified attribute name.
     * Portlet-scoped ID for a given attribute name has the following form:
     * <code>javax.portlet.p.&lt;ID&gt;?&lt;name&gt;</code>
     * where <code>ID</code> is a unique identification for the portlet window
     * (assigned by the portal/portlet-container) that must not contain a '?'
     * character. <code>name</code> is the attribute name.
     * <p>
     * Refer to Portlet Specification PLT. 15.3 for more details.
     * </p>
     * @param name  the attribute name.
     * @return portlet-scoped ID for the attribute name.
     */
    private String createPortletScopedId(String name) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(PORTLET_SCOPE_NAMESPACE);
    	buffer.append(internalPortletWindow.getId());
    	buffer.append(ID_NAME_SEPARATOR);
    	buffer.append(name);
    	return buffer.toString();
    }
    
    /**
     * Checks if the attribute name in APPLICATION_SCOPE is in the current
     * portlet scope. 
     * @param name  the attribute name to check.
     * @return true if the attribute name is in the current portlet scope.
     * @see #createPortletScopedId(String)
     */
    private boolean isInCurrentPortletScope(String name) {
    	// Portlet-scoped attribute names MUST start with "javax.portlet.p.",
    	//   and contain the ID-name separator '?'.
    	if (name.startsWith(PORTLET_SCOPE_NAMESPACE)
    			&& name.indexOf(ID_NAME_SEPARATOR) > -1) {
        	String id = name.substring(PORTLET_SCOPE_NAMESPACE.length(),
        	                           name.indexOf(ID_NAME_SEPARATOR));
        	return (id.equals(internalPortletWindow.getId().toString()));
        }
    	// Application-scoped attribute names are not in portlet scope.
    	else {
        	return false;
        }
    }
    
    
    // HttpSession Impl --------------------------------------------------------
    
    public ServletContext getServletContext() {
        return httpSession.getServletContext();
    }

    /**
     * DEPRECATED: implemented for backwards compatability with HttpSession.
     * @deprecated
     */
    public HttpSessionContext getSessionContext() {
        return httpSession.getSessionContext();
    }

    public Object getValue(String name) {
        return this.getAttribute(name, DEFAULT_SCOPE);
    }

    /**
     * DEPRECATED: Implemented for backwards compatibility with HttpSession.
     * @deprecated
     */
    public String[] getValueNames() {
        return httpSession.getValueNames();
    }

    public void putValue(String name, Object value) {
        this.setAttribute(name, value, DEFAULT_SCOPE);
    }

    public void removeValue(String name) {
        this.removeAttribute(name, DEFAULT_SCOPE);
    }
    
}

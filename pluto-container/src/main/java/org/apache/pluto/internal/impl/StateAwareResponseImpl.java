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
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.rmi.MarshalException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.StateAwareResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.PortletContainerException;
import org.apache.pluto.descriptors.portlet.EventDefinitionDD;
import org.apache.pluto.descriptors.portlet.PortletAppDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.pluto.descriptors.portlet.SupportsDD;
import org.apache.pluto.internal.InternalPortletWindow;
import org.apache.pluto.spi.EventProvider;
import org.apache.pluto.spi.PortalCallbackService;
import org.apache.pluto.spi.PublicRenderParameterProvider;
import org.apache.pluto.spi.ResourceURLProvider;
import org.apache.pluto.util.StringUtils;

/**
 * Implementation of JSR-286 <code>StateAwareResponse</code>.
 *
 * @since 2.0
 */
public class StateAwareResponseImpl extends PortletResponseImpl implements
		StateAwareResponse {
	
	/** Logger. */
    private static final Log LOG = LogFactory.getLog(StateAwareResponseImpl.class);

	boolean redirectAllowed = true;
	private boolean redirected;
	private String redirectLocation;
    

	private InternalPortletWindow internalPortletWindow;
	private PortletContainer container;
	private Map<String, String[]> publicRenderParameter = new HashMap<String, String[]>();
	
    private Map renderParameters = new HashMap();
    private WindowState windowState = null;
    private PortletMode portletMode = null;
	private PortalCallbackService callback;
    private PortalContext context;
    
	public StateAwareResponseImpl(PortletContainer container,
			            InternalPortletWindow internalPortletWindow,
			            HttpServletRequest servletRequest,
			            HttpServletResponse servletResponse) {
		super(container, internalPortletWindow, servletRequest,
		servletResponse);
		this.internalPortletWindow = internalPortletWindow;
		this.container = container;
		context = container.getRequiredContainerServices().getPortalContext();
		callback = container.getRequiredContainerServices().getPortalCallbackService();
	}
	
	/* (non-Javadoc)
	 * @see javax.portlet.StateAwareResponse#setEvent(javax.xml.namespace.QName, java.lang.Object)
	 */
	public void setEvent(QName qname, Serializable value) throws IllegalArgumentException {
		EventProvider provider = callback.getEventProvider(
				getHttpServletRequest(),getHttpServletResponse(), container);

		if (isDeclaredAsPublishingEvent(qname)) {
			
			if (!isValueInstanceOfDefinedClass(qname,value))
				throw new IllegalArgumentException("Payload has not the right class");

			XMLStreamReader xml = null;
			try {

				if (value == null) {
					provider.registerToFireEvent(new EventImpl(qname));
				} else if (!(value instanceof Serializable)) {
					throw new IllegalArgumentException("Object payload must implement Serializable");
				}
				else {
	
					Class clazz = value.getClass();

					JAXBContext jc = JAXBContext.newInstance(clazz);

					Marshaller marshaller  = jc.createMarshaller();

					Writer out = new StringWriter();

					marshaller.marshal(new JAXBElement(qname,clazz,value), out);
//					marshaller.marshal(value, out);

					if (out != null) {
						provider.registerToFireEvent(new EventImpl(qname,(Serializable) out.toString()));
					} else { 
						provider.registerToFireEvent(new EventImpl(qname,(Serializable) value));
					}
				}
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PortletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PortletContainerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MarshalException e) {
				// there is no valid jaxb binding
				e.printStackTrace();
			} catch (JAXBException e) {
				// maybe there is no valid jaxb binding
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.portlet.StateAwareResponse#setEvent(java.lang.String, java.lang.Object)
	 */
	public void setEvent(String name, Serializable value) {
		String defaultEventNamespace = getDefaultEventNamespace();
		setEvent((new QName(defaultEventNamespace, name)),value);
	}
	
	/* (non-Javadoc)
	 * @see javax.portlet.StateAwareResponse#setEvents(java.util.Map)
	 */
// Not in API anymore
//	public void setEvents(Map events) {
//		Set keys = events.keySet();
//		for (Object key : keys) {
//			setEvent((QName)key,(Serializable)events.get(key));
//		}
//	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)
	 */
	public void sendRedirect(String location) throws java.io.IOException {
        if (redirectAllowed) {
            if (location != null) {
                HttpServletResponse redirectResponse = getHttpServletResponse();
                while (redirectResponse instanceof HttpServletResponseWrapper) {
                    redirectResponse = (HttpServletResponse)
                        ((HttpServletResponseWrapper)redirectResponse).getResponse();
                }

                ResourceURLProvider provider = callback.getResourceURLProvider(
                                getHttpServletRequest(),
                                getInternalPortletWindow()
                );

                if (location.indexOf("://") != -1) {
                    provider.setAbsoluteURL(location);
                } else {
                    provider.setFullPath(location);
                }
                location =
                redirectResponse.encodeRedirectURL(provider.toString());
                //redirectResponse.sendRedirect(location);
                redirectLocation = location;
                redirected = true;
            }
        } else {
            throw new java.lang.IllegalStateException(
                "Can't invoke sendRedirect() after certain methods have been called");
        }

    }

    

    public String getRedirectLocation() {
        return redirectLocation;
    }
	//
//	 javax.portlet.ActionResponse
	//
    public void setWindowState(WindowState windowState)
        throws WindowStateException {
        if (redirected) {
            throw new IllegalStateException(
                "it is not allowed to invoke setWindowState after sendRedirect has been called");
        }

        if (isWindowStateAllowed(windowState)) {
            this.windowState = windowState;
        } else {
            throw new WindowStateException("Can't set this WindowState",
                                           windowState);
        }
        redirectAllowed = false;
    }
	    
    public void setPortletMode(PortletMode portletMode)
        throws PortletModeException {
        if (redirected) {
            throw new IllegalStateException(
                "it is not allowed to invoke setPortletMode after sendRedirect has been called");
        }

        // check if portal supports portlet mode
        boolean supported = isPortletModeAllowed(portletMode);


        // if porlet mode is allowed
        if (supported) {
            this.portletMode = portletMode;
        } else {
            throw new PortletModeException("Can't set this PortletMode",
                                           portletMode);
        }

        redirectAllowed = false;

    }
    
    public void setRenderParameters(java.util.Map<String, String[]> parameters) {
        if (redirected) {
            throw new IllegalStateException(
                "Can't invoke setRenderParameters() after sendRedirect() has been called");
        }
        if (parameters == null) {
            throw new IllegalArgumentException(
                "Render parameters must not be null.");
        }
        for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException(
                    "Key must not be null and of type java.lang.String.");
            }
            if (!(entry.getValue() instanceof String[])) {
                throw new IllegalArgumentException(
                    "Value must not be null and of type java.lang.String[].");
            }
        }
        
        renderParameters.clear();
        publicRenderParameter.clear();
        if (parameters.keySet()!= null){
        	for (Object key : parameters.keySet()) {
        		this.setRenderParameter((String)key, (String[])parameters.get(key));
    		}
        }
        
        redirectAllowed = false;
    }
    
    public void setRenderParameter(String key, String value) {
        if (redirected) {
            throw new IllegalStateException(
                "Can't invoke setRenderParameter() after sendRedirect() has been called");
        }

        if ((key == null)) {
            throw new IllegalArgumentException(
                "Render parameter key must not be null.");
        }
        PublicRenderParameterProvider provider = container.getRequiredContainerServices().getPortalCallbackService().getPublicRenderParameterProvider();
        //only if the value is null, if it is a public parameter will deleted from list.
        if (value == null){
        	//test if this is a public render parameter
        	if (provider.isPublicRenderParameter(internalPortletWindow.getId().getStringId(), key)){
        		publicRenderParameter.put(key, new String[] {null});
        	}
        	else{
        		throw new IllegalArgumentException(
                	"Render parameter value must not be null.");
        	}
        }
        else if (provider.isPublicRenderParameter(internalPortletWindow.getId().getStringId(), key)){
        	publicRenderParameter.put(key, new String[] {value});
        }
        else{
        	renderParameters.put(key, new String[]{value});
        }
        redirectAllowed = false;
    }
    
    public void setRenderParameter(String key, String[] values) {
    	if (redirected) {
            throw new IllegalStateException(
                "Can't invoke setRenderParameter() after sendRedirect() has been called");
        }        
        
        if (key == null) {
	        throw new IllegalArgumentException(
	        	"name and values must not be null or values be an empty array");
	    }
	    PublicRenderParameterProvider provider = container.getRequiredContainerServices().getPortalCallbackService().getPublicRenderParameterProvider();
	    if (values == null){
	    	if (provider.isPublicRenderParameter(internalPortletWindow.getId().getStringId(), key)){
	    		publicRenderParameter.put(key,new String[] {null});
		    }
	    	else{
	    		throw new IllegalArgumentException(
	    			"name and values must not be null or values be an empty array");
	    	}
	    }
	    
	    if (provider.isPublicRenderParameter(internalPortletWindow.getId().getStringId(), key)){
	    	publicRenderParameter.put(key,StringUtils.copy(values));
	    }
	    else{
	    	renderParameters.put(key, StringUtils.copy(values));
	    }
    }
    // --------------------------------------------------------------------------------------------
    
    // org.apache.pluto.core.InternalActionResponse implementation --------------------------------
    public Map getRenderParameters() {
        return renderParameters;
    }
    
    public Map getRenderParameterMap() {
		return renderParameters;
	}
    
    public Map<String, String[]> getPublicRenderParameter(){
    	return publicRenderParameter;
    }

    public PortletMode getChangedPortletMode() {
        return this.portletMode;
    }
    
    public PortletMode getPortletMode() {
		return getChangedPortletMode();
	}

    public WindowState getChangedWindowState() {
        return this.windowState;
    }
    
    public WindowState getWindowState() {
		return getChangedWindowState();
	}
    
    private boolean isPortletModeAllowed(PortletMode mode) {
        return isPortletModeAllowedByPortlet(mode)
               && isPortletModeAllowedByPortal(mode);
    }

    private boolean isPortletModeAllowedByPortlet(PortletMode mode) {
        PortletDD dd = getInternalPortletWindow().getPortletEntity()
            .getPortletDefinition();

        Iterator supports = dd.getSupports().iterator();
        while(supports.hasNext()) {
            SupportsDD sup = (SupportsDD)supports.next();
            Iterator modes = sup.getPortletModes().iterator();
            while(modes.hasNext()) {
                if (modes.next().toString().equalsIgnoreCase(mode.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPortletModeAllowedByPortal(PortletMode mode) {
        Enumeration supportedModes = context.getSupportedPortletModes();
        while (supportedModes.hasMoreElements()) {
            if (supportedModes.nextElement().toString().equalsIgnoreCase(
                (mode.toString()))) {
                return true;
            }
        }
        return false;
    }

    private boolean isWindowStateAllowed(WindowState state) {
        Enumeration supportedStates = context.getSupportedWindowStates();
        while (supportedStates.hasMoreElements()) {
            if (supportedStates.nextElement().toString().equalsIgnoreCase(
                (state.toString()))) {
                return true;
            }
        }
        return false;
    }

	public void setNextPossiblePortletModes(Enumeration portletModes) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method needs to be implemented.");
	}
	
// Not in API anymore
//	public void setDefaultNamspacedEvents(Map<String, Object> events) {
//		String defaultEventNamespace = getDefaultEventNamespace();
//		Set<String> keys = events.keySet();
//		for (String key : keys) {
//			setEvent((new QName(defaultEventNamespace, key)),(Serializable) events.get(key));
//		}	
//	}

	/**
	 * Gets the default event namespace.
	 * 
	 * @return the default event namespace
	 */
	private String getDefaultEventNamespace() {
		EventProvider provider = callback.getEventProvider(
				getHttpServletRequest(),getHttpServletResponse(), container);
		String defaultEventNamespace = provider.getDefaultEventNamespace(this.getInternalPortletWindow().getPortletName());
		return (defaultEventNamespace == null || defaultEventNamespace.equals("")) ? XMLConstants.NULL_NS_URI : defaultEventNamespace; 
	}
	
	// ****** private methods ******

	private boolean isDeclaredAsPublishingEvent(QName qname) {
		List<QName> events = internalPortletWindow.getPortletEntity()
			.getPortletDefinition().getPublishingEvents();
		return events != null && events.contains(qname);
	}
	
	private boolean isValueInstanceOfDefinedClass(QName qname, Serializable value){
		PortletAppDD portletAppDD = null;
		try {
			 portletAppDD =
				container.getPortletApplicationDescriptor(internalPortletWindow.getContextPath());
			 if (portletAppDD.getEvents() != null) {
				 for (EventDefinitionDD event : portletAppDD.getEvents()) {
					 if (event.getName().toString().equals(qname.toString())){
						 return value.getClass().getName().equals(event.getJavaClass());
					 }
				 }
			 }
		} catch (PortletContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// event not declared
		return true;
	}
}

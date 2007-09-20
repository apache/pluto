/*
 * Copyright 2006 The Apache Software Foundation.
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
package org.apache.pluto.driver.services.container;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.Event;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.Constants;
import org.apache.pluto.EventContainer;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.PortletContainerException;
import org.apache.pluto.PortletWindow;
import org.apache.pluto.core.PortletContainerImpl;
import org.apache.pluto.descriptors.portlet.EventDefinitionDD;
import org.apache.pluto.descriptors.portlet.PortletAppDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.pluto.driver.AttributeKeys;
import org.apache.pluto.driver.config.DriverConfiguration;
import org.apache.pluto.driver.core.PortletWindowImpl;
import org.apache.pluto.driver.services.portal.PageConfig;
import org.apache.pluto.driver.services.portal.PortletApplicationConfig;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.url.PortalURL;
import org.apache.pluto.driver.url.impl.PortalURLParserImpl;
import org.apache.pluto.internal.impl.EventImpl;
import org.apache.pluto.spi.EventProvider;

public class EventProviderImpl implements org.apache.pluto.spi.EventProvider {
	
	private static final int DEFAULT_MAPSIZE = 10;

	/** Logger. */
    private static final Log LOG = LogFactory.getLog(EventProviderImpl.class);

	private static final long WAITING_CYCLE = 100;
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PortletContainer container;
    
    ThreadGroup threadGroup = new ThreadGroup("FireEventThreads");
    
    private EventList savedEvents = new EventList();
    
    private Map<String,PortletWindowThread> portletWindowThreads = new HashMap<String, PortletWindowThread>();
    	
    /**
     * factory method
     * gets the EventProvider out of the Request, or sets a new one
     * @param request The {@link HttpServletRequest} of the EventProvider
     * @param response The {@link HttpServletResponse} of the EventProvider
     * @return The corresponding EventProvierImpl instance
     */
    public static EventProviderImpl getEventProviderImpl(HttpServletRequest request,
    		HttpServletResponse response, PortletContainer container) {
    	EventProviderImpl event = (EventProviderImpl) request.getAttribute(Constants.PROVIDER);
    	if (event == null) {
    		event = new EventProviderImpl();
    		event.request = request;
    		event.response = response;
    		event.container = container;
    		request.setAttribute(Constants.PROVIDER, event);
    	}
    	return event;
    }
    
    /**
     * factory method, for accessing the static elements without a
     * request / response
     * FIXME: bad idea 
     * @return The EventProvider for accessing the static elements
     */
    public static EventProvider getEventProviderImpl() {
		return new EventProviderImpl();
	}
   
    /**
     * c'tor
     */
    private EventProviderImpl(){
    	
    }
    
    /**
     * Register an event, which should be fired within that request
     * @param qname
     * @param value
     * @throws {@link IllegalArgumentException}
     */
    public void registerToFireEvent(QName qname, Serializable value) 
    throws IllegalArgumentException {
			
		try {

			if (value == null) {
				savedEvents.addEvent(new EventImpl(qname,value));
			} else if (!(value instanceof Serializable)) {
				throw new IllegalArgumentException("Object payload must implement Serializable");
			}
			else {
				
				Class clazz = value.getClass();

				JAXBContext jc = JAXBContext.newInstance(clazz);

				Marshaller marshaller  = jc.createMarshaller();

				Writer out = new StringWriter();

				JAXBElement<Serializable> element = new JAXBElement<Serializable>(qname,clazz,value);
				marshaller.marshal(element, out);
//					marshaller.marshal(value, out);

				if (out != null) {
					savedEvents.addEvent(new EventImpl(qname,(Serializable) out.toString()));
				} else { 
					savedEvents.addEvent(new EventImpl(qname,value));
				}
			}
		} catch (JAXBException e) {
			// maybe there is no valid jaxb binding
			// TODO wsrp:eventHandlingFailed
			LOG.error("Event handling failed", e);
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
		}
    }

	/**
	 * Fire all saved events
	 * Note, that the order isn't important @see PLT14.3.2 
	 * @param eventContainer The {@link PortletContainerImpl} to fire the events
	 */
	public void fireEvents(EventContainer eventContainer) {
		ServletContext servletContext = eventContainer.getServletContext();
		DriverConfiguration driverConfig = (DriverConfiguration) servletContext.getAttribute(
        		AttributeKeys.DRIVER_CONFIG);

		PortalURL portalURL = PortalURLParserImpl.getParser().parse(request);
		
		while (savedEvents.hasMoreEvents() && savedEvents.getSize() < Constants.MAX_EVENTS_SIZE){
			
			Event eActual = getArbitraryEvent();
			
			this.savedEvents.setProcessed(eActual);
			
        	List<String> portletNames = getAllPortletsRegisteredForEvent(eActual,driverConfig);
        	
        	Collection<PortletWindowConfig> portlets = getAllPortlets(driverConfig);
        	
        	// iterate all portlets in the portal        	
        	for (PortletWindowConfig config : portlets) {
        		PortletWindow window = new PortletWindowImpl(config, portalURL);
        		if (portletNames != null) {
        			for (String portlet : portletNames) {
        				if (portlet.equals(config.getId())){

        					// the thread now is a new one, with possible waiting,
        					// for the old to exit
        					PortletWindowThread portletWindowThread = 
        						getPortletWindowThread(eventContainer, config, window);

        					// is this event 
        					portletWindowThread.addEvent(eActual);

        					portletWindowThread.start();
        				}
        			}
        		}
        	}
        	waitForEventExecution();
        	try {
				Thread.sleep(WAITING_CYCLE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		waitForEventExecution();
	}

	private List<String> getAllPortletsRegisteredForEvent(Event event, DriverConfiguration driverConfig) {
		Set<String> resultSet = new HashSet<String>();
		List<String> resultList = new ArrayList<String>();
		QName eventName = event.getQName();
		Collection<PortletWindowConfig> portlets = getAllPortlets(driverConfig);
		
		for (PortletWindowConfig portlet : portlets) {
			String contextPath = portlet.getContextPath();
			PortletAppDD portletAppDD = null;
			try {
				portletAppDD = container.getPortletApplicationDescriptor(contextPath);
				List<PortletDD> portletDDs = portletAppDD.getPortlets();
				List<QName> aliases = getAllAliases(eventName, portletAppDD);
				for (PortletDD portletDD : portletDDs) {
					List<QName> processingEvents = portletDD.getProcessingEvents();
					if ((processingEvents != null) && processingEvents.contains(eventName)){
						if (portletDD.getPortletName().equals(portlet.getPortletName()) ){
							resultSet.add(portlet.getId());							
						}
					} else {
						
						if (processingEvents!= null) {
							for (QName name : processingEvents) {		
								// add also grouped portlets, that ends with "."
								if (name.toString().endsWith(".") && eventName.toString().startsWith(name.toString())
										&& portletDD.getPortletName().equals(portlet.getPortletName())){
									resultSet.add(portlet.getId());
								}
								// also look for alias names:
								if (aliases != null) {
									for (QName alias : aliases) {
										if (alias.toString().equals(name.toString()) && 
												portletDD.getPortletName().equals(portlet.getPortletName())) {
											resultSet.add(portlet.getId());
										}
									}
								}								
								// also look for default namespaced events
								if (name.getNamespaceURI() == null || name.getNamespaceURI().equals("")){
									String defaultNamespace = portletAppDD.getDefaultNamespace();
									QName qname = new QName(defaultNamespace,name.getLocalPart());
									if (eventName.toString().equals(qname.toString()) &&
											portletDD.getPortletName().equals(portlet.getPortletName())){
										resultSet.add(portlet.getId());
									}
								}
							}
						}
					} 
				}
			} catch (PortletContainerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//make list
		for (String name : resultSet) {
			resultList.add(name);
		}
		return resultList;
	}

	private List<QName> getAllAliases(QName eventName, PortletAppDD portletAppDD) {
		if (portletAppDD.getEvents() != null) {
			for (EventDefinitionDD eventDefinition : portletAppDD.getEvents()) {
				if (eventName.toString().equals(eventDefinition.getName().toString())){
					return eventDefinition.getAlias();
				}
			}
		}
		return null;
	}

	/**
	 * gets the right PortletWindowThread or makes a new one,
	 * if theres none
	 * @param eventContainer
	 * @param config
	 * @param window
	 * @return
	 */
	private PortletWindowThread getPortletWindowThread(EventContainer eventContainer, PortletWindowConfig config, PortletWindow window) {
		String windowID = window.getId().getStringId();
		PortletWindowThread portletWindowThread = portletWindowThreads.get(windowID);
		if (portletWindowThread == null){
			portletWindowThread = new PortletWindowThread(
					threadGroup,config.getId(),this, window, eventContainer);
			portletWindowThreads.put(windowID, portletWindowThread);
		} else {
			// a thread could be started twice, so we make a new one,
			// after the old thread stopped
//			try {
			try {
				portletWindowThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			portletWindowThreads.remove(portletWindowThread);
			portletWindowThread = new PortletWindowThread(
					threadGroup,config.getId(),this, window, eventContainer);
			portletWindowThreads.put(windowID, portletWindowThread);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return portletWindowThread;
	}

	/**
	 * Wait for event execution.
	 */
	private void waitForEventExecution() {
		long counter = 0;
		while (threadGroup.activeCount()>0) {
			try {
				counter =+ WAITING_CYCLE;
				if (counter > 500) {
					threadGroup.stop();
				}
				Thread.sleep(WAITING_CYCLE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * gets an arbitrary event, which is not processed yet.
	 * @return the arbitrary event
	 */
	private Event getArbitraryEvent() {
		Event eActual = null;
		for (Event event : this.savedEvents.getEvents()) {
			if (this.savedEvents.isNotProcessed(event)){
				eActual = event;
			}				
		}
		return eActual;
	}


	/**
	 * 
	 */
	private Collection<PortletWindowConfig> getAllPortlets(DriverConfiguration driverConfig) {
		Collection<PortletApplicationConfig> apps = driverConfig.getPortletApplications();
		Collection<PortletWindowConfig> portlets = new ArrayList<PortletWindowConfig>();
		for (PortletApplicationConfig app : apps) {
			portlets.addAll(app.getPortlets());			
		}
		return portlets;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
	
	/**
	 * Gets the saved events.
	 * 
	 * @return the saved events
	 */
	public EventList getSavedEvents(){
		return savedEvents;
	}

}

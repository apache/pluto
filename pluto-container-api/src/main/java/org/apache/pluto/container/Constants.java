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
package org.apache.pluto.container;

import org.apache.pluto.container.spi.optional.PortletRequestContext;

/**
 * Constant values used to bind internal portlet objects as attributes,
 * typically to a request.
 * @version 1.0
 */
public class Constants {

    /**
     * The key used to bind the <code>PortletRequest</code> to the underlying
     * <code>HttpServletRequest</code>.
     */
    public final static String PORTLET_REQUEST = "javax.portlet.request";

    /**
     * The key used to bind the <code>PortletResponse</code> to the underlying
     * <code>HttpServletRequest</code>.
     */
    public final static String PORTLET_RESPONSE = "javax.portlet.response";

    /**
     * The key used to bind the <code>PortletConfig</code> to the underlying
     * PortletConfig.
     */
    public final static String PORTLET_CONFIG = "javax.portlet.config";

    /**
     * The <code>PortletRequest</code> key used to retrieve the <code>PortletRequestContext</code> instance
     */
    public final static String REQUEST_CONTEXT = PortletRequestContext.class.getName();

    /**
     *
     */
    public final static String TITLE_KEY = "javax.portlet.title";

    /**
     *
     */
    public final static String SHORT_TITLE_KEY = "javax.portlet.short-title";

    /**
     * 
     */
    public final static String KEYWORDS_KEY = "javax.portlet.keywords";

    /**
     * The key used to bind the method of processing being requested by the
     * container to the underlying <code>PortletRquest</code>.
     */
    public final static String METHOD_ID = "org.apache.pluto.core.method";


    /**
     * The unique method identifier for render requests.  Render requests are
     * requested through a call to the {@link PortletContainer#doRender(org.apache.pluto.container.PortletWindow,
        * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    public final static Integer METHOD_RENDER = new Integer(1);

    /**
     * The unique method identifier for render requests.  Render requests are
     * requested through a call to the {@link PortletContainer#doAction(org.apache.pluto.container.PortletWindow,
        * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    public final static Integer METHOD_ACTION = new Integer(3);

    /**
     * The unique method identifier for noop (load) requests.  Load requests are
     * requested through a call to the {@link PortletContainer#doLoad(org.apache.pluto.container.PortletWindow,
        * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    public final static Integer METHOD_NOOP = new Integer(5);

    /**
     * The unique method identifier for resource Serving requests.  Resource requests are
     * requested through a call to the {@link PortletContainer#doServeResource(PortletWindow,
     *  javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    public final static Integer METHOD_RESOURCE = new Integer(7);
    
    /**
     * The unique method identifier for render requests.  Render requests are
     * requested through a call to the {@link PortletContainer#doEvent(org.apache.pluto.container.PortletWindow,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    public final static Integer METHOD_EVENT = new Integer(9);
    
    /**
     * The unique method identifier for admin requests.  Admin requests
     * are requested through a call to the {@link PortletContainer#doAdmin(PortletWindow, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
     * method.
     */
    public final static Integer  METHOD_ADMIN = new Integer(11);
    
	/**
	 * the public key, to store the EventProvider in the Request
	 */
	public static final String PROVIDER = "EventProvider";
	
	/**
	 * The public key, to store the event name in the request 
	 */
	public static final String EVENT_NAME = "EventName";
	
	/**
	 * The public key, to store the PublicRenderProvider in the request
	 */
	public static final String PUBLIC_RENDER_PARAMETER_PROVIDER = "PublicRenderParameterProvider";
	
	/**
	 * The public key, to store the FilterManager in the request.
	 */
	public static final String FILTER_MANAGER = "FilterManager";
	
	/**
	 * The public key, to store, if the render parameter are already cleared in the request
	 */
	public static final String RENDER_ALREADY_CLEARED = "RenderAlreadyCleared";

	public static final String EVENT_NUMBER = "EventNumber";

	public static final int MAX_EVENTS_SIZE = 10;

	public static final long THREAD_DELAY_TIME = 300;

}

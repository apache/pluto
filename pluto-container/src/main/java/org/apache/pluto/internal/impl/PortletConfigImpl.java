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

import org.apache.pluto.internal.InternalPortletConfig;
import org.apache.pluto.descriptors.common.InitParamDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.servlet.ServletConfig;
import javax.xml.namespace.QName;

import java.util.*;

public class PortletConfigImpl implements PortletConfig, InternalPortletConfig {

    private static final Log LOG = LogFactory.getLog(PortletConfigImpl.class);

    /**
     * The servlet config for which we exist.
     */
    private ServletConfig servletConfig;

    /**
     * The Portlet Application Context within which we exist.
     */
    private PortletContext portletContext;

    /**
     * The portlet descriptor.
     */
    protected PortletDD portletDD;

    private ResourceBundleFactory bundles;

    public PortletConfigImpl(ServletConfig servletConfig,
                             PortletContext portletContext,
                             PortletDD portletDD) {
        this.servletConfig = servletConfig;
        this.portletContext = portletContext;
        this.portletDD = portletDD;
    }

    public String getPortletName() {
        return portletDD.getPortletName();
    }

    public PortletContext getPortletContext() {
        return portletContext;
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Resource Bundle requested: "+locale);
        }
        if (bundles == null) {
            bundles = new ResourceBundleFactory(portletDD);
        }
        return bundles.getResourceBundle(locale);
    }

    public String getInitParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name == null");
        }

        Iterator<InitParamDD> parms = portletDD.getInitParams().iterator();
        while(parms.hasNext()) {
            InitParamDD param = parms.next();
            if (param.getParamName().equals(name)) {
                return param.getParamValue();
            }
        }
        return null;
    }

    public Enumeration<String> getInitParameterNames() {
        return new java.util.Enumeration<String>() {
            private Iterator<InitParamDD> iterator =
                new ArrayList<InitParamDD>(portletDD.getInitParams()).iterator();

            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            public String nextElement() {
                if (iterator.hasNext()) {
                    return iterator.next().getParamName();
                } 
                return null;
            }
        };
    }


    public javax.servlet.ServletConfig getServletConfig() {
        return servletConfig;
    }

    public PortletDD getPortletDefinition() {
        return portletDD;
    }
    // --------------------------------------------------------------------------------------------

	public Enumeration<String> getPublicRenderParameterNames() {
		if (portletDD.getPublicRenderParameter() != null){
			return Collections.enumeration(portletDD.getPublicRenderParameter());
		}
		return null;
	}

	public String getDefaultNamespace() {
		// how to get the corresponding porletAppDD
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method needs to be implemented.");
	}

	public Enumeration<QName> getProcessingEventQNames() {
		
		return (portletDD.getProcessingEvents() != null) ? 
				Collections.enumeration(portletDD.getProcessingEvents()) :
					null;
	}

	public Enumeration<QName> getPublishingEventQNames() {
		return (portletDD.getPublishingEvents() != null) ?
				Collections.enumeration(portletDD.getPublishingEvents()) :
					null;
	}

	public Enumeration<Locale> getSupportedLocales() {
		// for each String entry in SupportedLocales (portletDD)
		// add an entry in the resut list (new Locale(string))
		List<Locale> locals = new ArrayList<Locale>();
		List<String> localsAsStrings = portletDD.getSupportedLocale();
		for (String string : localsAsStrings) {
			locals.add(new Locale(string));
		}
		return Collections.enumeration(locals);
	}
}

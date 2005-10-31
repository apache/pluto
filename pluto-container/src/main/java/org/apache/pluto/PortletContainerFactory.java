/*
 * Copyright 2004 The Apache Software Foundation.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.core.PortletContainerImpl;
import org.apache.pluto.core.DefaultOptionalServices;
import org.apache.pluto.services.PortletContainerServices;
import org.apache.pluto.services.OptionalPortletContainerServices;
import org.apache.pluto.util.ArgumentUtility;

/**
 * Factory used to create new PortletContainer instances.  The factor constructs
 * the underlying pluto container implementation by using the the given
 * container services.
 *
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @version 1.0
 * @since Sep 18, 2004
 */
public class PortletContainerFactory {

    /** Internal Logger. */
    private static final Log LOG =
        LogFactory.getLog(PortletContainerFactory.class);


    /**
     * Singleton instance of the <code>PortletContainerFactory</code>
     */
    private static PortletContainerFactory factory;

    /**
     * Accessor method for the singleton instance of the
     * <code>PortletContainerFactory</code>.
     * @return singleton instance of the PortletContainerFactory
     */
    public static PortletContainerFactory getInstance() {
        if (factory == null) {
            factory = new PortletContainerFactory();
        }
        return factory;
    }

    /**
     * Hidden constructor.
     */
    private PortletContainerFactory() {

    }

    /**
     * Create a container with the given containerName, initialized from the given
     * servlet config, and using the given container services.
     * @param containerName
     * @param services
     * @return newly created PortletContainer
     * @throws PortletContainerException
     */
    public PortletContainer createContainer(String containerName,
                                            PortletContainerServices services)
        throws PortletContainerException {
        return createContainer(containerName, services, new DefaultOptionalServices());
   }

    public PortletContainer createContainer(String containerName,
                                            PortletContainerServices services,
                                            OptionalPortletContainerServices optionalServices) {

        ArgumentUtility.validateNotNull("containerServices", services);
        ArgumentUtility.validateNotEmpty("containerName", containerName);

        PortletContainer container =
                new PortletContainerImpl(containerName, services, optionalServices);

        if (LOG.isInfoEnabled()) {
            LOG.info("Portlet Container [" + containerName + "] created.");
        }

        return container;
    }
}


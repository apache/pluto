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
package org.apache.pluto.driver.tags;

import java.io.IOException;

import javax.portlet.PortletMode;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pluto.driver.AttributeKeys;
import org.apache.pluto.driver.config.DriverConfiguration;
import org.apache.pluto.driver.core.PortalRequestContext;
import org.apache.pluto.driver.services.portal.PortletWindowConfig;
import org.apache.pluto.driver.url.PortalURL;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * The tag is used to render a portlet mode anchor specified by the portlet ID and mode.
 * This is designed to live inside of a <pluto:portlet/> tag.
 * 
 * <pluto:modeAnchor portletId="" portletMode="edit"/>
 *
 * @author <a href="mailto:esm@apache.org">Elliot Metsger</a>
 * @author <a href="mailto:cdoremus@apache.org">Craig Doremus</a>
 * @todo Test supported Window States using a version of ActionResponseImpl.isWindowStateAllowed()
 */
public class PortletModeAnchorTag extends BodyTagSupport {
    
    /** Logger. */
    private static final Log LOG = LogFactory.getLog(PortletModeAnchorTag.class);
        
    
    // Private Member Variables ------------------------------------------------
    private String portletMode = null;
    
    /** The portlet ID attribute obtained from parent tag. */
    private String portletId = null;
    
    /** The evaluated value of the portlet ID attribute. */
    private String evaluatedPortletId = null;       
    
    // BodyTagSupport Impl -----------------------------------------------------
    
    /**
     * Method invoked when the start tag is encountered.
     * @throws JspException  if an error occurs.
     */
    public int doStartTag() throws JspException {
        
        // Ensure that the modeAnchor tag resides within a portlet tag.
        PortletTag parentTag = (PortletTag) TagSupport.findAncestorWithClass(
                this, PortletTag.class);
        if (parentTag == null) {
            throw new JspException("Portlet window controls may only reside "
                    + "within a pluto:portlet tag.");
        }
        
        portletId = parentTag.getPortletId();        
        // Evaluate portlet ID attribute.
        evaluatePortletId();
        
        // Retrieve the portlet window config for the evaluated portlet ID.
        ServletContext servletContext = pageContext.getServletContext();
        DriverConfiguration driverConfig = (DriverConfiguration)
                servletContext.getAttribute(AttributeKeys.DRIVER_CONFIG);
       
        if (isPortletModeAllowed(driverConfig, portletMode)) {
            // Retrieve the portal environment.
            PortalRequestContext portalEnv = PortalRequestContext.getContext(
                    (HttpServletRequest) pageContext.getRequest());        

            PortalURL portalUrl =  portalEnv.createPortalURL();
            portalUrl.setPortletMode(evaluatedPortletId, new PortletMode(portletMode));

            // Print the mode anchor tag.
            try {
                JspWriter out = pageContext.getOut();
                out.print("<a href=\"");
                out.print(portalUrl.toString());
                out.print("\">");
                out.print("<span class=\"");
                out.print(portletMode);
                out.print("\"></span></a>");
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        
        
        // Continue to evaluate the tag body.
        return EVAL_BODY_INCLUDE;
    }
    
    
    // Package Methods ---------------------------------------------------------
    
    /**
     * Returns the evaluated portlet ID.
     * @return the evaluated portlet ID.
     */
    String getEvaluatedPortletId() {
        return evaluatedPortletId;
    }

    
    
    // Private Methods ---------------------------------------------------------
    
    /**
     * Evaluates the portlet ID attribute passed into this tag. This method
     * evaluates the member variable <code>portletId</code> and saves the
     * evaluated result to <code>evaluatedPortletId</code>
     * @throws JspException  if an error occurs.
     */
    private void evaluatePortletId() throws JspException {
        Object obj = ExpressionEvaluatorManager.evaluate(
                "portletId", portletId, String.class, this, pageContext);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Evaluated portletId to: " + obj);
        }
        evaluatedPortletId = (String) obj;
    }

    /**
     * @return the portletMode
     */
    public String getPortletMode() {
        return portletMode;
    }

    /**
     * @param portletMode the portletMode to set
     */
    public void setPortletMode(String portletMode) {
        this.portletMode = portletMode;
    }
    
    private boolean isPortletModeAllowed(DriverConfiguration config, String mode) {
        LOG.debug("Testing if PortletWindowConfig [" + getEvaluatedPortletId() + "] supports mode [" + mode + "]");
        return config.isPortletModeSupported(getEvaluatedPortletId(), mode);       
    }

    
}
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

import org.apache.pluto.core.InternalRenderRequest;
import org.apache.pluto.core.InternalRenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Implementation of the <code>PortletRequestDispatcher</code> interface.
 * The portlet request dispatcher is used to dispatch <b>RenderRequest</b> and
 * <b>RenderResponse</b> to a URI. Note that ActionRequest and ActionResponse
 * can never be dispatched.
 * 
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 */
public class PortletRequestDispatcherImpl implements PortletRequestDispatcher {
	
	/** Logger. */
    private static final Log LOG = LogFactory.getLog(PortletRequestDispatcherImpl.class);
    
    
    // Private Member Variables ------------------------------------------------
    
    /** The nested servlet request dispatcher instance. */
    private RequestDispatcher requestDispatcher = null;
    
    /** The appended query parameters. */
    private Map queryParams = null;
    
    
    // Constructors ------------------------------------------------------------
    
    public PortletRequestDispatcherImpl(RequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public PortletRequestDispatcherImpl(RequestDispatcher requestDispatcher,
                                        Map queryParams) {
        this(requestDispatcher);
        this.queryParams = queryParams;
    }
    
    
    // PortletRequestDispatcher Impl -------------------------------------------
    
    public void include(RenderRequest request, RenderResponse response)
    throws PortletException, IOException {

        InternalRenderRequest internalRequest = (InternalRenderRequest)
                InternalImplConverter.getInternalRequest(request);
        InternalRenderResponse internalResponse = (InternalRenderResponse)
                InternalImplConverter.getInternalResponse(response);

        if (queryParams != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating Included Render Request to override query parameters.");
            }
            internalRequest = new IncludedRenderRequestImpl(
            		internalRequest, queryParams);
        }
        boolean isIncluded = (internalRequest.isIncluded()
        		|| internalResponse.isIncluded());
        try {
        	internalRequest.setIncluded(true);
        	internalResponse.setIncluded(true);

            requestDispatcher.include(
            		(HttpServletRequest) internalRequest,
            		(HttpServletResponse) internalResponse);
        } catch (IOException ex) {
            throw ex;
        } catch (ServletException ex) {
            if (ex.getRootCause() != null) {
                throw new PortletException(ex.getRootCause());
            } else {
                throw new PortletException(ex);
            }
        } finally {
        	internalRequest.setIncluded(isIncluded);
        	internalResponse.setIncluded(isIncluded);
        }
    }
    
}

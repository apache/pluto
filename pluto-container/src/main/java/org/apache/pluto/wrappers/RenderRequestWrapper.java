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
package org.apache.pluto.wrappers;

import javax.portlet.RenderRequest;

public class RenderRequestWrapper extends PortletRequestWrapper
    implements RenderRequest {

    /**
     * Creates a ServletRequest adaptor wrapping the given request object.
     * @throws java.lang.IllegalArgumentException
     *          if the request is null.
     */
    public RenderRequestWrapper(RenderRequest renderRequest) {
        super(renderRequest);

        if (renderRequest == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
    }

    // javax.portlet.RenderRequest implementation -------------------------------------------------

    /**
     * Returns an implementation of JSR-286 <code>ETag</code>.
     *
     * @since 2.0
     */
    public String getETag() {
		return getRenderRequest().getETag();
	}
    
    // --------------------------------------------------------------------------------------------
    
    // additional methods -------------------------------------------------------------------------
    /**
     * Return the wrapped ServletRequest object.
     */
    public RenderRequest getRenderRequest() {
        return (RenderRequest) getPortletRequest();
    }

    // --------------------------------------------------------------------------------------------
}


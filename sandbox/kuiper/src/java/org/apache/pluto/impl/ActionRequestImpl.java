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

package org.apache.pluto.impl;

import org.apache.pluto.PortletWindow;
import org.apache.pluto.PlutoEnvironment;

import javax.portlet.ActionRequest;
import javax.portlet.PortletContext;
import javax.portlet.PortalContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.IOException;

/**
 *
 * @author <A href="mailto:david.dewolf@vivare.com">David H. DeWolf</A>
 * @version 1.0
 * @since Mar 10, 2004 at 11:13:31 AM
 */
public class ActionRequestImpl extends PortletRequestImpl
    implements ActionRequest {

    public ActionRequestImpl(PlutoEnvironment plutoEnvironment,
                             PortletContext portletContext,
                             PortletWindow window,
                             HttpServletRequest request) {
        super(plutoEnvironment, portletContext, window, request);
    }

    public InputStream getPortletInputStream()
    throws IOException {
        return super.getInputStream();
    }
}

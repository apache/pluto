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
package org.apache.pluto.spi.optional;

import org.apache.pluto.internal.InternalPortletWindow;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Service used to invoke portlets.
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 */
public interface PortletInvokerService {
	
    public void action(ActionRequest req, ActionResponse res, InternalPortletWindow window)
    throws IOException, PortletException;

    public void render(RenderRequest req, RenderResponse res, InternalPortletWindow window)
    throws IOException, PortletException;

    public void load(PortletRequest req, PortletResponse res, InternalPortletWindow window)
    throws IOException, PortletException;

    public void admin(PortletRequest req, PortletResponse res, InternalPortletWindow window)
    throws IOException, PortletException;
}

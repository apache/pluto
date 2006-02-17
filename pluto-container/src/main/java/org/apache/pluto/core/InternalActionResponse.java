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

package org.apache.pluto.core;

import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

/**
 * The internal action response interface extends the internal portlet response
 * interface and provides some action-specific methods.
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 */
public interface InternalActionResponse extends InternalPortletResponse {

    /**
     * Retrieves the render parameters associated with this response.
     * @return map of all render parameters associated with this request.
     */
    public Map getRenderParameters();

    /**
     * Retrieve the portlet mode.
     * @return the changed portlet mode.
     */
    public PortletMode getChangedPortletMode();

    /**
     * Retrieve the window state.
     * @return the changed window state.
     */
    public WindowState getChangedWindowState();

    /**
     * Retreive the location for which the action should
     * be redirected.
     * @return the redirect location.
     */
    public String getRedirectLocation();
    
}



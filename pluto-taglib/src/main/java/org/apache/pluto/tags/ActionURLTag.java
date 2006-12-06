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

/*
 * Created on Feb 21, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.apache.pluto.tags;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

/**
 * Supporting class for the <CODE>actionURL</CODE> tag. Creates a url that
 * points to the current Portlet and triggers an action request with the
 * supplied parameters.
 */
public class ActionURLTag extends BasicURLTag {

    protected PortletURL createPortletURL() {
        RenderResponse renderResponse = (RenderResponse) pageContext.getRequest()
            .getAttribute("javax.portlet.response");
        return renderResponse.createActionURL();
    }

}


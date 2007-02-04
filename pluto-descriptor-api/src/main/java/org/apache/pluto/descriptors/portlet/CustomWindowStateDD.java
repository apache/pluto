/*
 * Copyright 2005-2007 The Apache Software Foundation
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
package org.apache.pluto.descriptors.portlet;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @version $Id$
 * @since 1.1.0
 */
public class CustomWindowStateDD {

    private List descriptions = new ArrayList();

    private String windowState;

    public List getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List descriptions) {
        this.descriptions = descriptions;
    }

    public String getWindowState() {
        return windowState;
    }

    public void setWindowState(String windowState) {
        this.windowState = windowState;
    }
}

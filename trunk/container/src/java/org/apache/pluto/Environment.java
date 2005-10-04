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
package org.apache.pluto;

import java.util.ResourceBundle;

/**
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 */
public final class Environment {

    public static final ResourceBundle PROPS;

    static {
        PROPS = ResourceBundle.getBundle("org.apache.pluto.environment");
    }


    public static final String getPortletContainerName() {
        return PROPS.getString("pluto.container.name");
    }

    public static final String getPortletContainerMajorVersion() {
        return PROPS.getString("pluto.container.version.major");
    }

    public static final String getPortletContainerMinorVersion() {
        return PROPS.getString("pluto.container.version.minor");
    }

    public static final int getMajorSpecificationVersion() {
        return Integer.parseInt(PROPS.getString("javax.portlet.version.major"));
    }

    public static final int getMinorSpecificationVersion() {
        return Integer.parseInt(PROPS.getString("javax.portlet.version.minor"));
    }

    public static final String getServerInfo() {
        StringBuffer sb = new StringBuffer(getPortletContainerName())
            .append("/")
            .append(getPortletContainerMajorVersion())
            .append(".")
            .append(getPortletContainerMinorVersion());
        return sb.toString();
    }

}

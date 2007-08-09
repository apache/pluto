/*
 * Copyright 2005 The Apache Software Foundation
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
package org.apache.pluto.descriptors.servlet;

/**
 * <B>TODO</B>: Document
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @version $Id: UserDataConstraintDD.java 156636 2005-03-09 12:16:31Z cziegeler $
 * @since Feb 28, 2005
 */
public class UserDataConstraintDD {

    public static final String NONE = "NONE";
    public static final String INTEGRAL = "INTEGRAL";
    public static final String CONFIDENTIAL = "CONFIDENTIAL";

    private String description;
    private String transportGuarantee;

    public UserDataConstraintDD() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransportGuarantee() {
        return transportGuarantee;
    }

    public void setTransportGuarantee(String transportGuarantee) {
        this.transportGuarantee = transportGuarantee;
    }

}


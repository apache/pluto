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

package org.apache.pluto.om.common;

/**
 * <P>
 * The <CODE>Parameter</CODE> interface ...
 * </P>
 * <P>
 * This interface defines the controller as known from the MVC pattern.
 * Its purpose is to provide write access to the data stored in the model.
 * </P>
 * 

 */
public interface ParameterCtrl extends org.apache.pluto.om.Controller
{


    /**
     * Sets the name
     * 
     * @param name   the new name
     */
    public void setName(String name);

    /**
     * Sets the value
     * 
     * @param value   the new value
     */
    public void setValue(String value);

    /**
     * Sets the description
     * 
     * @param descriptions the new description
     */
    public void setDescriptionSet(DescriptionSet descriptions);

}

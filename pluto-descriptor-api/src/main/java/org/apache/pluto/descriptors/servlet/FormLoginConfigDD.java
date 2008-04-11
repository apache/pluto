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
package org.apache.pluto.descriptors.servlet;

import org.apache.pluto.om.servlet.FormLoginConfig;

/**
 * <B>TODO</B>: Document
 * @version $Id: FormLoginConfigDD.java 156636 2005-03-09 12:16:31Z cziegeler $
 * @since Mar 4, 2005
 */
public class FormLoginConfigDD implements FormLoginConfig {

    private String formLoginPage;
    private String formErrorPage;

    public FormLoginConfigDD() {

    }

    /* (non-Javadoc)
	 * @see org.apache.pluto.descriptors.servlet.FormLoginConfig#getFormLoginPage()
	 */
    public String getFormLoginPage() {
        return formLoginPage;
    }

    /* (non-Javadoc)
	 * @see org.apache.pluto.descriptors.servlet.FormLoginConfig#setFormLoginPage(java.lang.String)
	 */
    public void setFormLoginPage(String formLoginPage) {
        this.formLoginPage = formLoginPage;
    }

    /* (non-Javadoc)
	 * @see org.apache.pluto.descriptors.servlet.FormLoginConfig#getFormErrorPage()
	 */
    public String getFormErrorPage() {
        return formErrorPage;
    }

    /* (non-Javadoc)
	 * @see org.apache.pluto.descriptors.servlet.FormLoginConfig#setFormErrorPage(java.lang.String)
	 */
    public void setFormErrorPage(String formErrorPage) {
        this.formErrorPage = formErrorPage;
    }
}


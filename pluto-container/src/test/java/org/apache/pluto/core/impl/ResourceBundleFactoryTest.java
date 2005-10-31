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
package org.apache.pluto.core.impl;

import org.apache.pluto.util.PlutoTestCase;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.pluto.descriptors.portlet.PortletInfoDD;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import junit.framework.Assert;

/**
 * Unit test for the resource bundle factory.
 * @author ddewolf@apache.org
 * @since Jul 30, 2005
 */
public class ResourceBundleFactoryTest extends PlutoTestCase {

    private PortletDD validDD;

    public void setUp() throws Exception {
        super.setUp();

        validDD = new PortletDD();

        PortletInfoDD info = new PortletInfoDD();
        info.setTitle("Info Title");
        info.setShortTitle("Info Short Title");
        info.setKeywords("Info Keywords");
        validDD.setPortletInfo(info);
        
        validDD.setResourceBundle(TestResourceBundle.class.getName());
    }

    public void tearDown() throws Exception {
        super.setUp();
        validDD = null;
    }

    public void testGetBundleAllSpecified() {
        ResourceBundleFactory factory = new ResourceBundleFactory(validDD);
        ResourceBundle bundle = factory.getResourceBundle(Locale.getDefault());

        Assert.assertEquals("Bundle Title", bundle.getString("javax.portlet.title"));
        Assert.assertEquals("Bundle Short Title", bundle.getString("javax.portlet.shorttitle"));
        Assert.assertEquals("Bundle Keywords", bundle.getString("javax.portlet.keywords"));
    }

    public void testGetResourceBundleNoBundle() {
        validDD.setResourceBundle(null);
        ResourceBundleFactory factory = new ResourceBundleFactory(validDD);
        ResourceBundle bundle = factory.getResourceBundle(Locale.getDefault());

        Assert.assertEquals("Info Title", bundle.getString("javax.portlet.title"));
        Assert.assertEquals("Info Short Title", bundle.getString("javax.portlet.shorttitle"));
        Assert.assertEquals("Info Keywords", bundle.getString("javax.portlet.keywords"));
    }

    public void testGetResourceBundleNoInfo() {
        validDD.setPortletInfo(null);
        ResourceBundleFactory factory = new ResourceBundleFactory(validDD);
        ResourceBundle bundle = factory.getResourceBundle(Locale.getDefault());

        Assert.assertEquals("Bundle Title", bundle.getString("javax.portlet.title"));
        Assert.assertEquals("Bundle Short Title", bundle.getString("javax.portlet.shorttitle"));
        Assert.assertEquals("Bundle Keywords", bundle.getString("javax.portlet.keywords"));
    }

    public void testGetResourceBundleNoBundleNullValues() {
        validDD.setResourceBundle(null);
        validDD.getPortletInfo().setTitle(null);
        validDD.getPortletInfo().setShortTitle(null);
        validDD.getPortletInfo().setKeywords(null);
        ResourceBundleFactory factory = new ResourceBundleFactory(validDD);
        ResourceBundle bundle = factory.getResourceBundle(Locale.getDefault());

        Assert.assertEquals("N/A", bundle.getString("javax.portlet.title"));
        Assert.assertEquals("N/A", bundle.getString("javax.portlet.shorttitle"));
        Assert.assertEquals("N/A", bundle.getString("javax.portlet.keywords"));
    }


    public static class TestResourceBundle extends ListResourceBundle {
        
        private Object[][] contents = {
            {"javax.portlet.title", "Bundle Title"},
            {"javax.portlet.shorttitle", "Bundle Short Title"},
            {"javax.portlet.keywords", "Bundle Keywords"}
        };

        protected Object[][] getContents() {
            return contents;
        }
    }



}

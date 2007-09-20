/*  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.pluto.descriptors.services.jaxb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.pluto.descriptors.portlet.PortletAppDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testclass for testing jaxb xml 2 java binding (portlet.xml)
 * @author <a href="mailto:chrisra@cs.uni-jena.de">Christian Raschka</a>
 */
public class JaxBDescriptorServiceImplTest extends TestCase{
	
	private StringBuffer xmlBegin286 = new StringBuffer();
	private StringBuffer portlet286 = new StringBuffer();
	private StringBuffer attrs286 = new StringBuffer();
	private StringBuffer xmlEnd = new StringBuffer();
	
	private StringBuffer xmlBegin168 = new StringBuffer();
	private StringBuffer portlet168 = new StringBuffer();
	private StringBuffer attrs168 = new StringBuffer();
	
	PortletAppDescriptorServiceImpl jaxb = new PortletAppDescriptorServiceImpl();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		xmlBegin286.append("" +
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<portlet-app id=\"id1\"\n" +
				"xmlns=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\"\n" +
				"version=\"2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"xmlns:portlet=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\"\n" +
				"xsi:schemaLocation=\"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\n" +
				"http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd\">\n ");
		portlet286.append(""+
				"<portlet id=\"id2\">\n" +
				"<description xml:lang=\"de\">description</description>\n" +
				"<portlet-name>portlet286</portlet-name>\n" +
				"<display-name xml:lang=\"de\">display-name</display-name>\n" +
				"<portlet-class>portlet-class</portlet-class>\n" +
				"<init-param id=\"init1\">\n" +
				"<description xml:lang=\"de\">description</description>\n" +
				"<name>name</name>\n" +
				"<value>value</value>\n" +
				"</init-param>\n" +
				"<expiration-cache id=\"exp1\">\n" +
				" <expiration-time>-1</expiration-time>\n" +
				"<scope>scope</scope>\n" +
				"</expiration-cache>\n" +
				" <supports id=\"sup1\">\n" +
				"<mime-type>mime-type</mime-type>\n" +
				"<portlet-mode>portlet-mode</portlet-mode>\n" +
				"</supports>\n" +
				" <supports id=\"sup2\">\n" +
				"<mime-type>mime-type2</mime-type>\n" +
				"<portlet-mode>portlet-mode2</portlet-mode>\n" +
				"</supports>\n" +
				" <supports id=\"sup3\">\n" +
				"<mime-type>mime-type3</mime-type>\n" +
				"<portlet-mode>portlet-mode3</portlet-mode>\n" +
				"</supports>\n" +
				"<supported-locale>supported-locale</supported-locale>\n" +
				"<resource-bundle>resource-bundle</resource-bundle>\n" +
				"<portlet-info id=\"info1\">\n" +
				"<title>title</title>\n" +
				"<short-title>short-title</short-title>\n" +
				"<keywords>keywords</keywords>\n" +
				"</portlet-info>\n" +
				"<portlet-preferences id=\"prefs\">\n" +
				"<preference id=\"pref1\">\n" +
				"<name>name</name>\n" +
				"<value>value</value>\n" +
				"<read-only>true</read-only>\n" +
				"</preference>\n" +
				"<preferences-validator>preferences-validator</preferences-validator>\n" +
				"</portlet-preferences>\n" +
				"<security-role-ref id=\"sec1\">\n" +
				"<description xml:lang=\"de\">description</description>\n" +
				"<role-name>NMTOKEN</role-name>\n" +
				"<role-link>role-link</role-link>\n" +
				"</security-role-ref>\n" +
				"<supported-processing-event><name>supported-processing-event</name></supported-processing-event>\n" +
				"<supported-publishing-event><qname xmlns:x=\"http://test.com\">supported-publishing-event</qname></supported-publishing-event>\n" +
				"<supported-public-render-parameter>supported-public-render-parameter</supported-public-render-parameter>\n" +
				"<container-runtime-option><name>Runtime-Option</name><value>true</value></container-runtime-option>\n" +
				"</portlet>\n" );
		attrs286.append("" + 
				"<custom-portlet-mode id=\"mode1\">\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<portlet-mode>portlet-mode</portlet-mode>\n" +
			  "</custom-portlet-mode>\n" +
			  "<custom-window-state id=\"state1\">\n" +
			   "<description xml:lang=\"de\">description</description>\n" +
			    "<window-state>window-state</window-state>\n" +
			  "</custom-window-state>\n" +
			  "<user-attribute id=\"att1\">\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<name>name</name>\n" +
			  "</user-attribute>\n" +
			  "<security-constraint id=\"cons1\">\n" +
			    "<display-name xml:lang=\"de\">display-name</display-name>\n" +
			    "<portlet-collection>\n" +
			     "<portlet-name>portlet-name</portlet-name>\n" +
			   "</portlet-collection>\n" +
			    "<user-data-constraint id=\"data1\">\n" +
			      "<description xml:lang=\"de\">description</description>\n" +
			      "<transport-guarantee>NONE</transport-guarantee>\n" +
			    "</user-data-constraint>\n" +
			  "</security-constraint>\n" +
			  "<resource-bundle>resource-bundle</resource-bundle>\n" +
			  "<filter>\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<display-name xml:lang=\"de\">display-name</display-name>\n" +
			    "<filter-name>filter-name</filter-name>\n" +
			    "<filter-class>filter-class</filter-class>\n" +
			    "<lifecycle>lifecycle</lifecycle>\n" +
			    "<init-param id=\"init2\">\n" +
			      "<description xml:lang=\"de\">description</description>\n" +
			      "<name>name</name>\n" +
			      "<value>value</value>\n" +
			    "</init-param>\n" +
			  "</filter>\n" +
			  "<filter-mapping>\n" +
			    "<filter-name>filter-name</filter-name>\n" +
			    "<portlet-name>portlet-name</portlet-name>\n" +			   
			  "</filter-mapping>\n" +
			  "<event-definition id=\"event1\">\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<name>QName</name>\n" +
			    "<alias>QName</alias>\n" +
			    "<value-type>java-class</value-type>\n" +
			  "</event-definition>\n" +
			  "<public-render-parameter id=\"public1\">\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<identifier>identifier</identifier>\n" +
			    "<qname>QName</qname>\n" +
			  "</public-render-parameter>\n" +
			  "<public-render-parameter id=\"public2\">\n" +
			    "<description xml:lang=\"de\">description2</description>\n" +
			    "<identifier>identifier2</identifier>\n" +
			    "<name>Name</name>\n" +
			  "</public-render-parameter>\n" +
			  "<container-runtime-option><name>Runtime-Option-Portlet-App</name><value>false</value></container-runtime-option>\n" +
			  "" );
		xmlEnd.append("" +
				"</portlet-app>" );
		
		// test old jsr 168 portlet.xml
		xmlBegin168.append("" +
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<portlet-app id=\"id1\"\n" +
				"xmlns=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\"\n" +
				"version=\"1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				"xmlns:portlet=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\"\n" +
				"xsi:schemaLocation=\"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\n" +
				"http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd\">\n ");
		portlet168.append(""+
				"<portlet id=\"id2\">\n" +
				"<description xml:lang=\"de\">description</description>\n" +
				"<portlet-name>portlet168</portlet-name>\n" +
				"<display-name xml:lang=\"de\">display-name</display-name>\n" +
				"<portlet-class>portlet-class</portlet-class>\n" +
				"<init-param id=\"init1\">\n" +
				"<description xml:lang=\"de\">description</description>\n" +
				"<name>name</name>\n" +
				"<value>value</value>\n" +
				"</init-param>\n" +
				"<expiration-cache id=\"exp1\">\n" +
				"-1" +
				"</expiration-cache>\n" +
				" <supports id=\"sup1\">\n" +
				"<mime-type>mime-type</mime-type>\n" +
				"<portlet-mode>portlet-mode</portlet-mode>\n" +
				"</supports>\n" +
				" <supports id=\"sup2\">\n" +
				"<mime-type>mime-type2</mime-type>\n" +
				"<portlet-mode>portlet-mode2</portlet-mode>\n" +
				"</supports>\n" +
				" <supports id=\"sup3\">\n" +
				"<mime-type>mime-type3</mime-type>\n" +
				"<portlet-mode>portlet-mode3</portlet-mode>\n" +
				"</supports>\n" +
				"<supported-locale>supported-locale</supported-locale>\n" +
				"<resource-bundle>resource-bundle</resource-bundle>\n" +
				"<portlet-info id=\"info1\">\n" +
				"<title>title</title>\n" +
				"<short-title>short-title</short-title>\n" +
				"<keywords>keywords</keywords>\n" +
				"</portlet-info>\n" +
				"<portlet-preferences id=\"prefs\">\n" +
				"<preference id=\"pref1\">\n" +
				"<name>name</name>\n" +
				"<value>value</value>\n" +
				"<read-only>true</read-only>\n" +
				"</preference>\n" +
				"<preferences-validator>preferences-validator</preferences-validator>\n" +
				"</portlet-preferences>\n" +
				"<security-role-ref id=\"sec1\">\n" +
				"<description xml:lang=\"de\">description</description>\n" +
				"<role-name>NMTOKEN</role-name>\n" +
				"<role-link>role-link</role-link>\n" +
				"</security-role-ref>\n" +
				"</portlet>\n" );
		attrs168.append("" + 
				"<custom-portlet-mode id=\"mode1\">\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<portlet-mode>portlet-mode</portlet-mode>\n" +
			  "</custom-portlet-mode>\n" +
			  "<custom-window-state id=\"state1\">\n" +
			   "<description xml:lang=\"de\">description</description>\n" +
			    "<window-state>window-state</window-state>\n" +
			  "</custom-window-state>\n" +
			  "<user-attribute id=\"att1\">\n" +
			    "<description xml:lang=\"de\">description</description>\n" +
			    "<name>name</name>\n" +
			  "</user-attribute>\n" +
			  "<security-constraint id=\"cons1\">\n" +
			    "<display-name xml:lang=\"de\">display-name</display-name>\n" +
			    "<portlet-collection>\n" +
			     "<portlet-name>portlet-name</portlet-name>\n" +
			   "</portlet-collection>\n" +
			    "<user-data-constraint id=\"data1\">\n" +
			      "<description xml:lang=\"de\">description</description>\n" +
			      "<transport-guarantee>NONE</transport-guarantee>\n" +
			    "</user-data-constraint>\n" +
			  "</security-constraint>\n" +
			  "<resource-bundle>resource-bundle</resource-bundle>\n" +
		"" );
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.apache.pluto.descriptors.services.jaxb.JaxBDescriptorServiceImpl#read(java.net.URL)}.
	 */
	@Test
	public void testRead() {
		
		try {
			
			StringBuffer xml286 = new StringBuffer()
	    	.append(xmlBegin286).append(portlet286)
	    	.append(attrs286).append(xmlEnd);
			
			StringBuffer xml168 = new StringBuffer()
			.append(xmlBegin168).append(portlet168)
			.append(attrs168).append(xmlEnd);
			
	        InputStream in = new ByteArrayInputStream(xml286.toString().getBytes());
	        
			PortletAppDD portletApp286 = jaxb.read(in);
			
			in = new ByteArrayInputStream(xml168.toString().getBytes());
			PortletAppDD portletApp168 = jaxb.read(in);
			
			// test if portlet has the right params:
			
			// test jsr168 compliant portlets
			PortletDD portlet168 = (PortletDD)portletApp168.getPortlets().get(0);
			assertTrue(portlet168.getExpirationCacheDD().getExpirationTime()==-1);
			assertEquals(portlet168.getPortletName(),"portlet168");
			assertEquals(portlet168.getInitParams().get(0).getParamValue(),"value");
			assertEquals(portlet168.getSecurityRoleRefs().get(0).getRoleLink(), "role-link");
			assertEquals(portletApp168.getCustomPortletMode().get(0).getPortletMode(), "portlet-mode");
			assertEquals(portletApp168.getCustomWindowState().get(0).getWindowState(), "window-state");
			assertEquals(portletApp168.getUserAttribute().get(0).getName(), "name" );
			assertEquals(portletApp168.getSecurityConstraint().get(0).getPortletCollection().getPortletName().get(0), "portlet-name");
			
			// id (isn't supported yet)
//			assertFalse(portletApp.getId().equals("id2"));
//			assertTrue(portletApp.getId().equals("id1"));
			
			// portlet id
			PortletDD portlet286 = (PortletDD)portletApp286.getPortlets().get(0);
//			assertTrue(portlet1.getId().equals("id2"));
			
			// portlet class
			assertTrue(portlet286.getPortletClass().equals("portlet-class"));
			
			// portlet info
			// id isn't supported yet
//			assertTrue(portlet1.getPortletInfo().getId().equals("info1"));
			assertTrue(portlet286.getPortletInfo().getTitle().equals("title"));
			
			assertEquals("supports size should be 3", 3, portlet286.getSupports().size());
			
			assertEquals(portletApp286.getCustomPortletMode().get(0).getPortletMode(), "portlet-mode");
			assertEquals(portletApp286.getCustomWindowState().get(0).getWindowState(), "window-state");
			assertEquals(portletApp286.getUserAttribute().get(0).getName(), "name" );
			assertEquals(portletApp286.getSecurityConstraint().get(0).getPortletCollection().getPortletName().get(0), "portlet-name");
			assertEquals(portletApp286.getEvents().get(0).getJavaClass(), "java-class");
//			assertEquals(portletApp286.getRender().get(0).getName(), "QName");
			assertEquals(portletApp286.getFilter().get(0).getLifecycle().get(0), "lifecycle");
			assertEquals(portletApp286.getFilterMapping().get(0).getPortletName().get(0), "portlet-name");
			assertEquals(portletApp286.getResourceBundle(), "resource-bundle");
			assertEquals(portletApp286.getVersion(), "2.0");
			
			// test container runtime options
			assertEquals(portletApp286.getContainerRuntimeOption().size(),1);
			assertEquals(portletApp286.getContainerRuntimeOption().get(0).getName(),"Runtime-Option-Portlet-App");
			assertEquals(portletApp286.getContainerRuntimeOption().get(0).getValue().get(0),"false");
			
			assertEquals(portlet286.getContainerRuntimeOption().size(),1);
			assertEquals(portlet286.getContainerRuntimeOption().get(0).getName(),"Runtime-Option");
			assertEquals(portlet286.getContainerRuntimeOption().get(0).getValue().get(0),"true");
			
			
		} catch (IOException e) {
			fail("exception was thrown");
		}
	}

	/**
	 * Test method for {@link org.apache.pluto.descriptors.services.jaxb.JaxBDescriptorServiceImpl#writePortletApp(com.sun.java.xml.ns.portlet.portlet_app_2_0_xsd.PortletAppType, java.io.OutputStream)}.
	 */
	@Test
	public void testWrite() {
		//fail("Not yet implemented");
	}
	
	public void testReadNullDescriptor() {
		try {
			Assert.assertNull("Null descriptor did not return null", jaxb.read(null));
			fail("exception should be thrown");
		} catch (IOException ioe){
			
		}
    }

}

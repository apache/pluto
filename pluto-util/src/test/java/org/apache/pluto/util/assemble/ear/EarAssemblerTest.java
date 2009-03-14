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
package org.apache.pluto.util.assemble.ear;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pluto.container.PortletAppDescriptorService;
import org.apache.pluto.container.impl.PortletAppDescriptorServiceImpl;
import org.apache.pluto.container.om.portlet.PortletApplicationDefinition;
import org.apache.pluto.container.om.portlet.PortletDefinition;
import org.apache.pluto.util.assemble.ArchiveBasedAssemblyTest;
import org.apache.pluto.util.assemble.Assembler;
import org.apache.pluto.util.assemble.AssemblerConfig;
import org.apache.pluto.util.descriptors.web.PlutoWebXmlRewriter;

/**
 * This test assembles an EAR file which contains a single portlet
 * web application for assembly.
 */
public class EarAssemblerTest extends ArchiveBasedAssemblyTest {

    private static final String earResource = "/org/apache/pluto/util/assemble/ear/EarDeployerTest.ear";
    private static final String testPortletName = "WarTestPortletName";
    private File earFile = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        this.earFile = new File( this.getClass().getResource( earResource ).getFile() );
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        this.earFile = null;
    }
    
    public void testEarAssemblyToTempDir() throws Exception {
        AssemblerConfig config = new AssemblerConfig();
        config.setSource( earFile );
        File assembledEar = File.createTempFile( earFile.getName(), ".ear" ); 
        config.setDestination( assembledEar );
        EarAssembler assembler = new EarAssembler();
        assembler.assemble( config );
        validateEarAssembly( assembledEar );
        assembledEar.delete();
    }
    
    public void testEarAssemblyInPlace() throws Exception {
        // copy the test ear file to a temp directory, so we don't overwrite the
        // test ear file distributed with Pluto.
        File inplaceEarFile = File.createTempFile( earFile.getName(), ".ear" );
        FileUtils.copyFile( earFile, inplaceEarFile );
        
        AssemblerConfig config = new AssemblerConfig();
        config.setSource( inplaceEarFile );
        config.setDestination( inplaceEarFile );
        EarAssembler assembler = new EarAssembler();
        assembler.assemble( config );
        validateEarAssembly( inplaceEarFile );
        
        inplaceEarFile.delete();        
    }
    
    protected void validateEarAssembly( File earFile ) throws Exception {
        assertTrue( "EAR archive [" + earFile.getAbsolutePath() + "] cannot be found or cannot be read", 
                earFile.exists() && earFile.canRead() );
        
        PortletAppDescriptorService portletSvc = new PortletAppDescriptorServiceImpl();
        PortletApplicationDefinition portletApp = null;

        PlutoWebXmlRewriter webXmlRewriter = null;
        
        int earEntryCount = 0;
        int warEntryCount = 0;
        
        JarInputStream earIn = new JarInputStream( new FileInputStream( earFile ) );
        
        JarEntry earEntry;
        JarEntry warEntry;
        
        while ( ( earEntry = earIn.getNextJarEntry() ) != null ) {
            earEntryCount++;
            if ( earEntry.getName().endsWith( ".war" ) ) {
                warEntryCount++;
                JarInputStream warIn = new JarInputStream( earIn );
                while ( ( warEntry = warIn.getNextJarEntry() ) != null ) {
                    if ( Assembler.PORTLET_XML.equals( warEntry.getName() ) ) {
                    	portletApp = portletSvc.read( "test", "/test",
                                new ByteArrayInputStream( IOUtils.toByteArray( warIn ) ) );
                    }
                    if ( Assembler.SERVLET_XML.equals( warEntry.getName() ) ) {
                        webXmlRewriter = new PlutoWebXmlRewriter( new ByteArrayInputStream( IOUtils.toByteArray( warIn ) ) );
                    }
                }                
            }
        }
        
        assertTrue( "EAR archive did not contain any entries", earEntryCount > 0 );
        assertTrue( "WAR archive did not contain any entries", warEntryCount > 0 );
        assertNotNull( "WAR archive did not contain a portlet.xml", portletApp );
        assertNotNull( "WAR archive did not contain a servlet.xml", webXmlRewriter );
        assertTrue( "WAR archive did not contain any servlets", webXmlRewriter.hasServlets() );
        assertTrue( "WAR archive did not contain any servlet mappings", webXmlRewriter.hasServletMappings() );
        assertTrue( "WAR archive did not contain any portlets", portletApp.getPortlets().size() > 0 );
        
        PortletDefinition portlet = (PortletDefinition) portletApp.getPortlets().iterator().next();
        assertEquals( "Unexpected test portlet name.", testPortletName, portlet.getPortletName() );
        
        String servletClassName = webXmlRewriter.getServletClass( portlet.getPortletName() );
        assertNotNull( "web.xml does not contain assembly for test portlet", servletClassName );
        assertEquals( "web.xml does not contain correct dispatch servet", Assembler.DISPATCH_SERVLET_CLASS, 
                servletClassName );
    }

    protected Assembler getAssemblerUnderTest() {
        return new EarAssembler();
    }

    protected File getFileToAssemble() {
        return earFile;
    }
    
}

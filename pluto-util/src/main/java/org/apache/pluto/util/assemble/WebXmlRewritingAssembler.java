/* Copyright 2007 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package org.apache.pluto.util.assemble;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.pluto.descriptors.portlet.PortletAppDD;
import org.apache.pluto.descriptors.portlet.PortletDD;
import org.apache.pluto.descriptors.services.PortletAppDescriptorService;
import org.apache.pluto.descriptors.services.castor.EntityResolverImpl;
import org.apache.pluto.descriptors.services.castor.PortletAppDescriptorServiceImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision$
 */
public abstract class WebXmlRewritingAssembler implements Assembler {
    /** The XML output properties. */
    private static final Properties PROPERTIES = new Properties();
    
    /** Element tagnames that may appear before servlet elements. */
    private static final Collection BEFORE_SERVLET_DEF = new ArrayList();
    
    /** Element tagnames that may appear before servlet-mapping elements. */
    private static final Collection BEFORE_MAPPING_DEF = new ArrayList();

    static {
        // Initialize xml output properties.
        PROPERTIES.setProperty(OutputKeys.INDENT, "yes");
        
        // Initialize BEFORE_SERVLET_DEF collection.
        BEFORE_SERVLET_DEF.add("icon");
        BEFORE_SERVLET_DEF.add("display-name");
        BEFORE_SERVLET_DEF.add("description");
        BEFORE_SERVLET_DEF.add("distributable");
        BEFORE_SERVLET_DEF.add("context-param");
        BEFORE_SERVLET_DEF.add("filter");
        BEFORE_SERVLET_DEF.add("filter-mapping");
        BEFORE_SERVLET_DEF.add("listener");
        
        // initialize BEFORE_MAPPING_DEF collection.
        BEFORE_MAPPING_DEF.addAll(BEFORE_SERVLET_DEF);
        BEFORE_MAPPING_DEF.add("servlet");
    }
    
    
    /**
     * Updates the webapp descriptor by injecting portlet wrapper servlet
     * definitions and mappings.
     * 
     * TODO: currently we rely specifically on the castor implementation.
     * 
     * @param webXmlIn  input stream to the webapp descriptor, it will be closed before the web xml is written out.
     * @param portletXmlIn  input stream to the portlet app descriptor, it will be closed before the web xml is written out.
     * @param webXmlOut output stream to the webapp descriptor, it will be flushed and closed.
     * @param dispatchServletClass The name of the servlet class to use for
     *                         handling portlet requests
     * @throws IOException
     */
    protected void updateWebappDescriptor(InputStream webXmlIn,
                                              InputStream portletXmlIn,
                                              OutputStream webXmlOut,
                                              String dispatchServletClass)
    throws IOException {

        if (dispatchServletClass == null ||
            dispatchServletClass.length() == 0 ||
            dispatchServletClass.trim().length() == 0) {
            dispatchServletClass = DISPATCH_SERVLET_CLASS;
        }
        
        Document webXmlDoc = parse(webXmlIn);
        webXmlIn.close();
        
        Collection servletElements = new ArrayList();
        Collection mappingElements = new ArrayList();

        PortletAppDescriptorService portletAppDescriptorService =
                new PortletAppDescriptorServiceImpl();
        PortletAppDD portletAppDD = portletAppDescriptorService.read(portletXmlIn);
        portletXmlIn.close();
        
        for (Iterator it = portletAppDD.getPortlets().iterator();
                it.hasNext(); ) {
            
            // Read portlet definition.
            PortletDD portlet = (PortletDD) it.next();
            String name = portlet.getPortletName();
            
            // Create servlet definition element.
            Element servlet = webXmlDoc.createElement("servlet");
            Element servletName = webXmlDoc.createElement("servlet-name");
            servletName.appendChild(webXmlDoc.createTextNode(name));
            servlet.appendChild(servletName);
            
            Element servletClass = webXmlDoc.createElement("servlet-class");
            servletClass.appendChild(webXmlDoc.createTextNode(dispatchServletClass));
            servlet.appendChild(servletClass);
            
            Element initParam = webXmlDoc.createElement("init-param");
            Element paramName = webXmlDoc.createElement("param-name");
            paramName.appendChild(webXmlDoc.createTextNode("portlet-name"));
            
            Element paramValue = webXmlDoc.createElement("param-value");
            paramValue.appendChild(webXmlDoc.createTextNode(name));
            
            initParam.appendChild(paramName);
            initParam.appendChild(paramValue);
            servlet.appendChild(initParam);
            
            Element load = webXmlDoc.createElement("load-on-startup");
            load.appendChild(webXmlDoc.createTextNode("1"));
            servlet.appendChild(load);
            
            // Create servlet mapping element.
            Element mapping = webXmlDoc.createElement("servlet-mapping");
            servletName = webXmlDoc.createElement("servlet-name");
            servletName.appendChild(webXmlDoc.createTextNode(name));
            Element uri = webXmlDoc.createElement("url-pattern");
            uri.appendChild(webXmlDoc.createTextNode("/PlutoInvoker/"+name));
            mapping.appendChild(servletName);
            mapping.appendChild(uri);
            
            // Save servlet definition and servlet mapping.
            servletElements.add(servlet);
            mappingElements.add(mapping);
        }

        Element webAppNode = webXmlDoc.getDocumentElement();
        NodeList nodes = webAppNode.getChildNodes();
        
        // Find the first node that shouldn't be before the servlet and start
        // appending. This is kind of ugly, but the hack works for now!
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                
                if (!BEFORE_SERVLET_DEF.contains(node.getNodeName())) {
                    for (Iterator it = servletElements.iterator();
                            it.hasNext(); ) {
                        Node servlet = (Node) it.next();
                        webAppNode.insertBefore(servlet, node);
                        it.remove();
                    }
                }
                
                if(!BEFORE_MAPPING_DEF.contains(node.getNodeName())) {
                    for (Iterator it = mappingElements.iterator();
                            it.hasNext(); ) {
                        Node mapping = (Node) it.next();
                        webAppNode.insertBefore(mapping, node);
                        it.remove();
                    }
                }
            }
        }

        // Now, in case there are not any nodes after the servlet def!
        for (Iterator it = servletElements.iterator(); it.hasNext(); ) {
            webAppNode.appendChild((Node)it.next());
        }
        for (Iterator it = mappingElements.iterator(); it.hasNext(); ) {
            webAppNode.appendChild((Node)it.next());
        }
        
        // Write out the updated web.xml document.
        this.save(webXmlDoc, webXmlOut);
    }
    
    /**
     * Saves the XML document to the specified output stream.
     * @param xmlDoc  the XML document.
     * @param out  the output stream.
     * @throws IOException  if an error occurs.
     */
    protected void save(Document xmlDoc, OutputStream out)
    throws IOException {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperties(PROPERTIES);
            transformer.transform(new DOMSource(xmlDoc),
                                  new StreamResult(out));
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        } catch (TransformerException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        } finally {
            out.flush();
            out.close();
        }
    }
    
    /**
     * Parses an input stream of an XML file to an XML document.
     * @param xmlIn  the input stream of an XML file.
     * @return the XML document.
     * @throws IOException  if an error occurs.
     */
    protected Document parse(InputStream xmlIn) throws IOException {
        Document xmlDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new EntityResolverImpl());
            xmlDoc = builder.parse(xmlIn);
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex.getMessage());
        } catch (SAXException ex) {
            throw new IOException(ex.getMessage());
        }
        return xmlDoc;
    }
}

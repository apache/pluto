/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Pluto", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
/* 

 */

package org.apache.pluto.portalImpl.om.page.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletConfig;

import org.apache.pluto.util.StringUtils;

public class PortalImpl implements java.io.Serializable {

    private ArrayList fragments = new ArrayList();

    public PortalImpl()
    {
    }

    // additional methods.
    
    public Collection getFragments()
    {
        return fragments;
    }

    public org.apache.pluto.portalImpl.aggregation.RootFragment build(ServletConfig config)    
    throws Exception
    {
        org.apache.pluto.portalImpl.aggregation.RootFragment root = 
        new org.apache.pluto.portalImpl.aggregation.RootFragment(config);

        Iterator iterator = fragments.iterator();

        while (iterator.hasNext()) {
            FragmentImpl fragmentimpl = (FragmentImpl)iterator.next();

            org.apache.pluto.portalImpl.aggregation.Fragment _fragment = 
            fragmentimpl.build(config, root);
            if (_fragment!=null) {
                root.addChild(_fragment);
            }
        }

        return root;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer(2000);
        StringUtils.newLine(buffer,0);
        buffer.append(getClass().toString()); buffer.append(":");
        StringUtils.newLine(buffer,0);
        buffer.append("{");

        Iterator iterator = fragments.iterator();

        while (iterator.hasNext()) {
            buffer.append(((FragmentImpl)iterator.next()).toString(2));
        }

        StringUtils.newLine(buffer,0);
        buffer.append("}");
        return buffer.toString();
    }

}

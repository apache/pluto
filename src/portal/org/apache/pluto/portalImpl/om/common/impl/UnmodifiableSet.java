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

package org.apache.pluto.portalImpl.om.common.impl;

import java.io.*;
import java.util.*;

public class UnmodifiableSet implements Set, Serializable {

    // use serialVersionUID from JDK 1.2.2 for interoperability
    private static final long serialVersionUID = 1820017752578914078L;

    protected Set c;

    public UnmodifiableSet(Set c)
    {
        if (c == null) {
            throw new NullPointerException();
        }
        this.c = c;
    }

    public int size()
    {
        return c.size();
    }

    public boolean isEmpty()
    {
        return c.isEmpty();
    }

    public boolean contains(Object o)
    {
        return c.contains(o);
    }

    public Object[] toArray()
    {
        return c.toArray();
    }

    public Object[] toArray(Object[] a)
    {
        return c.toArray(a);
    }

    public String toString()
    {
        return c.toString();
    }

    public Iterator iterator()
    {
        return new Iterator()
        {
            Iterator i = c.iterator();

            public boolean hasNext()
            {
                return i.hasNext();
            }

            public Object next()
            {
                return i.next();
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    public boolean add(Object o)
    {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection coll)
    {
        return c.containsAll(coll);
    }

    public boolean addAll(Collection coll)
    {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection coll)
    {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection coll)
    {
        throw new UnsupportedOperationException();
    }

    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object o)
    {
        return c.equals(o);
    }

    public int hashCode()
    {
        return c.hashCode();
    }

    // additional methods.

    /**
     * This method is only used by the ControllerFactoryImpl
     * to unwrap the unmodifiable Set and allow to
     * modify the set via controllers
     * 
     * @return the modifiable set
     */
    public Set getModifiableSet()
    {
        return c;
    }
}

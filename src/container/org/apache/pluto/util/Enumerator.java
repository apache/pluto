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

package org.apache.pluto.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Uitlity class to wraps an <code>Enumeration</code> around a
 * Collection, i.e. <code>Iterator</code> classes.
 */

public final class Enumerator implements Enumeration
{


    // Iterator over which the Enumeration takes place
    private Iterator iterator = null;


    /**
     * Returns an Enumeration over the specified Collection.
     *
     * @param collection Collection with values that should be enumerated
     */
    public Enumerator(Collection collection)
    {
        this(collection.iterator());
    }


    /**
     * Returns an Enumeration over the values of the
     * specified Iterator.
     *
     * @param iterator Iterator to be wrapped
     */
    public Enumerator(Iterator iterator)
    {
        super();
        this.iterator = iterator;
    }


    /**
     * Returns an Enumeration over the values of the specified Map.
     *
     * @param map Map with values that should be enumerated
     */
    public Enumerator(Map map)
    {
        this(map.values().iterator());
    }



    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if this enumeration contains at 
     *          least one more element to provide,
     *          <code>false</code> otherwise.
     */
    public boolean hasMoreElements()
    {
        return(iterator.hasNext());
    }


    /**
     * Returns the next element of this enumeration.
     *
     * @return the next element of this enumeration
     *
     * @exception NoSuchElementException if no more elements exist
     */
    public Object nextElement() throws NoSuchElementException {
        return(iterator.next());
    }


}

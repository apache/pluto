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

package org.apache.pluto;

/**
 ** The <CODE>PortletContainerException</CODE> class defines a general exception
 ** that the portlet container can throw when it encounters difficulty.
 **

 **/

public class PortletContainerException extends Exception
{

    private Throwable cause;

    /**
     ** Constructs a new portlet exception.
     **/

    public PortletContainerException ()
    {
    }

    /**
     ** Constructs a new portlet invoker exception with the given text. The
     ** layout system may use the text write it to a log.
     **
     ** @param   text
     **          the exception text
     **/

    public PortletContainerException (String text)
    {
        super (text);
    }

    /**
     ** Constructs a new portlet invoker exception when the invoker needs to throw an
     ** exception and include a message about the "root case" that interfered
     ** with its normal operation, including a description message.
     **
     ** @param   text
     **          the exception text
     ** @param   cause
     **          the root cause
     **/

    public PortletContainerException (String text, Throwable cause)
    {
        super (text); //, cause);

        this.cause = cause;
    }

    /**
     ** Constructs a new portlet invoker exception when the portlet needs to throw an
     ** exception. The exception's message is based on the localized message
     ** of the underlying exception.
     **
     ** @param   cause
     **          the root cause
     **/

    public PortletContainerException (Throwable cause)
    {
        super (cause.getLocalizedMessage ());

        this.cause = cause;
    }

    /**
     ** Returns the exception that cause this portlet exception.
     **
     ** @return   the <CODE>Throwable</CODE> that caused this portlet exception.
     **/

    public Throwable getRootCause ()
    {
        return (cause);
    }
}

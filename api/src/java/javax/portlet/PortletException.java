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
 *
 * ====================================================================
 *
 * This source code implements specifications defined by the Java
 * Community Process. In order to remain compliant with the specification
 * DO NOT add / change / or delete method signatures!
 */

package javax.portlet;



/**
 * The <CODE>PortletException</CODE> class defines a general exception
 * that a portlet can throw when it is unable to perform its operation
 * successfully.
 */

public class PortletException extends java.lang.Exception
{


  private Throwable _cause;


  /**
   * Constructs a new portlet exception.
   */

  public PortletException ()
  {
    super();
  }

  /**
   * Constructs a new portlet exception with the given text. The
   * portlet container may use the text write it to a log.
   *
   * @param   text
   *          the exception text
   */

  public PortletException (String text)
  {
    super (text);
  }

  /**
   * Constructs a new portlet exception when the portlet needs to do
   * the following:
   * <ul>
   * <li>throw an exception 
   * <li>include the "root cause" exception
   * <li>include a description message
   * </ul>
   *
   * @param   text
   *          the exception text
   * @param   cause
   *          the root cause
   */
  
  public PortletException (String text, Throwable cause)
  {
    super(text);
    _cause = cause;
    // change this when going to jdk1.4:    super (text, cause);
  }

  /**
   * Constructs a new portlet exception when the portlet needs to throw an
   * exception. The exception's message is based on the localized message
   * of the underlying exception.
   *
   * @param   cause
   *          the root cause
   */

  public PortletException (Throwable cause)
  {
    _cause = cause;
    // change this when going to jdk1.4:        super (cause);
  }

  /**
   * Prints the stack trace of this exception to the standard error stream.
   */
  public void printStackTrace()
  {
    this.printStackTrace(System.err);
  }
  
  /**
   * Prints the stack trace of this exception to the specified print stream.
   *
   * @param out the <code>PrintStream</code> to be used for output
   */
  public void printStackTrace(java.io.PrintStream out) 
  {
    this.printStackTrace(new java.io.PrintWriter(out, true));
  }

  /**
   * Prints the stack trace of this exception to the specified print writer.
   * 
   * @param out the <code>PrintWriter</code> to be used for output
   */
  public void printStackTrace(java.io.PrintWriter out)
  {
    super.printStackTrace(out);

    if( getCause () != null ) {
      out.println();
      out.print("Nested Exception is ");
      getCause ().printStackTrace(out);
    }
    // change this when going tojdk1.4:
      /*
        super.printStackTrace(out);

        if( getRootCause () != null )
        {
            out.println();
            out.print("Nested Exception is ");
            getRootCause ().printStackTrace(out);
        }
        */
  }

  /**
   * Returns the cause of this throwable or <code>null</code> if the
   * cause is nonexistent or unknown.  (The cause is the throwable that
   * caused this throwable to get thrown.)
   *
   * <p>This implementation returns the cause that was supplied via one of
   * the constructors requiring a <tt>Throwable</tt>.
   *
   * @return  the cause of this throwable or <code>null</code> if the
   *          cause is nonexistent or unknown.
   */
  public Throwable getCause() {
    return (_cause!=null ? _cause : null);
  }

}

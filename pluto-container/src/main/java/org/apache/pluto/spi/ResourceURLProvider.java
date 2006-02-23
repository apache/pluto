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

package org.apache.pluto.spi;

/**
 *
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @version 1.0
 */
public interface ResourceURLProvider {
	
    /**
     * Sets the absolute URL to be returned by the provider. E.g.
     * http://host/wps/portal/hello.gif
     * @param path the new absolute url
     */
    public void setAbsoluteURL(String path);

    /**
     * Sets a full path URI including the context root. E.g.
     * /wps/portal/hello.gif
     * @param path the new absolute url
     */
    public void setFullPath(String path);

    /**
     * Returns a url to a resource as absolute URL starting with protocol so
     * that it can be accessed by a browser.
     * @return the URL as string
     */
    public String toString();
    
}

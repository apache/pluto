/*
 * Copyright 2008 The Apache Software Foundation
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
package org.apache.pluto.om.portlet;

public interface CustomPortletMode {

	public abstract boolean isPortalManaged();

	public abstract String getDecorationName();

	public abstract void setDecorationName(String decorationName);

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract String getPortletMode();

	public abstract void setPortletMode(String portletMode);

}
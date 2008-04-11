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

import java.util.List;

public interface Listener {

	public abstract String getDescription();

	public abstract void setDescription(String description);

	public abstract List<String> getDisplayName();

	public abstract void setDisplayName(List<String> displayName);

	public abstract String getID();

	public abstract void setID(String id);

	public abstract String getListenerClass();

	public abstract void setListenerClass(String listenerClass);

}
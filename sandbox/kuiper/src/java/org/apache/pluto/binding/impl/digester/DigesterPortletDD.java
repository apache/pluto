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

package org.apache.pluto.binding.impl.digester;

import org.apache.pluto.binding.PortletDD;
import org.apache.pluto.binding.InitParameterDD;
import org.apache.pluto.binding.PortletPreferenceDD;
import org.apache.pluto.binding.PortletInfoDD;

import java.util.ArrayList;

/**
 * 
 * @author <A href="mailto:david.dewolf@vivare.com">David H. DeWolf</A>
 * @version 1.0
 * @since Mar 10, 2004 at 5:21:11 PM
 */
public class DigesterPortletDD implements PortletDD {

    private String description;
    private String portletName;
    private String displayName;
    private String portletClass;

    private PortletInfoDD portletInfo;
    private ArrayList portletPreferences;

    private int expirationCache;
    private String preferencesValidator;

    private ArrayList initParameters;
    private ArrayList mimeTypeSupport;
    private ArrayList portletModeSupport;
    private ArrayList localeSupport;


    public DigesterPortletDD() {
        this.portletPreferences = new ArrayList();
        this.initParameters = new ArrayList();
        this.mimeTypeSupport = new ArrayList();
        this.portletModeSupport = new ArrayList();
        this.localeSupport = new ArrayList();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPortletClass() {
        return portletClass;
    }

    public void setPortletClass(String portletClass) {
        this.portletClass = portletClass;
    }

    public InitParameterDD[] getInitParameters() {
        return (InitParameterDD[])initParameters.toArray(
                   new InitParameterDD[initParameters.size()]
               );
    }

    public void addInitParameter(InitParameterDD initParameter) {
        this.initParameters.add(initParameter);
    }

    public int getExpirationCache() {
        return expirationCache;
    }

    public void setExpirationCache(int cache) {
        this.expirationCache = cache;
    }

    public String[] getSupportedMimeTypes() {
        return (String[])mimeTypeSupport
                    .toArray(new String[mimeTypeSupport.size()]);
    }

    public void addSupportedMimeType(String mimeType) {
        this.mimeTypeSupport.add(mimeType);
    }

    public String[] getSupportedPortletModes() {
        return (String[])portletModeSupport
                    .toArray(new String[portletModeSupport.size()]);
    }

    public void addSupportedPortletMode(String mode) {
        this.portletModeSupport.add(mode);
    }

    public String[] getSupportedLocales() {
        return (String[])localeSupport
                    .toArray(new String[localeSupport.size()]);
    }

    public void addSupportedLocale(String locale) {
        this.localeSupport.add(locale);
    }

    public PortletInfoDD getPortletInfo() {
        return portletInfo;
    }

    public void setPortletInfo(PortletInfoDD dd) {
        this.portletInfo = dd;
    }

    public PortletPreferenceDD[] getPortletPreferences() {
        return (PortletPreferenceDD[])portletPreferences
            .toArray(new PortletPreferenceDD[portletPreferences.size()]);
    }

    public void addPortletPreference(PortletPreferenceDD dd) {
        this.portletPreferences.add(dd);
    }

    public String getPreferenceValidator() {
        return this.preferencesValidator;
    }

    public void setPreferenceValidator(String val) {
        this.preferencesValidator = val;
    }
}

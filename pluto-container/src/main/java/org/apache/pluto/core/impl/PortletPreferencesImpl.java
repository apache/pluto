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
/* 

 */

package org.apache.pluto.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import org.apache.pluto.Constants;
import org.apache.pluto.PortletContainer;
import org.apache.pluto.PortletContainerException;
import org.apache.pluto.util.StringManager;
import org.apache.pluto.core.InternalPortletRequest;
import org.apache.pluto.core.InternalPortletWindow;
import org.apache.pluto.core.PortletPreference;
import org.apache.pluto.core.PortletEntity;
import org.apache.pluto.services.optional.PortletPreferencesService;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Implementation of the <code>javax.portlet.PortletPreferences</code>
 * interface.
 * 
 * @see org.apache.pluto.core.PortletPreference
 * @see org.apache.pluto.core.impl.PortletPreferenceImpl
 * 
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 */
public class PortletPreferencesImpl implements PortletPreferences {
	
	/** Logger. */
    private static final Log LOG = LogFactory.getLog(PortletPreferencesImpl.class);
    
    private static final StringManager EXCEPTIONS = StringManager.getManager(
    		PortletPreferencesImpl.class.getPackage().getName());
    
    
    // Private Member Variables ------------------------------------------------
    
    /** The portlet preferences service provided by the portal. */
    private PortletPreferencesService preferencesService = null;

    private InternalPortletWindow window = null;

    private InternalPortletRequest request = null;
    
    /**
     * Default portlet preferences retrieved from portlet.xml, and used for
     * resetting portlet preferences.
     */
    private PortletPreference[] defaultPreferences = null;
    
    /** Current portlet preferences. */
    private Map preferences = new HashMap();

    /** Current method used for managing these preferences. */
    private Integer methodId = null;
    
    
    // Constructor -------------------------------------------------------------
    
    /**
     * Constructs an instance.
     * @param container  the portlet container.
     * @param portletWindow  the internal portlet window.
     * @param request  the internal portlet request.
     * @param methodId  the request method ID: render request or action request.
     */
    public PortletPreferencesImpl(PortletContainer container,
                                  InternalPortletWindow window,
                                  InternalPortletRequest request,
                                  Integer methodId) {
        this.window = window;
        this.request = request;
        this.methodId = methodId;
        
        // Get the portlet preferences service from container.
        preferencesService = container.getOptionalContainerServices()
        		.getPortletPreferencesService();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using PortletPreferencesService: "
            		+ preferencesService.getClass().getName());
        }
        
        // Put default portlet preferences into preferences map.
        PortletEntity entity = window.getPortletEntity();
        defaultPreferences = entity.getDefaultPreferences();
        for (int i = 0; i < defaultPreferences.length; i++) {
            preferences.put(defaultPreferences[i].getName(),
                            (PortletPreference) defaultPreferences[i].clone());
        }
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Loaded default preferences: " + toString());
        }
        
        // Merge stored portlet preferences into preferences map.
        try {
            PortletPreference[] storedPreferences = preferencesService
            		.getStoredPreferences(window, request);
            for (int i = 0; i < storedPreferences.length; i++) {
            	if (LOG.isDebugEnabled()) {
            		LOG.debug("Merging stored preference: "
            				+ storedPreferences[i].getName());
            	}
                preferences.put(storedPreferences[i].getName(),
                                storedPreferences[i]);
            }
        	// Store the preferences retrieved from portlet.xml.
            //   Portlet preferences are stored everytime when a
            //   PortletPreferencesImpl instance is created.
            //   So here we do not check the portlet request method ID.
        	internalStore();
        	
        } catch (PortletContainerException ex) {
            LOG.error("Error retrieving preferences.", ex);
            //TODO: Rethrow up the stack????
        } catch (IOException ex) {
            LOG.error("Error retrieving preferences.", ex);        	
            //TODO: Rethrow up the stack????
        } catch (ValidatorException ex) {
            LOG.warn("ValidatorException initializing portlet preferences. "
            		+ "This is not illegal at this point "
            		+ "since we are just retreiving from portlet.xml.", ex);    	
        }
        if (LOG.isDebugEnabled()) {
        	LOG.debug("Merged stored preferences: " + toString());
        }
    }
    
    
    // PortletPreferences Impl -------------------------------------------------
    
    public boolean isReadOnly(String key) {
        if (key == null) {
            throw new IllegalArgumentException(
            		EXCEPTIONS.getString("error.null", "Preference key "));
        }
        PortletPreference pref = (PortletPreference) preferences.get(key);
        return (pref != null && pref.isReadOnly());
    }

    public String getValue(String key, String defaultValue) {
        String[] values = getValues(key, new String[] { defaultValue });
        return (values != null && values.length > 0) ? values[0] : null;
    }

    public String[] getValues(String key, String[] defaultValues) {
        if (key == null) {
            throw new IllegalArgumentException(
            		EXCEPTIONS.getString("error.null", "Preference key "));
        }
        String[] values = null;
        PortletPreference pref = (PortletPreference) preferences.get(key);
        if (pref != null) {
            values = pref.getValues();
        } else {
            values = defaultValues;
        }
        return values;
    }

    public void setValue(String key, String value) throws ReadOnlyException {
        if (isReadOnly(key)) {
            throw new ReadOnlyException(EXCEPTIONS.getString(
            		"error.preference.readonly", key));
        }
        PortletPreference pref = (PortletPreference) preferences.get(key);
        if (pref != null) {
            pref.setValues(new String[]{value});
        } else {
            pref = new PortletPreferenceImpl(key, new String[]{value});
            preferences.put(key, pref);
        }
    }

    public void setValues(String key, String[] values) throws ReadOnlyException {
        if (isReadOnly(key)) {
            throw new ReadOnlyException(EXCEPTIONS.getString("error.preference.readonly"));
        }

        PortletPreference pref = (PortletPreference) preferences.get(key);
        if (pref != null) {
            pref.setValues(values);
        } else {
            pref = new PortletPreferenceImpl(key, values);
            preferences.put(key, pref);
        }
    }

    public Enumeration getNames() {
        return new Vector(preferences.keySet()).elements();
    }

    public Map getMap() {
        Map map = new HashMap();
        Iterator it = preferences.keySet().iterator();
        while (it.hasNext()) {
            PortletPreference pref = (PortletPreference) preferences.get(it.next());
            map.put(pref.getName(), pref.getValues().clone());
        }
        return Collections.unmodifiableMap(map);
    }
    
    public void reset(String key) throws ReadOnlyException {
    	// Read-only preferences cannot be reset.
        if (isReadOnly(key)) {
            throw new ReadOnlyException(EXCEPTIONS.getString(
            		"error.preference.readonly", "Preference key "));
        }
        // Try to reset preference to the default values.
        boolean resetDone = false;
        for (int i = 0; !resetDone && i < defaultPreferences.length; i++) {
        	if (key.equals(defaultPreferences[i].getName())) {
        		if (LOG.isDebugEnabled()) {
        			LOG.debug("Resetting preference for key: " + key);
        		}
        		preferences.put(key,
        				(PortletPreference) defaultPreferences[i].clone());
        		resetDone = true;
        	}
        }
        // Remove preference if default values are not defined (PLT.14.1).
        if (!resetDone) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("Resetting preference to null for key: " + key);
        	}
        	preferences.remove(key);
        }
    }
    
    /**
     * Stores the portlet preferences to a persistent storage. This method
     * should only be invoked within <code>processAction()</code> method.
     * 
     * @see #internalStore()
     * 
     * @throws IllegalStateException  if this method is not invoked within
     *         <code>processAction()</code> method.
     * @throws ValidatorException  if the portlet preferences are not valid.
     * @throws IOException  if an error occurs with the persistence mechanism.
     */
    public void store() throws IOException, ValidatorException {
        if (!Constants.METHOD_ACTION.equals(methodId)) {
            throw new IllegalStateException(
                	"store is only allowed inside a processAction call.");
        }
        internalStore();
    }
    
    
    // Private Methods ---------------------------------------------------------
    
    /**
     * Stores the portlet preferences to a persistent storage. If a preferences
     * validator is defined for this portlet, this method firstly validates the
     * portlet preferences.
     * <p>
     * This method is invoked internally, thus it does not check the portlet
     * request method ID (METHOD_RENDER or METHOD_ACTION).
     * </p>
     * @throws ValidatorException  if the portlet preferences are not valid.
     * @throws IOException  if an error occurs with the persistence mechanism.
     */
    private void internalStore() throws IOException, ValidatorException {
        // Validate the preferences before storing, if a validator is defined.
        //   If the preferences cannot pass the validation,
        //   an ValidatorException will be thrown out.
        PreferencesValidator validator = window.getPortletEntity()
        		.getPreferencesValidator();
        if (validator != null) {
        	validator.validate(this);
        }
        // Store the portlet preferences.
        PortletPreference[] prefs = (PortletPreference[]) 
        		(new ArrayList(preferences.values())).toArray(
        				new PortletPreference[preferences.size()]);
        try {
        	preferencesService.store(window, request, prefs);
        } catch (PortletContainerException ex) {
            LOG.error("Error storing preferences.", ex);
            throw new IOException("Error storing perferences: " + ex.getMessage());
        }
    }
    
    
    // Object Methods ----------------------------------------------------------
    
    /**
     * Returns the string representation of this object. Preferences are
     * separated by ';' character, while values in one preference are separated
     * by ',' character.
     * @return the string representation of this object.
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(getClass().getName()).append("[");
    	for (Enumeration en = getNames(); en.hasMoreElements(); ) {
    		String name = (String) en.nextElement();
    		buffer.append(name);
    		buffer.append("(readOnly:").append(isReadOnly(name)).append(")=");
    		String[] values = getValues(name, null);
    		if (values != null) {
	    		for (int i = 0; i < values.length; i++) {
	    			buffer.append(values[i]);
	    			if (i < values.length - 1) {
	    				buffer.append(",");
	    			}
				}
    		} else {
    			buffer.append("NULL");
    		}
    		buffer.append(";");
    	}
    	buffer.append("]");
    	return buffer.toString();
    }
    
}

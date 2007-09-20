/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pluto.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.JspException;

import javax.portlet.BaseURL;


/**
 * Abstract supporting class for actionURL tag, renderURL tag and resourceURL tag.
 * 
 * @version 2.0
 * 
 */

public abstract class BaseURLTag extends TagSupport {
	
	/**
	 * TagExtraInfo class for BaseUrlTag.
	 */
	public static class TEI extends TagExtraInfo {
		
        public VariableInfo[] getVariableInfo(TagData tagData) {
            VariableInfo vi[] = null;
            String var = tagData.getAttributeString("var");
            if (var != null) {
                vi = new VariableInfo[1];
                vi[0] =
                	new VariableInfo(var, "java.lang.String", true,
                                 VariableInfo.AT_BEGIN);
            }
            return vi;
        }

    }
	
	//--------------------------------------------------------------------------
	
	protected String secure = null;
	protected Boolean secureBoolean;
	protected String var = null;
	//TODO: not the default value (should be true)
	protected Boolean escapeXml = false;
		
	protected Map<String,List<String>> parametersMap = new HashMap<String,List<String>> ();
	protected Map<String, List<String>> propertiesMap = new HashMap<String,List<String>> ();
	
	

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public abstract int doStartTag() throws JspException;
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public abstract int doEndTag() throws JspException;
	
	

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	//Called at the end of the lifecycle.
	@Override
	public void release(){
		super.release();
		parametersMap = null;
		secureBoolean = null;
	}
	
	
	/**
	 * Returns secure property as String.
     * @return String
     */
    public String getSecure() {
        return secure;
    }
    
    
    /**
     * Returns secure property as Boolean.
     * @return boolean
     */
    public boolean getSecureBoolean() {
        return this.secureBoolean.booleanValue();
    }  
    
    
    /**
     * Returns the var property.
     * @return String
     */
    public String getVar() {
        return var;
    }
    
    
    /**
     * Returns escapeXml property.
     * @return Boolean
     */
    public Boolean getEscapeXml() {
        return escapeXml;
    }
    
    
    /**
     * Sets secure property to boolean value of the string.
     * @param secure
     * @return void
     */
    public void setSecure(String secure) {
        this.secure = secure;
        this.secureBoolean = new Boolean(secure);
    }
     
    
    /**
     * Sets the var property.
     * @param var The var to set
     * @return void
     */
    public void setVar(String var) {
        this.var = var;
    }
    
    
    /**
     * Sets the escapeXml property.
     * @param escapeXml
     * @return void
     */
    public void setEscapeXml(Boolean escapeXml) {
        this.escapeXml = escapeXml;
    }
    
    
    /**
     * Adds a key,value pair to the parameter map. 
     * @param key String
     * @param value String
     * @return void
     */
    protected void addParameter(String key,String value) {
    	if(key == null){
    		throw new NullPointerException();
    	}
    	
    	if((value == null) || (value.length() == 0)){//remove parameter
    		if(parametersMap.containsKey(key)){
    			parametersMap.remove(key);
    		}
    	}
    	else{//add value
    	   	List<String> valueList = null;
    	
    	   	if(parametersMap.containsKey(key)){
    	   		valueList = parametersMap.get(key);//get old value list    		    	
    	   	}
    	   	else{
    	   		valueList = new ArrayList<String>();// create new value list    		    		
    	   	}
    	
    	   	valueList.add(value);
    	
    	   	parametersMap.put(key, valueList);
    	}
    }
    
    /**
     * Adds a key,value pair to the property map. 
     * @param key String
     * @param value String
     * @return void
     */
    protected void addProperty(String key,String value) {
    	if(key == null){
    		throw new NullPointerException();
    	}
    	
    	List<String> valueList = null;
    	
    	if(propertiesMap.containsKey(key)){
    		valueList = propertiesMap.get(key);//get old value list    		    	
    	}
    	else{
    		valueList = new ArrayList<String>();// create new value list    		    		
    	}

    	valueList.add(value);

    	propertiesMap.put(key, valueList);
    	
    }
    
    
    /**
     * Copies the parameters from map to the BaseURL.
     * @param url BaseURL
     * @return void
     */
    protected void setUrlParameters(BaseURL url) {
    	Set<String> keySet = parametersMap.keySet();
    	
		
		for(String key : keySet){
			
			List<String> valueList = parametersMap.get(key);
			
			String[] valueArray = valueList.toArray(new String[0]);
			
			url.setParameter(key, valueArray);
		}
    }
    
    /**
     * Copies the properties from map to the BaseURL.
     * @param url BaseURL
     * @return void
     */
    protected void setUrlProperties(BaseURL url) {
    	Set<String> keySet = propertiesMap.keySet();
		
		for(String key : keySet){
			
			List<String> valueList = propertiesMap.get(key);
			
			for(String value:valueList){
				url.addProperty(key, value);
			}
		}
    }
    
    
    /**
     * Replaces in String str the characters &,>,<,",' 
     * with their corresponding character entity codes.
     * @param str - the String where to replace 
     * @return String 
     */
    protected String doEscapeXml(String str) {
        str = replace(str,"&","&amp;");
        str = replace(str,"<","&lt;");
        str = replace(str,">","&gt;");
        str = replace(str,"\"","&#034;");
        str = replace(str,"'","&#039;");
        return str;
    }
       
    
    /**
     * Checks if string is empty.
     * This method is a copy from <code>org.apache.commons.lang.StringUtils</code> class.
     * @param str String
     * @return boolean
     */
    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    
    /**
     * Replaces String repl with String with in String text.
     * This method is a copy from <code>org.apache.commons.lang.StringUtils</code> class.
     * @param text - the String where to replace 
     * @param repl - the sub-String what to replace
     * @param with - the sub-String what to replace repl with
     * @return String 
     */
    private String replace(String text, String repl, String with) {
    	int max=-1;
        if (isEmpty(text) || isEmpty(repl) || with == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(repl, start);
        if (end == -1) {
            return text;
        }
        int replLength = repl.length();
        int increase = with.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(repl, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

}

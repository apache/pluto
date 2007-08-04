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
package org.apache.pluto.descriptors.servlet;

/**
 * <B>TODO</B>: Document
 * @version $Id: EnvEntryDD.java 156636 2005-03-09 12:16:31Z cziegeler $
 * @since Feb 28, 2005
 */
public class EnvEntryDD {

    private String description;
    private String envEntryName;
    private String envEntryValue;
    private String envEntryType;

    public EnvEntryDD() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnvEntryName() {
        return envEntryName;
    }

    public void setEnvEntryName(String envEntryName) {
        this.envEntryName = envEntryName;
    }

    public String getEnvEntryValue() {
        return envEntryValue;
    }

    public void setEnvEntryValue(String envEntryValue) {
        this.envEntryValue = envEntryValue;
    }

    public String getEnvEntryType() {
        return envEntryType;
    }

    public void setEnvEntryType(String envEntryType) {
        this.envEntryType = envEntryType;
    }

}


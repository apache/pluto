/*
 * Copyright 2004 The Apache Software Foundation
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
package org.apache.pluto.util.install.impl;

import org.apache.pluto.util.install.InstallationConfig;
import org.apache.pluto.util.install.PortalInstaller;
import org.apache.pluto.util.ManagementException;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Collection;

/**
 * File System based installer.  Copies files to the appropriate
 * locations.
 *
 */
public abstract class FileSystemInstaller implements PortalInstaller {

    protected void copyFilesToDirectory(Collection dependencies, File destination)
    throws IOException {
        Iterator it = dependencies.iterator();
        while(it.hasNext()) {
            File from = (File)it.next();
            FileUtils.copyFileToDirectory(from, destination);
        }
    }

    protected void copyFileToDirectory(File file, File destination)
    throws IOException {
        FileUtils.copyFileToDirectory(file, destination);
    }

    /**
     * NOTE: Order is important.  If the server is running, we want to
     *       make sure that the correct order is preserved
     *
     * 1) Install endorsed dependencies
     * 2) Install shared dependencies
     * 4) Prep Time
     *    -- Create a domain directory for the portal
     *    -- Init the configs holder
     * 5) Install the Portlet Applications
     * 6) Install the Portal Application
     * 7) Finally, install the configs
     * @param config
     * @throws org.apache.pluto.util.ManagementException
     */
    public void install(InstallationConfig config) throws ManagementException {
        File endorsedDir = getEndorsedDir(config);
        File sharedDir = getSharedDir(config);
        File domainDir = getWebAppDir(config);

        endorsedDir.mkdirs();
        sharedDir.mkdirs();
        domainDir.mkdirs();

        try {
            copyFilesToDirectory(config.getEndorsedDependencies(), endorsedDir);
            copyFilesToDirectory(config.getSharedDependencies(), sharedDir);

            copyFilesToDirectory(config.getPortletApplications().values(),  domainDir);
            copyFileToDirectory(config.getPortalApplication(), domainDir);

            writeConfiguration(config);
        }
        catch(IOException io) {
            throw new ManagementException(
                "Unable to install portal to Tomcat",
                io,
                config.getInstallationDirectory()
            );
        }
    }

    public abstract void writeConfiguration(InstallationConfig config)
    throws IOException;

    protected abstract File getEndorsedDir(InstallationConfig config );

    protected abstract File getSharedDir(InstallationConfig config);

    protected abstract File getWebAppDir(InstallationConfig config);

    public void uninstall(InstallationConfig config) {
    }

    public abstract boolean isValidInstallationDirectory(File installDir);
}

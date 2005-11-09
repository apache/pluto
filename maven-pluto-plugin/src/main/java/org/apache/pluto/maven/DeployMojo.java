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

package org.apache.pluto.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.pluto.deploy.Deployer;
import org.apache.pluto.deploy.DeploymentConfig;
import org.apache.pluto.deploy.impl.Tomcat5FileSystemDeployer;

import java.util.Properties;


/**
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @todo Document
 * @since Jul 30, 2005
 *
 * @goal deploy
 * @requiresDependencyResolution runtime
 */
public class DeployMojo extends AbstractPortletMojo {

    /**
     * @parameter expression="${pluto.deploy.impl}"
     */
    private String deployerClass = Tomcat5FileSystemDeployer.class.getName();

    /**
     * @parameter expression="${pluto.deploy.tomcat5.service}"
     */
    private String tomcatService = "Catalina";

    /**
     * @parameter expression="${pluto.deploy.tomcat5.host}"
     */
    private String tomcatHost = "localhost";

    protected void doExecute() throws Exception {
        if(!deployment.getName().endsWith(".war")) {
            throw new MojoExecutionException(deployment.getName()+" is an invalid deployment.  Please specify a war.");
        }

        Deployer deployer = createDeployer();
        deployer.deploy(createConfig(), createInputStream());

    }

    private DeploymentConfig createConfig() {
        return new DeploymentConfigImpl();
    }


    private Deployer createDeployer() throws Exception {
        Class cl = Class.forName(deployerClass);
        return (Deployer)cl.newInstance();
    }

    private class DeploymentConfigImpl extends DeploymentConfig {
        private Properties props;

        public DeploymentConfigImpl() {
            super(deployment.getName().substring(0, deployment.getName().lastIndexOf(".")));
            props = new Properties(project.getProperties());
            props.putAll(System.getProperties());
            props.setProperty("tomcat5.home", installationDirectory.getAbsolutePath());
            props.setProperty("tomcat.service", tomcatService);
            props.setProperty("tomcat.host", tomcatHost);
        }

        public String getDeploymentProperty(String key) {
            String property = props.getProperty(key);
            return property;
        }
    }
}

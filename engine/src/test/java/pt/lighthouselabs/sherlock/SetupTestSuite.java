/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pt.lighthouselabs.sherlock;

import java.io.File;
import org.apache.cassandra.service.EmbeddedCassandraService;
import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * Testing environment configuration.
 */
public final class SetupTestSuite {

	private static final Logger logger = LoggerFactory
	        .getLogger(SetupTestSuite.class);

	public final static int SERVER_PORT = 8181;
	private GlassFish gfServer;
	private String appName;

	/**
	 * This is meant to run before all tests are performed.
	 *
	 * @throws Throwable
	 */
	@BeforeSuite
	public final void setUp() {
		try {
			// set-up embedded Cassandra instance
			logger.info("Starting embedded Cassandra instance..");
			new EmbeddedCassandraService().start();
			logger.info("Cassandra started.");

			// set-up embedded Glassfish instance
			GlassFishProperties gfProperties = new GlassFishProperties();
			gfProperties.setPort("http-listener", SERVER_PORT);
			gfServer = GlassFishRuntime.bootstrap().newGlassFish(gfProperties);
			gfServer.start();

			// Create JMS resources
			CommandResult res = gfServer.getCommandRunner().run(
			        "create-jms-resource",
			        "--restype=javax.jms.QueueConnectionFactory",
			        Sherlock.JNDI_QUEUE_CONN_FACTORY_NAME);
			logger.info("{}, error:{}", res.getOutput(), res.getFailureCause());

			String queueProps = "Name=PhysicalSherlockQueue";
			res = gfServer.getCommandRunner().run("create-jms-resource",
			        "--restype=javax.jms.Queue", "--property", queueProps,
			        Sherlock.JNDI_QUEUE_NAME);
			logger.info("{}, error:{}", res.getOutput(), res.getFailureCause());

			// create WAR
			Deployer deployer = gfServer.getDeployer();
			ScatteredArchive archive = new ScatteredArchive("sherlock",
			        ScatteredArchive.Type.WAR);
			/*
			 * by adding individual files and folders, we keep control of what
			 * is and what isn't deployed for testing. for instance, we want to
			 * ignore EJB timers, as they blow up :-/
			 */
			archive.addClassPath(new File("../api/target", "classes"));
			archive.addClassPath(new File("../daos/target", "classes"));
			archive.addClassPath(new File("target", "classes"));
			archive.addClassPath(new File("target", "test-classes"));
			archive.addMetadata(new File("src/main/webapp/WEB-INF", "web.xml"));

			// Deploy the scattered web archive.
			appName = deployer
			        .deploy(archive.toURI(), "--contextroot=sherlock");
		} catch (Exception e) {
			logger.error(
			        "There was an error while setting up the testing environment.",
			        e);
      throw new RuntimeException(e);
		}
    logger.info("Application sucessfully deployed");
	}

	/**
	 * @throws GlassFishException
	 *             This is meant to run after all tests are performed.
	 *
	 * @throws
	 */
	@AfterSuite
	public final void tearDown() {
		try {
			gfServer.getDeployer().undeploy(appName);
			gfServer.stop();
			gfServer.dispose();
		} catch (Exception e) {
			logger.error("There was an error while shutting down embedded Glassfish");
		}
	}

}
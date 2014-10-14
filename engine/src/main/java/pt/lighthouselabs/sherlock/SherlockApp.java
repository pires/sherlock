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


import java.util.logging.Logger;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.client.filter.EncodingFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;
import pt.lighthouselabs.sherlock.rest.GsonProvider;

@ApplicationPath("/")
public class SherlockApp extends ResourceConfig {

  org.slf4j.Logger logger = LoggerFactory.getLogger(SherlockApp.class);

  public SherlockApp () {
    // Register resources and providers using package-scanning.
    packages("pt.lighthouselabs");

    //gzip encoding
    EncodingFeature encoding = new EncodingFeature(DeflateEncoder.class,
        GZipEncoder.class);
    register(encoding);

    // register our serializer
    register(GsonProvider.class);

    // Register an instance of LoggingFilter.
    LoggingFilter requestLogger;
    if (logger.isDebugEnabled()) {
      requestLogger = new LoggingFilter(Logger.getLogger("SherlockApp"), true);
    } else {
      requestLogger = new LoggingFilter(Logger.getLogger("SherlockApp"), false);
    }
    register(requestLogger);

    property(CommonProperties.MOXY_JSON_FEATURE_DISABLE, true);
  }
}

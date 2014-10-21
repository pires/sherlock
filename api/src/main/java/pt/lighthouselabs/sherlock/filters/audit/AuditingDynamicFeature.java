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

package pt.lighthouselabs.sherlock.filters.audit;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import pt.lighthouselabs.sherlock.Sherlock;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageProducer;

@Provider
public class AuditingDynamicFeature implements DynamicFeature {

  private static final Logger log = Logger
      .getLogger(AuditingDynamicFeature.class.getName());

  @Context
  HttpServletRequest webRequest;

  private SherlockMessageProducer producer;

  // do not inject jms resources as an injection
  // failure will cause application deployment failure :(

  public AuditingDynamicFeature() {

    Queue queue;
    QueueConnectionFactory factory;
    try {
      final InitialContext jndi = new InitialContext();
      factory = (QueueConnectionFactory) jndi
          .lookup(Sherlock.JNDI_QUEUE_CONN_FACTORY_NAME);
      queue = (Queue) jndi.lookup(Sherlock.JNDI_QUEUE_NAME);
    } catch (NamingException e) {
      log.log(Level.SEVERE, "JMS resources not found. Disabling auditing");
      return;
    }

    log.log(Level.INFO, "JMS resources found. enabling auditing");

    if (factory != null && queue != null) {
      producer = new SherlockMessageProducer(factory, queue);
    }
  }

  @Override
  public void configure(final ResourceInfo resourceInfo,
      final FeatureContext context) {

    // try to avoid reflexion
    if (producer == null) {
      return;
    }

    final Method resourceMethod = resourceInfo.getResourceMethod();

    Audit ra = resourceMethod.getAnnotation(Audit.class);
    if (ra != null) {

      context.register(
          new AuditingFilter(ra.value(), producer, webRequest));
    }
  }

}

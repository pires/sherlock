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
package pt.lighthouselabs.sherlock.messaging;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.QueueConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.lighthouselabs.sherlock.Sherlock;

/**
 * JMS message producer.
 */
@Stateless
public class Producer extends SherlockMessageProducer {

	private static final Logger logger = LoggerFactory
	        .getLogger(Producer.class);

	@Resource(name = Sherlock.JNDI_QUEUE_CONN_FACTORY_NAME)
	private QueueConnectionFactory connFactory;

	@Resource(name = Sherlock.JNDI_QUEUE_NAME)
	private Destination queue;

	@PostConstruct
	private void initJMS() throws JMSException {
		logger.info("Initializing connection...");
		super.init(connFactory, queue);
	}

	@PreDestroy
	private void closeJMS() {
		logger.info("Finalizing connection...");

		try {
			connection.close();
		} catch (JMSException e) {
			logger.error("There was an error while closing JMS connection.", e);
		}
	}

}
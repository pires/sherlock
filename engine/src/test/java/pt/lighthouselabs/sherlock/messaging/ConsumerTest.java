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

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import pt.lighthouselabs.sherlock.EjbHelper;
import pt.lighthouselabs.sherlock.messaging.SherlockMessage;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageAttribute;

public class ConsumerTest {

	private static final Logger logger = LoggerFactory
	        .getLogger(ConsumerTest.class);

	private static Producer producer = null;

	/**
	 * TODO find out how can we check ConsumerMDB.onMessage. mocks? read
	 * database for new audit message?
	 * 
	 * Right now, we can assert it's working by looking at the log output.
	 */
	@Test
	public static void basicTest() throws JMSException {
		SherlockMessage msg = new SherlockMessage();
		msg.putAttribute(SherlockMessageAttribute.TIMESTAMP, DateTime.now()
		        .getMillis());
		getProducer().sendObjectMessage(msg);
	}

	private static Producer getProducer() {
		if (producer == null) {
			try {
				producer = (Producer) EjbHelper
				        .getBean("java:global/sherlock/Producer");
			} catch (NamingException e) {
				logger.error("There was error retrieving Producer.", e);
			}
		}

		return producer;
	}

}
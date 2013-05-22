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

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;

import pt.lighthouselabs.sherlock.Sherlock;

/**
 * JMS producer service.
 */
public class SherlockMessageProducer {

	@Resource(name = Sherlock.JNDI_QUEUE_CONN_FACTORY_NAME)
	private QueueConnectionFactory connFactory;

	@Resource(name = Sherlock.JNDI_QUEUE_NAME)
	private Destination targetQueue;

	/**
	 * Sends an object-message to a parameterized JMS Queue.
	 * 
	 * @param targetQueue
	 * @param obj
	 * @throws JMSException
	 */
	public void sendObjectMessage(final SherlockMessage obj)
	        throws JMSException {
		// init JMS
		QueueConnection connection = connFactory.createQueueConnection();
		QueueSession session = connection.createQueueSession(false,
		        Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(targetQueue);

		// assemble message
		final ObjectMessage msg = session.createObjectMessage();
		msg.setObject(obj);

		// send message
		producer.send(msg);

		// clean-up
		producer.close();
		session.close();
		connection.close();
	}

}
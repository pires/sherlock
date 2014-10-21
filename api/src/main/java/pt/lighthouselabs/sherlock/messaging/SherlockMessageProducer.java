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

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.NamingException;

/**
 * JMS producer service.
 */
public class SherlockMessageProducer {

	private final QueueConnectionFactory connFactory;
	private final Queue targetQueue;

	public SherlockMessageProducer(QueueConnectionFactory factory, Queue queue) {
    connFactory = factory;
    targetQueue = queue;
	}

	/**
	 * Sends an object-message to a parameterized JMS Queue.
	 *
	 * @param targetQueue
	 * @param obj
	 * @throws JMSException
	 * @throws NamingException
	 */
	public void sendObjectMessage(final SherlockMessage obj) {
    try (JMSContext context = connFactory.createContext();){
      context.createProducer().send(targetQueue, obj);
    }

	}

}
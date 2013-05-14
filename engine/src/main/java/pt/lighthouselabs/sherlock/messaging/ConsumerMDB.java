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

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.lighthouselabs.sherlock.Sherlock;
import pt.lighthouselabs.sherlock.messaging.SherlockMessage;

/**
 * 
 */
@MessageDriven(mappedName = Sherlock.JNDI_QUEUE_NAME)
public class ConsumerMDB implements MessageListener {

	private static final Logger logger = LoggerFactory
	        .getLogger(ConsumerMDB.class.getName());

	/**
	 * when a JMS message is received, do this.
	 */
	public void onMessage(final Message msg) {
		try {
			ObjectMessage oMsg = (ObjectMessage) msg;
			SherlockMessage jobMsg = (SherlockMessage) oMsg.getObject();

			if (jobMsg != null) {
				logger.info("Received new message");
			} else {
				// log
			}
		} catch (JMSException e) {
			logger.error("There was an error while reading message", e);
		}
	}

}
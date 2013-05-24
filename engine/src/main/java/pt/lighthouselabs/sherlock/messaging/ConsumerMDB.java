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

import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.lighthouselabs.sherlock.Sherlock;
import pt.lighthouselabs.sherlock.dao.AuditRecordDao;
import pt.lighthouselabs.sherlock.model.AuditRecord;
import pt.lighthouselabs.sherlock.model.AuditRecordId;

/**
 * Consumes {@link SherlockMessage} and persists it.
 */
@MessageDriven(mappedName = Sherlock.JNDI_QUEUE_NAME)
public class ConsumerMDB implements MessageListener {

	private static final Logger logger = LoggerFactory
	        .getLogger(ConsumerMDB.class.getName());

	@EJB
	private AuditRecordDao arDao;

	/**
	 * when a JMS message is received, do this.
	 */
	public void onMessage(final Message msg) {
		try {
			ObjectMessage oMsg = (ObjectMessage) msg;
			SherlockMessage sherlockMsg = (SherlockMessage) oMsg.getObject();

			if (sherlockMsg != null) {
				logger.info("Received new message: {}", sherlockMsg);
				// read audit record
				// TODO extract to factory
				String appId = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.APP_ID);
				String username = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.USERNAME);
				String sessionId = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.SESSION_ID);
				Long timestamp = sherlockMsg
				        .getLongAttribute(SherlockMessageAttribute.TIMESTAMP);
				String method = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.METHOD);
				String action = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.ACTION);
				Long elapsed = sherlockMsg
				        .getLongAttribute(SherlockMessageAttribute.ELAPSED);
				String requestBody = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.REQUEST_BODY);
				Integer responseStatus = sherlockMsg
				        .getIntegerAttribute(SherlockMessageAttribute.RESPONSE_STATUS);
				String responseBody = sherlockMsg
				        .getStringAttribute(SherlockMessageAttribute.RESPONSE_BODY);

				// instantiate
				AuditRecordId arid = new AuditRecordId(appId, username,
				        sessionId, timestamp);
				AuditRecord ar = new AuditRecord(arid, appId, timestamp,
				        method, action, elapsed, requestBody, responseStatus,
				        responseBody);

				// persist
				arDao.create(ar);
			} else {
				// log
			}
		} catch (JMSException e) {
			logger.error("There was an error while reading message", e);
		}
	}

}
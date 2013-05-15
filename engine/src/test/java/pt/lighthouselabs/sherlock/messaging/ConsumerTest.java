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

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import pt.lighthouselabs.sherlock.AbstractTest;

public class ConsumerTest extends AbstractTest {

	/**
	 * TODO find out how can we check ConsumerMDB.onMessage. mocks? read
	 * database for new audit message?
	 * 
	 * Right now, we can assert it's working by looking at the log output.
	 */
	@Test
	public void basicTest() throws JMSException {
		SherlockMessage msg = new SherlockMessage();
		msg.putAttribute(SherlockMessageAttribute.TIMESTAMP, DateTime.now()
		        .getMillis());
		getProducer().sendObjectMessage(msg);
	}

}
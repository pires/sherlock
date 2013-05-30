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

import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.lighthouselabs.sherlock.dao.AuditRecordDao;

/**
 * Test helper class.
 */
public abstract class AbstractTest {

	private static final Logger logger = LoggerFactory
	        .getLogger(AbstractTest.class);

	private AuditRecordDao arDao = null;

	protected final AuditRecordDao getAuditRecordDao() {
		if (arDao == null) {
			try {
				arDao = (AuditRecordDao) EjbHelper
				        .getBean("java:global/sherlock/AuditRecordDao");
			} catch (NamingException e) {
				logger.error("There was error retrieving AuditRecordDao.", e);
			}
		}

		return arDao;
	}

}
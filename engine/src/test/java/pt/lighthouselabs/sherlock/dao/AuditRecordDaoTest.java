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
package pt.lighthouselabs.sherlock.dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pt.lighthouselabs.sherlock.AbstractTest;
import pt.lighthouselabs.sherlock.model.AuditRecord;
import pt.lighthouselabs.sherlock.model.AuditRecordId;

/**
 * Tests for {@link AuditRecordDao} class.
 */
public class AuditRecordDaoTest extends AbstractTest {

	private static final Logger logger = LoggerFactory
	        .getLogger(AuditRecordDaoTest.class);

	private final String APP1 = "APP1";
	private final String APP2 = "APP2";

	@Test
	public void find_all() {
		assertEquals(getAuditRecordDao().count(), 5);
		for (AuditRecord record : getAuditRecordDao().findAll())
			logger.info("[findAll] {}", record);
	}

	@Test
	public void find_all_by_appId() {
		for (AuditRecord record : getAuditRecordDao().find_all_by_appId("app2")) {
			assertTrue(record.getId().getAppId().equals(APP2));
			logger.info("[find_all_by_appId] {}", record);
		}
	}

	@Test
	public void find_all_by_appId_and_between_time_interval() {
		final long timestamp = DateTime.now().getMillis();
		for (AuditRecord record : getAuditRecordDao()
		        .find_all_by_appId_and_between_time_interval(APP1, 1L,
		                timestamp)) {
			assertTrue(record.getId().getTimestamp() > 1L);
			assertTrue(record.getId().getTimestamp() < timestamp);
			logger.info("[find_all_between_time_interval] {}", record);
		}
	}

	@BeforeClass
	private void setUp() {
		Long t = 0L;

		AuditRecordId arid1 = new AuditRecordId(APP1, "user1", "0", t);
		AuditRecord ar1 = new AuditRecord();
		ar1.setId(arid1);
		ar1.setAppIdIndex(APP1);
		ar1.setTimestampIndex(t);
		getAuditRecordDao().create(ar1);

		t = new Date().getTime();
		AuditRecordId arid2 = new AuditRecordId(APP1, "user2", "1", t);
		AuditRecord ar2 = new AuditRecord();
		ar2.setId(arid2);
		ar2.setAppIdIndex(APP1);
		ar2.setTimestampIndex(t);
		getAuditRecordDao().create(ar2);

		t -= 1000;
		AuditRecordId arid3 = new AuditRecordId(APP1, "user1", "1", t);
		AuditRecord ar3 = new AuditRecord();
		ar3.setId(arid3);
		ar3.setAppIdIndex(APP1);
		ar3.setTimestampIndex(t);
		getAuditRecordDao().create(ar3);

		t += 100;
		AuditRecordId arid4 = new AuditRecordId(APP1, "user1", "0", t);
		AuditRecord ar4 = new AuditRecord();
		ar4.setId(arid4);
		ar4.setAppIdIndex(APP1);
		ar4.setTimestampIndex(t);
		getAuditRecordDao().create(ar4);

		t -= 15000;
		AuditRecordId arid5 = new AuditRecordId(APP2, "user1", "0", t);
		AuditRecord ar5 = new AuditRecord();
		ar5.setId(arid5);
		ar5.setAppIdIndex(APP2);
		ar5.setTimestampIndex(t);
		getAuditRecordDao().create(ar5);
	}

}
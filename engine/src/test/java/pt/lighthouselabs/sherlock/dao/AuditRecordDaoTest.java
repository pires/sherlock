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

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import pt.lighthouselabs.sherlock.AbstractTest;
import pt.lighthouselabs.sherlock.model.AuditRecord;
import pt.lighthouselabs.sherlock.model.AuditRecordId;

/**
 * Tests for {@link AuditRecordDao} class.
 */
public class AuditRecordDaoTest extends AbstractTest {

	@Test
	public void persist_auditrecord_test() {
		AuditRecordId arid1 = new AuditRecordId("user1", "0", 0L);
		AuditRecord ar1 = new AuditRecord();
		ar1.setId(arid1);
		getAuditRecordDao().create(ar1);

		AuditRecordId arid2 = new AuditRecordId("user2", "1", DateTime.now()
		        .getMillis());
		AuditRecord ar2 = new AuditRecord();
		ar2.setId(arid2);
		getAuditRecordDao().create(ar2);

		AuditRecordId arid3 = new AuditRecordId("user1", "1", DateTime.now()
		        .getMillis());
		AuditRecord ar3 = new AuditRecord();
		ar3.setId(arid3);
		getAuditRecordDao().create(ar3);

		AuditRecordId arid4 = new AuditRecordId("user1", "0", DateTime.now()
		        .getMillis());
		AuditRecord ar4 = new AuditRecord();
		ar4.setId(arid4);
		getAuditRecordDao().create(ar4);

		AuditRecordId arid5 = new AuditRecordId("user1", "0", DateTime.now()
		        .getMillis());
		AuditRecord ar5 = new AuditRecord();
		ar5.setId(arid5);
		getAuditRecordDao().create(ar5);

		assertTrue(getAuditRecordDao().find(arid3).getId().getUsername()
		        .equals("user1"));
		assertEquals(getAuditRecordDao().count(), 5);

		for (AuditRecord record : getAuditRecordDao().findAll())
			System.out.println("AuditRecord: " + record.toString());
	}

	@Test
	public void find_auditrecord_from_specific_user() {
		final long begin = 0L;
		final long end = DateTime.now().getMillis();
		for (AuditRecord record : getAuditRecordDao()
		        .find_all_between_time_interval(begin + 1, end)) {
			assertTrue(record.getId().getTimestamp() > begin);
			assertTrue(record.getId().getTimestamp() < end);
		}

	}

}
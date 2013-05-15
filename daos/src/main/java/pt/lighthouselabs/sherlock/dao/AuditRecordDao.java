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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import pt.lighthouselabs.sherlock.model.AuditRecord;

@Stateless
@Local
/**
 * DAO for {@link AuditRecord} entity.
 */
public class AuditRecordDao extends AbstractDao<AuditRecord> {

	public AuditRecordDao() {
		super(AuditRecord.class);
	}

	/**
	 * Retrieves all records from two timestamps.
	 * @param begin
	 * @param end
	 * @return
	 * 
	 * TODO check that begin is no greater than end
	 */
	public List<AuditRecord> find_all_between_time_interval(Long begin, Long end) {
		String cql = "select a from AuditRecord a where a.id.timestamp between "
		        + begin + " and " + end;
		List<AuditRecord> results = getEntityManager().createQuery(cql)
		        .getResultList();

		return results == null ? new ArrayList<AuditRecord>() : results;
	}

}

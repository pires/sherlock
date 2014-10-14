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
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import pt.lighthouselabs.sherlock.model.AuditRecord;

@Stateless
@LocalBean
/**
 * DAO for {@link AuditRecord} entity.
 */
public class AuditRecordDao extends AbstractDao<AuditRecord> {

	public AuditRecordDao() {
		super(AuditRecord.class);
	}

	/**
	 * Retrieves all records from an app.
	 *
	 * @param appId
	 *            the application identifier
	 * @return a list of {@link AuditRecord} instances
	 */
	public List<AuditRecord> find_all_by_appId(String appId) {
		String cql = "select a from AuditRecord a where a.appIdIndex = :appIdIndex";
		Query q = getEntityManager().createQuery(cql);
		q.setParameter("appIdIndex", appId);
		List<AuditRecord> results = q.getResultList();

		return results == null ? new ArrayList<AuditRecord>() : results;
	}

	/**
	 * Retrieves all records between two timestamps.
	 *
	 * @param begin
	 * @param end
	 * @return
	 *
	 *         TODO check that begin is no greater than end
	 */
	public List<AuditRecord> find_all_by_appId_and_between_time_interval(
	        String appId, Long begin, Long end) {
		String cql = "select a from AuditRecord a where "
        + "a.appIdIndex = :appIdIndex and"
        + " a.timestampIndex >= :begin and"
        + " a.timestampIndex <= :end";
		Query q = getEntityManager().createQuery(cql);
		q.setParameter("appIdIndex", appId);
		q.setParameter("begin", begin);
		q.setParameter("end", end);
		List<AuditRecord> results = q.getResultList();

		return results == null ? new ArrayList<AuditRecord>() : results;
	}

}
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
package pt.lighthouselabs.sherlock.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import pt.lighthouselabs.sherlock.dao.AuditRecordDao;
import pt.lighthouselabs.sherlock.model.AuditRecord;

/**
 * Sherlock REST Endpoint.
 */
@Stateless
@Path("/")
public class SherlockService {

	@EJB
	private AuditRecordDao arDao;

	/**
	 * @return a list of all persisted {@link AuditRecord}.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public List<AuditRecord> retrieveAllAuditRecords() {
		return arDao.findAll();
	}

	/**
	 * @return a list of all persisted {@link AuditRecord} by application ID.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list/by_app_id")
	public List<AuditRecord> retrieveAllAuditRecordsFromAppId(
	        @QueryParam("appId") String appId) {
		return arDao.find_all_by_appId(appId);
	}

	/**
	 * @return a list of all persisted {@link AuditRecord} by application ID and
	 *         by time-window.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list/by_app_id_and_between_timestamps")
	public List<AuditRecord> retrieveAllAuditRecordsFromAppIdAndBetweenTimestamps(
	        @QueryParam("appId") String appId, @QueryParam("begin") Long begin,
	        @QueryParam("end") Long end) {
		return arDao.find_all_by_appId_and_between_time_interval(appId, begin,
		        end);
	}
}
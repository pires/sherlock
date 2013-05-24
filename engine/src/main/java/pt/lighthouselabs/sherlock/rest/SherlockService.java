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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pt.lighthouselabs.sherlock.dao.AuditRecordDao;
import pt.lighthouselabs.sherlock.filters.audit.Audit;
import pt.lighthouselabs.sherlock.model.AuditRecord;

/**
 * Sherlock REST Endpoint.
 */
@Path("/")
public class SherlockService {

	@EJB
	private AuditRecordDao arDao;

	/**
	 * Triggers and audit-event.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/fire")
	@Audit("fire-event")
	public Response fire() {
		return Response.ok().build();
	}

	/**
	 * @return a list of all persisted {@link AuditRecord}.
	 */
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/list")
//	public List<AuditRecord> retrieveAllAuditRecords() {
//		return arDao.findAll();
//	}

}
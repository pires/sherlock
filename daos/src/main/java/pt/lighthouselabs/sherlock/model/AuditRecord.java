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
package pt.lighthouselabs.sherlock.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

@Entity
@Table(name = "audit_records", schema = "SherlockKS@SherlockPU")
@IndexCollection(columns = { @Index(name = "appIdIndex", type = "UTF8Type"),
        @Index(name = "timestampIndex", type = "LongType") })
public class AuditRecord {

	@EmbeddedId
	private AuditRecordId id;

	@Column
	private String appIdIndex;

	@Column
	private Long timestampIndex;

	@Column
	private String method;

	@Column
	private String path;

	@Column
	private String action;

	@Column
	private Long elapsed;

	@Column
	private String requestBody;

	@Column
	private Integer responseStatus;

	@Column
	private String responseBody;

	public AuditRecord() {
	}

	public AuditRecord(AuditRecordId id, String appIdIndex,
	        Long timestampIndex, String method, String path, String action,
	        Long elapsed, String requestBody, Integer responseStatus,
	        String responseBody) {
		this.id = id;
		this.appIdIndex = appIdIndex;
		this.timestampIndex = timestampIndex;
		this.method = method;
		this.path = path;
		this.action = action;
		this.elapsed = elapsed;
		this.requestBody = requestBody;
		this.responseStatus = responseStatus;
		this.responseBody = responseBody;
	}

	public AuditRecordId getId() {
		return id;
	}

	public void setId(AuditRecordId id) {
		this.id = id;
	}

	public String getAppIdIndex() {
		return appIdIndex;
	}

	public void setAppIdIndex(String appIdIndex) {
		this.appIdIndex = appIdIndex;
	}

	public Long getTimestampIndex() {
		return timestampIndex;
	}

	public void setTimestampIndex(Long timestampIndex) {
		this.timestampIndex = timestampIndex;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getElapsed() {
		return elapsed;
	}

	public void setElapsed(Long elapsed) {
		this.elapsed = elapsed;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public Integer getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Integer responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("method", method)
		        .add("path", path).add("action", action)
		        .add("elapsed", elapsed).add("requestBody", requestBody)
		        .add("responseBody", responseBody).toString();
	}

}
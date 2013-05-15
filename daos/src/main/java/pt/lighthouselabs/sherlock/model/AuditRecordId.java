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
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

@Embeddable
public class AuditRecordId {

	@Column
	private String username;

	@Column
	private String sessionId;

	@Column
	private Long timestamp;

	public AuditRecordId() {
	}

	public AuditRecordId(final String username, final String sessionId,
	        final Long timestamp) {
		this.username = username;
		this.sessionId = sessionId;
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("username", username)
		        .add("sessionId", sessionId).add("timestamp", timestamp)
		        .toString();
	}

}
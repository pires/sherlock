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
package pt.lighthouselabs.sherlock.api.messaging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class JobMessage implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	private Map<JobAttributeHeader, Object> attributes;

	public JobMessage() {
		this.attributes = new HashMap<JobAttributeHeader, Object>();
	}

	public Map<JobAttributeHeader, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<JobAttributeHeader, Object> Attributes) {
		this.attributes = Attributes;
	}

	public String getStringAttribute(JobAttributeHeader attribute) {
		String result = attributes.get(attribute) == null ? "null"
		        : (String) attributes.get(attribute);
		return result;
	}

	public Long getLongAttribute(JobAttributeHeader attribute) {
		Long result = attributes.get(attribute) == null ? 0L
		        : (Long) attributes.get(attribute);
		return result;
	}

	public Boolean getBooleanAttribute(JobAttributeHeader attribute) {
		Boolean result = attributes.get(attribute) == null ? false
		        : (Boolean) attributes.get(attribute);
		return result;
	}

	public void putAttribute(JobAttributeHeader attribute, Object value) {
		this.attributes.put(attribute, value);
	}

	public JobMessage clone() {
		JobMessage jobMsg = new JobMessage();
		// don't ever clone an hashmap unless you know what you're doing.
		for (Map.Entry<JobAttributeHeader, Object> entry : this.attributes
		        .entrySet())
			jobMsg.putAttribute(entry.getKey(), entry.getValue());

		return jobMsg;
	}

}
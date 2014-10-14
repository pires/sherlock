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
package pt.lighthouselabs.sherlock.messaging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
//public class SherlockMessage implements Serializable, Cloneable {
public class SherlockMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<SherlockMessageAttribute, Object> attributes;

	public SherlockMessage() {
		this.attributes = new HashMap<>();
	}

	public Map<SherlockMessageAttribute, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<SherlockMessageAttribute, Object> Attributes) {
		this.attributes = Attributes;
	}

	public String getStringAttribute(SherlockMessageAttribute attribute) {
		return attributes.get(attribute) == null ? "null" : (String) attributes
		        .get(attribute);
	}

	public Long getLongAttribute(SherlockMessageAttribute attribute) {
		return attributes.get(attribute) == null ? 0L : (Long) attributes
		        .get(attribute);
	}

	public Integer getIntegerAttribute(SherlockMessageAttribute attribute) {
		return attributes.get(attribute) == null ? 0 : (Integer) attributes
		        .get(attribute);
	}

	public Boolean getBooleanAttribute(SherlockMessageAttribute attribute) {
		return attributes.get(attribute) == null ? false : (Boolean) attributes
		        .get(attribute);
	}

	public void putAttribute(SherlockMessageAttribute attribute, Object value) {
		this.attributes.put(attribute, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<SherlockMessageAttribute, Object> attribute : attributes
		        .entrySet())
			sb.append(attribute.getKey().name()).append(":")
			        .append(attribute.getValue()).append(" | ");
		return sb.toString();
	}
}
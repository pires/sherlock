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

/**
 * All possible {@link SherlockMessage} attribute names.
 */
public enum SherlockMessageAttribute {
	APP_ID,
	TIMESTAMP,
	USERNAME,
	SESSION_ID,
	METHOD,
	PATH,
	ACTION,
	ELAPSED,
	REQUEST_BODY,
	RESPONSE_STATUS,
	RESPONSE_BODY
}
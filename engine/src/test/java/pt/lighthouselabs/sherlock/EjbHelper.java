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
package pt.lighthouselabs.sherlock;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for tests that need to get EJBs from the container.
 */
public final class EjbHelper {

	private static final Logger logger = LoggerFactory
	        .getLogger(EjbHelper.class);

	private static InitialContext context;

	static {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			logger.error("Error creating initial context", e);
		}
	}

	public static Object getBean(String entity) throws NamingException {
		return context.lookup(entity);
	}

}
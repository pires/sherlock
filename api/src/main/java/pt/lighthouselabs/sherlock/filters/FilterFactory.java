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
package pt.lighthouselabs.sherlock.filters;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import pt.lighthouselabs.sherlock.filters.audit.Audit;
import pt.lighthouselabs.sherlock.filters.audit.AuditingFilter;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * FilterFactory to create List of request/response filters to be applied on a
 * particular AbstractMethod of a resource.
 * 
 */
@Provider
public class FilterFactory implements ResourceFilterFactory {

	@Context
	HttpServletRequest hr;

	public List<ResourceFilter> create(AbstractMethod method) {
		List<ResourceFilter> filters = new ArrayList<ResourceFilter>();

		// is @Audit present?
		if (method.isAnnotationPresent(Audit.class))
			filters.add(new AuditingFilter(hr, method));

		return filters;
	}

}
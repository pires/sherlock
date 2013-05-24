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

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

import pt.lighthouselabs.sherlock.filters.audit.Audit;
import pt.lighthouselabs.sherlock.filters.audit.AuditingFilter;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageProducer;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

/**
 * FilterFactory to create List of request/response filters to be applied on a
 * particular AbstractMethod of a resource.
 * 
 */
public class FilterFactory implements ResourceFilterFactory {

	@Context
	private Providers providers;

	public List<ResourceFilter> create(AbstractMethod method) {
		// is @Audit present?
		if (method.isAnnotationPresent(Audit.class)) {
			// get message producer
			final ContextResolver<SherlockMessageProducer> resolver = providers
			        .getContextResolver(SherlockMessageProducer.class, null);
			// return filter
			return Collections
			        .<ResourceFilter> singletonList(new AuditingFilter(method
			                .getAnnotation(Audit.class).value(), "SID0",
			                resolver.getContext(SherlockMessageProducer.class)));
		}

		return null;
	}
}
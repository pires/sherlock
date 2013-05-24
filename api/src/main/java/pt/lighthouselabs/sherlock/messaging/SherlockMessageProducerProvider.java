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

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

/**
 * JAX-RS provider for injecting {@link SherlockMessageProducer}.
 */
@Provider
public final class SherlockMessageProducerProvider extends
        AbstractHttpContextInjectable<SherlockMessageProducer> implements
        InjectableProvider<Context, Type>,
        ContextResolver<SherlockMessageProducer> {

	@Override
	public Injectable<SherlockMessageProducer> getInjectable(
	        ComponentContext componentContext, Context annotation, Type type) {
		if (type.equals(SherlockMessageProducer.class))
			return this;
		return null;
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public SherlockMessageProducer getValue(HttpContext httpContext) {
		return new SherlockMessageProducer();
	}

	@Override
	public SherlockMessageProducer getContext(Class<?> type) {
		return new SherlockMessageProducer();
	}

}
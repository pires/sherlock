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
package pt.lighthouselabs.sherlock.filters.audit;

import java.io.IOException;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.joda.time.DateTime;

import pt.lighthouselabs.sherlock.filters.CustomHttpServletRequestWrapper;
import pt.lighthouselabs.sherlock.messaging.SherlockMessage;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageAttribute;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageProducer;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Filter all incoming requests, look for session and logged-user information
 * and put it into our audit log.
 */
@Provider
public class AuditingFilter implements ResourceFilter, ContainerRequestFilter,
        ContainerResponseFilter {

	private HttpServletRequest hr;
	private final AbstractMethod am;
	CustomHttpServletRequestWrapper wrapper;

	@Context
	private SherlockMessageProducer producer;

	private long requestTimestamp = 0L;

	public AuditingFilter(HttpServletRequest hr, AbstractMethod am) {
		this.hr = hr;
		this.am = am;
	}

	/**
	 * TODO this is where we'll start to compile data to audit
	 */
	public ContainerRequest filter(ContainerRequest request) {
		requestTimestamp = DateTime.now().getMillis();

		try {
			wrapper = new CustomHttpServletRequestWrapper(hr);
			request.setEntityInputStream(wrapper.getInputStream());
		} catch (IOException e) {
			// ignore?
		}

		return request;
	}

	/**
	 * TODO this is where we'll stop compiling data and send to audit engine
	 */
	public ContainerResponse filter(ContainerRequest request,
	        ContainerResponse response) {
		/*
		 * TODO
		 * 
		 * If principal name ever is an email, do the following: 1) Get UserDao
		 * by context lookup since injection is not possible 2) Find by email
		 */
		final String appId = "testApp"; // TODO this shall be supplied by the
		                                // Sherlock client
		final String username = hr.getUserPrincipal().getName();
		final String sessionId = hr.getSession(true).getId();

		final Long elapsed = DateTime.now().getMillis() - requestTimestamp;
		final String method = request.getMethod();
		final String path = request.getRequestUri().toASCIIString();
		final String action = am.getAnnotation(Audit.class).value();
		final String requestBody = wrapper.getBody();
		final int responseStatus = response.getStatus();
		final String responseBody = response.getEntity().toString();

		// send message
		SherlockMessage msg = new SherlockMessage();
		msg.putAttribute(SherlockMessageAttribute.APP_ID, appId);
		msg.putAttribute(SherlockMessageAttribute.USERNAME, username);
		msg.putAttribute(SherlockMessageAttribute.SESSION_ID, sessionId);
		msg.putAttribute(SherlockMessageAttribute.TIMESTAMP, requestTimestamp);
		msg.putAttribute(SherlockMessageAttribute.ELAPSED, elapsed);
		msg.putAttribute(SherlockMessageAttribute.METHOD, method);
		msg.putAttribute(SherlockMessageAttribute.PATH, path);
		msg.putAttribute(SherlockMessageAttribute.ACTION, action);
		msg.putAttribute(SherlockMessageAttribute.REQUEST_BODY, requestBody);
		msg.putAttribute(SherlockMessageAttribute.RESPONSE_STATUS,
		        responseStatus);
		msg.putAttribute(SherlockMessageAttribute.RESPONSE_BODY, responseBody);
		try {
			producer.sendObjectMessage(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

}
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.jms.JMSException;

import org.joda.time.DateTime;

import pt.lighthouselabs.sherlock.messaging.SherlockMessage;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageAttribute;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageProducer;

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.core.util.ReaderWriter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ContainerResponseWriter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * Filter all incoming requests, look for session and logged-user information
 * and put it into our audit log.
 */
public final class AuditingFilter implements ResourceFilter,
        ContainerRequestFilter, ContainerResponseFilter {

	private SherlockMessageProducer producer;

	private long requestTimestamp = 0L;
	private String requestBody;
	private final String appId = "testApp"; // TODO to be supplied by app
	private String username, method;
	private final String action, sessionId;
	private int responseStatus;
	private String responseBody;

	public AuditingFilter(String auditValue, String sessionId,
	        SherlockMessageProducer producer) {
		this.action = auditValue;
		this.sessionId = sessionId;
		this.producer = producer;
	}

	/**
	 * TODO this is where we'll start to compile data to audit
	 */
	public ContainerRequest filter(ContainerRequest request) {
		requestTimestamp = DateTime.now().getMillis();
		username = request.getUserPrincipal() == null ? "UNAUTHENTICATED"
		        : request.getUserPrincipal().getName();
		method = request.getMethod();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = request.getEntityInputStream();
		try {
			if (in.available() > 0) {
				ReaderWriter.writeTo(in, out);
				byte[] requestEntity = out.toByteArray();
				request.setEntityInputStream(new ByteArrayInputStream(
				        requestEntity));
				requestBody = new String(out.toByteArray(), "UTF-8");
			}
		} catch (IOException e) {
			throw new ContainerException(e);
		}

		System.out.println("FILTERING REQUEST: " + username);
		return request;
	}

	/**
	 * TODO this is where we'll stop compiling data and send to audit engine
	 */
	public ContainerResponse filter(ContainerRequest request,
	        ContainerResponse response) {
		final Long elapsed = DateTime.now().getMillis() - requestTimestamp;
		System.out.println("FILTERING RESPONSE");

		response.setContainerResponseWriter(new Adapter(response
		        .getContainerResponseWriter()));

		if (producer != null) {
			// craft message
			SherlockMessage msg = new SherlockMessage();
			msg.putAttribute(SherlockMessageAttribute.APP_ID, appId);
			msg.putAttribute(SherlockMessageAttribute.USERNAME, username);
			msg.putAttribute(SherlockMessageAttribute.SESSION_ID, sessionId);
			msg.putAttribute(SherlockMessageAttribute.TIMESTAMP,
			        requestTimestamp);
			msg.putAttribute(SherlockMessageAttribute.ELAPSED, elapsed);
			msg.putAttribute(SherlockMessageAttribute.METHOD, method);
			msg.putAttribute(SherlockMessageAttribute.ACTION, action);
			msg.putAttribute(SherlockMessageAttribute.REQUEST_BODY, requestBody);
			msg.putAttribute(SherlockMessageAttribute.RESPONSE_STATUS,
			        responseStatus);
			msg.putAttribute(SherlockMessageAttribute.RESPONSE_BODY,
			        responseBody);

			if (producer.isReady()) {
				try {
					producer.sendObjectMessage(msg);
				} catch (JMSException e) {
					throw new ContainerException(e);
				}
			}
		}

		return response;
	}

	/**
	 * We need this to send response to client.
	 */
	private final class Adapter implements ContainerResponseWriter {
		private final ContainerResponseWriter crw;
		private ContainerResponse response;
		private ByteArrayOutputStream baos;

		Adapter(ContainerResponseWriter crw) {
			this.crw = crw;
		}

		public OutputStream writeStatusAndHeaders(long contentLength,
		        ContainerResponse response) throws IOException {
			responseStatus = response.getStatus();
			responseBody = "TODO";
			this.response = response;
			return this.baos = new ByteArrayOutputStream();
		}

		public void finish() throws IOException {
			byte[] entity = baos.toByteArray();
			OutputStream out = crw.writeStatusAndHeaders(-1, response);
			out.write(entity);
			crw.finish();
		}
	}

	public ContainerRequestFilter getRequestFilter() {
		return this;
	}

	public ContainerResponseFilter getResponseFilter() {
		return this;
	}

}
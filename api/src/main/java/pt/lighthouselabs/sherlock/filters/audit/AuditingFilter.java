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
/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package pt.lighthouselabs.sherlock.filters.audit;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import pt.lighthouselabs.sherlock.messaging.SherlockMessage;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageAttribute;
import pt.lighthouselabs.sherlock.messaging.SherlockMessageProducer;

/**
 * Filter all incoming requests, look for session and logged-user information
 * and put it into our audit log.
 */
public final class AuditingFilter implements
    ContainerRequestFilter, ContainerResponseFilter {

  HttpServletRequest webRequest;

  private SherlockMessageProducer producer;

  private long requestTimestamp = 0L;
  private String requestBody, responseBody;
  private String appId = "testApp"; // TODO to be supplied by app
  private String username, method;
  private final String action;
  private String sessionId;
  private int responseStatus;

  public AuditingFilter(String auditValue, SherlockMessageProducer producer,
      HttpServletRequest webRequest) {

    this.action = auditValue;
    this.producer = producer;
    this.webRequest = webRequest;
  }

  /**
   * TODO this is where we'll start to compile data to audit
   */
  @Override
  public void filter(ContainerRequestContext request) throws IOException {
    requestTimestamp = System.currentTimeMillis();

    Principal user = request.
        getSecurityContext().
        getUserPrincipal();

    username = (user == null)
        ? "UNAUTHENTICATED"
        : user.getName();

    method = request.getMethod();

    URI requestUri;
    try {
      requestUri = new URI(webRequest.getRequestURL().
          toString());
    } catch (URISyntaxException ex) {
      requestUri = URI.create("http://ubknownHost/");
    }

    this.appId = new StringBuilder().
        append(requestUri.getHost()).
        append(webRequest.getContextPath()).
        toString();
  }

  /**
   * TODO this is where we'll stop compiling data and send to audit engine
   */
  @Override
  public void filter(ContainerRequestContext request,
      ContainerResponseContext response) throws IOException {
    final Long elapsed = System.currentTimeMillis() - requestTimestamp;

    sessionId = webRequest.getSession(true).
        getId();

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

      producer.sendObjectMessage(msg);
    }
  }
}

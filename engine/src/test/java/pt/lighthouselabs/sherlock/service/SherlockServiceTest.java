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
package pt.lighthouselabs.sherlock.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import pt.lighthouselabs.sherlock.RESTClient;
import pt.lighthouselabs.sherlock.SetupTestSuite;
import pt.lighthouselabs.sherlock.model.AuditRecord;
import pt.lighthouselabs.sherlock.rest.SherlockService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Tests {@link SherlockService}.
 */
public class SherlockServiceTest {

	private static final String URL = "http://localhost:"
	        + SetupTestSuite.SERVER_PORT + "/sherlock";

	/**
	 * Tests reading a list of {@link AuditRecord}.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void test_list_all_auditrecords() throws ClientProtocolException,
	        IOException {
		HttpResponse response = RESTClient.doHttpGet(URL.concat("/list"), null,
		        null);
		assertEquals(response.getStatusLine().getStatusCode(), 200);
		Type paginateType = new TypeToken<List<AuditRecord>>() {
		}.getType();
		Reader reader = new InputStreamReader(
		        response.getEntity().getContent(), "UTF-8");
		List<AuditRecord> records = new Gson().fromJson(reader, paginateType);
		assertTrue(records.size() > 0);
	}

	/**
	 * Tests reading a list of {@link AuditRecord} by application ID.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void test_list_all_auditrecords_by_appId()
	        throws ClientProtocolException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", "APP2");

		HttpResponse response = RESTClient.doHttpGet(
		        URL.concat("/list/by_app_id"), null, params);
		assertEquals(response.getStatusLine().getStatusCode(), 200);
		Type paginateType = new TypeToken<List<AuditRecord>>() {
		}.getType();
		Reader reader = new InputStreamReader(
		        response.getEntity().getContent(), "UTF-8");
		List<AuditRecord> records = new Gson().fromJson(reader, paginateType);
		for (AuditRecord record : records)
			assertEquals(record.getAppIdIndex(), "APP2");
	}

	/**
	 * Tests reading a list of {@link AuditRecord} by application ID and between
	 * time window.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void test_list_all_auditrecords_by_appId_and_between_timestamp()
	        throws ClientProtocolException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", "APP1");
		params.put("begin", "1");
		final Long timestamp = DateTime.now().getMillis();
		params.put("end", Long.toString(timestamp));
		HttpResponse response = RESTClient.doHttpGet(
		        URL.concat("/list/by_app_id_and_between_timestamps"), null,
		        params);
		assertEquals(response.getStatusLine().getStatusCode(), 200);
		Type paginateType = new TypeToken<List<AuditRecord>>() {
		}.getType();
		Reader reader = new InputStreamReader(
		        response.getEntity().getContent(), "UTF-8");
		List<AuditRecord> records = new Gson().fromJson(reader, paginateType);
		for (AuditRecord record : records) {
			assertEquals(record.getAppIdIndex(), "APP1");
			assertTrue(record.getId().getTimestamp() > 1L);
			assertTrue(record.getId().getTimestamp() < timestamp);
		}
	}

}
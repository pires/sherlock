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
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import pt.lighthouselabs.sherlock.AbstractTest;
import pt.lighthouselabs.sherlock.RESTClient;
import pt.lighthouselabs.sherlock.SetupTestSuite;
import pt.lighthouselabs.sherlock.model.AuditRecord;
import pt.lighthouselabs.sherlock.rest.SherlockService;

/**
 * Tests {@link SherlockService}.
 */
public class SherlockServiceTest extends AbstractTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SherlockServiceTest.class);

	private static final String URL = "http://localhost:"
	        + SetupTestSuite.SERVER_PORT + "/sherlock";

	/**
	 * Tests firing an audit event.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void testFireAuditEvent() throws ClientProtocolException,
	        IOException {
		HttpResponse response = RESTClient.doHttpGet(URL.concat("/fire"), null,
		        null);
		assertEquals(response.getStatusLine().getStatusCode(), 200);
	}

	/**
	 * Tests reading a list of {@link AuditRecord}.
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Test
	public void testListAllAuditRecords() throws ClientProtocolException,
	        IOException {
		/*
		 * This can be uncommented when AuditRecordDao is packaged as EJB.
		 */
//		HttpResponse response = RESTClient.doHttpGet(URL.concat("/list"), null,
//		        null);
//		assertEquals(response.getStatusLine().getStatusCode(), 200);
//
//		Type paginateType = new TypeToken<List<AuditRecord>>() {
//		}.getType();
//		Reader reader = new InputStreamReader(
//		        response.getEntity().getContent(), "UTF-8");
//		List<AuditRecord> records = new Gson().fromJson(reader, paginateType);
		
		List<AuditRecord> records = getAuditRecordDao().findAll();
		assertTrue(records.size() > 0);
		for (AuditRecord record : records)
			logger.info("{}", record);
	}

}
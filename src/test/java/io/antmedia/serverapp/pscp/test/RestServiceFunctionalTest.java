package io.antmedia.serverapp.pscp.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.antmedia.datastore.db.types.Broadcast;
import io.antmedia.datastore.db.types.Endpoint;
import io.antmedia.rest.BroadcastRestService;
import io.antmedia.rest.BroadcastRestService.Result;
import io.antmedia.social.endpoint.VideoServiceEndpoint.DeviceAuthParameters;


public class RestServiceFunctionalTest {


	private static final String ROOT_APP_URL = "http://localhost:5080/PSCP";

	private static final String ROOT_SERVICE_URL = "http://localhost:5080/PSCP/rest";
	private static Process tmpExec;
	private BroadcastRestService restService = null;

	Gson gson = new Gson();

	@Before
	public void before() {
		restService = new BroadcastRestService();
	}

	@After
	public void after() {
		restService = null;
	}

	@Test
	public void testBroadcastCreateFunctional() {
		createBroadcast("name");
	}

	public Broadcast createBroadcast(String name) {
		String url = ROOT_SERVICE_URL + "/broadcast/create";

		HttpClient client = HttpClients.custom()
				.setRedirectStrategy(new LaxRedirectStrategy())
				.build();
		Gson gson = new Gson();
		Broadcast broadcast = new Broadcast();
		if (name != null) {
			broadcast.setName(name);
		}

		try {

			HttpUriRequest post = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			HttpResponse response = client.execute(post);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp);
			assertEquals(tmp.getName(), broadcast.getName());
			return tmp;
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return null;
	}

	@Test
	public void testBroadcastCreateFunctionalWithoutName() {

		createBroadcast(null);

	}

	@Test
	public void testBroadcastCreateFunctionalWithoutObject() {

		String url = ROOT_SERVICE_URL + "/broadcast/create";

		HttpClient client = HttpClients.custom()
				.setRedirectStrategy(new LaxRedirectStrategy())
				.build();
		Gson gson = new Gson();
		Broadcast broadcast = null; //new Broadcast();
		//broadcast.name = "name";

		try {

			HttpUriRequest post = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			HttpResponse response = client.execute(post);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp);

		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	@Test
	public void testBroadcasGetUndefined() {
		try {
			/// get broadcast 
			String url = ROOT_SERVICE_URL + "/broadcast/get";

			CloseableHttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			Gson gson = new Gson();
			//Broadcast broadcast = null; //new Broadcast();
			//broadcast.name = "name";



			HttpUriRequest get = RequestBuilder.get()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//	.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			CloseableHttpResponse response = client.execute(get);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp2 = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp2);
			assertEquals(tmp2.getStreamId(), null);

		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testBroadcasGetUnknown() {
		Broadcast tmp2 = getBroadcast("dsfsfsfs");
		assertNotNull(tmp2);
		assertEquals(tmp2.getStreamId(), null);
	}

	public Broadcast getBroadcast(String streamId) {
		try {
			/// get broadcast 
			String url = ROOT_SERVICE_URL + "/broadcast/get";

			CloseableHttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			Gson gson = new Gson();
			//Broadcast broadcast = null; //new Broadcast();
			//broadcast.name = "name";



			HttpUriRequest get = RequestBuilder.get()
					.setUri(url + "?id=" + streamId)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//	.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			CloseableHttpResponse response = client.execute(get);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp2 = gson.fromJson(result.toString(), Broadcast.class);
			return tmp2;
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return null;
	}


	@Test
	public void testBroadcasGetFree() {
		try {
			/// get broadcast 
			String url = ROOT_SERVICE_URL + "/broadcast/get";

			CloseableHttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			Gson gson = new Gson();
			//Broadcast broadcast = null; //new Broadcast();
			//broadcast.name = "name";



			HttpUriRequest get = RequestBuilder.get()
					.setUri(url + "?id=")
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//	.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			CloseableHttpResponse response = client.execute(get);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp2 = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp2);
			assertEquals(tmp2.getStreamId(), null);

		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testBroadcasGet() {
		try {

			String url = ROOT_SERVICE_URL + "/broadcast/create";

			HttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			Gson gson = new Gson();
			Broadcast broadcast = new Broadcast();
			broadcast.setName("namesdfsf");



			HttpUriRequest post = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			HttpResponse response = client.execute(post);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp);
			assertNotSame(tmp.getDate(), 0L);



			/// get broadcast 
			url = ROOT_SERVICE_URL + "/broadcast/get";

			client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			//Gson gson = new Gson();
			//Broadcast broadcast = null; //new Broadcast();
			//broadcast.name = "name";



			HttpUriRequest get = RequestBuilder.get()
					.setUri(url + "?id="+tmp.getStreamId())
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//	.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			response = client.execute(get);

			result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp2 = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp);
			assertEquals(tmp.getStreamId(), tmp2.getStreamId());
			assertEquals(tmp.getName(), tmp2.getName());

		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testBroadcastDeleteNULL() {

		Result result2 = deleteBroadcast(null);

		assertNotNull(result2);
		assertFalse(result2.success);
	}

	@Test
	public void testDeleteVoDFile() {

		try {
			String streamName = "vod_delete_test";
			Broadcast broadcast = createBroadcast(streamName);
			assertNotNull(broadcast);
			assertNotNull(broadcast.getStreamId());

			Process execute = execute("/usr/local/bin/ffmpeg -re -i src/test/resources/test.flv -acodec copy "
					+ "	-vcodec copy -f flv rtmp://localhost/PSCP/" + broadcast.getStreamId());


			Thread.sleep(20000);

			execute.destroy();

			//let mp4 file to be created
			Thread.sleep(2000);

			boolean vodExists = AppFunctionalTest.exists(ROOT_APP_URL + "/streams/" + broadcast.getStreamId() + ".mp4", false);

			assertTrue(vodExists);


			String url = ROOT_SERVICE_URL + "/broadcast/deleteVoDFile/"+ broadcast.getStreamId();

			String response = makePOSTRequest(url, null);

			Result deleteVoDResponse = gson.fromJson(response.toString(), Result.class);
			assertTrue(deleteVoDResponse.success);


			try {
				Process process = Runtime.getRuntime().exec("./src/test/resources/scripts/check_file_deleted.sh " + broadcast.getStreamId());
				InputStream errorStream = process.getInputStream();
				byte[] data = new byte[1024];
				int length = 0;
				String errorStr = null;

				try {
					while ((length = errorStream.read(data, 0, data.length)) > 0) {
						errorStr  = new String(data, 0, length);
						System.out.println(errorStr);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				assertTrue(errorStr == null || errorStr.length() == 1);


			} catch (IOException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}


	public String makePOSTRequest(String url, String entity) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();


			RequestBuilder builder = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

			if (entity != null) {
				builder.setEntity(new StringEntity(entity));
			}

			HttpUriRequest post = builder.build();
			CloseableHttpResponse response = client.execute(post);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			return result.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return null;
	}


	public Result deleteBroadcast(String id) {
		try {
			//delete broadcast
			String url = ROOT_SERVICE_URL + "/broadcast/delete/"+ id;

			CloseableHttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();


			HttpUriRequest post = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.build();

			CloseableHttpResponse response = client.execute(post);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Result result2 = gson.fromJson(result.toString(), Result.class);
			return result2;
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		return null;
	}


	public Result updateNameAndDescription(String broadcastId, String name,	String description ) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();


		HttpPost httppost = new HttpPost(ROOT_SERVICE_URL + "/broadcast/updateInfo");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded;"));
		nameValuePairs.add(new BasicNameValuePair("id", broadcastId));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("description", description));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));


		CloseableHttpResponse response = httpclient.execute(httppost);

		StringBuffer result = readResponse(response);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(result.toString());
		}
		Gson gson = new Gson();
		System.out.println("result string: " + result.toString());
		Result tmp = gson.fromJson(result.toString(), Result.class);

		return tmp;
	}

	public Result updatePublish(String broadcastId, boolean publish) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();


		HttpPost httppost = new HttpPost(ROOT_SERVICE_URL + "/broadcast/updatePublishStatus");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded;"));
		nameValuePairs.add(new BasicNameValuePair("id", broadcastId));
		nameValuePairs.add(new BasicNameValuePair("publish", String.valueOf(publish)));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));


		CloseableHttpResponse response = httpclient.execute(httppost);

		StringBuffer result = readResponse(response);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(result.toString());
		}
		Gson gson = new Gson();
		System.out.println("result string: " + result.toString());
		Result tmp = gson.fromJson(result.toString(), Result.class);
		return tmp;
	}


	@Test
	public void testUpdate() {

		//create broadcast
		Broadcast broadcast = createBroadcast(null);

		String name = "string name";
		String description = "String descriptio";
		//update name and description
		try {
			Result result = updateNameAndDescription(broadcast.getStreamId().toString(), name, description);
			assertTrue(result.success);

			//get broadcast
			broadcast = getBroadcast(broadcast.getStreamId().toString());

			//check name and description
			assertEquals(broadcast.getName(), name);
			assertEquals(broadcast.getDescription(), description);
			assertTrue(broadcast.isPublish());

			//update publish info
			boolean publish = false;
			result = updatePublish(broadcast.getStreamId().toString(), publish);
			assertTrue(result.success);

			//get broacdast
			broadcast = getBroadcast(broadcast.getStreamId().toString());

			//check publish info
			assertEquals(broadcast.getName(), name);
			assertEquals(broadcast.getDescription(), description);
			assertEquals(broadcast.isPublish(), publish);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	public Result addEndpoint(String broadcastId, String rtmpUrl) throws Exception 
	{
		CloseableHttpClient httpclient = HttpClients.createDefault();


		HttpPost httppost = new HttpPost(ROOT_SERVICE_URL + "/broadcast/addEndpoint");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded;"));
		nameValuePairs.add(new BasicNameValuePair("id", broadcastId));
		nameValuePairs.add(new BasicNameValuePair("rtmpUrl", rtmpUrl));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));


		CloseableHttpResponse response = httpclient.execute(httppost);

		StringBuffer result = readResponse(response);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(result.toString());
		}
		Gson gson = new Gson();
		System.out.println("result string: " + result.toString());
		Result tmp = gson.fromJson(result.toString(), Result.class);

		return tmp;
	}

	public Result addSocialEndpoint(String broadcastId, String name) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();


		HttpPost httppost = new HttpPost(ROOT_SERVICE_URL + "/broadcast/addSocialEndpoint");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded;"));
		nameValuePairs.add(new BasicNameValuePair("id", broadcastId));
		nameValuePairs.add(new BasicNameValuePair("serviceName", name));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));


		CloseableHttpResponse response = httpclient.execute(httppost);

		StringBuffer result = readResponse(response);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(result.toString());
		}
		Gson gson = new Gson();
		System.out.println("result string: " + result.toString());
		Result tmp = gson.fromJson(result.toString(), Result.class);

		return tmp;
	}

	private DeviceAuthParameters getDeviceAuthParameters(String serviceName) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();


		HttpPost httppost = new HttpPost(ROOT_SERVICE_URL + "/broadcast/getDeviceAuthParameters/" + serviceName);
		//	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//	nameValuePairs.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded;"));
		//	nameValuePairs.add(new BasicNameValuePair("serviceName", serviceName));
		//	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));


		CloseableHttpResponse response = httpclient.execute(httppost);

		StringBuffer result = readResponse(response);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(result.toString());
		}


		Gson gson = new Gson();
		System.out.println("result string: " + result.toString());
		DeviceAuthParameters tmp = gson.fromJson(result.toString(), DeviceAuthParameters.class);

		return tmp;

	}

	private Result checkDeviceAuthStatus(String serviceName) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();


		HttpPost httppost = new HttpPost(ROOT_SERVICE_URL + "/broadcast/checkDeviceAuthStatus/" + serviceName);
		//	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//	nameValuePairs.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded;"));
		//	nameValuePairs.add(new BasicNameValuePair("serviceName", serviceName));
		//	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));


		CloseableHttpResponse response = httpclient.execute(httppost);

		StringBuffer result = readResponse(response);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(result.toString());
		}
		Gson gson = new Gson();
		System.out.println("result string: " + result.toString());
		Result tmp = gson.fromJson(result.toString(), Result.class);

		return tmp;

	}

	//TODO: restart server after testAddEndpoint and 
	// check that social endpoints are added correctly 


	@Test
	public void testCheckSocialEndpointRecreated() {
		Result result;
		try {
			//create broadcast
			Broadcast broadcast = createBroadcast("social_endpoint_check");
			//add facebook endpoint


			result = addSocialEndpoint(broadcast.getStreamId().toString(), "facebook");

			//check that it is successfull
			assertTrue(result.success);


			//add youtube endpoint
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "youtube");

			//check that it is succes full
			assertTrue(result.success);


			//add twitter endpoint
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "periscope");

			//check that it is succes full
			assertTrue(result.success);

			//get endpoint list
			broadcast = getBroadcast(broadcast.getStreamId().toString());



			//check that 3 element exist
			assertNotNull(broadcast.getEndPointList());
			assertEquals(broadcast.getEndPointList().size(), 3);



			broadcast = getBroadcast(broadcast.getStreamId().toString());
			List<Endpoint> endpointList = broadcast.getEndPointList();

			for (Endpoint endpoint : endpointList) {
				System.out.println("endpoint url: " + endpoint.rtmpUrl + " broadcast.id=" + endpoint.broadcastId + " stream id: " + endpoint.streamId);

			}

			Process execute = execute("/usr/local/bin/ffmpeg -re -i src/test/resources/test.flv -acodec copy -vcodec copy -f flv rtmp://localhost/PSCP/" + broadcast.getStreamId());

			Thread.sleep(20000);

			execute.destroy();

			//this value is critical because server creates endpoints on social networks
			Thread.sleep(15000);

			broadcast = getBroadcast(broadcast.getStreamId().toString());
			List<Endpoint> endpointList2 = broadcast.getEndPointList();
			assertEquals(endpointList2.size(), 3);

			for (Endpoint endpoint : endpointList2) {
				System.out.println("new endpoint url: " + endpoint.rtmpUrl + " broadcast.id=" + endpoint.broadcastId + " stream id: " + endpoint.streamId);

			}


			for (Endpoint endpoint : endpointList2) {
				for (Endpoint endpointFirst : endpointList) {
					assertTrue(!endpoint.rtmpUrl.equals(endpointFirst.rtmpUrl) || !endpoint.broadcastId.equals(endpointFirst.broadcastId));
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	public static Process execute(final String command) {
		tmpExec = null;
		new Thread() {
			public void run() {
				try {

					tmpExec = Runtime.getRuntime().exec(command);
					InputStream errorStream = tmpExec.getErrorStream();
					byte[] data = new byte[1024];
					int length = 0;

					while ((length = errorStream.read(data, 0, data.length)) > 0) {
						System.out.println(new String(data, 0, length));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();

		while (tmpExec == null) {
			try {
				System.out.println("Waiting for exec get initialized...");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tmpExec;
	}


	@Test
	public void testAddEndpoint() {

		try {
			//create broadcast
			Broadcast broadcast = createBroadcast(null);

			updateNameAndDescription(broadcast.getStreamId().toString(), "name", "description");

			System.out.println("broadcast id string: " + broadcast.getStreamId().toString());

			//get broadcast
			broadcast = getBroadcast(broadcast.getStreamId().toString());

			//checek there no endpoint
			assertNull(broadcast.getEndPointList());

			//add facebook end point
			Result result = addSocialEndpoint(broadcast.getStreamId().toString(), "facebook");

			//check that error returns because it is not authenticated
			assertFalse(result.success);
			assertNotNull(result.message);

			//add twitter end point
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "twitter");

			//check that error returns
			assertFalse(result.success);
			assertNotNull(result.message);

			//add youtube end point
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "youtube");

			//check that error returns
			assertFalse(result.success);
			assertNotNull(result.message);

			//authenticate facebook
			//get parameters
			DeviceAuthParameters deviceAuthParameters = getDeviceAuthParameters("facebook");
			System.out.println(" url: " + deviceAuthParameters.verification_url );
			System.out.println(" user code: " + deviceAuthParameters.user_code );
			assertNotNull(deviceAuthParameters.verification_url);
			assertNotNull(deviceAuthParameters.user_code);

			//ask if authenticated
			do {
				System.out.println("You should enter this code: " + deviceAuthParameters.user_code +
						" to this url: " + deviceAuthParameters.verification_url);
				System.out.println("Waiting before asking auth status");

				Thread.sleep(deviceAuthParameters.interval * 1000);
				result = checkDeviceAuthStatus("facebook");
				System.out.println("auth status is " + result.success);

			} while (!result.success);

			//add facebook endpoint
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "facebook");

			//check that it is successfull
			assertTrue(result.success);

			//authenticate twitter
			deviceAuthParameters = getDeviceAuthParameters("periscope");
			System.out.println(" url: " + deviceAuthParameters.verification_url );
			System.out.println(" user code: " + deviceAuthParameters.user_code );
			assertNotNull(deviceAuthParameters.verification_url);
			assertNotNull(deviceAuthParameters.user_code);

			do {
				System.out.println("You should enter this code: " + deviceAuthParameters.user_code +
						" to this url: " + deviceAuthParameters.verification_url);
				System.out.println("Waiting "+ deviceAuthParameters.interval + " seconds before asking auth status");

				Thread.sleep(deviceAuthParameters.interval * 1000);
				result = checkDeviceAuthStatus("periscope");
				System.out.println("auth status is " + result.success);

			} while (!result.success);


			//add twitter endpoint
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "periscope");

			//check that it is succes full
			assertTrue(result.success);


			//authenticate youtube
			deviceAuthParameters = getDeviceAuthParameters("youtube");
			System.out.println(" url: " + deviceAuthParameters.verification_url );
			System.out.println(" user code: " + deviceAuthParameters.user_code );
			assertNotNull(deviceAuthParameters.verification_url);
			assertNotNull(deviceAuthParameters.user_code);

			do {
				System.out.println("You should enter this code: " + deviceAuthParameters.user_code +
						" to this url: " + deviceAuthParameters.verification_url);
				System.out.println("Waiting "+ deviceAuthParameters.interval + " seconds before asking auth status");

				Thread.sleep(deviceAuthParameters.interval * 1000);
				result = checkDeviceAuthStatus("youtube");
				System.out.println("auth status is " + result.success);

			} while (!result.success);

			//add youtube endpoint
			result = addSocialEndpoint(broadcast.getStreamId().toString(), "youtube");

			//check that it is succes full
			assertTrue(result.success);

			//add generic endpoint
			result = addEndpoint(broadcast.getStreamId().toString(), "rtmp://dfjdksafjlaskfjalkfj");

			//check that it is successfull
			assertTrue(result.success);

			//get endpoint list
			broadcast = getBroadcast(broadcast.getStreamId().toString());

			//check that 4 element exist
			assertNotNull(broadcast.getEndPointList());
			assertEquals(broadcast.getEndPointList().size(), 4);

		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}



	}


	@Test
	public void testBroadcastDelete() {
		try {

			String url = ROOT_SERVICE_URL + "/broadcast/create";

			HttpClient client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			Gson gson = new Gson();
			Broadcast broadcast = new Broadcast();
			broadcast.setName("namesdfsf");



			HttpUriRequest post = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			HttpResponse response = client.execute(post);

			StringBuffer result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp);
			assertNotSame(tmp.getDate(), 0L);



			/// get broadcast 
			url = ROOT_SERVICE_URL + "/broadcast/get";

			client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			//Gson gson = new Gson();
			//Broadcast broadcast = null; //new Broadcast();
			//broadcast.name = "name";



			HttpUriRequest get = RequestBuilder.get()
					.setUri(url + "?id="+tmp.getStreamId())
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//	.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			response = client.execute(get);

			result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Broadcast tmp2 = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp);
			assertEquals(tmp.getStreamId(), tmp2.getStreamId());
			assertEquals(tmp.getName(), tmp2.getName());


			//delete broadcast
			url = ROOT_SERVICE_URL + "/broadcast/delete/"+ tmp2.getStreamId().toString();

			client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();


			post = RequestBuilder.post()
					.setUri(url)
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//.setEntity(new StringEntity(gson.toJson(tmp2)))
					.build();

			response = client.execute(post);

			result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			Result result2 = gson.fromJson(result.toString(), Result.class);
			assertNotNull(result2);
			assertTrue(result2.success);


			//get the same object
			/// get broadcast 
			url = ROOT_SERVICE_URL + "/broadcast/get";

			client = HttpClients.custom()
					.setRedirectStrategy(new LaxRedirectStrategy())
					.build();
			//Gson gson = new Gson();
			//Broadcast broadcast = null; //new Broadcast();
			//broadcast.name = "name";



			get = RequestBuilder.get()
					.setUri(url + "?id="+tmp.getStreamId())
					.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					//	.setEntity(new StringEntity(gson.toJson(broadcast)))
					.build();

			response = client.execute(get);

			result = readResponse(response);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Exception(result.toString());
			}
			System.out.println("result string: " + result.toString());
			tmp2 = gson.fromJson(result.toString(), Broadcast.class);
			assertNotNull(tmp2);
			assertEquals(tmp2.getStreamId(), null);
			assertEquals(tmp2.getName(), null);

		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}




	protected StringBuffer readResponse(HttpResponse response) throws IOException {
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result;
	}

}

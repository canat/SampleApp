package io.antmedia.serverapp.pscp.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.antmedia.datastore.db.types.Broadcast;
import io.antmedia.serverapp.pscp.Application;
import io.antmedia.serverapp.pscp.BroadcastRestService;
import io.antmedia.serverapp.pscp.BroadcastRestService.Result;


public class AppFunctionalTest {


	private BroadcastRestService restService = null;
	public static Process process;

	@Before
	public void before() {
		restService = new BroadcastRestService();
	}

	@After
	public void after() {
		restService = null;
	}
	
	@Test
	public void testRegularExp() {
		String regularExp = "^.*_{1}[0-9]{3}p{1}\\.mp4{1}$";
		
		String name = "ksdfjs39483948348ksdfksf_240p.mp4";
		assertTrue(name.matches(regularExp));
			
		
	}
	
	@Test
	public void testCreateBroadcast() {
		RestServiceFunctionalTest restService = new RestServiceFunctionalTest();


		Broadcast broadcast = restService.createBroadcast("TOBB Demo");
		
		
		System.out.println("broadcast id:" + broadcast.getStreamId());
		
		
	}

	
	//Before running test all endpoints should be authenticated
	@Test
	public void testBroadcastStream() {
		try {
			//call web service to create stream

			RestServiceFunctionalTest restService = new RestServiceFunctionalTest();


			Broadcast broadcast = restService.createBroadcast("name");
			
			broadcast = restService.getBroadcast(broadcast.getStreamId());
			assertEquals(broadcast.getName(), "name");
			
			Result result = restService.addSocialEndpoint(broadcast.getStreamId(), "facebook");
			assertTrue(result.success);
			
			result = restService.addSocialEndpoint(broadcast.getStreamId(), "youtube");
			assertTrue(result.success);
			
			result = restService.addSocialEndpoint(broadcast.getStreamId(), "periscope");
			assertTrue(result.success);
			

			executeProcess("/usr/local/bin/ffmpeg -re -i src/test/resources/test.flv -acodec copy -vcodec copy -f flv rtmp://localhost/PSCP/" + broadcast.getStreamId());

			Thread.sleep(40000);

			//call web service to get stream info and check status
			broadcast = restService.getBroadcast(broadcast.getStreamId().toString());
			assertNotNull(broadcast);
			assertEquals(broadcast.getStatus(), Application.BROADCAST_STATUS_BROADCASTING);

			process.destroy();


			Thread.sleep(10000);

			//call web service to get stream info and check status
			broadcast = restService.getBroadcast(broadcast.getStreamId().toString());
			assertNotNull(broadcast);
			assertEquals(broadcast.getStatus(), Application.BROADCAST_STATUS_FINISHED);
			
	
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public static void executeProcess(final String command) {
		new Thread() {
			public void run() {
				try {

					AppFunctionalTest.process = Runtime.getRuntime().exec(command);
					InputStream errorStream = process.getErrorStream();
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
	}
	
	public static void destroyProcess() {
		process.destroy();
	}

	public static boolean exists(String URLName, boolean followRedirects){
		try {
			HttpURLConnection.setFollowRedirects(followRedirects);
			// note : you may also need
			//        HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con =
					(HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

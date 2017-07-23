package io.antmedia.serverapp.pscp.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.api.services.youtube.model.LiveStreamStatus;

import io.antmedia.datastore.db.types.Endpoint;
import io.antmedia.datastore.preference.PreferenceStore;
import io.antmedia.enterprise.social.endpoint.FacebookEndpoint;
import io.antmedia.social.endpoint.VideoServiceEndpoint.DeviceAuthParameters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FacebookEndpointTest {

	private static final String TARGET_TEST_PROPERTIES = "target/facebook.properties";
	public String CLIENT_ID = "1898164600457124";
	public String CLIENT_SECRET = "51828acd849db391e1d7f3ae58cffff8";



	@Test
	public void testAccessToken() {

		try {
			PreferenceStore dataStore = new PreferenceStore(TARGET_TEST_PROPERTIES);
			dataStore.setFullPath(TARGET_TEST_PROPERTIES);
			FacebookEndpoint endPoint = new FacebookEndpoint(CLIENT_ID, CLIENT_SECRET, dataStore);
			DeviceAuthParameters device = null;
			endPoint.start();

			device = endPoint.askDeviceAuthParameters();
			assertNotNull(device);

			assertNotNull(device.user_code);
			assertNotNull(device.device_code);
			assertNotNull(device.expires_in);
			assertNotNull(device.interval);
			assertNotNull(device.verification_url);

			System.out.println("user code :" + device.user_code);
			System.out.println("device code :" + device.device_code);
			System.out.println("expires in:" + device.expires_in);
			System.out.println("interval :" + device.interval);
			System.out.println("verification_url :" + device.verification_url);



			do {
				boolean authentiated;
				try {
					authentiated = endPoint.askIfDeviceAuthenticated();

					if (authentiated) 
					{
						break;
					}
					System.out.println("waiting for " + device.interval + " seconds");
					Thread.sleep(device.interval * 1000);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} while (true);

			



		} catch (Exception e) {

			e.printStackTrace();
			fail(e.getMessage());
		} 
	}

	@Test
	public void testCreateBroadcastNoName() {
		PreferenceStore dataStore = new PreferenceStore(TARGET_TEST_PROPERTIES);
		dataStore.setFullPath(TARGET_TEST_PROPERTIES);
		FacebookEndpoint endPoint = new FacebookEndpoint(CLIENT_ID, CLIENT_SECRET, dataStore);
		endPoint.start();

		try {
			Endpoint endpoint = endPoint.createBroadcast(null, null, false, false, 720);
			assertNotNull(endpoint);
			assertNotNull(endpoint.rtmpUrl);
			assertTrue(endpoint.rtmpUrl.length() > 0);

		} catch (Exception e) {

			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	@Test
	public void testCreateBroadcast() {
		PreferenceStore dataStore = new PreferenceStore(TARGET_TEST_PROPERTIES);
		dataStore.setFullPath(TARGET_TEST_PROPERTIES);
		FacebookEndpoint endPoint = new FacebookEndpoint(CLIENT_ID, CLIENT_SECRET, dataStore);
		endPoint.start();
		try {
			String name = "evet name";
			Endpoint endpoint = endPoint.createBroadcast("name24", "description", false, false, 720);

			System.out.println("rtmp url is:" + endpoint.rtmpUrl);

			AppFunctionalTest.executeProcess("/usr/local/bin/ffmpeg -re -i src/test/resources/test.flv -acodec copy -vcodec copy -f flv " + endpoint.rtmpUrl);

			LiveStreamStatus streamStatus = null;


			boolean started = false;

			endPoint.publishBroadcast(endpoint);


			Thread.sleep(40000);


			endPoint.stopBroadcast(endpoint);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 

	}

}

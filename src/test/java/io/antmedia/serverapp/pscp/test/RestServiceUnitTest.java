package io.antmedia.serverapp.pscp.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;

import io.antmedia.datastore.db.IDataStore;
import io.antmedia.datastore.db.MongoStore;
import io.antmedia.datastore.db.types.Broadcast;
import io.antmedia.rest.BroadcastRestService;
import io.antmedia.rest.BroadcastRestService.Result;
import io.antmedia.serverapp.pscp.Application;


@ContextConfiguration(locations = { 
		"/WEB-INF/red5-web.xml" 
})
public class RestServiceUnitTest  {


	private BroadcastRestService restService = null;

	@Before
	public void before() {
		restService = new BroadcastRestService();
	}

	@After
	public void after() {
		restService = null;
	}

	@Test
	public void testAllInOne() {
		Broadcast broadcast = new Broadcast(null, "name");
		IDataStore store = new MongoStore("testdb");
		restService.setDataStore(store);
		Broadcast createBroadcast = restService.createBroadcast(broadcast);

		assertNotNull(createBroadcast);
		assertNotNull(createBroadcast.getStreamId());
		assertNotNull(createBroadcast.getName());
		assertNotNull(createBroadcast.getStatus());

		Broadcast createBroadcast2 = restService.createBroadcast(null);

		assertNotNull(createBroadcast2);
		assertNotNull(createBroadcast2.getStreamId());
		assertNull(createBroadcast2.getName());
		assertNotNull(createBroadcast2.getStatus());

		
		
		Gson gson = new Gson();

		Broadcast broadcastTmp = restService.getBroadcast(createBroadcast.getStreamId());
		assertNotNull(broadcastTmp);
		assertEquals(createBroadcast.getStatus(), broadcastTmp.getStatus());

		//update status
		boolean updateStatus = store.updateStatus(createBroadcast.getStreamId(), Application.BROADCAST_STATUS_BROADCASTING);
		assertTrue(updateStatus);

		//check status
		broadcastTmp = restService.getBroadcast(createBroadcast.getStreamId());
		assertNotNull(broadcastTmp);
		assertEquals(broadcastTmp.getStatus(), Application.BROADCAST_STATUS_BROADCASTING);

		//update status again
		updateStatus = store.updateStatus(createBroadcast.getStreamId(), Application.BROADCAST_STATUS_FINISHED);
		assertTrue(updateStatus);

		//check status
		broadcastTmp = restService.getBroadcast(createBroadcast.getStreamId());
		assertNotNull(broadcastTmp);
		assertEquals(broadcastTmp.getStatus(), Application.BROADCAST_STATUS_FINISHED);


		broadcastTmp = restService.getBroadcast("jdkdkdkdk");
		assertNotNull(broadcastTmp);
		assertNull(broadcastTmp.getStatus());


		broadcastTmp = restService.getBroadcast(createBroadcast.getStreamId());
		assertNotNull(broadcastTmp);
		assertEquals(broadcastTmp.getStatus(), Application.BROADCAST_STATUS_FINISHED);
		assertEquals(broadcastTmp.getStreamId(), createBroadcast.getStreamId());
		assertEquals(broadcastTmp.getName(), createBroadcast.getName());

		Result deleteBroadcast = restService.deleteBroadcast(createBroadcast.getStreamId());
		assertTrue(deleteBroadcast.success);


		deleteBroadcast = restService.deleteBroadcast(createBroadcast.getStreamId());
		assertFalse(deleteBroadcast.success);

		deleteBroadcast = restService.deleteBroadcast(null);
		assertFalse(deleteBroadcast.success);

		try {
			createBroadcast.setStreamId(null);
			fail("it shoudl throw exception");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		deleteBroadcast = restService.deleteBroadcast(null);
		assertFalse(deleteBroadcast.success);



	}

}

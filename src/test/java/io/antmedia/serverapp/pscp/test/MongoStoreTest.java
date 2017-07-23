package io.antmedia.serverapp.pscp.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.antmedia.datastore.db.IDataStore;
import io.antmedia.datastore.db.MongoStore;
import io.antmedia.datastore.db.types.Broadcast;
import io.antmedia.datastore.db.types.Endpoint;
import io.antmedia.serverapp.pscp.Application;


public class MongoStoreTest {
	
	
	@Test
	public void testNullCheck() {
		IDataStore mongoStore = new MongoStore("testdb");
		String save = mongoStore.save(null);
		assertNull(save);
		
		assertNull(mongoStore.get(null));
		
		assertFalse(mongoStore.updateName(null, "name", "description"));
		
		assertFalse(mongoStore.updateDuration(null, 100000));
		
		assertFalse(mongoStore.updateStatus(null, "created"));
		
		assertFalse(mongoStore.addEndpoint(null, null));
	}
	
	@Test
	public void testSimpleOperations() {
		IDataStore mongoStore = new MongoStore("testdb");
		
		Broadcast broadcast = new Broadcast();
		String key = mongoStore.save(broadcast);

		assertNotNull(key);
		assertNotNull(broadcast.getStreamId());
		
		assertEquals(broadcast.getStreamId().toString(), key);
		
		Broadcast broadcast2 = mongoStore.get(key);
		assertEquals(broadcast.getStreamId(), broadcast2.getStreamId());
		assertTrue(broadcast2.isPublish());
		
		String name = "name 1";
		String description = "description 2";
		boolean result = mongoStore.updateName(broadcast.getStreamId().toString(), name, description);
		assertTrue(result);
		
		broadcast2 = mongoStore.get(key);
		
		assertEquals(name, broadcast2.getName());
		assertEquals(description, broadcast2.getDescription());
		
		result = mongoStore.updateDuration(broadcast.getStreamId().toString(), 100000);
		assertTrue(result);
		
		broadcast2 = mongoStore.get(key);
		
		assertEquals(name, broadcast2.getName());
		assertEquals(description, broadcast2.getDescription());
		assertEquals(100000, (long)broadcast2.getDuration());
		
		
		result = mongoStore.updateStatus(broadcast.getStreamId().toString(), Application.BROADCAST_STATUS_CREATED);
		assertTrue(result);
		
		broadcast2 = mongoStore.get(key);
		
		assertEquals(name, broadcast2.getName());
		assertEquals(description, broadcast2.getDescription());
		assertEquals(100000, (long)broadcast2.getDuration());
		assertEquals(Application.BROADCAST_STATUS_CREATED, broadcast2.getStatus());
		
		
		result = mongoStore.updateStatus(broadcast.getStreamId().toString(), Application.BROADCAST_STATUS_FINISHED);
		assertTrue(result);
		
		broadcast2 = mongoStore.get(key);
		
		assertEquals(name, broadcast2.getName());
		assertEquals(description, broadcast2.getDescription());
		assertEquals(100000, (long)broadcast2.getDuration());
		assertEquals(Application.BROADCAST_STATUS_FINISHED, broadcast2.getStatus());
		
		assertEquals(null, broadcast2.getEndPointList());
		
		String rtmpUrl = "rtmp:((ksklasjflakjflaskjflsadfkjsal";
		Endpoint endPoint = new Endpoint("broacdast id", "stream id", broadcast2.getName(), rtmpUrl, "generic");
		
		result = mongoStore.addEndpoint(broadcast2.getStreamId().toString(), endPoint);
		assertTrue(result);
		
		broadcast2 = mongoStore.get(key);
		assertNotNull(broadcast2.getEndPointList());
		assertEquals(broadcast2.getEndPointList().size(), 1);
		assertEquals(broadcast2.getEndPointList().get(0).name, broadcast2.getName());
		assertEquals(broadcast2.getEndPointList().get(0).rtmpUrl, rtmpUrl);
		
		
		rtmpUrl = "rtmp:(sdfsfsf(ksklasjflakjflaskjflsadfkjsal";
		endPoint = new Endpoint("broacdast id 2", "stream id 2", broadcast2.getName(), rtmpUrl, "facebook");
		
		result = mongoStore.addEndpoint(broadcast2.getStreamId().toString(), endPoint);
		assertTrue(result);
		
		broadcast2 = mongoStore.get(key);
		assertNotNull(broadcast2.getEndPointList());
		assertEquals(broadcast2.getEndPointList().size(), 2);
		assertEquals(broadcast2.getEndPointList().get(1).name, broadcast2.getName());
		assertEquals(broadcast2.getEndPointList().get(1).rtmpUrl, rtmpUrl);
		
		
		result = mongoStore.delete(key);
		assertTrue(result);
		
		assertNull(mongoStore.get(key));
		
		
	}

}

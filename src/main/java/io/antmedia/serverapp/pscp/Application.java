package io.antmedia.serverapp.pscp;


import java.io.File;
import java.util.List;

import org.red5.server.adapter.AntMediaApplicationAdapter;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.scheduling.IScheduledJob;
import org.red5.server.api.scheduling.ISchedulingService;
import org.red5.server.api.scope.IScope;
//import org.slf4j.Logger;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IStreamListener;
import org.red5.server.api.stream.IStreamPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import io.antmedia.datastore.db.IDataStore;
import io.antmedia.muxer.IMuxerListener;
import io.antmedia.muxer.MuxAdaptor;
import io.antmedia.social.endpoint.VideoServiceEndpoint;
import io.antmedia.social.endpoint.VideoServiceEndpoint.DeviceAuthParameters;

/**
 * Sample application that uses the client manager.
 * 
 * @author The Red5 Project (red5@osflash.org)
 */
public class Application extends AntMediaApplicationAdapter {

	protected static Logger logger = LoggerFactory.getLogger(Application.class);

	public static final String STORAGE_FORWARD_URL = "https://s3.eu-central-1.amazonaws.com/";

	/** {@inheritDoc} */
	@Override
	public boolean connect(IConnection conn, IScope scope, Object[] params) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void disconnect(IConnection conn, IScope scope) {
		super.disconnect(conn, scope);
	}

	@Override
	public boolean appStart(IScope app) {
		return super.appStart(app);
	}

}



package io.antmedia.serverapp.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.antmedia.storage.AmazonS3StorageClient;
import io.antmedia.storage.StorageClient.FileType;

public class AmazonS3StorageClientTest {
	
	public static final String testAccessKey = "AKIAI67H5KXQOULQE6AQ";
	
	public static final String testSecretKey = "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";

	private AmazonS3StorageClient client;
	
	
	@Before
	public void before() {
		client = 
				new AmazonS3StorageClient();
		client.setAccessKey(testAccessKey);
		client.setSecretKey(testSecretKey);
		client.setRegion("eu-central-1");
		client.setStorageName("antmediatest");
	}
	
	@After
	public void after() {
		client = null;
	}
	
	
	@Test
	public void testAmazonS3SaveStream() {
		
		File f = new File("target/test-classes/test.flv");
		assertTrue(f.exists());
		
		File destFile = new File("target/test-classes/test2.flv");
		try {
			FileUtils.copyFile(f, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(destFile.exists());
		
		client.save(destFile, FileType.TYPE_STREAM);
		
		assertFalse(destFile.exists());
		
		assertTrue(client.fileExist(destFile.getName(), FileType.TYPE_STREAM));
		
		
		client.delete(destFile.getName(), FileType.TYPE_STREAM);
		
		assertFalse(client.fileExist(destFile.getName(), FileType.TYPE_STREAM));
		
	}
	
	@Test
	public void testAmazonS3SavePreview() {
		
		File f = new File("target/test-classes/test.png");
		assertTrue(f.exists());
		
		File destFile = new File("target/test-classes/test2.png");
		try {
			FileUtils.copyFile(f, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertTrue(destFile.exists());
		
		client.save(destFile, FileType.TYPE_PREVIEW);
		
		assertFalse(destFile.exists());
		
		assertTrue(client.fileExist(destFile.getName(), FileType.TYPE_PREVIEW));
		
		
		client.delete(destFile.getName(), FileType.TYPE_PREVIEW);
		
		assertFalse(client.fileExist(destFile.getName(), FileType.TYPE_PREVIEW));
		
	}

}

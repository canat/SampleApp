package io.antmedia.serverapp.pscp.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.prefs.Preferences;

import org.junit.Test;

import io.antmedia.datastore.preference.PreferenceStore;

public class PreferencesTest {


	@Test
	public void testStorePreferences() {

		File f  = new File("data.properties");
		try {
			Files.delete(f.toPath());


			PreferenceStore dataStore = new PreferenceStore("data.properties");
			assertNull(dataStore.get("data1"));

			dataStore.put("data1", "value1");
			dataStore.put("data2", "value2");
			dataStore.put("data3", "value3");

			assertEquals(dataStore.get("data1"), "value1");

			dataStore.save();

			assertEquals(dataStore.get("data1"), "value1");

			dataStore = new PreferenceStore("data.properties");
			assertNotNull(dataStore.get("data1"));
			assertEquals(dataStore.get("data1"), "value1");
			dataStore.put("data4", "value4");
			dataStore.save();

			assertEquals(dataStore.get("data3"), "value3");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}

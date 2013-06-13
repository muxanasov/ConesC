package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileManager;

public class FileManagerTest {
	
	String TEST_FILE = "test";
	String TEST_STRING = "test";

	@Before
	public void setUp() throws Exception {
		File f = new File("test");
		assertTrue(f.createNewFile());
	}

	@After
	public void tearDown() throws Exception {
		File f = new File("test");
		assertTrue(f.delete());
	}

	@Test
	public void testFreadFwrite() {
		FileManager.fwrite(TEST_FILE, TEST_STRING);
		String data = FileManager.fread(TEST_FILE);
		
		assertEquals(TEST_STRING+"\n", data);
	}

}

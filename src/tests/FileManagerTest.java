package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileManager;
import core.Print;

public class FileManagerTest {
	
	String TEST_FILE = "test";
	String TEST_STRING = "test";
	
	String TEST_CNC_3 =
		"configuration DemoAppC { }\n" +
		"implementation {\n" +
		"}";
	
	String TEST_MAKEFILE = 
			"COMPONENT = DemoAppC\n" +
			"PFLAGS += -I ./interfaces -I ./\n" +
			"include $(MAKERULES)\n";

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
		
		assertEquals(TEST_STRING, data);
	}
}

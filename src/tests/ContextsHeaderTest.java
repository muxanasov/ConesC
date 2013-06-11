package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.ContextsHeader;

public class ContextsHeaderTest {
	
	String TEST_CONTEXT_1 = "High";
	String TEST_CONTEXT_2 = "Normal";
	String TEST_CONTEXT_3 = "Low";

	@Test
	public void buildHeaderTest() {
		String test_header = 
			"#ifndef CONTEXT_H\n" +
			"#define CONTEXT_H\n" +
			"typedef enum {\n" +
			"  HIGH,\n" +
			"  NORMAL,\n" +
			"  LOW\n" +
			"} context_t;\n" +
			"#endif";
		
		ArrayList<String> contexts = new ArrayList<>();
		contexts.add(TEST_CONTEXT_1);
		contexts.add(TEST_CONTEXT_2);
		
		ContextsHeader.reset();
		ContextsHeader.addAll(contexts);
		ContextsHeader.add(TEST_CONTEXT_3);
		
		assertEquals(test_header, ContextsHeader.buildHeader());
	}

}

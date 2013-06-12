// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import parsers.configuration.ConfigurationFile;
import parsers.configuration.ParseException;
import parsers.configuration.Parser;

public class ConfigurationParserTest {
	
	String TEST_CONFIGURATION_NAME = "Temperature";
	String LAYERED = "layered";
	String TEST_RETURN_TYPE = "void";
	String TEST_FUNCTION_NAME = "toggle_leds";
	String TEST_COMPONENT = "LedsC";
	String TEST_CONTEXT_1 = "High";
	String TEST_DEFAULT_CONTEXT = "Normal";
	String TEST_CONTEXT_2 = "Low";
	String TEST_ERROR_CONTEXT = "Error";
	String TEST_INTERFACE_1 = "High.Leds";
	String TEST_INTERFACE_2 = "Normal.Leds";
	String TEST_INTERFACE_3 = "Low.Leds";
	String TEST_INTERFACE_4 = "Error.Leds";
	
	String TEST_INTERFACE_5 = "FancyInterface";
	String TEST_INTERFACE_6 = "Interface.FancyInterface";
	
	String TEST_GROUP_1 = "Temperature";
	String TEST_GROUP_2 = "Location";
	String TEST_GROUP_3 = "YetAnotherGroup";
	
	@Test
	public void testParse() {
		String contextGroupFile = 
		"context configuration " + TEST_CONFIGURATION_NAME + " {\n"+
		"  " + LAYERED + " " + TEST_RETURN_TYPE + " " + TEST_FUNCTION_NAME + "();\n"+
		"  provides interface " + TEST_INTERFACE_5 + ";\n" +
		"}\n"+
		"implementation {\n"+
		"  context groups\n" +
		"    " + TEST_GROUP_1 + ",\n" +
		"    " + TEST_GROUP_2 + ";\n" +
		"  context groups " + TEST_GROUP_3 + ";\n" +
		"  components \n"+
		"    " + TEST_COMPONENT + ";\n"+
		"  contexts \n"+
		"    " + TEST_CONTEXT_1 + ",\n"+
		"    " + TEST_DEFAULT_CONTEXT + " is default,\n"+
		"    " + TEST_ERROR_CONTEXT + " is error,\n" +
		"    " + TEST_CONTEXT_2 + ";\n"+
		"  \n"+
		"  " + TEST_COMPONENT + " <- " + TEST_INTERFACE_1 + ";\n"+
		"  " + TEST_INTERFACE_2 + " -> " + TEST_COMPONENT + ";\n"+
		"  " + TEST_INTERFACE_3 + " -> " + TEST_COMPONENT + ";\n"+
		"  " + TEST_INTERFACE_4 + " -> " + TEST_COMPONENT + ";\n"+
		"  " + TEST_INTERFACE_5 + " = " + TEST_INTERFACE_6 + ";\n" +
		"}";
		
		Parser parser = new Parser(new StringReader(contextGroupFile));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConfigurationFile file = parser.getParsedFile();
		
		assertEquals(file.functions.size(), 1);
		assertEquals(file.functions.get(0).returnType, TEST_RETURN_TYPE);
		assertEquals(file.functions.get(0).name, TEST_FUNCTION_NAME);
		assertEquals(file.functions.get(0).variables.size(), 0);
		
		assertEquals(file.components.size(), 1);
		assertEquals(file.components.get(0), TEST_COMPONENT);
		
		assertEquals(file.contexts.size(), 4);
		assertEquals(file.contexts.get(0), TEST_CONTEXT_1);
		assertEquals(file.contexts.get(1), TEST_DEFAULT_CONTEXT);
		assertEquals(file.contexts.get(2), TEST_ERROR_CONTEXT);
		assertEquals(file.contexts.get(3), TEST_CONTEXT_2);
		
		assertEquals(3, file.contextGroups.size());
		assertEquals(TEST_GROUP_1, file.contextGroups.get(0));
		assertEquals(TEST_GROUP_2, file.contextGroups.get(1));
		assertEquals(TEST_GROUP_3, file.contextGroups.get(2));
		
		assertEquals(file.defaultContext, TEST_DEFAULT_CONTEXT);
		assertEquals(file.errorContext, TEST_ERROR_CONTEXT);
		
		assertEquals(file.wires.size(), 4);
		
		assertEquals(file.wires.get(TEST_INTERFACE_1), TEST_COMPONENT);
		assertEquals(file.wires.get(TEST_INTERFACE_2), TEST_COMPONENT);
		assertEquals(file.wires.get(TEST_INTERFACE_3), TEST_COMPONENT);
		assertEquals(file.wires.get(TEST_INTERFACE_4), TEST_COMPONENT);
		
		assertEquals(file.interfaces.get("provides").size(), 1);
		assertEquals(file.interfaces.get("provides").get(0), TEST_INTERFACE_5);
		
		assertEquals(file.interfaces.get("uses").size(), 0);
		
		assertEquals(file.equality.size(), 1);
		assertTrue(file.equality.containsKey(TEST_INTERFACE_5));
		assertTrue(file.equality.containsValue(TEST_INTERFACE_6));
		assertEquals(file.equality.get(TEST_INTERFACE_5), TEST_INTERFACE_6);
		
	}

}

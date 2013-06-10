// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.Context;
import core.Function;

public class ContextTest {
	
	String TEST_GROUP = "Temperature";

	@Test
	public void testBuild() {
		String context_cnc =
		"context High {\n" +
		"  transition Normal;\n" +
		"  uses interface Leds;\n" +
		"}\n" +
		"implementation {\n" +
		"  event void activated() {\n" +
		"    dbg(\"Debug\", \"HighTemperatureContext si activated.\n\");\n" +
		"  }\n" +
		"  event void deactivated() {\n" +
		"    dbg(\"Debug\", \"HighTemperatureContext si deactivated.\n\");\n" +
		"  }\n" +
		"  command bool check() {\n" +
		"    return (25 > 20);\n" +
		"  }\n" +
		"  layered void toggle_leds() {\n" +
		"    call Leds.set(1);\n" +
		"  }\n" +
		"}";
		String testContext_nc =
		"module HighTemperatureContext {\n" +
		"  uses interface Leds;\n" +
		"  provides interface ContextCommands as Command;\n" +
		"  provides interface TemperatureLayer as Layered;\n" +
		"  uses interface ContextEvents as Event;\n" +
		"}\n" +
		"implementation {\n" +
		"  command void Layered.toggle_leds() {\n" +
		"    call Leds.set(1);\n" +
		"  }\n" +
		"  event void Event.activated() {\n" +
		"    dbg(\"Debug\", \"HighTemperatureContext si activated.\n\");\n" +
		"  }\n" +
		"  event void Event.deactivated() {\n" +
		"    dbg(\"Debug\", \"HighTemperatureContext si deactivated.\n\");\n" +
		"  }\n" +
		"  command bool Command.check() {\n" +
		"    return (25 > 20);\n" +
		"  }\n" +
		"  command void Command.activate() {\n" +
		"    signal Event.activated();\n" +
		"  }\n" +
		"  command void Command.deactivate() {\n" +
		"    signal Event.deactivated();\n" +
		"  }\n" +
		"}";
		
		ArrayList<Function> layeredFunctions = new ArrayList<Function>();
		
		Function testFunction = new Function();
		testFunction.name = "toggle_leds";
		testFunction.returnType = "void";
		
		layeredFunctions.add(testFunction);
		
		Context context = new Context(TEST_GROUP, context_cnc, layeredFunctions);
		String builtContext_nc = "";
		try {
			builtContext_nc = context.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(builtContext_nc, testContext_nc);
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testException() throws Exception {
		String context_cnc = 
		"context Test {\n" +
		"}\n" +
		"implementation {\n" +
		"}";
		
		ArrayList<Function> layeredFunctions = new ArrayList<Function>();
		
		Function testFunction = new Function();
		testFunction.name = "toggle_leds";
		testFunction.returnType = "void";
		
		layeredFunctions.add(testFunction);
		
		Context context = new Context(TEST_GROUP, context_cnc, layeredFunctions);
		
		exception.expect(Exception.class);
	    exception.expectMessage("In context Test:\nvoid toggle_leds() is not implemented!\n");
		String builtContext_nc = context.build();
	}

}

// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import core.Component;
import core.Context;
import core.FileManager;
import core.Function;
import core.Print;

import java.io.ByteArrayOutputStream;

public class ContextTest {
	
	private String TEST_CNC =
			"context configuration Temperature {\n" +
			"  layered void toggle_leds();\n" +
			"  layered int32 test_function(int a, bool& b, string* c);\n" +
			"}\n" +
			"implementation {\n" +
			"  context groups\n" +
			"    Location;\n" +
			"  contexts\n" +
			"   High,\n" +
			"   Normal is default,\n" +
			"   Low,\n" +
			"   Other is error;\n" +
			"  components\n" +
			"   LedsC;\n" +
			"  High.Leds -> LedsC;\n" +
			"  Normal.Leds -> LedsC;\n" +
			"  Low.Leds -> LedsC; \n" +
			"  Other.Leds -> LedsC;\n" +
			"  Other.Some -> Location;\n" +
			"}\n";

		
	String TEST_CONTEXT_1 =
			"#include \"include.h\"\n" +
			"#include <stdio.hpp>\n" +
			"context High {\n" +
			"  transition Normal;\n" +
			"  uses interface Leds;\n" +
			"  uses context group Location;\n" +
			"  uses context group Group;\n" +
			"}\n" +
			"implementation {\n" +
			"  event void activated() {\n" +
			"    dbg(\"Debug\", \"HighTemperatureContext si activated.\n\");\n" +
			"    activate Location.Indoor;\n" +
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
			"  layered int32 test_function(int a, bool& b, string* c) {\n" +
			"    call Leds.set(1);\n" +
			"  }\n" +
			"  event void Group.contextChanged(context_t con) {\n" +
			"    if (some source = TRUE)\n" +
			"      code of the \n" +
			"    else\n" +
			"      function(int 1, int b)\n" +
			"  }\n" +
			"}";
	String TEST_CONTEXT_2 =
			"context Normal {\n" +
			"}implementation{}";
	String TEST_CONTEXT_3 =
			"context Low {\n" +
			"}implementation{}";
	String TEST_CONTEXT_4 =
			"context Other {\n" +
			"}implementation{}";
		
	String TEST_GROUP =
			"context configuration Location {}\n" +
			"implementation{}";
		
	String TEST_CNC_3 =
				"configuration DemoAppC { }\n" +
				"implementation {\n" +
				"  context groups\n" +
				"    Temperature;\n" +
		 		"  components\n" +
				"    MainC,\n" +
				"    DemoC,\n" +
				"    new TimerMilliC();\n" +
				"  DemoC.Temperature -> Temperature;\n" +
				"  DemoC.Boot -> MainC;\n" +
				"  DemoC.Timer -> TimerMilliC;\n" +
				"}";
		
	Component _context = null;
		
	@Before
	public void setUp() throws Exception {
		Print.init(Print.LogLevel.LOG_DEBUG);
		FileManager.fwrite("Temperature.cnc", TEST_CNC);
		FileManager.fwrite("Location.cnc", TEST_GROUP);
		FileManager.fwrite("High.cnc", TEST_CONTEXT_1);
		FileManager.fwrite("Normal.cnc", TEST_CONTEXT_2);
		FileManager.fwrite("Low.cnc", TEST_CONTEXT_3);
		FileManager.fwrite("Other.cnc", TEST_CONTEXT_3);
		FileManager.fwrite("DemoAppC.cnc", TEST_CNC_3);
		String makeFile = 
					"COMPONENT = DemoAppC\n" +
					"PFLAGS += -I ./interfaces -I ./\n" +
					"include $(MAKERULES)\n";
		FileManager.fwrite("Makefile", makeFile);
		
		Component temperature = null;
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		mainConf.parse();
			
		for (Component component : mainConf.getComponents())
			if (component.getName().equals("Temperature")) {
				temperature = component;
				break;
			}
		assertNotNull(temperature);
		temperature.parse();
		
		for (Component component : temperature.getComponents())
			if (component.getName().equals("High")) {
				_context = component;
				break;
			}
		assertNotNull(_context);
		_context.build();
	}

	@After
	public void tearDown() throws Exception {
		File app = new File ("DemoAppC.cnc");
		app.delete();
		File location = new File ("Location.cnc");
		location.delete();
		File high = new File ("High.cnc");
		high.delete();
		File normal = new File ("Normal.cnc");
		normal.delete();
		File low = new File ("Low.cnc");
		low.delete();
		File other = new File ("Other.cnc");
		other.delete();
		File temperature = new File ("Temperature.cnc");
		temperature.delete();
		File makefile = new File ("Makefile");
		makefile.delete();
			
		_context = null;
		Print.init(Print.LogLevel.LOG_NOTHING);
	}
	
	@Test
	public void testBuild() {
		String testContext_nc =
		"#include \"Contexts.h\"\n" +
		"#include \"include.h\"\n" +
		"#include <stdio.hpp>\n" +
		"module HighTemperatureContext {\n" +
		"  provides interface ContextCommands as Command;\n" +
		"  provides interface TemperatureLayer as Layered;\n" +
		"  uses interface Leds;\n" +
		"  uses interface ContextEvents as Event;\n" +
		"  uses interface ContextGroup as LocationGroup;\n" +
		"  uses interface LocationLayer;\n" +
		"  uses interface ContextGroup as GroupGroup;\n" +
		"  uses interface GroupLayer;\n" +
		"}\n" +
		"implementation {\n" +
		"  command void Layered.toggle_leds() {\n" +
		"    call Leds.set(1);\n" +
		"  }\n" +
		"  command int32 Layered.test_function(int a, bool& b, string* c) {\n" +
		"    call Leds.set(1);\n" +
		"  }\n" +
		"  event void Event.activated() {\n" +
		"    dbg(\"Debug\", \"HighTemperatureContext si activated.\n\");\n" +
		"    call LocationGroup.activate(INDOORLOCATION);\n" +
		"  }\n" +
		"  event void Event.deactivated() {\n" +
		"    dbg(\"Debug\", \"HighTemperatureContext si deactivated.\n\");\n" +
		"  }\n" +
		"  event void GroupGroup.contextChanged(context_t con) {\n" +
		"    if (some source = TRUE)\n" +
		"      code of the \n" +
		"    else\n" +
		"      function(int 1, int b)\n" +
		"  }\n" +
		"  command bool Command.check() {\n" +
		"    return (25 > 20);\n" +
		"  }\n" +
		"  event void LocationGroup.contextChanged(context_t con){\n" +
		"  }\n" +
		"  command void Command.activate() {\n" +
		"    signal Event.activated();\n" +
		"  }\n" +
		"  command void Command.deactivate() {\n" +
		"    signal Event.deactivated();\n" +
		"  }\n" +
		"}\n";
		
		assertEquals(testContext_nc, _context.getGeneratedFiles().get("HighTemperatureContext.nc"));
	}
	
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Test
	public void testException() throws Exception {
		String simple_context_cnc =
			"context High {}\n" +
			"implementation {}";
		
		FileManager.fwrite("High.cnc", simple_context_cnc);
		
		Component temperature = null;
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		mainConf.parse();
			
		for (Component component : mainConf.getComponents())
			if (component.getName().equals("Temperature")) {
				temperature = component;
				break;
			}
		assertNotNull(temperature);
		temperature.parse();
		
		for (Component component : temperature.getComponents())
			if (component.getName().equals("High")) {
				_context = component;
				break;
			}
		assertNotNull(_context);
		
		System.setErr(new PrintStream(errContent));
		
		_context.build();
		assertTrue(errContent.toString().contains("High.cnc: \n" +
				"void toggle_leds() is not implemented!\n" +
				"int32 test_function(int a, bool& b, string* c) is not implemented!"));
		
		System.setErr(null);
	}

}

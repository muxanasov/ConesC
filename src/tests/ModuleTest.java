// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Component;
import core.FileManager;
import core.Module;

public class ModuleTest {
	
	String TEST_CNC =
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
	String TEST_MODULE_CNC =
			"#include \"blah-blah-blah.h\"\n" +
			"#include <Supr_duper_lib.h>\n" +
			"module DemoC {\n" +
			"  uses context group Temperature2;\n" +
			"  uses interface Boot;\n" +
			"  uses interface Leds;\n" +
			"  uses interface Timer<TMilli>;\n" +
			"  uses interface Read<uint16_t>;\n" +
			"}\n" +
			"implementation {\n" +
			"  uint16_t T_min = 27;\n" +
			"  uint16_t T_max = 32;\n" +
			"  event void Boot.booted() {\n" +
			"    dbg(\"Debug\", \"App is booted.\n\");\n" +
			"    call Timer.startPeriodic(100);\n" +
			"    activate Temperature2.High;\n" +
			"  }\n" +
			"  event void Temperature2.contextChanged(context_t con){\n" +
			"    dbg(\"Debug\", \"Temperature class context changed %d.\n\", con);\n" +
			"  }\n" +
			"  event void Timer.fired() {\n" +
			"    call Read.read();\n" +
			"  }\n" +
			"  event void Read.readDone(error_t result, uint16_t data) {\n" +
			"    uint16_t temp = -39 + 0.01*data;\n" +
			"    if (result != SUCCESS) return;\n" +
			"    if (temp > T_min && temp < T_max)\n" +
			"      activate Temperature2.Normal;\n" +
			"    else if (temp >= T_max)\n" +
			"      activate Temperature2.High;\n" +
			"    else if (temp <= T_min)\n" +
			"      activate Temperature2.Low;\n" +
			"    else activate Temperature2.Error;\n" +
			"    call Temperature2.toggle_leds();\n" +
			"  }\n" +
			"}";
	private Component _module = null;
		
	@Before
	public void setUp() throws Exception {
		FileManager.fwrite("DemoAppC.cnc", TEST_CNC);
		FileManager.fwrite("DemoC.cnc", TEST_MODULE_CNC);
		String makeFile = 
					"COMPONENT = DemoAppC\n" +
					"PFLAGS += -I ./interfaces -I ./\n" +
					"include $(MAKERULES)\n";
		FileManager.fwrite("Makefile", makeFile);
		
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		mainConf.parse();
		
		for (Component component : mainConf.getComponents().values())
			if (component.getName().equals("DemoC")) {
				_module = component;
				break;
			}
		assertNotNull(_module );
		_module.build();
	}

	@After
	public void tearDown() throws Exception {
		File app = new File ("DemoAppC.cnc");
		app.delete();
		File temperature = new File ("DemoC.cnc");
		temperature.delete();
		File makefile = new File ("Makefile");
		makefile.delete();
		
		_module = null;
	}

	@Test
	public void testBuild() {
		String test_nc =
		"#include \"Contexts.h\"\n" +
		"#include \"blah-blah-blah.h\"\n" +
		"#include <Supr_duper_lib.h>\n" +
		"module DemoC {\n" +
		"  uses interface Boot;\n" +
		"  uses interface Leds;\n" +
		"  uses interface Timer<TMilli>;\n" +
		"  uses interface Read<uint16_t>;\n" +
		"  uses interface ContextGroup as Temperature2Group;\n" +
		"}\n" +
		"implementation {\n" +
		"  uint16_t T_min = 27;\n" +
		"  uint16_t T_max = 32;\n" +
		"  event void Boot.booted() {\n" +
		"    dbg(\"Debug\", \"App is booted.\\n\");\n" +
		"    call Timer.startPeriodic(100);\n" +
		"    call Temperature2Group.activate(HIGHTEMPERATURE2);\n" +
		"  }\n" +
		"  event void Temperature2Group.contextChanged(context_t con){\n" +
		"    dbg(\"Debug\", \"Temperature class context changed %d.\\n\", con);\n" +
		"  }\n" +
		"  event void Timer.fired() {\n" +
		"    call Read.read();\n" +
		"  }\n" +
		"  event void Read.readDone(error_t result, uint16_t data) {\n" +
		"    uint16_t temp = -39 + 0.01*data;\n" +
		"    if (result != SUCCESS) return;\n" +
		"    if (temp > T_min && temp < T_max)\n" +
		"      call Temperature2Group.activate(NORMALTEMPERATURE2);\n" +
		"    else if (temp >= T_max)\n" +
		"      call Temperature2Group.activate(HIGHTEMPERATURE2);\n" +
		"    else if (temp <= T_min)\n" +
		"      call Temperature2Group.activate(LOWTEMPERATURE2);\n" +
		"    else call Temperature2Group.activate(ERRORTEMPERATURE2);\n" +
		"    call Temperature2Layer.toggle_leds();\n" +
		"  }\n" +
		"}\n";
		
		assertEquals(test_nc, _module.getGeneratedFiles().get("DemoC.nc"));
	}

}

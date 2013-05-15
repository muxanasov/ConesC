// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import core.Module;

public class ModuleTest {

	@Test
	public void testBuild() {
		String test_cnc =
		"#include \"blah-blah-blah.h\"\n" +
		"#include <Supr_duper_lib>\n" +
		"module ConesCDemoC {\n" +
		"  uses context group Temperature;\n" +
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
		"    activate Temperature.High;\n" +
		"  }\n" +
		"  event void Temperature.contextChanged(context_t con){\n" +
		"    dbg(\"Debug\", \"Temperature class context changed %d.\n\", con);\n" +
		"  }\n" +
		"  event void Timer.fired() {\n" +
		"    call Read.read();\n" +
		"  }\n" +
		"  event void Read.readDone(error_t result, uint16_t data) {\n" +
		"    uint16_t temp = -39 + 0.01*data;\n" +
		"    if (result != SUCCESS) return;\n" +
		"    if (temp > T_min && temp < T_max)\n" +
		"      activate Temperature.Normal;\n" +
		"    else if (temp >= T_max)\n" +
		"      activate Temperature.High;\n" +
		"    else if (temp <= T_min)\n" +
		"      activate Temperature.Low;\n" +
		"    else activate Temperature.Error;\n" +
		"    call Temperature.toggle_leds();\n" +
		"  }\n" +
		"}";
		
		String test_nc =
		"#include \"Contexts.h\"\n" +
		"#include \"blah-blah-blah.h\"\n" +
		"#include <Supr_duper_lib>\n" +
		"module ConesCDemoC {\n" +
		"  uses interface Boot;\n" +
		"  uses interface Leds;\n" +
		"  uses interface Timer<TMilli>;\n" +
		"  uses interface Read<uint16_t>;\n" +
		"  uses interface ContextGroup as TemperatureGroup;\n" +
		"  uses interface LayeredInterface as TemperatureLayer;\n" +
		"}\n" +
		"implementation {\n" +
		"  uint16_t T_min = 27;\n" +
		"  uint16_t T_max = 32;\n" +
		"  event void Boot.booted() {\n" +
		"    dbg(\"Debug\", \"App is booted.\n\");\n" +
		"    call Timer.startPeriodic(100);\n" +
		"    call TemperatureGroup.activate(HIGHTEMPERATURE);\n" +
		"  }\n" +
		"  event void TemperatureGroup.contextChanged(context_t con){\n" +
		"    dbg(\"Debug\", \"Temperature class context changed %d.\n\", con);\n" +
		"  }\n" +
		"  event void Timer.fired() {\n" +
		"    call Read.read();\n" +
		"  }\n" +
		"  event void Read.readDone(error_t result, uint16_t data) {\n" +
		"    uint16_t temp = -39 + 0.01*data;\n" +
		"    if (result != SUCCESS) return;\n" +
		"    if (temp > T_min && temp < T_max)\n" +
		"      call TemperatureGroup.activate(NORMALTEMPERATURE);\n" +
		"    else if (temp >= T_max)\n" +
		"      call TemperatureGroup.activate(HIGHTEMPERATURE);\n" +
		"    else if (temp <= T_min)\n" +
		"      call TemperatureGroup.activate(LOWTEMPERATURE);\n" +
		"    else call TemperatureGroup.activate(ERRORTEMPERATURE);\n" +
		"    call TemperatureLayer.toggle_leds();\n" +
		"  }\n" +
		"}\n";
		
		Module module = new Module(test_cnc);
		
		String builtModule = module.build();
		
		assertEquals(builtModule, test_nc);
	}

}

package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemTest {
	
	String TEST_DEMOAPPC =
		"configuration DemoAppC { }\n" +
		"implementation {\n" +
	 	"  components\n" +
	 	"    Temperature,\n" +
		"    MainC,\n" +
		"    DemoC,\n" +
		"    new TimerMilliC();\n" +
		"  DemoC.Temperature -> Temperature;\n" +
		"  DemoC.Boot -> MainC;\n" +
		"  DemoC.Timer -> TimerMilliC;\n" +
		"}";
	
	String TEST_DEMOC =
		"module DemoC {\n" +
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
		"    else activate Temperature.Other;\n" +
		"    call Temperature.toggle_leds();\n" +
		"  }\n" +
		"}";
	
	String TEST_TEMPERATURE =
		"context configuration Temperature {\n" +
		"  layered void toggle_leds();\n" +
		"}\n" +
		"implementation {\n" +
		"  components\n" +
		"   High,\n" +
		"   Normal is default,\n" +
		"   Low,\n" +
		"   Other is error,\n" +
		"   LedsC;\n" +
		"  High.Leds -> LedsC;\n" +
		"  Normal.Leds -> LedsC;\n" +
		"  Low.Leds -> LedsC; \n" +
		"  Other.Leds -> LedsC;\n" +
		"}\n";
	
	String TEST_HIGH =
		"context High {\n" +
		"  transitions Normal;\n" +
		"  uses interface Leds;\n" +
		"}\n" +
		"implementation {\n" +
		"  layered void toggle_leds() {\n" +
		"    call Leds.set(1);\n" +
		"  }\n" +
		"}";
	
	String TEST_NORMAL =
		"context Normal {\n" +
		"  uses interface Leds;\n" +
		"}\n" +
		"implementation {\n" +
		"  layered void toggle_leds() {\n" +
		"    call Leds.set(2);\n" +
		"  }\n" +
		"}";
	
	String TEST_LOW =
		"context Low {\n" +
		"  transitions Normal;\n" +
		"  uses interface Leds;\n" +
		"}\n" +
		"implementation {\n" +
		"  layered void toggle_leds() {\n" +
		"    call Leds.set(4);\n" +
		"  }\n" +
		"}";
	
	String TEST_Other =
		"context Other {\n" +
		"  uses interface Leds;\n" +
		"}\n" +
		"implementation {\n" +
		"  layered void toggle_leds() {\n" +
		"    call Leds.set(7);\n" +
		"  }\n" +
		"}";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

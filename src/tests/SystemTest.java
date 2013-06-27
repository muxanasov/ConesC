package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Component;
import core.ContextsHeader;
import core.FileManager;
import core.Translator;

public class SystemTest {
	
	String TEST_DEMOAPPC =
		"configuration DemoAppC { }\n" +
		"implementation {\n" +
		"  components new SensirionSht11C() as Sensor;\n" +
	 	"  components\n" +
	 	"    Temperature,\n" +
		"    MainC,\n" +
		"    DemoC,\n" +
		"    new TimerMilliC();\n" +
		"  DemoC.Temperature -> Temperature;\n" +
		"  DemoC.Boot -> MainC;\n" +
		"  DemoC.Timer -> TimerMilliC;\n" +
		"  DemoC.Read -> Sensor.Temperature;\n" +
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
	
	String TEST_OTHER =
		"context Other {\n" +
		"  uses interface Leds;\n" +
		"}\n" +
		"implementation {\n" +
		"  layered void toggle_leds() {\n" +
		"    call Leds.set(7);\n" +
		"  }\n" +
		"}";
	
	String TEST_MAKEFILE =
		"COMPONENT = DemoAppC\n" +
		"PFLAGS += -I ./interfaces -I ./\n" +
		"include $(MAKERULES)\n";

	@Before
	public void setUp() throws Exception {
		FileManager.fwrite("Temperature.cnc", TEST_TEMPERATURE);
		FileManager.fwrite("Normal.cnc", TEST_NORMAL);
		FileManager.fwrite("High.cnc", TEST_HIGH);
		FileManager.fwrite("Low.cnc", TEST_LOW);
		FileManager.fwrite("Other.cnc", TEST_OTHER);
		FileManager.fwrite("DemoAppC.cnc", TEST_DEMOAPPC);
		FileManager.fwrite("DemoC.cnc", TEST_DEMOC);
		FileManager.fwrite("Makefile", TEST_MAKEFILE);
		
		ContextsHeader.reset();
	}

	@After
	public void tearDown() throws Exception {
		File temperature = new File("Temperature.cnc");
		temperature.delete();
		File high = new File("High.cnc");
		high.delete();
		File normal = new File("Normal.cnc");
		normal.delete();
		File low = new File("Low.cnc");
		low.delete();
		File other = new File("Other.cnc");
		other.delete();
		File demoappc = new File("DemoAppC.cnc");
		demoappc.delete();
		File democ = new File("DemoC.cnc");
		democ.delete();
		File makefile = new File("Makefile");
		makefile.delete();
	}

	@Test
	public void test() {
		//new Translator(new String[0]).make();
	}

}

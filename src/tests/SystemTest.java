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
	
	String TEST_TEMPERATURE =
		"context configuration Temperature {\n" +
		"  layered void toggle_leds();\n" +
		"  layered int32 test_function(int a, bool& b, string* c);\n" +
		"}\n" +
		"implementation {\n" +
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
		"}\n";;

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

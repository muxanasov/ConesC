package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import core.Configuration;

public class ConfigurationTest {
	
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

	@Test
	public void test() {
		String test_nc =
			"configuration DemoAppC {\n" +
			"}\n" +
			"implementation {\n" +
			"  components\n" +
			"    TemperatureConfiguration,\n" +
			"    MainC,\n" +
			"    DemoC,\n" +
			"    new TimerMilliC();\n" +
			"  DemoC.TemperatureGroup -> TemperatureConfiguration;\n" +
			"  DemoC.TemperatureLayer -> TemperatureConfiguration;\n" +
			"  DemoC.Boot -> MainC;\n" +
			"  DemoC.Timer -> TimerMilliC;\n" +
			"}";
		
		Configuration conf = new Configuration(TEST_CNC);
		assertEquals(test_nc, conf.build());
	}

}

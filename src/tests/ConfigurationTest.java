package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Component;
import core.Configuration;
import core.FileManager;

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
	String TEST_CONF_CNC =
		"context configuration Temperature { }\n" +
		"implementation {\n" +
		"}";
	
	@Before
	public void setUp() throws Exception {
		FileManager.fwrite("DemoAppC.cnc", TEST_CNC);
		FileManager.fwrite("Temperature.cnc", TEST_CONF_CNC);
		String makeFile = 
				"COMPONENT = DemoAppC\n" +
				"PFLAGS += -I ./interfaces -I ./\n" +
				"include $(MAKERULES)\n";
		FileManager.fwrite("Makefile", makeFile);
	}

	@After
	public void tearDown() throws Exception {
		File app = new File ("DemoAppC.cnc");
		app.delete();
		File temperature = new File ("Temperature.cnc");
		temperature.delete();
		File makefile = new File ("Makefile");
		makefile.delete();
	}

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
		
		FileManager fm = new FileManager();
		Component conf = fm.getMainComponent();
		conf.build();
		assertEquals(test_nc, conf.getGeneratedFiles().get("DemoAppC.nc"));
	}

}

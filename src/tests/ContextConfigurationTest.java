package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.Component;
import core.ContextConfiguration;
import core.FileManager;
import core.Print;

public class ContextConfigurationTest {
	
	private String TEST_CNC =
		"context configuration Temperature {\n" +
		"  provides interface CoolInterface;\n" +
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
	
	private String TEST_CNC_2 =
			"context configuration Temperature {\n" +
			"  layered void toggle_leds();\n" +
			"  layered int32 test_function(int a, bool& b, string* c);\n" +
			"}\n" +
			"implementation {\n" +
			"  components\n" +
			"   High,\n" +
			"   Normal is default,\n" +
			"   Low,\n" +
			"   Other,\n" +
			"   LedsC;\n" +
			"  High.Leds -> LedsC;\n" +
			"  Normal.Leds -> LedsC;\n" +
			"  Low.Leds -> LedsC; \n" +
			"  Other.Leds -> LedsC;\n" +
			"}\n";
	
	private String TEST_CNC_4 =
			"context configuration Temperature {\n" +
			"  layered void toggle_leds();\n" +
			"  layered int32 test_function(int a, bool& b, string* c);\n" +
			"}\n" +
			"implementation {\n" +
			"  components\n" +
			"   High,\n" +
			"   Normal,\n" +
			"   Low,\n" +
			"   Other,\n" +
			"   LedsC is default;\n" +
			"  High.Leds -> LedsC;\n" +
			"  Normal.Leds -> LedsC;\n" +
			"  Low.Leds -> LedsC; \n" +
			"  Other.Leds -> LedsC;\n" +
			"}\n";
	
	String TEST_CONTEXT_1 =
		"context High {\n" +
	    "  transitions Normal if Location.Indoor, Low if Some.Group;\n" +
	    "  triggers Location.Outdoor;\n" +
		"}implementation{}";
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
	
	String TEST_CNC_5 =
			"context configuration Some {}\n" +
			"implementation{}";
	
	Component _temperature = null;
	
	@Before
	public void setUp() throws Exception {
		Print.init(Print.LogLevel.LOG_DEBUG);
		FileManager.fwrite("Temperature.cnc", TEST_CNC);
		FileManager.fwrite("Location.cnc", TEST_GROUP);
		FileManager.fwrite("High.cnc", TEST_CONTEXT_1);
		FileManager.fwrite("Normal.cnc", TEST_CONTEXT_2);
		FileManager.fwrite("Low.cnc", TEST_CONTEXT_3);
		FileManager.fwrite("Other.cnc", TEST_CONTEXT_4);
		FileManager.fwrite("DemoAppC.cnc", TEST_CNC_3);
		FileManager.fwrite("Some.cnc", TEST_CNC_5);
		String makeFile = 
				"COMPONENT = DemoAppC\n" +
				"PFLAGS += -I ./interfaces -I ./\n" +
				"include $(MAKERULES)\n";
		FileManager.fwrite("Makefile", makeFile);
		
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		mainConf.parse();
		
		for (Component component : mainConf.getComponents().values())
			if (component.getName().equals("Temperature")) {
				_temperature = component;
				break;
			}
		assertNotNull(_temperature);
		_temperature.build();
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
		
		File some = new File ("Some.cnc");
		some.delete();
		
		_temperature = null;
		Print.init(Print.LogLevel.LOG_NOTHING);
	}

	@Test
	public void buildInterfaceTest() {
		String test_interface =
			"interface TemperatureLayer {\n" +
			"  command void toggle_leds();\n" +
			"  command int32 test_function(int a, bool& b, string* c);\n" +
			"}\n";
		
		assertEquals(test_interface, _temperature.getGeneratedFiles().get("TemperatureLayer.nc"));
	}
	
	@Test
	public void buildConfigurationTest() {
		String test_configuration =
			"configuration TemperatureConfiguration {\n" +
			"  provides interface ContextGroup;\n" +
			"  provides interface TemperatureLayer;\n" +
			"  provides interface CoolInterface;\n" +
			"}\n" +
			"implementation {\n" +
			"  components\n" +
			"    LocationConfiguration,\n" +
			"    SomeConfiguration,\n" +
			"    TemperatureGroup,\n" +
			"    HighTemperatureContext,\n" +
			"    NormalTemperatureContext,\n" +
			"    LowTemperatureContext,\n" +
			"    OtherTemperatureContext,\n" +
			"    LedsC;\n" +
			"  TemperatureGroup.NormalTemperatureContext -> NormalTemperatureContext;\n" +
			"  OtherTemperatureContext.Leds -> LedsC;\n" +
			"  HighTemperatureContext.Leds -> LedsC;\n" +
			"  OtherTemperatureContext.SomeGroup -> LocationConfiguration;\n" +
			"  TemperatureGroup.LowTemperatureLayer -> LowTemperatureContext;\n" +
			"  TemperatureGroup.LocationGroup -> LocationConfiguration;\n" +
			"  TemperatureGroup.HighTemperatureLayer -> HighTemperatureContext;\n" +
			"  NormalTemperatureContext.Leds -> LedsC;\n" +
			"  LowTemperatureContext.Leds -> LedsC;\n" +
			"  TemperatureGroup.LowTemperatureContext -> LowTemperatureContext;\n" +
			"  TemperatureGroup.OtherTemperatureLayer -> OtherTemperatureContext;\n" +
			"  TemperatureGroup.NormalTemperatureLayer -> NormalTemperatureContext;\n" +
			"  TemperatureGroup.SomeGroup -> SomeConfiguration;\n" +
			"  TemperatureGroup.OtherTemperatureContext -> OtherTemperatureContext;\n" +
			"  TemperatureGroup.HighTemperatureContext -> HighTemperatureContext;\n" +
			"  TemperatureLayer = TemperatureGroup;\n" +
			"  ContextGroup = TemperatureGroup;\n" +
			"}";

		assertEquals(test_configuration, _temperature.getGeneratedFiles().get("TemperatureConfiguration.nc"));
	}
	
	@Test
	public void buildErrorContextTest() {
		String test_error =
			"module ErrorTemperatureContext {\n" +
			"  provides interface ContextCommands as Command;\n" +
			"  uses interface ContextEvents as Event;\n" +
			"}\n" +
			"implementation {\n" +
			"  event void Event.activated(){\n" +
			"  }\n" +
			"  event void Event.deactivated(){\n" +
			"  }\n" +
			"  command bool Command.check(){\n" +
			"    return TRUE;\n" +
			"  }\n" +
			"  command void Command.activate() {\n" +
			"    signal Event.activated();\n" +
			"  }\n" +
			"  command void Command.deactivate() {\n" +
			"    signal Event.deactivated();\n" +
			"  }\n" +
			"  command bool Command.transitionIsPossible(context_t con) {\n" +
			"    return TRUE;\n" +
			"  }\n" +
			"  command bool Command.conditionsAreSatisfied(context_t to, context_t cond) {\n" +
			"    return TRUE;\n" +
			"  }\n" +
			"}";
		FileManager.fwrite("Temperature.cnc", TEST_CNC_2);
		_temperature = null;
		
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		mainConf.parse();
		
		for (Component component : mainConf.getComponents().values())
			if (component.getName().equals("Temperature")) {
				_temperature = component;
				break;
			}
		assertNotNull(_temperature);
		_temperature.build();
		
		assertEquals(test_error, _temperature.getGeneratedFiles().get("ErrorTemperatureContext.nc"));
	}
	
	@Test
	public void buildGroupTest() {
		String test_group =
			"#include \"Contexts.h\"\n" +
			"module TemperatureGroup {\n" +
			"  provides interface ContextGroup as Group;\n" +
			"  provides interface TemperatureLayer as Layer;\n" +
			"  uses interface ContextCommands as HighTemperatureContext;\n" +
			"  uses interface TemperatureLayer as HighTemperatureLayer;\n" +
			"  uses interface ContextCommands as NormalTemperatureContext;\n" +
			"  uses interface TemperatureLayer as NormalTemperatureLayer;\n" +
			"  uses interface ContextCommands as LowTemperatureContext;\n" +
			"  uses interface TemperatureLayer as LowTemperatureLayer;\n" +
			"  uses interface ContextCommands as OtherTemperatureContext;\n" +
			"  uses interface TemperatureLayer as OtherTemperatureLayer;\n" +
			"  uses interface ContextGroup as SomeGroup;\n" +
			"  uses interface ContextGroup as LocationGroup;\n" +
			"}\n" +
			"implementation {\n" +
			"  context_t context = NORMALTEMPERATURE;\n" +
			"  void deactivate() {\n" +
			"    switch (context) {\n" +
			"      case HIGHTEMPERATURE:\n" +
			"        call HighTemperatureContext.deactivate();\n" +
			"        break;\n" +
			"      case NORMALTEMPERATURE:\n" +
			"        call NormalTemperatureContext.deactivate();\n" +
			"        break;\n" +
			"      case LOWTEMPERATURE:\n" +
			"        call LowTemperatureContext.deactivate();\n" +
			"        break;\n" +
			"      case OTHERTEMPERATURE:\n" +
			"        call OtherTemperatureContext.deactivate();\n" +
			"        break;\n" +
			"      default:\n" +
			"        break;\n" +
			"    }\n" +
			"  }\n" +
			"  bool transitionIsPossible(context_t con) {\n" +
			"    switch (context) {\n" +
			"      case HIGHTEMPERATURE:\n" +
			"        return call HighTemperatureContext.transitionIsPossible(con);\n" +
			"      case NORMALTEMPERATURE:\n" +
			"        return call NormalTemperatureContext.transitionIsPossible(con);\n" +
			"      case LOWTEMPERATURE:\n" +
			"        return call LowTemperatureContext.transitionIsPossible(con);\n" +
			"      case OTHERTEMPERATURE:\n" +
			"        return call OtherTemperatureContext.transitionIsPossible(con);\n" +
			"      default:\n" +
			"        return FALSE;\n" +
			"    }\n" +
			"  }\n" +
			
			"  bool conditionsAreSatisfied(context_t to) {\n" +
			"    switch (context) {\n" +
			"      case HIGHTEMPERATURE:\n" +
			"        return call HighTemperatureContext.conditionsAreSatisfied(to, call SomeGroup.getContext()) ||\n" +
			"               call HighTemperatureContext.conditionsAreSatisfied(to, call LocationGroup.getContext());\n" +
			"      default:\n" +
			"        return TRUE;\n" +
			"    }\n" +
			"  }\n" +
			
			"  command void Group.activate(context_t con) {\n" +
		    "    if (!transitionIsPossible(con)) {\n" +
		    "      deactivate();\n" +
		    "      call OtherTemperatureContext.activate();\n" +
		    "      context = OTHERTEMPERATURE;\n" +
		    "      signal Group.contextChanged(OTHERTEMPERATURE);\n" +
		    "      return;\n" +
		    "    }\n" +
		    
		    "    if (!conditionsAreSatisfied(con)) return;\n" +
		    
			"    switch (con) {\n" +
			"      case HIGHTEMPERATURE:\n" +
	        "        if (!call HighTemperatureContext.check()) return;\n" +
	        "        deactivate();\n" +
	        "        call HighTemperatureContext.activate();\n" +
	        "        context = HIGHTEMPERATURE;\n" +
	        "        call LocationGroup.activate(OUTDOORLOCATION);\n" +
			"        break;\n" +
			"      case NORMALTEMPERATURE:\n" +
			"        if (!call NormalTemperatureContext.check()) return;\n" +
	        "        deactivate();\n" +
	        "        call NormalTemperatureContext.activate();\n" +
	        "        context = NORMALTEMPERATURE;\n" +
			"        break;\n" +
			"      case LOWTEMPERATURE:\n" +
			"        if (!call LowTemperatureContext.check()) return;\n" +
	        "        deactivate();\n" +
	        "        call LowTemperatureContext.activate();\n" +
	        "        context = LOWTEMPERATURE;\n" +
			"        break;\n" +
			"      case OTHERTEMPERATURE:\n" +
			"        if (!call OtherTemperatureContext.check()) return;\n" +
	        "        deactivate();\n" +
	        "        call OtherTemperatureContext.activate();\n" +
	        "        context = OTHERTEMPERATURE;\n" +
			"        break;\n" +
			"      default:\n" +
			"        deactivate();\n" +
			"        call OtherTemperatureContext.activate();\n" +
			"        context = OTHERTEMPERATURE;\n" +
			"        signal Group.contextChanged(OTHERTEMPERATURE);\n" +
			"        return;\n" +
			"    }\n" +
			"    signal Group.contextChanged(con);\n" +
			"  }\n" +
			"  command context_t Group.getContext() {\n" +
			"    return context;\n" +
			"  }\n" +
			"  event void LocationGroup.contextChanged(context_t con) {\n" +
			"  }\n" +
			"  event void SomeGroup.contextChanged(context_t con) {\n" +
			"  }\n" +
			"  command void Layer.toggle_leds() {\n" +
			"    switch (context) {\n" +
			"      case HIGHTEMPERATURE:\n" +
			"        call HighTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      case NORMALTEMPERATURE:\n" +
			"        call NormalTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      case LOWTEMPERATURE:\n" +
			"        call LowTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      case OTHERTEMPERATURE:\n" +
			"        call OtherTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      default:\n" +
			"        break;\n" +
			"    }\n" +
			"  }\n" +
			"  command int32 Layer.test_function(int a, bool& b, string* c) {\n" +
			"    switch (context) {\n" +
			"      case HIGHTEMPERATURE:\n" +
			"        call HighTemperatureLayer.test_function(a, b, c);\n" +
			"        break;\n" +
			"      case NORMALTEMPERATURE:\n" +
			"        call NormalTemperatureLayer.test_function(a, b, c);\n" +
			"        break;\n" +
			"      case LOWTEMPERATURE:\n" +
			"        call LowTemperatureLayer.test_function(a, b, c);\n" +
			"        break;\n" +
			"      case OTHERTEMPERATURE:\n" +
			"        call OtherTemperatureLayer.test_function(a, b, c);\n" +
			"        break;\n" +
			"      default:\n" +
			"        break;\n" +
			"    }\n" +
			"  }\n" +
			"}\n";
		
		
		assertEquals(test_group, _temperature.getGeneratedFiles().get("TemperatureGroup.nc"));
	}
	
	@Test
	public void buildGroupTest_2() {
		String test_group =
				"#include \"Contexts.h\"\n" +
				"module TemperatureGroup {\n" +
				"  provides interface ContextGroup as Group;\n" +
				"  provides interface TemperatureLayer as Layer;\n" +
				"  uses interface ContextCommands as HighTemperatureContext;\n" +
				"  uses interface TemperatureLayer as HighTemperatureLayer;\n" +
				"  uses interface ContextCommands as NormalTemperatureContext;\n" +
				"  uses interface TemperatureLayer as NormalTemperatureLayer;\n" +
				"  uses interface ContextCommands as LowTemperatureContext;\n" +
				"  uses interface TemperatureLayer as LowTemperatureLayer;\n" +
				"  uses interface ContextCommands as OtherTemperatureContext;\n" +
				"  uses interface TemperatureLayer as OtherTemperatureLayer;\n" +
				"  uses interface ContextCommands as ErrorTemperatureContext;\n" +
				"  uses interface ContextGroup as SomeGroup;\n" +
				"  uses interface ContextGroup as LocationGroup;\n" +
				"}\n" +
				"implementation {\n" +
				"  context_t context = NORMALTEMPERATURE;\n" +
				"  void deactivate() {\n" +
				"    switch (context) {\n" +
				"      case HIGHTEMPERATURE:\n" +
				"        call HighTemperatureContext.deactivate();\n" +
				"        break;\n" +
				"      case NORMALTEMPERATURE:\n" +
				"        call NormalTemperatureContext.deactivate();\n" +
				"        break;\n" +
				"      case LOWTEMPERATURE:\n" +
				"        call LowTemperatureContext.deactivate();\n" +
				"        break;\n" +
				"      case OTHERTEMPERATURE:\n" +
				"        call OtherTemperatureContext.deactivate();\n" +
				"        break;\n" +
				"      case ERRORTEMPERATURE:\n" +
				"        call ErrorTemperatureContext.deactivate();\n" +
				"        break;\n" +
				"      default:\n" +
				"        break;\n" +
				"    }\n" +
				"  }\n" +
				"  bool transitionIsPossible(context_t con) {\n" +
				"    switch (context) {\n" +
				"      case HIGHTEMPERATURE:\n" +
				"        return call HighTemperatureContext.transitionIsPossible(con);\n" +
				"      case NORMALTEMPERATURE:\n" +
				"        return call NormalTemperatureContext.transitionIsPossible(con);\n" +
				"      case LOWTEMPERATURE:\n" +
				"        return call LowTemperatureContext.transitionIsPossible(con);\n" +
				"      case OTHERTEMPERATURE:\n" +
				"        return call OtherTemperatureContext.transitionIsPossible(con);\n" +
				"      default:\n" +
				"        return FALSE;\n" +
				"    }\n" +
				"  }\n" +
				"  bool conditionsAreSatisfied(context_t to) {\n" +
				"    switch (context) {\n" +
				"      case HIGHTEMPERATURE:\n" +
				"        return call HighTemperatureContext.conditionsAreSatisfied(to, call SomeGroup.getContext()) ||\n" +
				"               call HighTemperatureContext.conditionsAreSatisfied(to, call LocationGroup.getContext());\n" +
				"      default:\n" +
				"        return TRUE;\n" +
				"    }\n" +
				"  }\n" +
				"  command void Group.activate(context_t con) {\n" +
			    "    if (!transitionIsPossible(con)) {\n" +
			    "      deactivate();\n" +
			    "      call ErrorTemperatureContext.activate();\n" +
			    "      context = ERRORTEMPERATURE;\n" +
			    "      signal Group.contextChanged(ERRORTEMPERATURE);\n" +
			    "      return;\n" +
			    "    }\n" +
			    "    if (!conditionsAreSatisfied(con)) return;\n" +
				"    switch (con) {\n" +
				"      case HIGHTEMPERATURE:\n" +
		        "        if (!call HighTemperatureContext.check()) return;\n" +
		        "        deactivate();\n" +
		        "        call HighTemperatureContext.activate();\n" +
		        "        context = HIGHTEMPERATURE;\n" +
		        "        call LocationGroup.activate(OUTDOORLOCATION);\n" +
				"        break;\n" +
				"      case NORMALTEMPERATURE:\n" +
				"        if (!call NormalTemperatureContext.check()) return;\n" +
		        "        deactivate();\n" +
		        "        call NormalTemperatureContext.activate();\n" +
		        "        context = NORMALTEMPERATURE;\n" +
				"        break;\n" +
				"      case LOWTEMPERATURE:\n" +
				"        if (!call LowTemperatureContext.check()) return;\n" +
		        "        deactivate();\n" +
		        "        call LowTemperatureContext.activate();\n" +
		        "        context = LOWTEMPERATURE;\n" +
				"        break;\n" +
				"      case OTHERTEMPERATURE:\n" +
				"        if (!call OtherTemperatureContext.check()) return;\n" +
		        "        deactivate();\n" +
		        "        call OtherTemperatureContext.activate();\n" +
		        "        context = OTHERTEMPERATURE;\n" +
				"        break;\n" +
				"      default:\n" +
				"        deactivate();\n" +
				"        call ErrorTemperatureContext.activate();\n" +
				"        context = ERRORTEMPERATURE;\n" +
				"        signal Group.contextChanged(ERRORTEMPERATURE);\n" +
				"        return;\n" +
				"    }\n" +
				"    signal Group.contextChanged(con);\n" +
				"  }\n" +
				"  command context_t Group.getContext() {\n" +
				"    return context;\n" +
				"  }\n" +
				"  event void SomeGroup.contextChanged(context_t con) {\n" +
				"  }\n" +
				"  event void LocationGroup.contextChanged(context_t con) {\n" +
				"  }\n" +
				"  command void Layer.toggle_leds() {\n" +
				"    switch (context) {\n" +
				"      case HIGHTEMPERATURE:\n" +
				"        call HighTemperatureLayer.toggle_leds();\n" +
				"        break;\n" +
				"      case NORMALTEMPERATURE:\n" +
				"        call NormalTemperatureLayer.toggle_leds();\n" +
				"        break;\n" +
				"      case LOWTEMPERATURE:\n" +
				"        call LowTemperatureLayer.toggle_leds();\n" +
				"        break;\n" +
				"      case OTHERTEMPERATURE:\n" +
				"        call OtherTemperatureLayer.toggle_leds();\n" +
				"        break;\n" +
				"      default:\n" +
				"        break;\n" +
				"    }\n" +
				"  }\n" +
				"  command int32 Layer.test_function(int a, bool& b, string* c) {\n" +
				"    switch (context) {\n" +
				"      case HIGHTEMPERATURE:\n" +
				"        call HighTemperatureLayer.test_function(a, b, c);\n" +
				"        break;\n" +
				"      case NORMALTEMPERATURE:\n" +
				"        call NormalTemperatureLayer.test_function(a, b, c);\n" +
				"        break;\n" +
				"      case LOWTEMPERATURE:\n" +
				"        call LowTemperatureLayer.test_function(a, b, c);\n" +
				"        break;\n" +
				"      case OTHERTEMPERATURE:\n" +
				"        call OtherTemperatureLayer.test_function(a, b, c);\n" +
				"        break;\n" +
				"      default:\n" +
				"        break;\n" +
				"    }\n" +
				"  }\n" +
				"}\n";
		
		FileManager.fwrite("Temperature.cnc", TEST_CNC_2);
		_temperature = null;
		
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		mainConf.parse();
		
		for (Component component : mainConf.getComponents().values())
			if (component.getName().equals("Temperature")) {
				_temperature = component;
				break;
			}
		assertNotNull(_temperature);
		_temperature.build();
		assertEquals(test_group, _temperature.getGeneratedFiles().get("TemperatureGroup.nc"));
	}
	
	@Test
	public void testConfiguration_3() {
	
		FileManager.fwrite("Temperature.cnc", TEST_CNC_4);
		_temperature = null;
		
		FileManager fm = new FileManager();
		Component mainConf = fm.getMainComponent();
		
		ByteArrayOutputStream errContent = new ByteArrayOutputStream();
		
		System.setErr(new PrintStream(errContent));
		
		mainConf.parse();
		assertTrue(errContent.toString().contains("Temperature.cnc 10: Component LedsC is not a Context or does not exist, but declared as a default Context!"));
		
		System.setErr(System.out);
	}

}

package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import core.ContextConfiguration;

public class ContextConfigurationTest {
	
	private String TEST_CNC =
		"context configuration Temperature {\n" +
		"  layered void toggle_leds();\n" +
		"  layered int32 test_function(int a, bool& b, string* c);\n" +
		"}\n" +
		"implementation {\n" +
		"  contexts\n" +
		"   High,\n" +
		"   Normal is default,\n" +
		"   Low;\n" +
		"  components\n" +
		"   LedsC;\n" +
		"  High.Leds -> LedsC;\n" +
		"  Normal.Leds -> LedsC;\n" +
		"  Low.Leds -> LedsC; \n" +
		"  Error.Leds -> LedsC;\n" +
		"}\n";

	@Test
	public void buildInterfaceTest() {
		String test_interface =
			"interface TemperatureLayer {\n" +
			"  command void toggle_leds();\n" +
			"  command int32 test_function(int a, bool& b, string* c);\n" +
			"}\n";
		ContextConfiguration test_configuration = new ContextConfiguration(TEST_CNC);
		assertEquals(test_configuration.buildInterface(), test_interface);
	}
	
	@Test
	public void buildConfigurationTest() {
		String test_configuration =
			"configuration TemperatureConfiguration {\n" +
			"  provides interface ContextGroup;\n" +
			"  provides interface TemperatureLayer;\n" +
			"}\n" +
			"implementation {\n" +
			"  components\n" +
			"    TemperatureGroup,\n" +
			"    HighTemperatureContext,\n" +
			"    NormalTemperatureContext,\n" +
			"    LowTemperatureContext,\n" +
			"    ErrorTemperatureContext,\n" +
			"    LedsC;\n" +
			"  HighTemperatureContext.Leds -> LedsC;\n" +
			"  ErrorTemperatureContext.Leds -> LedsC;\n" +
			"  NormalTemperatureContext.Leds -> LedsC;\n" +
			"  LowTemperatureContext.Leds -> LedsC;\n" +
			"  TemperatureGroup.HighTemperatureContext -> HighTemperatureContext;\n" +
			"  TemperatureGroup.HighTemperatureLayer -> HighTemperatureContext;\n" +
			"  TemperatureGroup.NormalTemperatureContext -> NormalTemperatureContext;\n" +
			"  TemperatureGroup.NormalTemperatureLayer -> NormalTemperatureContext;\n" +
			"  TemperatureGroup.LowTemperatureContext -> LowTemperatureContext;\n" +
			"  TemperatureGroup.LowTemperatureLayer -> LowTemperatureContext;\n" +
			"  TemperatureGroup.ErrorTemperatureContext -> ErrorTemperatureContext;\n" +
			"  TemperatureGroup.ErrorTemperatureLayer -> ErrorTemperatureContext;\n" +
			"  ContextGroup = TemperatureGroup;\n" +
			"  TemperatureLayer = TemperatureGroup;\n" +
			"}\n";
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC);
		assertEquals(test_conf.buildConfiguration(), test_configuration);
	}
	
	@Test
	public void buildErrorContextTest() {
		String test_error =
			"module ErrorTemperatureContext {\n" +
			"  provides interface ContextCommands as Command;\n" +
			"  provides interface TemperatureLayer as Layered;\n" +
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
			"}";
		
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC);
		assertFalse(test_conf.buildErrorContext().isEmpty());
		assertEquals(test_error, test_conf.buildErrorContext());
	}
	
	@Test
	public void buildGroupTest() {
		String test_group =
			"#include \"Contexts.h\"\n" +
			"module TemperatureGroup {\n" +
			"  provides interface ContextGroup as Group;\n" +
			"  provides interface TemperatureLayer as Layer;\n" +
			"  uses interface ContextCommands as HighTemperatureContext;\n" +
			"  uses interface ContextCommands as NormalTemperatureContext;\n" +
			"  uses interface ContextCommands as LowTemperatureContext;\n" +
			"  uses interface ContextCommands as ErrorTemperatureContext;\n" +
			"  uses interface TemperatureLayer as HighTemperatureLayer;\n" +
			"  uses interface TemperatureLayer as NormalTemperatureLayer;\n" +
			"  uses interface TemperatureLayer as LowTemperatureLayer;\n" +
			"  uses interface TemperatureLayer as ErrorTemperatureLayer;\n" +
			"}\n" +
			"implementation {\n" +
			"  context_t context = NORMAL;\n" +
			"  command void ContextGroup.activate(context_t con) {\n" +
			"    switch (con) {\n" +
			"      case HIGH:\n" +
			"        call HighTemperatureContext.activate();\n" +
			"        break;\n" +
			"      case NORMAL:\n" +
			"        call NormalTemperatureContext.activate();\n" +
			"        break;\n" +
			"      case LOW:\n" +
			"        call LowTemperatureContext.activate();\n" +
			"        break;\n" +
			"      default:\n" +
			"        call ErrorTemperatureContext.activate();\n" +
			"        signal ContextGroup.contextChanged(ERRORTEMPERATURE);\n" +
			"        return;\n" +
			"    }\n" +
			"    call ContextGroup.contextChanged(con);\n" +
			"  }\n" +
			"  command void TemperatureLayer.toggle_leds() {\n" +
			"    switch (context) {\n" +
			"      case HIGH:\n" +
			"        call HighTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      case NORMAL:\n" +
			"        call NormalTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      case LOW:\n" +
			"        call LowTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"      default:\n" +
			"        call ErrorTemperatureLayer.toggle_leds();\n" +
			"        break;\n" +
			"    }\n" +
			"  }\n" +
			"}";
		
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC);
		assertEquals(test_group, test_conf.buildGroup());
	}

}

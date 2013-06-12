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
			"  contexts\n" +
			"   High,\n" +
			"   Normal is default,\n" +
			"   Low,\n" +
			"   Other;\n" +
			"  components\n" +
			"   LedsC;\n" +
			"  High.Leds -> LedsC;\n" +
			"  Normal.Leds -> LedsC;\n" +
			"  Low.Leds -> LedsC; \n" +
			"  Other.Leds -> LedsC;\n" +
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
			"    LocationConfiguration,\n" +
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
			"  OtherTemperatureContext.SomeLayer -> LocationConfiguration;\n" +
			"  TemperatureGroup.LowTemperatureLayer -> LowTemperatureContext;\n" +
			"  TemperatureGroup.HighTemperatureLayer -> HighTemperatureContext;\n" +
			"  NormalTemperatureContext.Leds -> LedsC;\n" +
			"  LowTemperatureContext.Leds -> LedsC;\n" +
			"  TemperatureGroup.LowTemperatureContext -> LowTemperatureContext;\n" +
			"  TemperatureGroup.OtherTemperatureLayer -> OtherTemperatureContext;\n" +
			"  TemperatureGroup.NormalTemperatureLayer -> NormalTemperatureContext;\n" +
			"  TemperatureGroup.OtherTemperatureContext -> OtherTemperatureContext;\n" +
			"  TemperatureGroup.HighTemperatureContext -> HighTemperatureContext;\n" +
			"  TemperatureLayer = TemperatureGroup;\n" +
			"  ContextGroup = TemperatureGroup;\n" +
			"}";
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC);
		assertEquals(test_configuration, test_conf.buildConfiguration());
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
		
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC_2);
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
			"  uses interface TemperatureLayer as HighTemperatureLayer;\n" +
			"  uses interface ContextCommands as NormalTemperatureContext;\n" +
			"  uses interface TemperatureLayer as NormalTemperatureLayer;\n" +
			"  uses interface ContextCommands as LowTemperatureContext;\n" +
			"  uses interface TemperatureLayer as LowTemperatureLayer;\n" +
			"  uses interface ContextCommands as OtherTemperatureContext;\n" +
			"  uses interface TemperatureLayer as OtherTemperatureLayer;\n" +
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
			"  command void Group.activate(context_t con) {\n" +
		    "    if (!transitionIsPossible(con)) {\n" +
		    "      deactivate();\n" +
		    "      call OtherTemperatureContext.activate();\n" +
		    "      context = OTHERTEMPERATURE;\n" +
		    "      signal Group.contextChanged(OTHERTEMPERATURE);\n" +
		    "      return;\n" +
		    "    }\n" +
			"    switch (con) {\n" +
			"      case HIGHTEMPERATURE:\n" +
	        "        if (!call HighTemperatureContext.check()) return;\n" +
	        "        deactivate();\n" +
	        "        call HighTemperatureContext.activate();\n" +
	        "        context = HIGHTEMPERATURE;\n" +
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
			"    call Group.contextChanged(con);\n" +
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
		
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC);
		assertEquals(test_group, test_conf.buildGroup());
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
				"  uses interface TemperatureLayer as ErrorTemperatureLayer;\n" +
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
				"  command void Group.activate(context_t con) {\n" +
			    "    if (!transitionIsPossible(con)) {\n" +
			    "      deactivate();\n" +
			    "      call ErrorTemperatureContext.activate();\n" +
			    "      context = ERRORTEMPERATURE;\n" +
			    "      signal Group.contextChanged(ERRORTEMPERATURE);\n" +
			    "      return;\n" +
			    "    }\n" +
				"    switch (con) {\n" +
				"      case HIGHTEMPERATURE:\n" +
		        "        if (!call HighTemperatureContext.check()) return;\n" +
		        "        deactivate();\n" +
		        "        call HighTemperatureContext.activate();\n" +
		        "        context = HIGHTEMPERATURE;\n" +
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
				"    call Group.contextChanged(con);\n" +
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
		
		ContextConfiguration test_conf = new ContextConfiguration(TEST_CNC_2);
		assertEquals(test_group, test_conf.buildGroup());
	}

}

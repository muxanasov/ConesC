// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.io.StringReader;
import org.junit.Test;

import core.Component;
import core.ComponentFile;

import parsers.module.ParseException;
import parsers.module.Parser;

public class ModuleParserTest {
	
	String TEST_EMPTY = "";
	String TEST_CONTEXT_NAME = "NormalTemperatureContext";
	String USES = "uses";
	String PROVIDES = "provides";
	String TRIGGERS = "triggers";
	String TRANSITION = "transition";
	String TEST_INTERFACE_1 = "ContextCommands as Command";
	String TEST_INTERFACE_2 = "LayeredInterface as Layered";
	String TEST_INTERFACE_3 = "ContextEvents as Event";
	String TEST_INTERFACE_4 = "Leds";
	String COMMAND = "command";
	String EVENT = "event";
	String LAYERED = "layered";
	String TEST_RETURN_TYPE_1 = "void";
	String TEST_RETURN_TYPE_2 = "bool";
	String TEST_FUNCTION_NAME_1 = "Command.activate";
	String TEST_FUNCTION_NAME_2 = "Command.check";
	String TEST_FUNCTION_NAME_3 = "Layered.toggle_leds";
	String TEST_FUNCTION_NAME_4 = "Event.activated";
	String TEST_FUNCTION_NAME_5 = "Event.deactivated";
	String TEST_BODY_1 = 
	"    /* user's code context */\n"+
	"    dbg(\"Debug\", \"Normal temperature context activated.\n\");\n"+
	"    signal Event.activated();\n";
	String TEST_BODY_2 = 
	"    /* user's code */\n"+
	"    /* or          */\n"+
	"    return TRUE;\n";
	String TEST_BODY_3 =
	"    /* user's code */\n"+
	"    dbg(\'Debug\', \"Normal temperature context activated event fired.\n\");\n";
	String TEST_BODY_4 = "    /* user's code */\n";
	String TEST_BODY_5 = 
	"    call Leds.set(2);\n"+
	"    super_duper(\"blah\",100500, true);\n" +
	"    while(true){\n" +
	"      blah-blah-blah;\n" +
	"    }\n";
	String TEST_BODY_6 =
	"  int a;\n" +
	"  structure bc{\n" +
	"    bool b;\n" +
	"  };\n" +
	"  super_duper(string a,int b, boolean c) {\n" +
	"    dbg(\'Debug\', \"Normal temperature context activated event fired.\n\");\n"+
	"  }\n" +
	"  super_duper(string a,int b, boolean c) {\n" +
	"    dbg(\'Debug\', \"Normal temperature context activated event fired.\n\");\n"+
	"  }\n";

	@Test
	public void testParseContext() {
		String contextFile =   "context " + TEST_CONTEXT_NAME + " {\n"+
		"  " + PROVIDES + " interface " + TEST_INTERFACE_1 + ";\n"+
		"  " + PROVIDES + " interface " + TEST_INTERFACE_2 + ";\n"+
		"  " + USES + " interface " + TEST_INTERFACE_3 + ";\n"+
		"  " + USES + " interface " + TEST_INTERFACE_4 + ";\n"+
		"}\n"+
		"implementation {\n"+
		"  int a;\n" +
		"  structure bc{\n" +
		"    bool b;\n" +
		"  };\n" +
		"  " + TEST_RETURN_TYPE_2 + " super_duper(string a,int b, boolean c) {\n" +
		"    dbg(\'Debug\', \"Normal temperature context activated event fired.\n\");\n"+
		"  }\n" +
		"  " + COMMAND + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_1 + "(int* a, int& b, int *a, int &b, int a, int b, int * a, int & b){\n"+
		TEST_BODY_1 +
		"  }\n"+
		"  " + TEST_RETURN_TYPE_1 + " super_duper(string a,int b, boolean c) {\n" +
		"    dbg(\'Debug\', \"Normal temperature context activated event fired.\n\");\n"+
		"  }\n" +
		"  " + COMMAND + " " + TEST_RETURN_TYPE_2 + " " + TEST_FUNCTION_NAME_2 + "() {\n"+
		TEST_BODY_2 +
		"  }\n"+
		"  " + EVENT + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_4 + "() {\n"+
		TEST_BODY_4 +
		"  }\n"+
		"  " + EVENT + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_5 + "() {\n"+
		TEST_BODY_5 +
		"  }\n"+
		"  " + COMMAND + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_3 + "() {\n"+
		TEST_BODY_3 +
		"  }\n"+
		"}\n"+
		"";
		
		String[] contextFileArray = contextFile.split("\n");
		
		Parser parser = new Parser(new StringReader(contextFile));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ComponentFile pfile = parser.getParsedFile();
		
		assertEquals(pfile.type, Component.Type.CONTEXT);
		
		assertEquals(pfile.name, TEST_CONTEXT_NAME);
		
		assertEquals(pfile.interfaces.size(), 4);
		assertEquals(pfile.interfaces.get(USES).size(), 2);
		assertEquals(pfile.interfaces.get(PROVIDES).size(), 2);
		assertEquals(pfile.interfaces.get(TRANSITION).size(), 0);
		assertEquals(pfile.interfaces.get(TRIGGERS).size(), 0);
		assertEquals(pfile.interfaces.get(USES).get(0), TEST_INTERFACE_3);
		assertEquals(pfile.interfaces.get(USES).get(1), TEST_INTERFACE_4);
		assertEquals(pfile.interfaces.get(PROVIDES).get(0), TEST_INTERFACE_1);
		assertEquals(pfile.interfaces.get(PROVIDES).get(1), TEST_INTERFACE_2);
		
		assertEquals(pfile.functions.size(), 3);
		assertEquals(pfile.functions.get(COMMAND).size(), 3);
		assertEquals(pfile.functions.get(EVENT).size(), 2);
		assertEquals(pfile.functions.get(LAYERED).size(), 0);
		
		assertEquals(pfile.functions.get(COMMAND).get(0).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(COMMAND).get(0).name, TEST_FUNCTION_NAME_1);
		assertEquals(pfile.functions.get(COMMAND).get(0).variables.size(), 8);
		
		assertEquals(pfile.functions.get(COMMAND).get(1).returnType, TEST_RETURN_TYPE_2);
		assertEquals(pfile.functions.get(COMMAND).get(1).name, TEST_FUNCTION_NAME_2);
		assertEquals(pfile.functions.get(COMMAND).get(1).variables.size(), 0);

		assertEquals(pfile.functions.get(COMMAND).get(2).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(COMMAND).get(2).name, TEST_FUNCTION_NAME_3);
		assertEquals(pfile.functions.get(COMMAND).get(2).variables.size(), 0);
		
		assertEquals(pfile.functions.get(EVENT).get(0).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(0).name, TEST_FUNCTION_NAME_4);
		assertEquals(pfile.functions.get(EVENT).get(0).variables.size(), 0);
		
		assertEquals(pfile.functions.get(EVENT).get(1).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(1).name, TEST_FUNCTION_NAME_5);
		assertEquals(pfile.functions.get(EVENT).get(1).variables.size(), 0);
		
		String body = "";
		for (int i = pfile.functions.get(COMMAND).get(0).bodyCoords.beginLine; 
				 i < pfile.functions.get(COMMAND).get(0).bodyCoords.endLine - 1; i++)
			body += contextFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_1);

		body = "";
		for (int i = pfile.functions.get(COMMAND).get(1).bodyCoords.beginLine; 
				 i < pfile.functions.get(COMMAND).get(1).bodyCoords.endLine - 1; i++)
			body += contextFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_2);

		body = "";
		for (int i = pfile.functions.get(COMMAND).get(2).bodyCoords.beginLine; 
				 i < pfile.functions.get(COMMAND).get(2).bodyCoords.endLine - 1; i++)
			body += contextFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_3);

		body = "";
		for (int i = pfile.functions.get(EVENT).get(0).bodyCoords.beginLine; 
				 i < pfile.functions.get(EVENT).get(0).bodyCoords.endLine - 1; i++)
			body += contextFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_4);

		body = "";
		for (int i = pfile.functions.get(EVENT).get(1).bodyCoords.beginLine; 
				 i < pfile.functions.get(EVENT).get(1).bodyCoords.endLine - 1; i++)
			body += contextFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_5);
	}
	
	String TEST_MODULE_NAME = "ConesCDemoC";
	String TEST_TYPE_1 = "uint16_t";
	String TEST_TYPE_2 = "context_t";
	String TEST_TYPE_3 = "error_t";
	String TEST_FUNCTION_NAME_6 = "Boot.booted";
	String TEST_FUNCTION_NAME_7 = "TemperatureClass.contextChanged";
	String TEST_FUNCTION_NAME_8 = "Layered.toggle_leds";
	String TEST_FUNCTION_NAME_9 = "Timer.fired";
	String TEST_FUNCTION_NAME_10 = "Read.readDone";
	String TEST_VARIABLE_1 = "con";
	String TEST_VARIABLE_2 = "result";
	String TEST_VARIABLE_3 = "data";
	String TEST_INTERFACE_5 = "Boot";
	String TEST_INTERFACE_6 = "ContextClass as TemperatureClass";
	String TEST_INTERFACE_7 = "LayeredInterface as TCLayer";
	String TEST_INTERFACE_8 = "Timer<TMilli>";
	String TEST_INTERFACE_9 = "Read<uint16_t>";
	String TEST_BODY_7 =
	"    dbg(\"Debug\", \"App is booted.\n\");\n" +
	"    call Timer.startPeriodic(100);\n" +
	"    call TemperatureClass.activate(HIGHTEMPERATURE);\n" +
	"	 call TCLayer.toggle_leds();\n";
	String TEST_BODY_8 =
	"    dbg(\"Debug\", \"Temperature class context changed %d.\n\", con);\n";
	String TEST_BODY_9 =
	"    call Leds.set(5);\n";
	String TEST_BODY_10 =
	"    call Read.read();\n";
	String TEST_BODY_11 =
	"    uint16_t temp = -39 + 0.01*data;\n" +
	"    if (result != SUCCESS) return;\n" +
	"    if (temp > T_min && temp < T_max)\n" +
	"      call TemperatureClass.activate(NORMALTEMPERATURE);\n" +
	"    else if (temp >= T_max)\n" +
	"      call TemperatureClass.activate(HIGHTEMPERATURE);\n" +
	"    else if (temp <= T_min)\n" +
	"      call TemperatureClass.activate(LOWTEMPERATURE);\n" +
	"    else call TemperatureClass.activate(ERROR);\n" +
	"    call TCLayer.toggle_leds();\n";
	
	@Test
	public void testParseModule() {
		String module =
		"module " + TEST_MODULE_NAME + " {\n" +
		"  " + PROVIDES + " interface " + TEST_INTERFACE_2 + ";\n" +
		"  " + USES + " interface " + TEST_INTERFACE_5 + ";\n" +
		"  " + USES + " interface " + TEST_INTERFACE_4 + ";\n" +
		"  " + USES + " interface " + TEST_INTERFACE_6 + ";\n" +
		"  " + USES + " interface " + TEST_INTERFACE_7 + ";\n" +
		"  " + USES + " interface " + TEST_INTERFACE_8 + ";\n" +
		"  " + USES + " interface " + TEST_INTERFACE_9 + ";\n" +
		"}\n" +
		"implementation {\n" +
		"  " + TEST_TYPE_1 + " T_min = 27;\n" +
		"  " + TEST_TYPE_1 + " T_max = 32;\n" +
		"  " + EVENT + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_6 + "() {\n" +
		TEST_BODY_7 +
		"  }\n" +
		"  " + EVENT + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_7 + "(" + TEST_TYPE_2 + " " + TEST_VARIABLE_1 + "){\n" +
		TEST_BODY_8 +
		"  }\n" +
		"  " + COMMAND + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_8 + "() {\n" +
		TEST_BODY_9 +
		"  }\n" +
		"  " + EVENT + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_9 + "() {\n" +
		TEST_BODY_10 +
		"  }\n" +
		"  " + EVENT + " " + TEST_RETURN_TYPE_1 + " " + TEST_FUNCTION_NAME_10 + "(" + TEST_TYPE_3 + " " + TEST_VARIABLE_2 + ", " + TEST_TYPE_1 + " " + TEST_VARIABLE_3 + ") {\n" +
		TEST_BODY_11 +
		"  }\n" +
		"}";
		
		String[] moduleFileArray = module.split("\n");
		
		Parser parser = new Parser(new StringReader(module));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ComponentFile pfile = parser.getParsedFile();
		
		assertEquals(pfile.type, Component.Type.MODULE);
		
		assertEquals(pfile.name, TEST_MODULE_NAME);
		
		assertEquals(pfile.interfaces.size(), 4);
		assertEquals(pfile.interfaces.get(USES).size(), 6);
		assertEquals(pfile.interfaces.get(PROVIDES).size(), 1);
		assertEquals(pfile.interfaces.get(TRANSITION).size(), 0);
		assertEquals(pfile.interfaces.get(TRIGGERS).size(), 0);
		assertEquals(pfile.interfaces.get(USES).get(0), TEST_INTERFACE_5);
		assertEquals(pfile.interfaces.get(USES).get(1), TEST_INTERFACE_4);
		assertEquals(pfile.interfaces.get(USES).get(2), TEST_INTERFACE_6);
		assertEquals(pfile.interfaces.get(USES).get(3), TEST_INTERFACE_7);
		assertEquals(pfile.interfaces.get(USES).get(4), TEST_INTERFACE_8);
		assertEquals(pfile.interfaces.get(USES).get(5), TEST_INTERFACE_9);
		assertEquals(pfile.interfaces.get(PROVIDES).get(0), TEST_INTERFACE_2);
		
		assertEquals(pfile.functions.size(), 3);
		assertEquals(pfile.functions.get(COMMAND).size(), 1);
		assertEquals(pfile.functions.get(EVENT).size(), 4);
		assertEquals(pfile.functions.get(LAYERED).size(), 0);
		
		assertEquals(pfile.functions.get(COMMAND).get(0).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(COMMAND).get(0).name, TEST_FUNCTION_NAME_8);
		assertEquals(pfile.functions.get(COMMAND).get(0).variables.size(), 0);
		
		assertEquals(pfile.functions.get(EVENT).get(0).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(0).name, TEST_FUNCTION_NAME_6);
		assertEquals(pfile.functions.get(EVENT).get(0).variables.size(), 0);
		
		assertEquals(pfile.functions.get(EVENT).get(1).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(1).name, TEST_FUNCTION_NAME_7);
		assertEquals(pfile.functions.get(EVENT).get(1).variables.size(), 1);
		
		assertEquals(pfile.functions.get(EVENT).get(1).variables.get(0).type, TEST_TYPE_2);
		assertEquals(pfile.functions.get(EVENT).get(1).variables.get(0).lexeme, TEST_EMPTY);
		assertEquals(pfile.functions.get(EVENT).get(1).variables.get(0).name, TEST_VARIABLE_1);
		
		assertEquals(pfile.functions.get(EVENT).get(2).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(2).name, TEST_FUNCTION_NAME_9);
		assertEquals(pfile.functions.get(EVENT).get(2).variables.size(), 0);
		
		assertEquals(pfile.functions.get(EVENT).get(3).returnType, TEST_RETURN_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(3).name, TEST_FUNCTION_NAME_10);
		assertEquals(pfile.functions.get(EVENT).get(3).variables.size(), 2);
		
		assertEquals(pfile.functions.get(EVENT).get(3).variables.get(0).type, TEST_TYPE_3);
		assertEquals(pfile.functions.get(EVENT).get(3).variables.get(0).lexeme, TEST_EMPTY);
		assertEquals(pfile.functions.get(EVENT).get(3).variables.get(0).name, TEST_VARIABLE_2);
		
		assertEquals(pfile.functions.get(EVENT).get(3).variables.get(1).type, TEST_TYPE_1);
		assertEquals(pfile.functions.get(EVENT).get(3).variables.get(1).lexeme, TEST_EMPTY);
		assertEquals(pfile.functions.get(EVENT).get(3).variables.get(1).name, TEST_VARIABLE_3);
		
		String body = "";
		for (int i = pfile.functions.get(COMMAND).get(0).bodyCoords.beginLine; 
				 i < pfile.functions.get(COMMAND).get(0).bodyCoords.endLine - 1; i++)
			body += moduleFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_9);
		
		body = "";
		for (int i = pfile.functions.get(EVENT).get(0).bodyCoords.beginLine; 
				 i < pfile.functions.get(EVENT).get(0).bodyCoords.endLine - 1; i++)
			body += moduleFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_7);
		
		body = "";
		for (int i = pfile.functions.get(EVENT).get(1).bodyCoords.beginLine; 
				 i < pfile.functions.get(EVENT).get(1).bodyCoords.endLine - 1; i++)
			body += moduleFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_8);
		
		body = "";
		for (int i = pfile.functions.get(EVENT).get(2).bodyCoords.beginLine; 
				 i < pfile.functions.get(EVENT).get(2).bodyCoords.endLine - 1; i++)
			body += moduleFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_10);
		
		body = "";
		for (int i = pfile.functions.get(EVENT).get(3).bodyCoords.beginLine; 
				 i < pfile.functions.get(EVENT).get(3).bodyCoords.endLine - 1; i++)
			body += moduleFileArray[i] + "\n";
		assertEquals(body, TEST_BODY_11);

	}

}

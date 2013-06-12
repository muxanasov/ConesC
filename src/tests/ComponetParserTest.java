package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import core.Component;

import parsers.component.ComponentFile;
import parsers.component.ParseException;
import parsers.component.Parser;

public class ComponetParserTest {
	
	String TEST_NAME = "Component";
	
	@Test
	public void ModuleTest() {
		String test_cnc =
			"#include \"include.h\"\n" +
			"#include <stdio.h>\n" +
			"module " + TEST_NAME + " {}\n" +
			"implementation {}";
		Parser parser = new Parser(new StringReader(test_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ComponentFile file = parser.getParsedFile();
		
		assertEquals(Component.Type.MODULE, file.type);
		assertEquals(TEST_NAME, file.name);
	}
	
	@Test
	public void ContextTest() {
		String test_cnc =
			"#include \"include.h\"\n" +
			"#include <stdio.hpp>\n" +
			"context " + TEST_NAME + " {}\n" +
			"implementation {}";
		Parser parser = new Parser(new StringReader(test_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ComponentFile file = parser.getParsedFile();
		
		assertEquals(Component.Type.CONTEXT, file.type);
		assertEquals(TEST_NAME, file.name);
	}
	
	@Test
	public void ContextConfigurationTest() {
		String test_cnc =
			"#include \"include.h\"\n" +
			"#include <stdio.hpp>\n" +
			"context configuration " + TEST_NAME + " {}\n" +
			"implementation {}";
		Parser parser = new Parser(new StringReader(test_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ComponentFile file = parser.getParsedFile();
		
		assertEquals(Component.Type.CONTEXT_CONFIGURATION, file.type);
		assertEquals(TEST_NAME, file.name);
	}
	
	@Test
	public void ConfigurationTest() {
		String test_cnc =
			"#include \"include.h\"\n" +
			"#include <stdio.hpp>\n" +
			"configuration " + TEST_NAME + " {}\n" +
			"implementation {}";
		Parser parser = new Parser(new StringReader(test_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ComponentFile file = parser.getParsedFile();
		
		assertEquals(Component.Type.CONFIGURATION, file.type);
		assertEquals(TEST_NAME, file.name);
	}

}

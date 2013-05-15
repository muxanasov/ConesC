// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import core.Coords;
import core.Function;
import core.Variable;

public class FunctionTest {
	
	int TEST_START_LINE = 1;
	int TEST_START_COLUMN = 2;
	int TEST_END_LINE = 3;
	int TEST_END_COLUMN = 4;
	String TEST_TYPE = "TEST_TYPE";
	String TEST_LEXEME = "TEST_LEXEME";
	String TEST_NAME = "TEST_NAME";
	int TEST_LENGTH = 10;
	String TEST_FUNCTION_NAME = "TEST_FUNCTION_NAME";
	String TEST_RETURN_TYPE = "TEST_RETURN_TYPE";

	@Test
	public void testEquals() {
		Function func1 = new Function();
		func1.bodyCoords = new Coords(TEST_START_LINE,
									 TEST_START_COLUMN,
									 TEST_END_LINE,
									 TEST_END_COLUMN);
		func1.definitionCoords = new Coords(TEST_START_LINE,
										  TEST_START_COLUMN,
										  TEST_END_LINE,
										  TEST_END_COLUMN);
		func1.name = TEST_FUNCTION_NAME;
		func1.returnType = TEST_RETURN_TYPE;
		
		for (int i = 0; i < TEST_LENGTH; i++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + i;
			var.name = TEST_NAME + i;
			var.type = TEST_TYPE + i;
			func1.variables.add(var);
		}
		
		Function func2 = new Function();
		func2.bodyCoords = new Coords(TEST_START_LINE,
									 TEST_START_COLUMN,
									 TEST_END_LINE,
									 TEST_END_COLUMN);
		func2.definitionCoords = new Coords(TEST_START_LINE,
										  TEST_START_COLUMN,
										  TEST_END_LINE,
										  TEST_END_COLUMN);
		func2.name = TEST_FUNCTION_NAME;
		func2.returnType = TEST_RETURN_TYPE;
		
		for (int i = 0; i < TEST_LENGTH; i++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + i;
			var.name = TEST_NAME + i;
			var.type = TEST_TYPE + i;
			func2.variables.add(var);
		}
		
		assertEquals(func1, func2);
	}
	
	@Test
	public void testContains() {
		List<Function> funcs = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Function func = new Function();
			func.bodyCoords = new Coords(TEST_START_LINE,
										 TEST_START_COLUMN,
										 TEST_END_LINE,
										 TEST_END_COLUMN);
			func.definitionCoords = new Coords(TEST_START_LINE,
											  TEST_START_COLUMN,
											  TEST_END_LINE,
											  TEST_END_COLUMN);
			func.name = TEST_FUNCTION_NAME + i;
			func.returnType = TEST_RETURN_TYPE + i;
			
			for (int j = 0; j < TEST_LENGTH; j++) {
				Variable var = new Variable();
				var.lexeme = TEST_LEXEME + j;
				var.name = TEST_NAME + j;
				var.type = TEST_TYPE + j;
				func.variables.add(var);
			}
			funcs.add(func);
		}
		
		Function func1 = new Function();
		func1.bodyCoords = new Coords(TEST_START_LINE,
									 TEST_START_COLUMN,
									 TEST_END_LINE,
									 TEST_END_COLUMN);
		func1.definitionCoords = new Coords(TEST_START_LINE,
										  TEST_START_COLUMN,
										  TEST_END_LINE,
										  TEST_END_COLUMN);
		func1.name = TEST_FUNCTION_NAME + 0;
		func1.returnType = TEST_RETURN_TYPE + 0;
		
		for (int j = 0; j < TEST_LENGTH; j++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + j;
			var.name = TEST_NAME + j;
			var.type = TEST_TYPE + j;
			func1.variables.add(var);
		}
		
		Function func2 = new Function();
		func2.bodyCoords = new Coords(TEST_START_LINE,
									 TEST_START_COLUMN,
									 TEST_END_LINE,
									 TEST_END_COLUMN);
		func2.definitionCoords = new Coords(TEST_START_LINE,
										  TEST_START_COLUMN,
										  TEST_END_LINE,
										  TEST_END_COLUMN);
		func2.name = TEST_FUNCTION_NAME + 0;
		func2.returnType = TEST_RETURN_TYPE + 0;
		
		assertFalse(func1.equals(func2));
		assertTrue(funcs.contains(func1));
		assertFalse(funcs.contains(func2));
	}
	
	@Test
	public void testListEquivalence() {
		List<Function> funcs1 = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Function func = new Function();
			func.bodyCoords = new Coords(TEST_START_LINE,
										 TEST_START_COLUMN,
										 TEST_END_LINE,
										 TEST_END_COLUMN);
			func.definitionCoords = new Coords(TEST_START_LINE,
											  TEST_START_COLUMN,
											  TEST_END_LINE,
											  TEST_END_COLUMN);
			func.name = TEST_FUNCTION_NAME + i;
			func.returnType = TEST_RETURN_TYPE + i;
			
			for (int j = 0; j < TEST_LENGTH; j++) {
				Variable var = new Variable();
				var.lexeme = TEST_LEXEME + j;
				var.name = TEST_NAME + j;
				var.type = TEST_TYPE + j;
				func.variables.add(var);
			}
			funcs1.add(func);
		}
		
		List<Function> funcs2 = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Function func = new Function();
			func.bodyCoords = new Coords(TEST_START_LINE,
										 TEST_START_COLUMN,
										 TEST_END_LINE,
										 TEST_END_COLUMN);
			func.definitionCoords = new Coords(TEST_START_LINE,
											  TEST_START_COLUMN,
											  TEST_END_LINE,
											  TEST_END_COLUMN);
			func.name = TEST_FUNCTION_NAME + i;
			func.returnType = TEST_RETURN_TYPE + i;
			
			for (int j = 0; j < TEST_LENGTH; j++) {
				Variable var = new Variable();
				var.lexeme = TEST_LEXEME + j;
				var.name = TEST_NAME + j;
				var.type = TEST_TYPE + j;
				func.variables.add(var);
			}
			funcs2.add(func);
		}
		
		assertEquals(funcs1, funcs2);
	}
	
	@Test
	public void testRemove() {
		List<Function> funcs = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Function func = new Function();
			func.bodyCoords = new Coords(TEST_START_LINE,
										 TEST_START_COLUMN,
										 TEST_END_LINE,
										 TEST_END_COLUMN);
			func.definitionCoords = new Coords(TEST_START_LINE,
											  TEST_START_COLUMN,
											  TEST_END_LINE,
											  TEST_END_COLUMN);
			func.name = TEST_FUNCTION_NAME + i;
			func.returnType = TEST_RETURN_TYPE + i;
			
			for (int j = 0; j < TEST_LENGTH; j++) {
				Variable var = new Variable();
				var.lexeme = TEST_LEXEME + j;
				var.name = TEST_NAME + j;
				var.type = TEST_TYPE + j;
				func.variables.add(var);
			}
			funcs.add(func);
		}
		
		Function func = new Function();
		func.bodyCoords = new Coords(TEST_START_LINE,
									 TEST_START_COLUMN,
									 TEST_END_LINE,
									 TEST_END_COLUMN);
		func.definitionCoords = new Coords(TEST_START_LINE,
										  TEST_START_COLUMN,
										  TEST_END_LINE,
										  TEST_END_COLUMN);
		func.name = TEST_FUNCTION_NAME + 0;
		func.returnType = TEST_RETURN_TYPE + 0;
		
		for (int j = 0; j < TEST_LENGTH; j++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + j;
			var.name = TEST_NAME + j;
			var.type = TEST_TYPE + j;
			func.variables.add(var);
		}
		
		assertTrue(funcs.contains(func));
		assertTrue(funcs.remove(func));
		assertFalse(funcs.contains(func));
	}

}

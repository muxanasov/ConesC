// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package tests;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import core.Variable;

public class VariableTest {
	
	String TEST_TYPE = "TEST_TYPE";
	String TEST_LEXEME = "TEST_LEXEME";
	String TEST_NAME = "TEST_NAME";
	int TEST_LENGTH = 10;

	@Test
	public void testEquals() {
		Variable var1 = new Variable();
		var1.lexeme = TEST_LEXEME;
		var1.name = TEST_NAME;
		var1.type = TEST_TYPE;
		Variable var2 = new Variable();
		var2.lexeme = TEST_LEXEME;
		var2.name = TEST_NAME;
		var2.type = TEST_TYPE;
		
		assertEquals(var1, var2);
	}
	
	@Test
	public void testContatins() {
		List<Variable> vars = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + i;
			var.name = TEST_NAME + i;
			var.type = TEST_TYPE + i;
			vars.add(var);
		}
		
		Variable var1 = new Variable();
		var1.lexeme = TEST_LEXEME;
		var1.name = TEST_NAME;
		var1.type = TEST_TYPE;
		
		Variable var2 = new Variable();
		var2.lexeme = TEST_LEXEME;
		var2.name = TEST_NAME;
		var2.type = TEST_TYPE;
		
		vars.add(var1);
		
		assertTrue(vars.contains(var2));
	}
	
	@Test
	public void testListEquivalence() {
		List<Variable> vars1 = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + i;
			var.name = TEST_NAME + i;
			var.type = TEST_TYPE + i;
			vars1.add(var);
		}
		
		List<Variable> vars2 = new ArrayList<>();
		for (int i = 0; i < TEST_LENGTH; i++) {
			Variable var = new Variable();
			var.lexeme = TEST_LEXEME + i;
			var.name = TEST_NAME + i;
			var.type = TEST_TYPE + i;
			vars2.add(var);
		}
		
		assertEquals(vars1, vars2);
		assertTrue(vars1.equals(vars2));
	}
}

// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.util.ArrayList;
import java.util.List;



public class Function {
	public String returnType = "";
	public String name = "";
	public Coords definitionCoords;
	public Coords bodyCoords;
	public List<Variable> variables = new ArrayList<>();
	
	@Override
	public String toString() {
		String signature = returnType + 
				" " + name + "(";
		for (Variable var : variables) {
			signature += var.type + var.lexeme + " " + var.name;
			int last = variables.size() - 1;
			if (variables.lastIndexOf(var) != last)
				signature += ", ";
		}
		signature += ")";
		return signature;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Function)) return false;
		Function otherFunc = (Function)other;
		return  (otherFunc.name.equals(name))&&
				(otherFunc.returnType.equals(returnType))&&
				(otherFunc.variables.equals(variables));
	}
}

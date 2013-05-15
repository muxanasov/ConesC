// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

public class Variable {
	public String type = "";
	public String lexeme = "";
	public String name = "";
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Variable)) return false;
		Variable otherVar = (Variable)other;
		return  (otherVar.lexeme.equals(lexeme))&&
				(otherVar.name.equals(name))&&
				(otherVar.type.equals(type));
	}
}

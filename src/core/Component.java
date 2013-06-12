// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import parsers.component.ComponentFile;
import parsers.component.ParseException;
import parsers.component.Parser;

public class Component {
	
	private ComponentFile _component;

	public Component(String file_cnc) {
		Parser parser = new Parser(new StringReader(file_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_component  = parser.getParsedFile();
	}
	
	public List<String> getComponents() {
		return new ArrayList<>();
	}
	
	public static class Type{
		public static int UNKNOWN = -1;
		public static int CONTEXT = 0;
		public static int MODULE = 1;
		public static int CONFIGURATION = 2;
		public static int CONTEXT_CONFIGURATION = 3;
	}
}

// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.util.ArrayList;
import java.util.List;

public class ContextsHeader {
	
	static private ArrayList<String> contexts = new ArrayList<>();
	
	static public void reset() {
		contexts = new ArrayList<>();
	}
	
	static public void add(String context) {
		contexts.add(context.toUpperCase());
	}
	
	static public void addAll(List<String> contexts) {
		for (String context : contexts)
			add(context);
	}
	
	static public String buildHeader() {
		String builtHeader = "";
		
		builtHeader += "#ifndef CONTEXT_H\n" +
				"#define CONTEXT_H\n" +
				"typedef enum {";
		for (String context : contexts)
			builtHeader += "\n  " + context + ",";
		
		builtHeader = builtHeader.substring(0, builtHeader.length()-1);
		builtHeader += "\n} context_t;\n#endif";
		
		return builtHeader;
	}

	public static void addAll(List<String> contexts2, String name) {
		for (String context : contexts2)
			add(context + name);
	}

}

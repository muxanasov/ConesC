// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContextsHeader {
	
	static private HashMap<String, ArrayList<String> > contexts = new HashMap<>();
	
	static public void reset() {
		contexts = new HashMap< String, ArrayList<String> >();
	}
	
	static public void add(String group, String context) {
		if (!contexts.containsKey(group)) contexts.put(group, new ArrayList<String>());
		if (contexts.get(group).contains(context.toUpperCase())) return;
		contexts.get(group).add(context.toUpperCase());
	}
	
	static public void addAll(String group, List<String> contexts) {
		for (String context : contexts)
			add(group, context);
	}
	
	static public String buildHeader() {
		String builtHeader = "";
		
		builtHeader += "#ifndef CONTEXT_H\n" +
				"#define CONTEXT_H\n" +
				"typedef enum {";
		int i = 1;
		for (String group : contexts.keySet())
			for (String context : contexts.get(group))
				builtHeader += "\n  " + context + " = " + (i++)*100 + contexts.get(group).indexOf(context) + ",";
		
		builtHeader = builtHeader.substring(0, builtHeader.length()-1);
		builtHeader += "\n} context_t;\n#endif";
		
		return builtHeader;
	}
	
	public static void generateAndWrite() {
		FileManager.fwrite("Contexts.h", buildHeader());
	}
/*
	public static void addAll(List<String> contexts2, String name) {
		for (String context : contexts2)
			add(context + name);
	}
	*/
	public static void delete() {
		FileManager.delete("Contexts.h");
	}

}

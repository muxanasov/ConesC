// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ComponentFile {
	
	public ComponentFile() {
		interfaces.put("uses", new ArrayList<String>());
		interfaces.put("provides", new ArrayList<String>());
		interfaces.put("transitions", new ArrayList<String>());
		interfaces.put("triggers", new ArrayList<String>());
		functions.put("command", new ArrayList<Function>());
		functions.put("event", new ArrayList<Function>());
		functions.put("layered", new ArrayList<Function>());
	}
	
	public ArrayList<String> bodies = new ArrayList<String>();
	
	public int type = Component.Type.UNKNOWN;
	public String name = "unknown";
	
	public Coords nameCoords;

	public Coords declarationCoords;
	public Coords implementationCoords;

	public HashMap<String, List<Function> > functions = new HashMap<>();
	public HashMap<String, List<String>> interfaces = new HashMap<>();
	public HashMap<String, List<String>> transitions = new HashMap<>();
	
	public List<String> usedGroups = new ArrayList<>();
	public List<String> includes = new ArrayList<>();
	
	public List<String> components = new ArrayList<>();
	public List<String> contexts = new ArrayList<>();
	public List<String> contextGroups = new ArrayList<>();
	public String defaultContext = "";
	public String errorContext = "";
	
	public HashMap<String, String> wires = new HashMap<>();
	
	public HashMap<String, ArrayList<String>> equality = new HashMap<>();
	public String safe = "";

}

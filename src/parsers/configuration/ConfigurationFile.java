// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package parsers.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.Component;
import core.Function;


public class ConfigurationFile {
	
	public HashMap<String, List<String>> interfaces = new HashMap<>();

	public ConfigurationFile(){
		interfaces.put("uses", new ArrayList<String>());
		interfaces.put("provides", new ArrayList<String>());
	}
	
	public int type = Component.Type.UNKNOWN;
	public String name = "";
	
	public List<String> components = new ArrayList<>();
	public List<String> contexts = new ArrayList<>();
	public List<String> contextGroups = new ArrayList<>();
	public String defaultContext = "";
	public String errorContext = "";
	
	public HashMap<String, String> wires = new HashMap<>();
	
	public ArrayList<Function> functions = new ArrayList<>();
	
	public HashMap<String, String> equality = new HashMap<>();

}

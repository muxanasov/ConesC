// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parsers.component.ParseException;
import parsers.component.Parser;

public class Component {
	
	protected ComponentFile _file = null;
	protected String _file_cnc = null;
	protected HashMap<String, String> _generatedFiles = new HashMap<>();
	protected HashMap<String,Component> _components = new HashMap<String,Component>();
	protected ArrayList<String> _componentsNames = new ArrayList<String>();
	protected FileManager _fm = null;
	protected String[] _sourceFileArray = null;
	
	protected boolean _isValid = false;

	public Component(FileManager fm, String filename) {
		_fm = fm;
		_file_cnc = _fm.findAndRead(filename + ".cnc");
		if (_file_cnc == null) return;
		Parser parser = new Parser(new StringReader(_file_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_file  = parser.getParsedFile();
		
		_isValid = true;
	}
	
	public void write() {
		for (String key : _generatedFiles.keySet())
			FileManager.fwrite(key, _generatedFiles.get(key));
	}
	
	public void writeRecursively() {
		write();
		for (Component component : _components.values())
			component.writeRecursively();
	}
	
	public void build() {
	}
	
	public void parse() {
	}
	
	public int getType() {
		return _file.type;
	}
	
	public String getName() {
		return _file.name;
	}
	
	public int getNumberOf(String str) {
		if (_sourceFileArray == null) return 0;
		Pattern p = Pattern.compile(str);
		for (int i = 0; i < _sourceFileArray.length; i++) 
			if (p.matcher(_sourceFileArray[i]).find())
			    return i;
		return 0;
	}
	
	public boolean isValid() {
		return _isValid;
	}
	
	protected void parseComponents() {
		for (String name : getComponentsNames()){
			Component component = new Component(_fm, name);
			if (!component.isValid()) {
				if (name.equals("Error")) {
					_file.contexts.add(name);
					continue;
				}
				if(!_file.components.contains(name))_file.components.add(name);
				continue;
			}
			switch(component.getType()){
			case Component.Type.MODULE:
				component = new Module(_fm, name);
				if(!_file.components.contains(name))_file.components.add(name);
				break;
			case Component.Type.CONTEXT:
				if (this.getType() != Component.Type.CONTEXT_CONFIGURATION)
					Print.error(getName() + ".cnc " + getNumberOf("(,\\s+|\\s+)" + name + "(\\s*,|\\s*;|\\s*)"),
							"Context " + name + " should be declared only in a context configuration!");
				component = new Context(_fm, name, this);
				if(!_file.contexts.contains(name))_file.contexts.add(name);
				break;
			case Component.Type.CONFIGURATION:
				component = new Configuration(_fm, name);
				if(!_file.components.contains(name))_file.components.add(name);
				break;
			case Component.Type.CONTEXT_CONFIGURATION:
				component = new ContextConfiguration(_fm, name);
				if(!_file.contextGroups.contains(name))_file.contextGroups.add(name);
				break;
			default:
				component = null;
			}
			if (component == null) continue;
			component.parse();
			_components.put(component.getName(), component);
		}
	}
	
	public boolean hasLayeredFunctions() {
		return false;
	}
	
	public Component find(String name) {
		Component result = null;
		for (Component component : getComponents().values()) {
			if (component.getName().equals(name)) return component;
			result = component.find(name);
			if (result != null) return result;
		}
		return result;
	}
	
	public ArrayList<String> getComponentsNames() {
		if (!_componentsNames.isEmpty()) return _componentsNames;
		_componentsNames.addAll(_file.components);
		_componentsNames.addAll(_file.contextGroups);
		_componentsNames.addAll(_file.contexts);
		_file.components.clear();
		_file.contextGroups.clear();
		_file.contexts.clear();
		return _componentsNames;
	}
	
	public HashMap<String,Component> getComponents() {
		return _components;
	}
	
	public HashMap<String, String> getGeneratedFiles() {
		return _generatedFiles;
	}
	
	public void buildRecursively() {
		build();
		for (Component component : _components.values())
			component.buildRecursively();
	}
	
	public String toString() {
		String tree = getName() + "\n";
		for (Component component : _components.values())
			tree += component.toString(1);
		return tree;
	}
	
	public String toString(int level) {
		String tree = "";
		for (int i = 0; i < level-1; i++)
			tree += "| ";
		tree += "|-" + getName() + "\n";
		for (Component component : _components.values())
			tree += component.toString(level+1);
		return tree;
	}
	
	public static class Type{
		public static final int UNKNOWN = -1;
		public static final int CONTEXT = 0;
		public static final int MODULE = 1;
		public static final int CONFIGURATION = 2;
		public static final int CONTEXT_CONFIGURATION = 3;
	}
}

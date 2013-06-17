// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import parsers.component.ParseException;
import parsers.component.Parser;

public class Component {
	
	protected ComponentFile _file = null;
	protected String _file_cnc = null;
	protected HashMap<String, String> _generatedFiles = new HashMap<>();
	protected ArrayList<Component> _components = new ArrayList<Component>();
	protected ArrayList<String> _componentsNames = new ArrayList<String>();
	protected FileManager _fm = null;
	
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
		for (Component component : _components)
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
			_components.add(component);
		}
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
	
	public ArrayList<Component> getComponents() {
		return _components;
	}
	
	public HashMap<String, String> getGeneratedFiles() {
		return _generatedFiles;
	}
	
	public void buildRecursively() {
		build();
		for (Component component : _components)
			component.buildRecursively();
	}
	
	public String toString() {
		String tree = getName() + "\n";
		for (Component component : _components)
			tree += component.toString(1);
		return tree;
	}
	
	public String toString(int level) {
		String tree = "";
		for (int i = 0; i < level-1; i++)
			tree += "| ";
		tree += "|-" + getName() + "\n";
		for (Component component : _components)
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

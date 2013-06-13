// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.module.ModuleFile;
import parsers.module.ParseException;
import parsers.module.Parser;

public class Context extends Module{
	
	private ModuleFile _file = null;
	private String[] _sourceFileArray = null;
	private String _builtContext = "";
	
	private HashMap<String, List<Function>> _defaultFunctions = new HashMap<>();
	private List<Function> _layeredFunctions = new ArrayList<>();
	private HashMap<String, List<String>> _deafultDeclaration = new HashMap<>();
	
	private Function _activated = new Function();
	private Function _deactivated = new Function();
	private Function _check = new Function();
	
	private String _group = "";
	private List<Function> _defaultEvents = new ArrayList<>();
	
	public Context(String contextGroup, String contextFile, ArrayList<Function> layered) {
		super(contextFile);
		init(contextGroup, contextFile, layered);
	}
	
	public Context(String contextGroup, String contextFile) {
		super(contextFile);
		init(contextGroup, contextFile, new ArrayList<Function>());
	}
	
	private void init(String contextGroup, String contextFile, ArrayList<Function> layered) {
		
		_sourceFileArray = contextFile.split("\n");
		_layeredFunctions = layered;
		
		_group = contextGroup;
		
		_activated.name = "activated";
		_activated.returnType = "void";
		
		_deactivated.name = "deactivated";
		_deactivated.returnType = "void";
		
		_check.name = "check";
		_check.returnType = "bool";
		
		_defaultFunctions.put("event", new ArrayList<Function>());
		_defaultFunctions.put("command", new ArrayList<Function>());
		_defaultFunctions.put("layered", _layeredFunctions);
		
		_defaultFunctions.get("event").add(_activated);
		_defaultFunctions.get("event").add(_deactivated);
		_defaultFunctions.get("command").add(_check);
		
		_deafultDeclaration.put("provides", new ArrayList<String>());
		_deafultDeclaration.put("uses", new ArrayList<String>());
		
		_deafultDeclaration.get("provides").add("ContextCommands as Command");
		_deafultDeclaration.get("provides").add(_group + "Layer as Layered");
		_deafultDeclaration.get("uses").add("ContextEvents as Event");
		
		Parser parser = new Parser(new StringReader(contextFile));
		
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_file = parser.getParsedFile();
		
		for (String group : _file.usedGroups) {
			Function function = new Function();
			function.name = group + ".contextChanged";
			function.returnType = "void";
			Variable var = new Variable();
		    var.name = "con";
		    var.type = "context_t";
		    function.variables.add(var);
		    if (!_file.functions.get("event").contains(function)) {
		    	_defaultEvents.add(function);
		    }
		}
	}
	
	public String build() throws Exception {
		String module = buildModule();
		return super.build(module);
	}

	public String buildModule() throws Exception {
		// if _file is null
		// if _file.type is not Component.CONTEXT
		
		// building includes
		for (String include : _file.includes)
			_builtContext += "#include " + include + "\n";
		
		// building the name
		_builtContext += "module " + _file.name + _group +"Context {\n";
		
		// building declaration section
		for (String key : _file.interfaces.keySet())
			if (!key.equals("transition")&&!key.equals("triggers"))
				for (String elem : _file.interfaces.get(key))
					_builtContext += "  " + key + " interface " + elem + ";\n";
		for (String key : _deafultDeclaration.keySet())
			for (String elem : _deafultDeclaration.get(key))
				_builtContext += "  " + key + " interface " + elem + ";\n";
		
		for (String group : _file.usedGroups)
			_builtContext += "  uses context group " + group + ";\n";
		
		// building implementation section
		_builtContext += "}\nimplementation {\n";
		
		// building implementation body
		
		// building user's functions
		for (String key : _file.functions.keySet())
			for (Function function : _file.functions.get(key)) {
				String firstName = "";
				// if default or layered function has been overridden
				// then we don't have to build it later
				// so delete it from _defaultFunctions
				if (_defaultFunctions.containsKey(key) &&
					_defaultFunctions.get(key).remove(function))
					firstName = Character.toUpperCase(key.charAt(0)) +
						key.substring(1) + ".";
				
				// building signature
				String fType = key;
				if (fType.equals("layered")) fType = "command";
				_builtContext += "  " + fType + " " + function.returnType + 
						" " + firstName + function.name + "(";
				for (Variable var : function.variables) {
					_builtContext += var.type + var.lexeme + " " + var.name;
					int last = function.variables.size() - 1;
					if (function.variables.lastIndexOf(var) != last)
						_builtContext += ", ";
				}
				_builtContext += ") {\n";
				
				// building body
				for (int i = function.bodyCoords.beginLine; 
						 i < function.bodyCoords.endLine - 1; i++)
					_builtContext += _sourceFileArray[i] + "\n";
				_builtContext += "  }\n";
			}
		
		// if not all of the layered function are implemented
		// then throw an exception
		if (_defaultFunctions.get("layered").size() > 0) {
			String msg = "In context " + _file.name + ":\n";
			for (Function function : _defaultFunctions.get("layered"))
				msg += function + " is not implemented!\n";
			throw new Exception(msg);
		}
		
		// building missing standard functions
		for (String key : _defaultFunctions.keySet())
			for (Function function : _defaultFunctions.get(key)) {
				// building signature
				String firstName = Character.toUpperCase(key.charAt(0)) +
						key.substring(1);
				_builtContext += "  " + key + " " + function.returnType + 
						" " + firstName + "." + function.name + "(";
				for (Variable var : function.variables) {
					_builtContext += var.type + var.lexeme + " " + var.name;
					int last = function.variables.size() - 1;
					if (function.variables.lastIndexOf(var) != last)
						_builtContext += ", ";
				}
				_builtContext += "){\n";
				
				// building body
				if (function.equals(_check))
					_builtContext += "    return TRUE;\n";
				_builtContext += "  }\n";
			}
		for (Function function : _defaultEvents) {
			// building signature
			
			_builtContext += "  event " + function.returnType + 
					" " + function.name + "(";
			for (Variable var : function.variables) {
				_builtContext += var.type + var.lexeme + " " + var.name;
				int last = function.variables.size() - 1;
				if (function.variables.lastIndexOf(var) != last)
					_builtContext += ", ";
			}
			_builtContext += "){\n  }\n";
		}
		
		_builtContext += "  command void Command.activate() {\n" +
			"    signal Event.activated();\n" +
			"  }\n" +
			"  command void Command.deactivate() {\n" +
			"    signal Event.deactivated();\n" +
			"  }\n";
		
		// end of building
		_builtContext += "}";
		
		return _builtContext;
	}

}

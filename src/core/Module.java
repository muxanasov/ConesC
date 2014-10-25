// Copyright (c) 2013 Mikhail Afanasov and DeepSe group. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package core;

import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parsers.module.ParseException;
import parsers.module.Parser;

public class Module extends Component{

	public Module(FileManager fm, String name) {
		super(fm, name);
	}
	
	@Override
	public void parse() {
		parse(_file_cnc);
	}
	
	@Override
	public void build() {
		parse();
		if ( _lang == Lang.NESC) return;
		buildModule();
	}
	
	protected void parse(String file_cnc) {
		Parser parser = new Parser(new StringReader(file_cnc));
		try {
			System.out.println("Parsing "+getFilename());
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(file_cnc);
		}
		_file  = parser.getParsedFile();
		
		_sourceFileArray  = file_cnc.split("\n");
	}
	
	protected void buildModule() {
		String builtModule = "";
		// adding default include
		_sourceFileArray[0] = "#include \"Contexts.h\"\n" + _sourceFileArray[0];
				
		for (int i = 0; i < _file.declarationCoords.beginLine; i++)
			builtModule += _sourceFileArray[i] + "\n";
				
		// building declarations
		for (String elem : _file.interfaces.get("provides"))
			builtModule += "  provides interface " + elem + ";\n";
		for (String elem : _file.interfaces.get("uses"))
			builtModule += "  uses interface " + elem + ";\n";
				
		// adding context groups
		for (String group : _file.usedGroups) {
			Component grpcomp = _fm.getMainComponent().find(group);
			if (grpcomp == null || grpcomp.getType() != Component.Type.CONTEXT_CONFIGURATION) {
				Print.error(_file.name + ".cnc", 
						"Can not find context group " + group + "!\n");
				continue;
			}
			builtModule += "  uses interface ContextGroup as " + group + "Group;\n";
			if (grpcomp.hasLayeredFunctions()) {
				builtModule += "  uses interface " + group + "Layer;\n";
			}
		}
				
		builtModule += "}\nimplementation {\n";
		
		String implementation = "";
		
		// Check if all the <context_group>.contextChanged() are implemented
		HashMap<String, Boolean> isCCImplemented = new HashMap<String, Boolean>();
		for (String group : _file.usedGroups)
			isCCImplemented.put(group, false);
		
		// I just use a regex here because I'm too lazy...
		// Actually I should use parsed functions and coords from (ModuleFile)_file
		for (int i = _file.implementationCoords.beginLine; i < _sourceFileArray.length; i++) {
			String toAdd = _sourceFileArray[i];
			for (String group : _file.usedGroups) {
				// trying to find activation like
				// activate <group_name>.<context_name>;
				Pattern p = Pattern.compile("activate\\s+" + group + ".(.*?);");
				Matcher m = p.matcher(toAdd);
				if (m.find()) {
				    // if found, change it to
				    // call <group_name>Group.actiavte(<CONTEXT_NAME><GROUP_NAME>);
				    toAdd = toAdd.replaceAll("activate\\s+" + group + ".\\w+;",
				    		"call " + group + "Group.activate(" + 
				    		m.group(1).toUpperCase() + group.toUpperCase() + ");");
				    break;
				}
				// trying to find layered function like
				// call <group_name>.
				p = Pattern.compile("call\\s+" + group + ".");
				m = p.matcher(toAdd);
				if (m.find()) {
				    // if found, change it to
				    // call <group_name>Layer.
					if (toAdd.contains(".getContext()")) {
						toAdd = toAdd.replaceAll("call\\s+" + group + ".",
					    		"call " + group + "Group.");
						toAdd = transformConstatnName(toAdd, group);
					} else
						toAdd = toAdd.replaceAll("call\\s+" + group + ".",
				    		"call " + group + "Layer.");
				    break;
				}
				// trying to find default functions like
				// event void <group_name>.contextChanged(context_t <var_name>)
				p = Pattern.compile("event\\s+void\\s+" + group + 
				    	".contextChanged\\s*\\(\\s*context_t\\s+(.*?)\\s*\\)");
				m = p.matcher(toAdd);
				if (m.find()) {
				    // if found, change it to
				    // event void <group_name>Group.contextChanged(context_t <var_name>)
				    toAdd = toAdd.replaceAll("event\\s+void\\s+" + group + 
					    	".contextChanged\\s*\\(\\s*context_t\\s+\\w+\\s*\\)",
				    		"event void " + group + "Group.contextChanged(context_t " +
					    	m.group(1) + ")");
				    isCCImplemented.put(group,true);
				    break;
				}
				
				// at last trying to find pattern like <group_name>.<context_name>,
				// which means programmer is trying to get the constant name of the context
				toAdd = transformConstatnName(toAdd, group);
			}
			implementation += toAdd + "\n";
		}
		
		for (String key : isCCImplemented.keySet())
			if (!isCCImplemented.get(key)) {
				// if <ContextGroup>.contextChanged() is not implememnted
				// generate it to the beginning of implememntation
				builtModule += "  event void " + key + "Group.contextChanged(context_t con){}\n";
			}	//Print.error(_file.name+".cnc", "event void " + key + ".contextChanged(context_t) is not implemented!\n");
		builtModule += implementation;
		_generatedFiles.put(_file.name + ".nc", builtModule);
	}
	
	private String transformConstatnName(String toAdd, String group) {
		// at last trying to find pattern like <group_name>.<context_name>,
		// which means programmer is trying to get the constant name of the context
		Pattern p = Pattern.compile(group + "\\.(.\\w+)");
		Matcher m = p.matcher(toAdd);
		if (m.find()) {
		    // if found, change it to
		    // <CONTEXT_NAME><GROUP_NAME>.
			toAdd = toAdd.replaceAll(group + "." + m.group(1),
			    		m.group(1).toUpperCase() + group.toUpperCase());
		}
		return toAdd;
	}

}

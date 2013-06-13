package core;

import java.io.StringReader;

import parsers.configuration.ConfigurationFile;
import parsers.configuration.ParseException;
import parsers.configuration.Parser;

public class ContextConfiguration extends Configuration{
	
	private ConfigurationFile _file;

	public ContextConfiguration(String file_cnc) {
		super(file_cnc);
		
		Parser parser = new Parser(new StringReader(file_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_file  = parser.getParsedFile();
		
		if (_file.errorContext.isEmpty() && !_file.contexts.contains("Error"))
			_file.contexts.add("Error");
		ContextsHeader.addAll(_file.contexts, _file.name);
	}
	
	public String buildInterface() {
		String builtInterface = "";
		
		builtInterface += "interface " + _file.name + "Layer {\n";
		for (Function f : _file.functions) {
			builtInterface += "  command " + f.returnType + " " + f.name + "(";
			int last = f.variables.size() - 1;
			for (Variable var : f.variables) {
				builtInterface += var.type + var.lexeme +" " + var.name;
				if (f.variables.lastIndexOf(var) != last)
					builtInterface += ", ";
			}
			builtInterface += ");\n";
		}
		
		builtInterface += "}\n";
		
		return builtInterface;
	}
	
	public String build() {
		String configuration = buildConfiguration();
		return super.build(configuration);
	}
	
	public String buildConfiguration() {
		String builtConf = "";
		
		builtConf += "configuration " + _file.name + "Configuration {\n";
		builtConf += "  provides interface ContextGroup;\n";
		builtConf += "  provides interface " + _file.name + "Layer;\n";
		for (String intrfce : _file.interfaces.get("provides"))
			builtConf += "  provides " + intrfce + ";\n";
		for (String intrfce : _file.interfaces.get("uses"))
			builtConf += "  uses " + intrfce + ";\n";
		builtConf += "}\n";
		
		builtConf += "implementation {\n";
		
		if (!_file.contextGroups.isEmpty())
			builtConf += "  context groups";
		for (String conf : _file.contextGroups)
			builtConf += "\n    " + conf + ",";
		if (!_file.contextGroups.isEmpty())
			builtConf = builtConf.substring(0, builtConf.length() - 1) + ";\n";
		
		builtConf += "  components";
		
		builtConf += "\n    " + _file.name + "Group,";
		
		for (String context : _file.contexts)
			builtConf += "\n    " + context + _file.name + "Context,";
		for (String component : _file.components)
			builtConf += "\n    " + component + ",";
		builtConf = builtConf.substring(0, builtConf.length()-1);
		builtConf += ";\n";
		
		for (String key : _file.wires.keySet()) {
			String[] splitted_key = key.split("\\.");
			if (!_file.contexts.contains(splitted_key[0])) {
				builtConf += "  " + key + " -> " + _file.wires.get(key) + ";\n";
			} else {
				builtConf += "  " + splitted_key[0] + _file.name + "Context." + 
								splitted_key[1] + " -> " + _file.wires.get(key) + ";\n";
			}
		}
		
		for (String context : _file.contexts) {
			builtConf += "  " + _file.name + "Group." + context + _file.name + "Context -> " +
						 context + _file.name + "Context;\n";
			builtConf += "  " + _file.name + "Group." + context + _file.name + "Layer -> " +
					 context + _file.name + "Context;\n";
		}
		
		builtConf += "  ContextGroup = " + _file.name + "Group;\n";
		builtConf += "  " + _file.name + "Layer = " + _file.name + "Group;\n";
		
		for (String key : _file.equality.keySet())
			builtConf += "  " + key + " = " + _file.equality.get(key) + ";\n";
		
		builtConf += "}\n";
		
		Configuration conf = new Configuration(builtConf);
		builtConf = conf.build();
		
		return builtConf;
	}
	
	public String buildErrorContext() {
		if (!_file.errorContext.isEmpty()) return "";
		
		String errorContext_cnc =
			"context Error {\n" +
			"}\n" +
			"implementation{\n" +
			"}\n";
		
		String builtErrorContext = "";
		
		Context errorContext = new Context(_file.name, errorContext_cnc);
		try {
			builtErrorContext = errorContext.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return builtErrorContext;
	}
	
	public String buildGroup(){
		String builtGroup = "";
		
		builtGroup += "#include \"Contexts.h\"\n" +
			"module " + _file.name + "Group {\n" +
			"  provides interface ContextGroup as Group;\n" +
			"  provides interface " + _file.name + "Layer as Layer;\n";
		
		for (String context : _file.contexts)
			builtGroup += "  uses interface ContextCommands as " + context + _file.name + "Context;\n" +
				"  uses interface " + _file.name + "Layer as " + context + _file.name + "Layer;\n";
		builtGroup += "}\nimplementation {\n";
		
		builtGroup += "  context_t context = " + _file.defaultContext.toUpperCase() + _file.name.toUpperCase() + ";\n";
		
		// building deactivate function, which is always called before context activation
		builtGroup += "  void deactivate() {\n" +
			"    switch (context) {\n";
		for (String context : _file.contexts)
			builtGroup += "      case " + context.toUpperCase() + _file.name.toUpperCase() + ":\n" +
					"        call " + context + _file.name + "Context.deactivate();\n" +
					"        break;\n";
		builtGroup += "      default:\n" +
					  "        break;\n" +
					  "    }\n" +
					  "  }\n";
		
		// building transitionIsPossible(), which is called to check id transition is possible
		builtGroup += "  bool transitionIsPossible(context_t con) {\n" +
					  "    switch (context) {\n";
		for (String context : _file.contexts) {
			if (_file.errorContext.isEmpty() && context.equals("Error")) continue;
			builtGroup += "      case " + context.toUpperCase() + _file.name.toUpperCase() + ":\n" +
				"        return call " + context + _file.name + "Context.transitionIsPossible(con);\n";
		}
		builtGroup += "      default:\n" +
				  "        return FALSE;\n" +
				  "    }\n" +
				  "  }\n";
		
		// building activate()
		builtGroup += "  command void Group.activate(context_t con) {\n" +
					  "    if (!transitionIsPossible(con)) {\n"+
					  "      deactivate();\n";
		if (_file.errorContext.isEmpty())
			builtGroup += "      call Error" + _file.name + "Context.activate();\n" +
		                  "      context = ERROR" + _file.name.toUpperCase() + ";\n" +
					  	  "      signal Group.contextChanged(ERROR" + _file.name.toUpperCase() + ");\n";
		else 
			builtGroup += "      call " + _file.errorContext + _file.name + "Context.activate();\n" +
		                  "      context = " + _file.errorContext.toUpperCase() + _file.name.toUpperCase() + ";\n" +
						  "      signal Group.contextChanged(" + _file.errorContext.toUpperCase() + _file.name.toUpperCase() + ");\n";
		builtGroup += "      return;\n" +
					  "    }\n";
		builtGroup += "    switch (con) {\n";
		
		for (String context : _file.contexts) {
			if (_file.errorContext.isEmpty() && context.equals("Error")) continue;
			builtGroup += "      case " + context.toUpperCase() + _file.name.toUpperCase() + ":\n" +
		        "        if (!call " + context + _file.name + "Context.check()) return;\n" +
				"        deactivate();\n" +
				"        call " + context + _file.name + "Context.activate();\n" +
				"        context = " + context.toUpperCase() + _file.name.toUpperCase() + ";\n" +
				"        break;\n";
		}
		
		builtGroup += "      default:\n" +
					  "        deactivate();\n";
		if (_file.errorContext.isEmpty())
			builtGroup += "        call Error" + _file.name + "Context.activate();\n" +
					  "        context = ERROR" + _file.name.toUpperCase() + ";\n" +
					  "        signal Group.contextChanged(ERROR" + _file.name.toUpperCase() + ");\n";
		else
			builtGroup += "        call " + _file.errorContext + _file.name + "Context.activate();\n" +
						  "        context = " + _file.errorContext.toUpperCase() + _file.name.toUpperCase() + ";\n" +
						  "        signal Group.contextChanged(" + _file.errorContext.toUpperCase() + _file.name.toUpperCase() + ");\n";
		builtGroup += "        return;\n" +
				  "    }\n" +
				  "    call Group.contextChanged(con);\n" + 
				  "  }\n";
		
		// building layered functions
		for (Function f : _file.functions) {
			builtGroup += "  command " + f.returnType + " Layer." + f.name + "(";
			int last = f.variables.size() - 1;
			for (Variable var : f.variables) {
				builtGroup += var.type + var.lexeme +" " + var.name;
				if (f.variables.lastIndexOf(var) != last)
					builtGroup += ", ";
			}
			builtGroup += ") {\n";
			
			builtGroup += "    switch (context) {\n";
			
			for (String context : _file.contexts) {
				if (_file.errorContext.isEmpty() && context.equals("Error")) continue;
				builtGroup += "      case " + context.toUpperCase() + _file.name.toUpperCase() + ":\n" +
			        "        call " + context + _file.name + "Layer." + f.name + "(";
				last = f.variables.size() - 1;
				for (Variable var : f.variables) {
					builtGroup += var.name;
					if (f.variables.lastIndexOf(var) != last)
						builtGroup += ", ";
				}
				builtGroup += ");\n";
				builtGroup += "        break;\n";
			}
			
			builtGroup += "      default:\n" +
						  "        break;\n" +
						  "    }\n" +
						  "  }\n";
		}
		
		builtGroup += "}\n";
		
		return builtGroup;
	}

}

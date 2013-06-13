package core;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import parsers.configuration.ConfigurationFile;
import parsers.configuration.ParseException;
import parsers.configuration.Parser;

public class Configuration extends Component{
	
	private ConfigurationFile _file = null;
	private String _file_cnc = "";

	public Configuration(String file_cnc) {
		super(file_cnc);
		
		_file_cnc = file_cnc;
	}
	
	private void parse(String file_cnc) {
		if (_file != null) return;
		
		Parser parser = new Parser(new StringReader(file_cnc));
		try {
			parser.parse();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_file  = parser.getParsedFile();
	}
	
	public String build() {
		return build(_file_cnc);
	}
	
	public List<String> getComponents() {
		ArrayList<String> components = new ArrayList<>();
		components.addAll(_file.components);
		components.addAll(_file.contextGroups);
		components.addAll(_file.contexts);
		return components;
	}
	
	public String build(String file_cnc) {
		parse(file_cnc);
		String builtConf = "";
		
		builtConf += "configuration " + _file.name + " {\n";
		for (String key : _file.interfaces.keySet())
			if(key.equals("uses")||key.equals("provides"))
				for (String elem : _file.interfaces.get(key))
					builtConf += "  " + key + " interface " + elem + ";\n";
		builtConf += "}\nimplementation {\n";
		
		builtConf += "  components";
		for (String group : _file.contextGroups)
			builtConf += "\n    " + group + "Configuration,";
		for (String component : _file.components)
			builtConf += "\n    " + component + ",";
		builtConf = builtConf.substring(0, builtConf.length() - 1);
		builtConf += ";\n";
		
		for (String key : _file.wires.keySet()) {
			if (_file.contextGroups.contains(_file.wires.get(key)))
				builtConf += "  " + key + "Group -> " + _file.wires.get(key) + "Configuration;\n" +
					"  " + key + "Layer -> " + _file.wires.get(key) + "Configuration;\n";
			else
				builtConf += "  " + key + " -> " + _file.wires.get(key) + ";\n";
		}
		
		for (String key : _file.equality.keySet())
			builtConf += "  " + key + " = " + _file.equality.get(key) + ";\n";
		
		builtConf += "}";
		
		return builtConf;
	}

}

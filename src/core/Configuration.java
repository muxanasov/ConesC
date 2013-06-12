package core;

import java.io.StringReader;

import parsers.configuration.ConfigurationFile;
import parsers.configuration.ParseException;
import parsers.configuration.Parser;

public class Configuration extends Component{
	
	private ConfigurationFile _file;

	public Configuration(String file_cnc) {
		super(file_cnc);
		
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

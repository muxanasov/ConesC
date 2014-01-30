package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Translator {
	
	FileManager _fm = null;
	String[] _args = null;
	boolean _clean = true;
	String _cmd = "make";

	public Translator(String[] args) {
		_fm = new FileManager();
		Print.init(Print.LogLevel.LOG_ERRORS);
		for (int i = 0; i < args.length; i++)
			if (args[i].toString().equals("-v"))
				_clean = false;
			else if (args[i].toString().equals("-DEBUG"))
				Print.init(Print.LogLevel.LOG_DEBUG);
			else if (args[i].toString().equals("-SILENT"))
				Print.init(Print.LogLevel.LOG_NOTHING);
			else if (args[i].toString().equals("-INFO"))
				Print.init(Print.LogLevel.LOG_INFO);
			else
				_cmd += " " + args[i];
	}
	
	public void translate() {
		Component mainComponent = _fm.getMainComponent();
		mainComponent.buildRecursively();
		mainComponent.writeRecursively();
		DefaultFiles.generateAndWrite();
		ContextsHeader.generateAndWrite();
	}
	
	public void compile() {
		try {
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec(_cmd);
			pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line=buf.readLine())!=null) {
				System.out.println(line);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void make() {
		translate();
		compile();
		if (_clean) clean();
	}

	public void clean() {
		_fm.getMainComponent().deleteRecursively();
		DefaultFiles.delete();
		ContextsHeader.delete();
	}
	
}

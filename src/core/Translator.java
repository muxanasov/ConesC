package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Translator {
	
	FileManager _fm = null;
	String[] _args = null;
	boolean _clean = true;

	public Translator(String[] args) {
		_fm = new FileManager();
		_args = args;
	}
	
	public void translate() {
		Component mainComponent = _fm.getMainComponent();
		mainComponent.buildRecursively();
		mainComponent.writeRecursively();
		DefaultFiles.generateAndWrite();
		ContextsHeader.generateAndWrite();
	}
	
	public void compile() {
		String cmd = "make";
		for (int i = 0; i < _args.length; i++)
			if (_args[i].toString().equals("-v"))
				_clean = false;
			else
			    cmd += " " + _args[i];
		try {
			Runtime run = Runtime.getRuntime();
			Process pr = run.exec(cmd);
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

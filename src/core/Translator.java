package core;

public class Translator {
	
	FileManager _fm = null;

	public Translator(String[] args) {
		_fm = new FileManager();
	}
	
	public void translate() {
		Component mainComponent = _fm.getMainComponent();
		mainComponent.buildRecursively();
		mainComponent.writeRecursively();
		DefaultFiles.generateAndWrite();
		ContextsHeader.generateAndWrite();
	}
	
	public void compile() {
		
	}
	
	public void make() {
		translate();
		compile();
	}
	
}

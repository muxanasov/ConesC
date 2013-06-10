package core;

import java.util.ArrayList;
import java.util.List;

public class ContextsHeader {
	
	static private ArrayList<String> contexts = new ArrayList<>();
	
	static public void add(String context) {
		contexts.add(context.toUpperCase());
	}
	
	static public void addAll(List<String> contexts) {
		for (String context : contexts)
			add(context);
	}
	
	static public String buildHeader() {
		return "";
	}

}

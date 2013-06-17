package core;

public class DefaultFiles {
	
	public static void generateAndWrite() {
		String contextCommands =
			"#include \"Contexts.h\"\n" +
			"interface ContextCommands {\n" +
			"  command bool check();\n" +
			"  command void activate();\n" +
			"  command void deactivate();\n" +
			"  command bool transitionIsPossible(context_t con);\n" +
			"}";
		String contextEvents = 
			"interface ContextEvents {\n" +
			"  event void activated();\n" +
			"  event void deactivated();\n" +
			"}";
		FileManager.fwrite("ContextCommands.nc", contextCommands);
		FileManager.fwrite("ContextEvents.nc", contextEvents);
	}

}

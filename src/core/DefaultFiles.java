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
			"  command bool conditionsAreSatisfied(context_t to, context_t cond);\n" +
			"}";
		String contextEvents = 
			"interface ContextEvents {\n" +
			"  event void activated();\n" +
			"  event void deactivated();\n" +
			"}";
		String contextGroup =
			"#include \"Contexts.h\"\n" +
			"interface ContextGroup {\n" +
			"  event void contextChanged(context_t con);\n" +
			"  command void activate(context_t con);\n" +
			"  command context_t getContext();\n" +
			"}";	
		FileManager.fwrite("ContextCommands.nc", contextCommands);
		FileManager.fwrite("ContextEvents.nc", contextEvents);
		FileManager.fwrite("ContextGroup.nc", contextGroup);
	}

}

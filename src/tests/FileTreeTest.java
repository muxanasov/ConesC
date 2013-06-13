package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import core.FileManager;
import core.FileTreeElem;
import core.Function;

public class FileTreeTest {
	
	String TEST_TREE =
		"App\n" +
		"|-Component00\n" +
		"|-Component10\n" +
		"|-Group0\n" +
		"| |-Component00\n" +
		"| |-Component01\n" +
		"| |-Context00\n" +
		"| |-Context01\n" +
		"| |-Context02\n" +
		"| |-unknown\n" +
		"|-Group1\n" +
		"| |-Component10\n" +
		"| |-Component11\n" +
		"| |-Group2\n" +
		"| | |-Component20\n" +
		"| | |-Component21\n" +
		"| | |-Context20\n" +
		"| | |-Context21\n" +
		"| | |-Context22\n" +
		"| | |-unknown\n" +
		"| |-Context10\n" +
		"| |-Context11\n" +
		"| |-Context12\n" +
		"| |-unknown\n";

	
	int GROUPS = 3;
	int CONTEXTS_PER_GROUP = 3;
	int COMPONENTS_PER_GROUP = 2;

	@Before
	public void setUp() throws Exception {
		for (int i = 0; i < GROUPS; i++){
			String group = "context configuration Group" + i + "{\n" +
					"}\nimplementation {\n" +
					"  contexts";
			for(int j = 0; j < CONTEXTS_PER_GROUP; j++){
				group += "\n    Context" + i + j + ",";
				String context = "context Context" + i + j + "{\n}\n" +
						"implementation {\n}";
				FileManager.fwrite("Context"+i+j+".cnc", context);
			}
			group = group.substring(0, group.length() - 1);
			group += ";\n  components";
			for(int j = 0; j < COMPONENTS_PER_GROUP; j++){
				group += "\n    Component" + i + j + ",";
				String module = "module Component" + i + j + "{\n}\n" +
						"implementation {\n}";
				FileManager.fwrite("Component"+i+j+".cnc", module);
			}
			group = group.substring(0, group.length() - 1);
			group += ";\n";
			if (i == 1)
				group += "  context groups Group2;\n";
			group += "}\n";
			FileManager.fwrite("Group"+i+".cnc", group);
		}
		
		String configuration =
			"configuration App {\n}\nimplementation {\n" +
			"  context groups Group0, Group1;\n" +
			"  components Component00, Component10;\n}";
		FileManager.fwrite("App.cnc", configuration);
		
		String makeFile = 
				"COMPONENT = App\n" +
				"PFLAGS += -I ./interfaces -I ./\n" +
				"include $(MAKERULES)\n";
		FileManager.fwrite("Makefile", makeFile);
	}

	@After
	public void tearDown() throws Exception {
		for (int i = 0; i < GROUPS; i++) {
			File f = new File ("Group"+i+".cnc");
			f.delete();
			for (int j = 0; j < CONTEXTS_PER_GROUP; j++){
				File cf = new File ("Context"+i+j+".cnc");
				cf.delete();
			}
			for (int j = 0; j < COMPONENTS_PER_GROUP; j++){
				File mf = new File ("Component"+i+j+".cnc");
				mf.delete();
			}
		}
		File app = new File ("App.cnc");
		app.delete();
		File makefile = new File ("Makefile");
		makefile.delete();
	}

	@Test
	public void test() {
		FileTreeElem tree = new FileManager().generateTree();
		assertEquals(TEST_TREE, tree.toString());
	}

}

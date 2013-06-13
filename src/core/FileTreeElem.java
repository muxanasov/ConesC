package core;

import java.util.ArrayList;
import java.util.List;

public class FileTreeElem {
	
	private Component _node = null;
	private List<FileTreeElem> _children = new ArrayList<>();
	private String _builtComponent = "";
	
	public FileTreeElem(FileManager fm, String name, String group, ArrayList<Function> layeredFunctions) {
		String source = fm.findAndRead(name+".cnc");
		switch (new Component(source).getType()) {
		case Component.Type.MODULE:
			_node = new Module(source);
			break;
		case Component.Type.CONTEXT:
			_node = new Context(group, source, layeredFunctions);
			break;
		case Component.Type.CONFIGURATION:
			_node = new Configuration(source);
			break;
		case Component.Type.CONTEXT_CONFIGURATION:
			_node = new ContextConfiguration(source);
			break;
		default:
			_node = new Component();
		}
		
		try {
			_builtComponent = _node.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String component : _node.getComponents())
			_children.add(new FileTreeElem(fm, component, _node.getName(), _node.getLayeredFunctions()));
	}
	
	public String getName() {
		return _node.getName();
	}
	
	public String toString() {
		String tree = _node.getName() + "\n";
		for (FileTreeElem child : _children)
			tree += child.toString(1);
		return tree;
	}
	
	public String getBuiltComponent() {
		return _builtComponent;
	}
	
	public String toString(int level) {
		String tree = "";
		for (int i = 0; i < level-1; i++)
			tree += "| ";
		tree += "|-" + _node.getName() + "\n";
		for (FileTreeElem child : _children)
			tree += child.toString(level+1);
		return tree;
	}

}

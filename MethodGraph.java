import java.util.ArrayList;

public class MethodGraph{
	Method root;
	ArrayList<Method> nodes;
	MethodGraph(Method startingMethod) {
		root = startingMethod;
		nodes = new ArrayList<>();
		addNode(root, null);
	}
	
	public void display() {
		for(Method node : nodes) {
			System.out.println(node.getName() + ":");
			System.out.println("\t- called by: ");
			if(node.getCallers().size() > 0) {
				for (Method m : node.getCallers()) {
					System.out.println("\t\t" + m.getName());
				}
			} else {
				System.out.println("\t\tnothing");
			}
			
			System.out.println("\t- calls: ");
			if(node.getCalls().size() > 0) {
				for (Method m : node.getCalls()) {
					System.out.println("\t\t" + m.getName());
				}
			} else {
				System.out.println("\t\tnothing");
			}
		}
	}
	
	public void addNode(Method node, Method callingNode) {
	
		if(node == null) {
			System.out.println("Can't add an empty node");
			return;
		}
	
		if(!nodes.contains(node)) {
			nodes.add(node);
		} else {
			return;
		}
		
		if(callingNode != null && !node.getCallers().contains(callingNode)) {
			node.addCaller(callingNode);
		}
		
		for(Method m : node.getCalls()) {
			addNode(m, node);
		}
	
		return;	
	}

}

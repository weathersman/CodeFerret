import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

abstract public class CodeFerret {
	final String FILE_EXTENSION = ".java";
	String target;
	String startingMethod;
	boolean isFile = false;
	boolean isDir = false;
	ArrayList<Method> methods;

	 static public boolean isFile(String path) {
		if(path == null || path.isEmpty()) {
			return false;
		}
			
		try {
			if (Files.isRegularFile(Paths.get(path))) {
				return true;
			}
		} catch(SecurityException se) {
			System.out.println("isFile() - Security Exception caught:\n" + se.getMessage() );
		}
		return false;
	}
		
	static public boolean isDirectory(String path) {
		if(path == null || path.isEmpty()) {
			return false;
		}
			
		try {
			if(Files.isDirectory(Paths.get(path))) {
				return true;
			}
		} catch(SecurityException se) {
			System.out.println("isDirectory() - SecurityException caught:\n" + se.getMessage() );
		}
		
		return false;
	}		
	
	protected ArrayList<Method> getMethods() { return methods; }
	
	protected void showMethods() {
		if(startingMethod != null && !startingMethod.isEmpty()) {
			Method start = null;
			for(Method method : methods) {
				if (method.getName().equalsIgnoreCase(startingMethod)) {
					start = method;
					break;
				}
			}
			if(start != null) {
				MethodGraph graph = new MethodGraph(start);
				graph.addNode(start, null);
				graph.display();
			}
		} else {
			for (Method method : methods) {
				System.out.println(method.getName() + " calls " + method.displayCalls());
			}
		}
	}

	protected void analyzeMethod(Method theMethod) {
		for(int i = 0; i < theMethod.getLines().size(); i++) {
			String theLine = theMethod.getLines().get(i);
			for(Method method : methods) {
				if(method != theMethod && theLine.contains(method.getName() + "(")) {
					if(! theMethod.getCalls().contains(method)) {
						theMethod.addCall(method);
					}
				}
			}
		}
	}
		
	protected void runAway(String msg) {
		System.out.println(msg);
		System.exit(1);
	}
	
}

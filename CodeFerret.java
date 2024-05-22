import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

abstract public class CodeFerret {
	final String FILE_EXTENSION = ".java";
	String target;
	boolean isFile = false;
	boolean isDir = false;
	ArrayList<Method> methods;

	
	ArrayList<Method> getMethods() { return methods; }
	
	void showMethods() {
		for(Method method : methods) {
			System.out.println(method.getName() + " calls " + method.displayCalls());
		}
	}

	void analyzeMethod(Method theMethod) {
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
		
	void runAway(String msg) {
		System.out.println(msg);
		System.exit(1);
	}
	
}

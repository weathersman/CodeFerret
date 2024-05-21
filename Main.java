import java.nio.file.*;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		String path = null;
		if (args.length > 0) {
   			path = args[0];
		} else {
   			System.err.println("Give me a path or file argument please.");
   			System.exit(1);
		}
		
		CodeFerret ferret = new CodeFerret(path);
		if(ferret.canSniff()) {
			ferret.explore();
		}
	}
}


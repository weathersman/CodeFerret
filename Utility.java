import java.nio.file.Files;
import java.nio.file.Paths;

public class Utility {

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
}

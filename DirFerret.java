import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.*;

public class DirFerret extends CodeFerret {
	private File[] dirFiles;

	DirFerret(String path) {
		this.target = path;
		if(Utility.isDirectory(path)) {
			isDir = true;
			if(! getDirectoryFiles()) {
				runAway("DirFerret() - Could not get files in directory: " + target);
			}
			methods = new ArrayList<>();
		} else {
			runAway("DirFerret() - I don't see a directory here: " + target);
		}
	}
	
	public void exploreDirectory() {
		if(isDir && dirFiles.length > 0) {
			for(int i = 0; i < dirFiles.length; i++) {
				FileFerret fileFerret = new FileFerret(dirFiles[i].getPath());
				fileFerret.exploreFile();
				for(Method m : fileFerret.getMethods()) {
					m.setLines(fileFerret.parseMethod(m));
				}
				collectMethods(fileFerret.getMethods());
			}
		} else {
			runAway("Error - DirFerret.exploreDirectory() found no relevant files");
		}
	}
	
	public void analyzeDirectoryMethods() {
		for(Method method : methods) {
			analyzeMethod(method);
		}
	}
		
	private void collectMethods(ArrayList<Method> methods) {
		for(Method m : methods) {
			if(! this.methods.contains(m)) {
				this.methods.add(m);
			}
		}
	}
			
	private boolean getDirectoryFiles() {
		try {
			File dir = new File(target);
			dirFiles = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(FILE_EXTENSION));
			return true;
		} catch (SecurityException se) {
			runAway("DirFerret.getDirectoryFiles() - IOException caught:\n" + se.getMessage());
			return false;
		}
	}
}
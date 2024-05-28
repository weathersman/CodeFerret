
public class Main {
	public static void main(String[] args) {
		String path = "";
		String startAtMethod = "";
		
		if (args.length == 1) {
   			path = args[0];
		} else if(args.length == 2) {
			path = args[0];
			startAtMethod = args[1];
		}
		else {
   			System.err.println("Usage: \nParameter 1 is path to directory or file containing java source code\nParameter 2 is starting method");
   			System.exit(1);
		}
		
		if(CodeFerret.isFile(path)) {
			FileFerret fileFerret = new FileFerret(path, startAtMethod);
			fileFerret.exploreFile();
			fileFerret.analyzeMethods();
			fileFerret.showMethods();
		} else if(CodeFerret.isDirectory(path)) {
			DirFerret dirFerret = new DirFerret(path, startAtMethod);
			dirFerret.exploreDirectory();
			dirFerret.analyzeDirectoryMethods();
			dirFerret.showMethods();
		}
	}
}


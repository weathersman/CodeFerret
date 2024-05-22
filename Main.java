
public class Main {
	public static void main(String[] args) {
		String path = null;
		if (args.length > 0) {
   			path = args[0];
		} else {
   			System.err.println("Give me a path or file argument please.");
   			System.exit(1);
		}
		
		if(CodeFerret.isFile(path)) {
			FileFerret fileFerret = new FileFerret(path);
			fileFerret.exploreFile();
			fileFerret.analyzeMethods();
			fileFerret.showMethods();
		} else if(CodeFerret.isDirectory(path)) {
			DirFerret dirFerret = new DirFerret(path);
			dirFerret.exploreDirectory();
			dirFerret.analyzeDirectoryMethods();
			dirFerret.showMethods();
		}
	}
}


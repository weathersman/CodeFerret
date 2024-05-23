import java.io.*;
import java.util.ArrayList;
import java.util.regex.*;

public class FileFerret extends CodeFerret {
		private ArrayList<String> fileLines;
		private final Pattern methodPattern = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?",Pattern.CASE_INSENSITIVE);
		
		FileFerret(String path) {
			this.target = path;
			methods = new ArrayList<>();
			fileLines = new ArrayList<String>();
			
			if(isFile(path) && path.endsWith(FILE_EXTENSION)) {
				isFile = true; 
			} else {
				runAway("FileFerret() - This doesn't look like a file I can explore");
			}
		}
		
		public void exploreFile(){
			if(isFile) {
				parseFile();
			}
		}
		
		public void analyzeMethods() {
			for(Method method : methods) {
				method.setLines(parseMethod(method));
				analyzeMethod(method);
			}
		}
				
		ArrayList<String> parseMethod(Method method) {
			ArrayList<String> methodLines = new ArrayList<>();
			boolean inMethod = false;
			int openBracketCount = 0;
			int closedBracketCount = 0;
			
			for(int i = 0; i < fileLines.size(); i++) {
				String theLine = fileLines.get(i);
				if(inMethod == false && theLine.contains(method.getSignature())) {
					openBracketCount++;
					inMethod = true;
					
					// if the function is just one line
					if(theLine.contains("{") && theLine.contains("}")) {
						return methodLines;
					}
					
				} else if(inMethod){
					if(theLine.contains("{")) {
						openBracketCount++;
					}
					
					if(theLine.contains("}")) {
						closedBracketCount++;
					}
					
					methodLines.add(theLine);
				}
				
				if(inMethod && openBracketCount == closedBracketCount) {
					return methodLines;
				}
			}
			
			return methodLines;
		}
		
		private void parseFile() {
			try {
            	File f = new File(target);
				BufferedReader b = new BufferedReader(new FileReader(f));
				String readLine = "";
				
				while ((readLine = b.readLine()) != null) {
                	fileLines.add(readLine);
            	}
            	
            	if(! fileLines.isEmpty()) {
					if(! parseLines()) {
						runAway("FileFerret.parseFile() - Error parsing file");
					}
				} else {
					runAway("File has no lines");
				}
            	
			} catch (IOException ioe) {
				runAway("FileFerret.parseFile() - IOException caught:\n" + ioe.getMessage());
			}
		}		
		
		private boolean parseLines() {
			Matcher methodMatcher = methodPattern.matcher("");
			for(int i = 0; i < fileLines.size(); i++) {
				try {
					methodMatcher = methodPattern.matcher(fileLines.get(i));
					if (methodMatcher.find()) {
						String methodFound = methodMatcher.group();
						
						String [] elements = methodFound.split("\\s+");
						for(int j = 0; j < elements.length; j++) {
							if(elements[j].contains("(")) {
								String name = elements[j].substring(0,elements[j].lastIndexOf('('));
								methods.add(new Method(methodFound,name,target));
							}
						}
					}
				} catch (IllegalStateException ise) {
					runAway("FileFerret.parseLines() -  IllegalStateException caught:\n" + ise.getMessage() );
				}
			}
			
			return true;
		}
}
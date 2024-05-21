import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;

public class CodeFerret {
		private String target;
		private boolean isFile = false;
		private boolean isDir = false;
		private boolean targetAcquired = false;
		private ArrayList<Method> methods;
		private ArrayList<String> fileLines;
		private final String [] blackList = {"if", "for", "while", "else", "catch", "switch", "instanceof", "throw"};
		private Pattern methodPattern = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?",Pattern.CASE_INSENSITIVE);
		
		CodeFerret(String path) {
			this.target = path;
			if(! seesTarget()) {
				runAway("Bad file or path sent to CodeFerret");
			}
			methods = new ArrayList<>();
		}
		
		private void runAway(String msg) {
			System.out.println(msg);
			System.exit(1);
		}
		
		public boolean canSniff() {
			if(! targetAcquired) {
				System.out.println("No target in sight, please check parameters\nI was given " + target);
				return false;
			}

			if(isFile && parseFile()) {
				return true;
			} else if (isDir) {
				return true;
			}
		
			return false;
		}		
		
		public void exploreFile(){
			if(isFile) {
				System.out.println("CodeFerret will explore the file");
				
				for(Method method : methods) {
					//System.out.println(method.getSignature());
					ArrayList<String> methodContents = parseMethod(method);
					analyzeMethod(method, methodContents);
				}
				
				for(Method method : methods) {
					System.out.println(method.getName() + " calls " + method.displayCalls());
				}
				
			} else if(isDir) {
				System.out.println("CodeFerret will explore the directory");
			}
		}
		public void explore(){
			if(isFile) {
				exploreFile();
			} else if(isDir) {
				System.out.println("ToDo: CodeFerret will explore the directory");
			}
		}
		
		private void analyzeMethod(Method theMethod, ArrayList<String> contents) {
			for(int i = 0; i < contents.size(); i++) {
				String theLine = contents.get(i);
				for(Method method : methods) {
					if(method != theMethod && theLine.contains(method.getName() + "(")) {
						if(! theMethod.getCalls().contains(method)) {
							theMethod.addCall(method);
						}
					}
				}
			}
		}
		
		private ArrayList<String> parseMethod(Method method) {
			ArrayList<String> methodLines = new ArrayList<>();
			boolean inMethod = false;
			int openBracketCount = 0;
			int closedBracketCount = 0;
			
			for(int i = 0; i < fileLines.size(); i++) {
				String theLine = fileLines.get(i);
				if(inMethod == false && theLine.contains(method.getSignature())) {
					openBracketCount++;
					inMethod = true;
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
		
		private boolean parseFile() {
			try {
            	File f = new File(target);
				BufferedReader b = new BufferedReader(new FileReader(f));
				String readLine = "";
				
				while ((readLine = b.readLine()) != null) {
                	fileLines.add(readLine);
            	}
            	
            	if(! fileLines.isEmpty()) {
					if(! parseLines()) {
						runAway("Error parsing file");
					}
				} else {
					runAway("File has no lines");
				}
            	
			} catch (IOException ioe) {
				runAway("parseFile() - IOException caught:\n" + ioe.getMessage());
			}
			
			return true;
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
								methods.add(new Method(methodFound,name));
							}
						}
					}
				} catch (IllegalStateException ise) {
					runAway("parseLines() - IllegalStateException caught:\n" + ise.getMessage() );
				}
			}
			
			return true;
		}
		
		private boolean seesTarget() {
		
			if(isFile(target)) {
				isFile = true;
				targetAcquired = true;
				fileLines = new ArrayList<String>();
				return true;
			} else if(isDirectory(target)) {
				isDir = true;
				targetAcquired = true;
				return true;
			}

			return false;
		}
		private boolean isFile(String path) {
			if(path == null || path.isEmpty()) {
				return false;
			}
			
			try {
				if (Files.isRegularFile(Paths.get(path))) {
					return true;
				}
			} catch(SecurityException se) {
				runAway("isFile() - Security Exception caught:\n" + se.getMessage() );
			}
			return false;
		}
		private boolean isDirectory(String path) {
			if(path == null || path.isEmpty()) {
				return false;
			}
			
			try {
				if(Files.isDirectory(Paths.get(path))) {
					return true;
				}
			} catch(SecurityException se) {
				runAway("isDirectory() - SecurityException caught:\n" + se.getMessage() );
			}
			
			return false;
		}
	}
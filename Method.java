import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Method {
 
  private String signature;
  private String name;
  private String fileName;
  private List<Method> calls;
  private List<Method> callers;
  private ArrayList<String> lines;
 
  public Method(String signature, String name, String fileName) {
    this.signature = signature;
    this.name = name;
    this.fileName = fileName;
    this.calls = new LinkedList<>();
    this.callers = new LinkedList<>(); 
    this.lines = new ArrayList<>();
  }
  
  public String displayCalls() {
    if(this.calls.size() == 0) {
      return "nothing";
    }
    
    StringBuilder calls = new StringBuilder();
    for(Method call: this.calls) {
      calls.append(call.getName() + " ");
    }
    return calls.toString();
  }
 
  public String getSignature() {
    return signature;
  }
  
  public String getName() {
    return name;
  }
  
  public String getFileName() {
    return fileName;
  }
 
  public List<Method> getCalls() {
    return calls;
  }
  
  public List<Method> getCallers() { return callers; }
  
  public void addCall(Method call) {
    if(call != null && !this.calls.contains(call)) {
      this.calls.add(call);
    }
  }
  
  public void addCaller(Method caller) {
    if(caller != null && !this.callers.contains(caller)) {
      this.callers.add(caller);
    }
  }
  
  public ArrayList<String> getLines() { return lines; }  
  
  public void setLines(ArrayList<String> lines) { this.lines = lines; }
  

}

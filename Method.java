import java.util.LinkedList;
import java.util.List;

public class Method {
 
  private String signature;
  private String name;
  private List<Method> calls;
 
  public Method(String signature, String name) {
    this.signature = signature;
    this.name = name;
    this.calls = new LinkedList<>();
  }
 
  public void addCall(Method call) {
    this.calls.add(call);
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
 
  public List<Method> getCalls() {
    return calls;
  }
}

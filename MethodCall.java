import java.util.ArrayList;

public class MethodCall {
    private String methodName;
    private int numOfParams;
    private ArrayList<String> paramArray = new ArrayList<>();
    public MethodCall(Tokens IDholder) {
        try{
            methodName = IDholder.getContents();
        }catch (Exception e){
            //pass
        }

    }
    void setToNull(MethodCall methodCall){
        methodCall.setMethodName("null");
    }
    int getNumOfParams() {
        return numOfParams;
    }

    void setNumOfParams(int numOfParams) {
        this.numOfParams = numOfParams;
    }

    ArrayList<String> getParamArray() {
        return paramArray;
    }

    String getMethodName() {
        return methodName;
    }

    public void setParamArray(String param) {
        this.paramArray.add(param);
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

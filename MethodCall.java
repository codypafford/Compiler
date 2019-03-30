
public class MethodCall {
    private String methodName;
    private int numOfParams;
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

    String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}

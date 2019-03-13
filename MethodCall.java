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

    public int getNumOfParams() {
        return numOfParams;
    }

    public void setNumOfParams(int numOfParams) {
        this.numOfParams = numOfParams;
    }

    public ArrayList<String> getParamArray() {
        return paramArray;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setParamArray(String param) {
        this.paramArray.add(param);
    }
}

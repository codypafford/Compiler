
import java.util.ArrayList;

public class Function {
    private String name;
    private String TYPE;
    private ArrayList<Tokens> variablesInParams = new ArrayList<>();
    private ArrayList<Tokens> variablesInFunction = new ArrayList<>();

    private Boolean hasReturnStmt = false;

    Function(String name) {
        this.name=name;
    }

    public Function(Function function) {
        this.name = function.getName();
        this.variablesInParams = function.variablesInParams;
        this.variablesInFunction = function.variablesInFunction;
        this.TYPE = function.TYPE;
    }

    public Function(MethodCall methodCall) {              // USED ONLY FOR METHOD CALLS
        name = methodCall.getMethodName();
    }


    public String getName() {
        return name;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }


    public void addToParamList(Tokens token){
        variablesInParams.add(token);
    }

    @Override
    public String toString() {
        return "Function{" +
                "\n\tname='" + name + '\'' +
                ", \n\tTYPE='" + TYPE + '\'' +
                ", \n\tvariablesInParameters=" + variablesInParams + ", \n\tvariablesInFunction='"+ variablesInFunction+
                '}';
    }


    public int getNumOfVariablesInParams() {             // USED IN METHOD DECLARATION
        return variablesInParams.size();
    }

    public void putInFunctionVarList(Tokens token) {
        variablesInFunction.add(token);
    }

    //CHECK IF DECLARATION ALREADY EXISTS
    public void containsThisDeclarationAlready(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInParams.size(); i++){
            Tokens t = variablesInParams.get(i);
            if (t.getContents().equals(nameOfDeclaration) && token.getDepth() == 1){
                System.out.println("ERROR: This variable: " +  nameOfDeclaration + " has already been declared in the function: " + name + " with depth: " + t.getDepth());
                System.out.println("REJECT");
                System.exit(0);
            }
        }
        for (int i = 0; i < variablesInFunction.size(); i++) {

            Tokens t = variablesInFunction.get(i);
            if (t.getContents().equals(nameOfDeclaration)  && token.getDepth() == t.getDepth()){
                System.out.println("ERROR: This variable: " +  nameOfDeclaration + " has already been declared in the function: " + name + " with depth: " + t.getDepth());
                System.out.println("REJECT");
                System.exit(0);
            }
        }
        for (int i = 0; i < SemanticAnalyzer.tempVarList.size(); i++){
            Tokens t = SemanticAnalyzer.tempVarList.get(i);
            if (t.getContents().equals(nameOfDeclaration) && token.getDepth() == t.getDepth()){
                System.out.println("ERROR: This variable: " +  nameOfDeclaration + " has already been declared in this scope: " + name + " with depth: " + t.getDepth());
                System.out.println("REJECT");
                System.exit(0);
            }
        }

    }

    public Boolean hasThisVariableBeenDeclared(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInParams.size(); i++){
            Tokens t = variablesInParams.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return true;
            }
        }
        for (int i = 0; i < SemanticAnalyzer.tempVarList.size(); i++){
            Tokens t = SemanticAnalyzer.tempVarList.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return true;
            }
        }
        for (int i = 0; i < variablesInFunction.size(); i++) {
            Tokens t = variablesInFunction.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return true;
            }
        }
        for (int i = 0; i < SemanticAnalyzer.globalVariables.size(); i++){
            Tokens t = SemanticAnalyzer.globalVariables.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Tokens> getVariablesInParams() {
        return variablesInParams;
    }

    public Boolean getHasReturnStmt() {
        return hasReturnStmt;
    }

    public void setHasReturnStmt(Boolean hasReturnStmt) {
        this.hasReturnStmt = hasReturnStmt;
    }

    public Boolean isThisAnArray(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInParams.size(); i++){
            Tokens t = variablesInParams.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                if (t.getArray()){
                    return true;
                }else return false;
            }
        }
        for (int i = 0; i < variablesInFunction.size(); i++) {
            Tokens t = variablesInFunction.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                if (t.getArray()){
                    return true;
                }else return false;
            }
        }
        for (int i = 0; i < SemanticAnalyzer.globalVariables.size(); i++){
            Tokens t = SemanticAnalyzer.globalVariables.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                if (t.getArray()){
                    return true;
                }else return false;
            }
        }

        return false;
    }

    public Tokens getDeclaredDataOfToken(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInParams.size(); i++){
            Tokens t = variablesInParams.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return t;
            }
        }
        for (int i = 0; i < SemanticAnalyzer.tempVarList.size(); i++){
            Tokens t = SemanticAnalyzer.tempVarList.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return t;
            }
        }
        for (int i = 0; i < variablesInFunction.size(); i++) {
            Tokens t = variablesInFunction.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return t;
            }
        }
        for (int i = 0; i < SemanticAnalyzer.globalVariables.size(); i++){
            Tokens t = SemanticAnalyzer.globalVariables.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return t;
            }
        }
        try{
            int num = Integer.parseInt(token.getContents());
            token.setDeclaredType("int");
        }catch (Exception e){
            if (Main.containsFloat(token.getContents())){
                token.setDeclaredType("float");
            }
        }

        return null;
    }

    public Tokens getParamVarByIndex(int x){
        try {
            return this.variablesInParams.get(x);
        }catch (Exception e){

        }
        return null;
    }

}

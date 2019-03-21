import com.sun.xml.internal.ws.server.sei.SEIInvokerTube;

import java.util.ArrayList;

public class Function {
    private String name;
    private String TYPE;
    private ArrayList<Tokens> variablesInParams = new ArrayList<>();
    private ArrayList<Tokens> variablesInFunction = new ArrayList<>();
    private int numOfVariablesInFunction = 0;
    static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    static final String ANSI_PURPLE = "\u001B[35m";
    static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private int numOfVariablesInParams = 0;
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
        numOfVariablesInParams = methodCall.getNumOfParams();
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
                "\n\tname='" + ANSI_BLUE+ name + '\'' +
                ", \n\tTYPE='" + ANSI_RED+ TYPE + '\'' +
                ", \n\tvariablesInParameters=" + ANSI_WHITE + variablesInParams + ANSI_PURPLE+ ", \n\tvariablesInFunction='"+ variablesInFunction+
                '}';
    }

    public void putInFunctionVarList(ArrayList variablesInFunction) {
        this.variablesInFunction = variablesInFunction;
    }

    public int getNumOfVariablesInParams() {             // USED IN METHOD DECLARATION
        return variablesInParams.size();
    }

    public void setNumOfVariablesInFunction(int numOfVariablesInFunction) {
        this.numOfVariablesInFunction = numOfVariablesInFunction;
    }

    public void putInFunctionVarList(Tokens token) {
        variablesInFunction.add(token);
    }

    public Boolean containsThisDeclarationAlready(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInFunction.size(); i++) {

            Tokens t = variablesInFunction.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                System.out.println("ERROR: This variable: " +  nameOfDeclaration + " has already been declared in the function: " + name + " with depth: " + t.getDepth());
                System.out.println("REJECT");
                System.exit(0);
            }
        }
        for (int i = 0; i < SemanticAnalyzer.tempVarList.size(); i++){
            Tokens t = SemanticAnalyzer.tempVarList.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                System.out.println("ERROR: This variable: " +  nameOfDeclaration + " has already been declared in this scope: " + name + " with depth: " + t.getDepth());
                System.out.println("REJECT");
                System.exit(0);
            }
        }

        return null;
    }

    public Boolean hasThisVariableBeenDeclared(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInFunction.size(); i++) {
            Tokens t = variablesInFunction.get(i);
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
        for (int i = 0; i < SemanticAnalyzer.globalVariables.size(); i++){
            Tokens t = SemanticAnalyzer.globalVariables.get(i);
            if (t.getContents().equals(nameOfDeclaration)){
                return true;
            }
        }
        return false;
    }

    public Boolean getHasReturnStmt() {
        return hasReturnStmt;
    }

    public void setHasReturnStmt(Boolean hasReturnStmt) {
        this.hasReturnStmt = hasReturnStmt;
    }

    public Boolean isThisAnArray(Tokens token){
        String nameOfDeclaration = token.getContents();
        for (int i = 0; i < variablesInFunction.size(); i++) {
            Tokens t = variablesInFunction.get(i);
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
        for (int i = 0; i < variablesInFunction.size(); i++) {
            Tokens t = variablesInFunction.get(i);
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

}

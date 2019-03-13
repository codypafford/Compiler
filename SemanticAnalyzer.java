
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class SemanticAnalyzer {

    private int n = 0;
    private Tokens token = Main.tokensForSemantics.get(n);
    Boolean endOfFile = false;
    static String Type = "";
    static HashTable functionList = new HashTable(501);
    private static ArrayList<Tokens> variableList = new ArrayList<>();
    public static ArrayList<Tokens> globalVariables = new ArrayList<>();
    public static ArrayList<Tokens> tempVarList = new ArrayList<>();
    Stack stack = new Stack<Function>();
    static String functionName = "";
    private Function function;
    private Tokens IDholder;
    private int numOfParamsinCall = 1;
    private MethodCall methodCall;


    public SemanticAnalyzer() {
        Boolean bool = parse_Program();
        if (bool) {
            System.out.println("ACCEPT");
        } else System.out.println("REJECT");
        System.out.println("VARIABLE LIST:");
        for (Tokens element : variableList) {
            System.out.println(element);
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("GLOBAL VARIABLES:");
        for (Tokens element : globalVariables) {
            System.out.println(element);
        }
        System.out.println("----------------------------------------------------------------------------------");
    }

    Boolean parse_Program() {
        if (!parse_DeclarationList()) {
            return false;
        }
        if (!parse_DeclarationListPrime()) {
            return false;
        }
        Function Lastfunction = (Function) stack.peek();
        if (Lastfunction.getName().equals("main") && (Lastfunction.getTYPE().equals("int") || Lastfunction.getTYPE().equals("void"))){
            return true;
        }else {
            System.out.println("ERROR: The last method was not main method - or - the type was not int or void");
            return false;
        }
    }

    private boolean parse_DeclarationList() {
        if (!parse_Declaration()) {
            return false;
        }
        if (!parse_DeclarationListPrime()) {
            return false;
        } else return true;
    }

    private boolean parse_DeclarationListPrime() {
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            if (!parse_Declaration()) {
                return false;
            }
            if (!parse_DeclarationListPrime()) {
                return false;
            }
            return true;
        } else if (endOfFile) {
            return true;
        } else return false;
    }


    private boolean parse_Declaration() {
        if (!parse_TypeSpecifier()) {
            return false;
        }
        if (isID(token)) {
            Accept();
        } else return false;

        if (!parse_DDD()) {
            return false;
        }
        return true;
    }

    private boolean parse_DDD() {
        if (token.getContents().equals("(")) {
            if (token.getDepth() == 0 && previousToken().getType().equals("ID")) {
                functionName = previousToken().getContents();
                function = new Function(functionName);
                function.setTYPE(prev2Token().getContents());
                stack.push(function);
                int hashValue = findhashVal(functionName);
                functionList.createHashArray(hashValue, function);

            }
            Accept();
            if (!parse_params()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
                if (!parse_compoundStmt()) {
                    return false;
                } else return true;
            }
        } else if (token.getContents().equals(";") || token.getContents().equals("[")) {
            if (!parse_varDeclarationPrime()) {
                return false;
            } else return true;
        }
        return false;
    }

    private boolean parse_compoundStmt() {
        if (token.getContents().equals("{")) {
            Accept();
            if (!parse_localDeclarations()) {
                return false;
            }
            if (!parse_statementList()) {
                return false;
            }
            if (token.getContents().equals("}")) {
                if (token.getDepth() <= 2){
//                    System.out.println("--------------------------------------------");
//                 //   System.out.println(Arrays.toString(tempVarList.toArray()));
//                    System.out.println("-------------------------------------------------");

                    tempVarList.clear();
                }
                Accept();
                return true;
            } else return false;
        } else return false;
    }

    private boolean parse_statementList() {
        if (!parse_statementListPrime()) {
            return false;
        } else return true;
    }

    private boolean parse_statementListPrime() {
        if ((token.getContents().equals(";") || isID(token) || isNUM(token)
                || token.getContents().equals("(") || token.getContents().equals("{") || token.getContents().equals("if")
                || token.getContents().equals("while") || token.getContents().equals("return")) && !endOfFile) {
            if (!parse_statement()) {
                return false;
            }
            if (!parse_statementListPrime()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("}")) {
            return true;
        }
        return false;
    }

    private boolean isID(Tokens token) {
        try {
            if (token.getType().equals("ID")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean isNUM(Tokens token) {
        try {
            if (token.getType().equals("NUM") || token.getType().equals("FLOAT")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean parse_statement() {
        if (token.getContents().equals("(") || token.getContents().equals(";") || isID(token) || isNUM(token)) {
            if (!parse_expressionStmt()) {
                return false;
            }
        } else if (token.getContents().equals("{")) {
            if (!parse_compoundStmt()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("if")) {
            if (!parse_selectionStmt()) {
                return false;
            }
        } else if (token.getContents().equals("while")) {
            if (!parse_iterationStmt()) {
                return false;
            }
        } else if (token.getContents().equals("return")) {
            if (!parse_returnStmt()) {
                return false;
            }
        }
        return true;
    }

    private boolean parse_iterationStmt() {
        if (token.getContents().equals("while")) {
            Accept();
            if (token.getContents().equals("(")) {
                Accept();
            } else return false;
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            } else return false;
            if (!parse_statement()) {
                return false;
            }
            return true;
        } else return false;
    }

    private boolean parse_expression() {
        if (isID(token)) {
            Accept();
            if (!parse_FFF()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("(")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            } else return false;
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_SSS()) {
                return false;
            }
            return true;
        } else if (isNUM(token)) {
            Accept();
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_SSS()) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean parse_SSS() {
        if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")) {
            if (!parse_relop()) {
                return false;
            }
            if (!parse_additiveExpression()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("+") || token.getContents().equals("-") || token.getContents().equals(",")) {
            if (!parse_additiveExpressionPrime()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")) {
            return true;
        }
        return false;
    }

    private boolean parse_additiveExpression() {
        if (!parse_term()) {
            return false;
        }
        if (!parse_additiveExpressionPrime()) {
            return false;
        }
        return true;
    }

    private boolean parse_term() {
        if (!parse_factor()) {
            return false;
        }
        if (!parse_termPrime()) {
            return false;
        }
        return true;
    }

    private boolean parse_factor() {
        if (token.getContents().equals("(")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
                return true;
            } else return false;
        } else if (isID(token)) {
            function = (Function) stack.peek();
            if (!function.hasThisVariableBeenDeclared(token)){
                System.out.println("ERROR: This variablllllllle: " +  token.getContents() + " has no declaration in the function: " + function.getName());
            }
            Accept();
            if (!parse_factorXYZ()) {
                return false;
            }
            return true;
        } else if (isNUM(token)) {
            Accept();
            return true;
        } else return false;
    }

    private boolean parse_factorXYZ() {
        if (token.getContents().equals("(")) {
            Accept();
            if (!parse_args()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
                return true;
            } else return false;
        } else {
            if (!parse_varPrime()) {
                return false;
            } else return true;
        }
    }

    private boolean parse_args() {
        if (isID(token) || token.getContents().equals("(") || isNUM(token)) {
            if (!parse_argList()) {
                return false;
            } else return true;
        } else if (token.getContents().equals(")")) {
            return true;
        } else return false;
    }

    private boolean parse_argList() {
        if (isID(token)) {
            function = (Function) stack.peek();
            if (!function.hasThisVariableBeenDeclared(token)){
                System.out.println("ERROR: This vwewewariablllllllle: " +  token.getContents() + " has no declaration in the function: " + function.getName());
            }
            Accept();
            if (!parse_CS()) {
                return false;
            } else return true;
        } else if (token.getContents().equals("(")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            } else return false;
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_FID()) {
                return false;
            }
            return true;
        } else if (isNUM(token)) {
            Accept();
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_FID()) {
                return false;
            } else return true;
        } else return false;
    }

    private boolean parse_FID() {
        if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")) {
            if (!parse_relop()) {
                return false;
            }
            if (!parse_additiveExpression()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        } else {
            if (!parse_additiveExpressionPrime()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        }
    }

    private boolean parse_returnStmtPrime() {
        if (token.getContents().equals(";")) {
            Accept();
            return true;
        } else if (isID(token)) {
            Accept();
            if (!parse_CCC()) {
                return false;
            }
            return true;
        } else if (isNUM(token)) {
            Accept();
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_BBB()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("(")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            }
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_BBB()) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean parse_CCC() {
        if (token.getContents().equals("(")) {
            Accept();
            if (!parse_args()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            } else return false;
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_BBB()) {
                return false;
            }
            return true;
        } else {
            if (!parse_varPrime()) {
                return false;
            }
            if (!parse_AAA()) {
                return false;
            }
            return true;
        }
    }

    private boolean parse_AAA() {
        if (token.getContents().equals("=")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(";")) {
                Accept();
                return true;
            }
        } else {
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_BBB()) {
                return false;
            } else return true;
        }
        return false;
    }

    private boolean parse_BBB() {
        if (token.getContents().equals("+") || token.getContents().equals("-") || token.getContents().equals(";")) {
            if (!parse_additiveExpressionPrime()) {
                return false;
            }
            if (token.getContents().equals(";")) {
                Accept();
                return true;
            } else return false;
        } else {
            if (!parse_relop()) {
                return false;
            }
            if (!parse_additiveExpression()) {
                return false;
            }
            if (token.getContents().equals(";")) {
                Accept();
                return true;
            } else return false;
        }
    }

    private boolean parse_relop() {
        if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")) {
            Accept();
            return true;
        }
        return false;
    }

    private boolean parse_CS() {
        if (token.getContents().equals("(")) {
            Accept();
            if (!parse_args()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            } else return false;
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_FID()) {
                return false;
            }
            return true;
        } else {
            if (!parse_varPrime()) {
                return false;
            }
            if (!parse_EEE()) {
                return false;
            }
            return true;
        }
    }

    private boolean parse_EEE() {
        if (token.getContents().equals("=")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        } else {
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_FID()) {
                return false;
            }
            return true;
        }
    }

    private boolean parse_argListPrime() {
        if (token.getContents().equals(",")) {
            numOfParamsinCall++;
            methodCall.setNumOfParams(numOfParamsinCall);
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals(")") || token.getContents().equals(";") || token.getContents().equals("]")) {
            return true;
        }
        return false;
    }

    private boolean parse_termPrime() {
        if (token.getContents().equals("*") || token.getContents().equals("/")) {
            if (!parse_mulop()) {
                return false;
            }
            if (!parse_factor()) {
                return false;
            }
            if (!parse_termPrime()) {
                return false;
            }
        } else if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")
                || token.getContents().equals("+") || token.getContents().equals("-") || token.getContents().equals(",")
                || token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")
                || token.getContents().equals("(")) {
            return true;
        }
        return true;
    }

    private boolean parse_mulop() {
        if (token.getContents().equals("*") || token.getContents().equals("/")) {
            Accept();
            return true;
        }
        return false;
    }

    private boolean parse_FFF() {
        Tokens anotherIDholder = previousToken();
        if (token.getContents().equals("(")) {
            IDholder = previousToken();
            methodCall = new MethodCall(IDholder);
            Accept();
            if (!parse_args()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                if (methodCall.getNumOfParams() == 0 && !previousToken().getContents().equals("(")){
                    methodCall.setNumOfParams(1);
                }
                Accept();
            } else return false;
            if (token.getContents().equals(";")){
                if (IDholder != null) {
                    Function funct = new Function(IDholder.getContents());
                    function = funct;
                    if (!functionList.SearchLinearProbe(funct)) {
                        System.out.println("Method call before declaration: " + function.getName());
                        return false;
                    }                                                         // This is where function call() is
                    numOfParamsinCall = 1;
                    isValidMethodCall(methodCall);
                    // checkNumOfParamsInCallAndFunct(methodCall);
                }
            }
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_SSS()) {
                return false;
            }
            return true;
        } else {
            function = (Function) stack.peek();
            if (!function.hasThisVariableBeenDeclared(anotherIDholder)){
                System.out.println("ERROR: This variable: " +  anotherIDholder.getContents() + " has no declaration in the function: " + function.getName());
            }
            if (!parse_varPrime()) {
                return false;
            }
            if (!parse_XXX()) {
                return false;
            }
            return true;
        }
    }

    private void isValidMethodCall(MethodCall methodCall) {
        Function call = new Function(methodCall);
        if (!functionList.SearchLinearProbe(call)){
            System.out.println("Method call before declaration: " + methodCall.getMethodName());
            return;
        }

        Function functionFromFunctList = functionList.SearchByFunction(call.getName());

        if (methodCall.getNumOfParams() != functionFromFunctList.getNumOfVariablesInParams()){
            System.out.println("ERROR: Params do not match function: " + call.getName());
        }

    }

    private boolean parse_varPrime() {
        if (token.getContents().equals("[")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals("]")) {
                Accept();
                return true;
            } else return false;
        } else if (token.getContents().equals("=") || token.getContents().equals("+") || token.getContents().equals("-")
                || token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")
                || token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")
                || token.getContents().equals(",") || token.getContents().equals("*") || token.getContents().equals("/")
                || token.getContents().equals("(") || isID(token) || isNUM(token) || token.getContents().equals("int")
                || token.getContents().equals("float") || token.getContents().equals("{") || token.getContents().equals("}")
                || token.getContents().equals("(") || token.getContents().equals("return") || token.getContents().equals("while")
                || token.getContents().equals("if")) {
            return true;
        } else return false;
    }

    private boolean parse_XXX() {
        if (token.getContents().equals("=")) {
            Accept();
            if (!parse_expression()) {
                return false;
            }
            if (!parse_argListPrime()) {
                return false;
            }
            return true;
        } else {
            if (!parse_termPrime()) {
                return false;
            }
            if (!parse_SSS()) {
                return false;
            }
            return true;
        }
    }

    private boolean parse_additiveExpressionPrime() {
        if (token.getContents().equals("+") || token.getContents().equals("-")) {
            if (!parse_addop()) {
                return false;
            }
            if (!parse_term()) {
                return false;
            }
            if (!parse_additiveExpressionPrime()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")
                || token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")
                || token.getContents().equals(",") || token.getContents().equals("(")) {
            return true;
        }
        return false;
    }

    private boolean parse_addop() {
        if (token.getContents().equals("+") || token.getContents().equals("-")) {
            Accept();
            return true;
        }
        return false;
    }

    private boolean parse_selectionStmt() {
        if (token.getContents().equals("if")) {
            Accept();
            if (token.getContents().equals("(")) {
                Accept();
            } else return false;
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(")")) {
                Accept();
            } else return false;
            if (!parse_statement()) {
                return false;
            }
            if (!parse_selectionStmtPrime()) {
                return false;
            }
        } else return false;

        return true;
    }

    private boolean parse_selectionStmtPrime() {
        if (token.getContents().equals("else") && (getNextToken().getContents().equals(";") || isID(getNextToken()) ||
                getNextToken().getContents().equals("(") || isNUM(getNextToken()) || getNextToken().getContents().equals("{")
                || getNextToken().getContents().equals("if") || getNextToken().getContents().equals("while") || getNextToken().getContents().equals("return"))) {
            Accept();
            tempVarList.clear();
            if (!parse_statement()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals("else") || token.getContents().equals(";") || isID(token)
                || token.getContents().equals("(") || isNUM(token) || token.getContents().equals("{")
                || token.getContents().equals("if") || token.getContents().equals("while") || token.getContents().equals("return")
                || token.getContents().equals("}")) {
            return true;
        }
        return false;
    }

    private boolean parse_returnStmt() {
        if (token.getContents().equals("return")) {
            function = (Function) stack.peek();
            if (token.getDepth() == 1 && function.getTYPE().equals("void") && !getNextToken().getContents().equals(";")){
                System.out.println("ERROR: Void functions cannot return a value");
            }
            Accept();
            if (!parse_returnStmtPrime()) {
                return false;
            } else return true;
        }
        return false;
    }


    private boolean parse_expressionStmt() {
        if (token.getContents().equals(";")) {
            Accept();
            return true;
        } else if (token.getContents().equals("(") || isID(token) || isNUM(token)) {
            if (!parse_expression()) {
                return false;
            }
            if (token.getContents().equals(";")) {
                Accept();
            }
            return true;
        } else return false;
    }


    private boolean parse_localDeclarations() {
        if (!parse_localDeclarationsPrime()) {
            return false;
        } else return true;
    }

    private boolean parse_localDeclarationsPrime() {
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            if (!parse_varDeclaration()) {
                return false;
            }
        }
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            if (!parse_localDeclarationsPrime()) {
                return false;
            }
        } else if (token.getContents().equals("}") || token.getContents().equals(";") || token.getContents().equals("{")
                || token.getContents().equals("if") || token.getContents().equals("while")
                || token.getContents().equals("return") || isID(token) || isNUM(token) || token.getContents().equals("(")) {
            return true;
        } else return false;

        return true;
    }

    private boolean parse_params() {
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            if (getNextToken().getContents().equals(")") && token.getContents().equals("void")) {
                Accept();
                function.addToParamList(previousToken());
                return true;
            } else {
                if (!parse_paramList()) {
                    return false;
                } else return true;
            }
        }
        return false;
    }

    private Tokens getNextToken() {
        Tokens nextToken = Main.tokensForSemantics.get(n + 1);
        return nextToken;
    }

    private Tokens previousToken() {
        Tokens prevToken = Main.tokensForSemantics.get(n - 1);
        return prevToken;
    }

    private Tokens prev2Token() {
        Tokens prev2Token = Main.tokensForSemantics.get(n - 2);
        return prev2Token;
    }

    private boolean parse_paramList() {
        if (!parse_param()) {
            return false;
        }
        if (!parse_paramListPrime()) {
            return false;
        }
        return true;
    }

    private boolean parse_paramListPrime() {
        if (token.getContents().equals(",")) {
            Accept();
            if (!parse_param()) {
                return false;
            }
            if (!parse_paramListPrime()) {
                return false;
            }
            return true;
        } else if (token.getContents().equals(")")) {
            return true;
        }
        return false;
    }


    private boolean parse_param() {
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            Accept();
        } else return false;
        if (isID(token)) {
            Accept();
            function = (Function) stack.peek();
            if (token.getContents().equals("[")){
                previousToken().setArray(true);
                previousToken().setDeclaredType(prev2Token().getContents());
                try{                                                   // ATTEMPTS TO SET ARRAY SIZE IF VALID INTEGER
                    if (!getNextToken().getContents().equals("]")){
                        int arrSize = Integer.parseInt(getNextToken().getContents());
                        previousToken().setArraySize(arrSize);
                    }
                }catch (Exception e){
                    //pass
                }
                function.addToParamList(previousToken());
            }else{
                previousToken().setArray(false);
                previousToken().setDeclaredType(prev2Token().getContents());
                function.addToParamList(previousToken());
            }

        } else return false;
        if (!parse_paramPrime()) {
            return false;
        }
        return true;
    }

    private boolean parse_paramPrime() {
        if (token.getContents().equals("[")) {
            Accept();
            if (token.getContents().equals("]")) {
                Accept();
                return true;
            }
        } else if (token.getContents().equals(")") || token.getContents().equals(",")) {
            return true;
        }
        return false;
    }

    private boolean parse_varDeclaration() {
        if (!parse_TypeSpecifier()) {
            return false;
        }
        if (isID(token)) {
            Accept();
        } else {
            return false;
        }
        if (!parse_varDeclarationPrime()) {
            return false;
        }
        return true;
    }

    private boolean parse_varDeclarationPrime() {
        if (token.getContents().equals(";")) {
            if (token.getDepth() == 0){
                previousToken().setDeclaredType(prev2Token().getContents());
                globalVariables.add(previousToken());    // CREATE GLOBAL VARIABLES
                Accept();
                return true;
            }else {
                previousToken().setDeclaredType(prev2Token().getContents());

                //SEARCH TO SEE IF VARIABLE ALREADY EXISTS
                function.containsThisDeclarationAlready(previousToken());

                if (token.getDepth() == 1){
                    function.putInFunctionVarList(previousToken());   // ADD VARIABLE TO FUNCTION'S VARIABLE LIST
                }
                variableList.add(previousToken());
                if (token.getDepth() > 1){
                    tempVarList.add(previousToken());
                }
                Accept();
                return true;
            }
        } else if (token.getContents().equals("[")) {
            if (token.getDepth() == 0){
                previousToken().setDeclaredType(prev2Token().getContents());
                previousToken().setArray(true);
                globalVariables.add(previousToken());           // CREATE GLOBAL VARIABLES
            }else {
                previousToken().setDeclaredType(prev2Token().getContents());
                previousToken().setArray(true);

                //SEARCH TO SEE IF VARIABLE ALREADY EXISTS
                function.containsThisDeclarationAlready(previousToken());

                if (token.getDepth() == 1){
                    function.putInFunctionVarList(previousToken());  //ADD VARIABLE TO FUNCTION'S VARIABLE LIST
                }
                if (token.getDepth() > 1){
                    tempVarList.add(previousToken());
                }
                variableList.add(previousToken());
            }
            Accept();
            if (isNUM(token)) {
                try{
                    if (!token.getContents().equals("]")){
                        int arrSize = Integer.parseInt(token.getContents());
                        prev2Token().setArraySize(arrSize);
                    }
                }catch (Exception e){       // HANDLES THE EXCEPTION IF ARRAY INDEX IS NOT INTEGER
                    System.out.println("Number format Exception for input: " + token.getContents());
                    return false;
                }
                Accept();
                if (token.getContents().equals("]")) {
                    Accept();
                    if (token.getContents().equals(";")) {
                        Accept();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void Accept() {
        try {
            if ((n + 1) <= Main.tokensForSemantics.size() - 1) {
                token = Main.tokensForSemantics.get(++n);
            }
        } catch (Exception e) {
            //
        }
        if ((n) >= Main.tokensForSemantics.size() - 1) {
            endOfFile(true);
        }
    }

    private void endOfFile(boolean b) {
        endOfFile = b;
    }

    private boolean parse_TypeSpecifier() {
        if (token.getContents().equals("int") || token.getContents().equals("float") || token.getContents().equals("void")) {
            Type = token.getContents();
            Accept();
            return true;
        } else return false;
    }

    public static int findhashVal(String key)   // Get Hash Value of each String

    {

        int hashVal = 0;

        for (int j = 0; j < key.length(); j++) {
            int Val = key.charAt(j);    //Using ASCII Values
            hashVal = (hashVal * 26 + Val) % 501;

        }

        return hashVal;

    }
}

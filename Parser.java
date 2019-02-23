
public class Parser {

    private int n = 0;
    private Tokens token = Main.TokenList.get(n);

    public Parser() {
            Boolean bool = parse_Program();
            if (bool){
                System.out.println("ACCEPT");
            }else System.out.println("REJECT");

    }

    Boolean parse_Program(){
        if (!parse_DeclarationList()){
             return false;
        }
        if (!parse_DeclarationListPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_DeclarationList() {
        if (!parse_Declaration()){
            return false;
        }
        if(!parse_DeclarationListPrime()){
              return false;
        }
        return true;
    }

    private boolean parse_DeclarationListPrime() {
            if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
                if (!parse_Declaration()) {
                    return false;
                }else return true;
            }

        return true;
    }


    private boolean parse_Declaration() {
        if (!parse_TypeSpecifier()){
            return false;
        }
        if (isID(token)){
            Accept();
        }else return false;

        if (!parse_DDD()){
            return false;
        }
        return true;
    }

    private boolean parse_DDD() {
        if (token.getContents().equals("(")){
            Accept();
            if(!parse_params()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
                if (!parse_compoundStmt()){
                    return false;
                }else return true;
            }
        }else if (token.getContents().equals(";") || token.getContents().equals("[")){
            if (!parse_varDeclarationPrime()){
                return false;
            }else return true;
        }
        return false;
    }

    private boolean parse_compoundStmt() {
        if (token.getContents().equals("{")){
            Accept();
            if (!parse_localDeclarations()){
                return false;
            }
            if (!parse_statementList()){
                return false;
            }
            if (token.getContents().equals("}")){
                Accept();
                return true;
            }
        }
        return false;               //ORIGINALLY SAID RETURN TRUE? PROB FOR TESTING PURPOSES THOUGH
    }

    private boolean parse_statementList() {
        if (!parse_statementListPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_statementListPrime() {
        if (token.getContents().equals(";") || isID(token) || isNUM(token)
                || token.getContents().equals("(") || token.getContents().equals("{") || token.getContents().equals("if")
                || token.getContents().equals("while") || token.getContents().equals("return")){
            if (!parse_statement()){
                return false;
            }
            if (!parse_statementListPrime()){
                return false;
            }
            return true;
        } else if (token.getContents().equals("}")){
            return true;
        }
        return false;
    }

    private boolean isID(Tokens token){
        try{
            if (token.getType().equals("ID")){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean isNUM(Tokens token){
        try{
            if (token.getType().equals("NUM")){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean parse_statement() {
        if (token.getContents().equals("(") || token.getContents().equals(";") || isID(token) || isNUM(token)){
            if (!parse_expressionStmt()){
                return false;
            }
        }
        else if (token.getContents().equals("{")){
            if (!parse_compoundStmt()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals("if")){
            if (!parse_selectionStmt()){
                return false;
            }
        }
        else if (token.getContents().equals("while")){
            if (!parse_iterationStmt()){
                return false;
            }
        }
        else if (token.getContents().equals("return")){
            if (!parse_returnStmt()){
                return false;
            }
        }
        return true;
    }

    private boolean parse_iterationStmt() {
        if (token.getContents().equals("while")){
            Accept();
            if (token.getContents().equals("(")){
                Accept();
            } else return false;
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            } else return false;
            if (!parse_statement()){
                return false;
            }
            return true;
        }else return false;
    }

    private boolean parse_expression() {
        if (isID(token)){
            Accept();
            if (!parse_FFF()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals("(")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            } else return false;
            if (!parse_termPrime()){
                return false;
            }
           if (!parse_SSS()){
               return false;
           }
            return true;
        }
        else if (isNUM(token)){
            Accept();
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_SSS()){
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean parse_SSS() {
        if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")){
            if (!parse_relop()){
                return false;
            }
            if (!parse_additiveExpression()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals("+") || token.getContents().equals("-") || token.getContents().equals(",")){
            if (!parse_additiveExpressionPrime()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")){
            return true;
        }
        return false;
    }

    private boolean parse_additiveExpression() {
        if (!parse_term()){
            return false;
        }
        if (!parse_additiveExpressionPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_term() {
        if (!parse_factor()){
            return false;
        }
        if (!parse_termPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_factor() {
        if (token.getContents().equals("(")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
                return true;
            }else return false;
        }else if (isID(token)){
            Accept();
            if (!parse_factorXYZ()){
                return false;
            }
            return true;
        }
        else if (isNUM(token)){
            Accept();
            return true;
        }
        else return false;
    }

    private boolean parse_factorXYZ() {
        if (token.getContents().equals("(")){
            Accept();
            if (!parse_args()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
                return true;
            }else return false;
        }
        else{
            if (!parse_varPrime()){
                return false;
            } else return true;
        }
    }

    private boolean parse_args() {
        if (isID(token) || token.getContents().equals("(") || isNUM(token)){
            if (!parse_argList()){
                return false;
            } else return true;
        }
        else if (token.getContents().equals(")")){
            return true;
        }
        else return false;
    }

    private boolean parse_argList() {
        if (isID(token)){
            Accept();
            if (!parse_CS()){
                return false;
            }else return true;
        }
        else if (token.getContents().equals("(")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            } else return false;
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_FID()){
                return false;
            }
            return true;
        }
        else if (isNUM(token)){
            Accept();
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_FID()){
                return false;
            }else return true;
        }
        else return false;
    }

    private boolean parse_FID() {
        if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")){
            if (!parse_relop()){
                return false;
            }
            if (!parse_additiveExpression()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }
        else{
            if (!parse_additiveExpressionPrime()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }
    }

    private boolean parse_returnStmtPrime() {
        if (token.getContents().equals(";")){
            Accept();
            return true;
        }
        else if (isID(token)){
            Accept();
            if (!parse_CCC()){
                return false;
            }
            return true;
        }
        else if (isNUM(token)){
            Accept();
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_BBB()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals("(")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            }
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_BBB()){
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean parse_CCC() {
        if (token.getContents().equals("(")){
            Accept();
            if (!parse_args()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            } else return false;
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_BBB()){
                return false;
            }
            return true;
        }
        else{
            if (!parse_varPrime()){
                return false;
            }
            if (!parse_AAA()){
                return false;
            }
            return true;
        }
    }

    private boolean parse_AAA() {
        if (token.getContents().equals("=")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(";")){
                Accept();
                return true;
            }
        }
        else {
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_BBB()){
                return false;
            } else return true;
        }
        return false;
    }

    private boolean parse_BBB() {
        if (token.getContents().equals("+") || token.getContents().equals("-") || token.getContents().equals(";")){
            if (!parse_additiveExpressionPrime()){
                return false;
            }
            if (token.getContents().equals(";")){
                Accept();
                return true;
            }else return false;
        }
        else {
            if (!parse_relop()){
                return false;
            }
            if (!parse_additiveExpression()){
                return false;
            }
            if (token.getContents().equals(";")){
                Accept();
                return true;
            } else return false;
        }
    }

    private boolean parse_relop() {
        if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")){
            Accept();
            return true;
        }
        return false;
    }

    private boolean parse_CS() {
        if (token.getContents().equals("(")){
            Accept();
            if (!parse_args()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            } else return false;
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_FID()){
                return false;
            }
            return true;
        }else {
            if (!parse_varPrime()){
                return false;
            }
            if (!parse_EEE()){
                return false;
            }
            return true;
        }
    }

    private boolean parse_EEE() {
        if (token.getContents().equals("=")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }else {
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_FID()){
                return false;
            }
            return true;
        }
    }

    private boolean parse_argListPrime() {
        if (token.getContents().equals(",")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals(")") || token.getContents().equals(";") || token.getContents().equals("]")){
            return true;
        }
        return false;
    }

    private boolean parse_termPrime() {
        if (token.getContents().equals("*") || token.getContents().equals("/")){
            if (!parse_mulop()){
                return false;
            }
            if (!parse_factor()){
                return false;
            }
            if (!parse_termPrime()){
                return false;
            }
        }
        else if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")
                || token.getContents().equals("+") || token.getContents().equals("-") || token.getContents().equals(",")
                || token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")
                || token.getContents().equals("(")){
            return true;
        }
        return true;
    }

    private boolean parse_mulop() {
        if (token.getContents().equals("*") || token.getContents().equals("/")){
            Accept();
            return true;
        }
        return false;
    }

    private boolean parse_FFF() {
       if (token.getContents().equals("(")){
           Accept();
           if (!parse_args()){
               return false;
           }
           if (token.getContents().equals(")")){
               Accept();
           } else return false;
           if (!parse_termPrime()){
               return false;
           }
           if (!parse_SSS()){
               return false;
           }
           return true;
       }
       else {
           if (!parse_varPrime()){
               return false;
           }
           if (!parse_XXX()){
               return false;
           }
           return true;
       }
    }

    private boolean parse_varPrime() {
        if (token.getContents().equals("[")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals("]")){
                Accept();
                return true;
            } else return false;
        }
        else if (token.getContents().equals("=") || token.getContents().equals("+") || token.getContents().equals("-")
                || token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")
                || token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")
                || token.getContents().equals(",") || token.getContents().equals("*") || token.getContents().equals("/")
                || token.getContents().equals("(") || isID(token) || isNUM(token) || token.getContents().equals("int")
                || token.getContents().equals("float") || token.getContents().equals("{") || token.getContents().equals("}")
                || token.getContents().equals("(") || token.getContents().equals("return") || token.getContents().equals("while")
                || token.getContents().equals("if")){
            return true;
        }else return false;
    }

    private boolean parse_XXX() {
        if (token.getContents().equals("=")){
            Accept();
            if (!parse_expression()){
                return false;
            }
            if (!parse_argListPrime()){
                return false;
            }
            return true;
        }
        else{
            if (!parse_termPrime()){
                return false;
            }
            if (!parse_SSS()){
                return false;
            }
            return true;
        }
    }

    private boolean parse_additiveExpressionPrime() {
        if (token.getContents().equals("+") || token.getContents().equals("-")){
            if (!parse_addop()){
                return false;
            }
            if (!parse_term()){
                return false;
            }
            if (!parse_additiveExpressionPrime()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals("<=") || token.getContents().equals("<") || token.getContents().equals(">")
                || token.getContents().equals(">=") || token.getContents().equals("==") || token.getContents().equals("!=")
                || token.getContents().equals(";") || token.getContents().equals(")") || token.getContents().equals("]")
                || token.getContents().equals(",") || token.getContents().equals("(")){
            return true;
        }
        return false;
    }

    private boolean parse_addop() {
        if (token.getContents().equals("+") || token.getContents().equals("-")){
            Accept();
            return true;
        }
        return false;
    }

    private boolean parse_selectionStmt() {
        if (token.getContents().equals("if")){
            Accept();
            if (token.getContents().equals("(")){
                Accept();
            } else return false;
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(")")){
                Accept();
            } else return false;
            if (!parse_statement()){
                return false;
            }
            if (!parse_selectionStmtPrime()){
                return false;
            }
        }else return false;

        return true;
    }

    private boolean parse_selectionStmtPrime() {
        if (token.getContents().equals("else") && (getNextToken().getContents().equals(";") || isID(getNextToken()) ||
                getNextToken().getContents().equals("(") || isNUM(getNextToken()) || getNextToken().getContents().equals("{")
                || getNextToken().getContents().equals("if") || getNextToken().getContents().equals("while") || getNextToken().getContents().equals("return")) ){
            Accept();
            if (!parse_statement()){
                return false;
            }
            return true;
        }
        else if (token.getContents().equals("else") || token.getContents().equals(";") || isID(token)
                || token.getContents().equals("(") || isNUM(token) || token.getContents().equals("{")
                || token.getContents().equals("if") || token.getContents().equals("while") || token.getContents().equals("return")
                || token.getContents().equals("}")){
            return true;
        }
        return false;
    }

    private boolean isKEYWORD(Tokens token) {
        try{
            if (token.getType().equals("KEYWORD")){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    private boolean parse_returnStmt() {
        if (token.getContents().equals("return")){
            Accept();
            if (!parse_returnStmtPrime()){
                return false;
            }else return true;
        }
        return false;
    }


    private boolean parse_expressionStmt() {
        if (token.getContents().equals(";")){
            Accept();
            return true;
        }
        else if (token.getContents().equals("(") || isID(token) || isNUM(token)){
            if (!parse_expression()){
                return false;
            }
            if (token.getContents().equals(";")){
                Accept();
                return true;
            }
        }
        return false;
    }


    private boolean parse_localDeclarations() {
        if (!parse_localDeclarationsPrime()){
            return false;
        }else return true;
    }

    private boolean parse_localDeclarationsPrime() {
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            if (!parse_varDeclaration()){
                return false;
            }
        }
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")){
            if (!parse_localDeclarationsPrime()){
                return false;
            }
        }else if (token.getContents().equals("}") || token.getContents().equals(";") || token.getContents().equals("{")
                || token.getContents().equals("if") || token.getContents().equals("while")
                || token.getContents().equals("return") || isID(token) || isNUM(token) || token.getContents().equals("(")){
            return true;
        }else return false;

        return true;
    }

    private boolean parse_params() {
       if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            if (getNextToken().getContents().equals(")") && token.getContents().equals("void")) {
                Accept();
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
        Tokens nextToken = Main.TokenList.get(n+1);
        return nextToken;
    }

    private boolean parse_paramList() {
        if (!parse_param()){
            return false;
        }
        if (!parse_paramListPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_paramListPrime() {
        if (token.getContents().equals(",")){
            Accept();
            if (!parse_param()){
                return false;
            }
            if (!parse_paramListPrime()){
                return false;
            }
            return true;
        }else if (token.getContents().equals(")")){
            return true;
        }
        return false;
    }


    private boolean parse_param() {
        if (token.getContents().equals("int") || token.getContents().equals("void") || token.getContents().equals("float")) {
            Accept();
        } else return false;
        if (isID(token)){
            Accept();
        } else return false;
        if (!parse_paramPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_paramPrime() {
        if (token.getContents().equals("[")){
            Accept();
            if (token.getContents().equals("]")){
                Accept();
                return true;
            }
        }else if (token.getContents().equals(")") || token.getContents().equals(",")){
            return true;
        }
        return false;
    }

    private boolean parse_varDeclaration() {
        if (!parse_TypeSpecifier()){
            return false;
        }
        if (isID(token)){
            Accept();
        }else {
            return false;
        }
        if (!parse_varDeclarationPrime()){
            return false;
        }
        return true;
    }

    private boolean parse_varDeclarationPrime() {
        if (token.getContents().equals(";")){
            Accept();
            return true;
        }else if (token.getContents().equals("[")){
            Accept();
            if (isNUM(token)){
                Accept();
                if (token.getContents().equals("]")){
                    Accept();
                    if (token.getContents().equals(";")){
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
            if ((n + 1) <= Main.TokenList.size() - 1) {
                token = Main.TokenList.get(++n);
            }
        }catch (Exception e){
//
        }
    }

    private boolean parse_TypeSpecifier() {
        if (token.getContents().equals("int") || token.getContents().equals("float") || token.getContents().equals("void")){
            Accept();
            return true;
        }else return false;
    }
}

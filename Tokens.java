public class Tokens {

    private String type;
    private String contents;
    private Boolean isValid = true;
    private int depth = 0;
    private int commentDepth = 0;
    private Boolean isComment;
    private Boolean isArray = false;
    private String declaredType = "null";
    private int arraySize;
    private Boolean isMethodCall;

    public Tokens(String token){
        contents = token;
        if (isNumeric(token)){
            type = "NUM";
        }else if (isAlpha(token)){
            for (int i = 0; i < Main.keywords.size(); i++){
                if (token.equals(Main.keywords.get(i).trim())){
                    type = "KEYWORD";
                    break;
                }else if (i == Main.keywords.size() - 1){
                    type = "ID";
                    break;
                }
            }
        }
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }


    @Override
    public String toString() {
        return (String.format("Contents= %5s isArray= %5s declaredType= %5s ArraySize= %5s ",Function.ANSI_PURPLE+ contents + Function.ANSI_RESET, Function.ANSI_GREEN + isArray+
                Function.ANSI_RESET, Function.ANSI_RED+ declaredType+ Function.ANSI_RESET, Function.ANSI_CYAN + arraySize + Function.ANSI_RESET));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContents() {
        return contents;
    }

    public Boolean getIsComment() {
        return isComment;
    }

    public void setIsComment(Tokens token) {
       if (token.commentDepth != 0){
           isComment = true;
       }

    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getCommentDepth() {
        return commentDepth;
    }

    public void setCommentDepth(int commentDepth) {
        this.commentDepth = commentDepth;
    }

    public int getDepth() {
        return depth;
    }

    public Boolean getArray() {
        return isArray;
    }

    public void setArray(Boolean array) {
        isArray = array;
    }

    public String getDeclaredType() {
        return declaredType;
    }

    public void setDeclaredType(String declaredType) {
        this.declaredType = declaredType;
    }

    public Boolean getMethodCall() {
        return isMethodCall;
    }

    public void setMethodCall(Boolean methodCall) {
        isMethodCall = methodCall;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }
}

public class Tokens {

    private String type;
    private String contents;
    private Boolean isValid = true;
    private int depth = 0;
    private int commentDepth = 0;
    private Boolean isArray = false;
    private String declaredType = "null";
    private int arraySize;
    private String trueValue;

    Tokens(String token){
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

    private static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    private boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    void setValid(Boolean valid) {
        isValid = valid;
    }


    @Override
    public String toString() {
        return (String.format("Contents= %5s isArray= %5s declaredType= %5s ArraySize= %5s ",contents, isArray, declaredType, arraySize));
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    String getContents() {
        return contents;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    int getCommentDepth() {
        return commentDepth;
    }

    void setCommentDepth(int commentDepth) {
        this.commentDepth = commentDepth;
    }

    int getDepth() {
        return depth;
    }

    Boolean getArray() {
        return isArray;
    }

    void setArray(Boolean array) {
        isArray = array;
    }

    String getDeclaredType() {
        return declaredType;
    }

    void setDeclaredType(String declaredType) {
        this.declaredType = declaredType;
    }

    Boolean getValid() {
        return isValid;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public String getTrueValue() {
        return trueValue;
    }

    public void setTrueValue(String trueValue) {
        this.trueValue = trueValue;
    }
}

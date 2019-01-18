public class Tokens {

    private String type;
    private String contents;
    private Boolean isValid = true;
    private int depth = 0;


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
        return (String.format("Type =%s Contents =%s isValid =%s depth =%s", type, contents, isValid, depth));
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

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Boolean getValid() {
        return isValid;
    }
}

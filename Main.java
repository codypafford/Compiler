import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static int depthCounter = 0;
    static int commentDepth = 0;
    static ArrayList<String> opTable = new ArrayList<>();
    static ArrayList<Tokens> list = new ArrayList<>();
    static ArrayList<String> keywords = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("missing file name...");
            System.exit(0);
        }
        createKeywordTable();
        createOpTable();
        readFile(args);
        String x = "#123.45f";
        System.out.println(containsFloat(x));
    }

    private static Boolean containsFloat(String s){
        Pattern regex = Pattern.compile(".*[+-]?([0-9]*[.])[0-9]+.*");
        Matcher matcher = regex.matcher(s);
        return matcher.matches();
    }

    private static void createKeywordTable(){
        keywords.add("else");
        keywords.add("if");
        keywords.add("int");
        keywords.add("return");
        keywords.add("void");
        keywords.add("while");
    }
//  this will split strings by letters and numbers
    private static void check(String str) {
        if (containsFloat(str)){
            ///HOW DO I KEEP THE SECOND PART OF MY SPLIT!!!!!!!!!!!!!!!!!!!!!!
         //   String [] p = str.split("([-+]?\\d+(?:(?:\\.\\d+)?[eE][-+]?\\d+|\\.\\d+))(.*)$");
          //  System.out.println(p[0]);
          //  str = p[0];
          //  System.out.println(p[1]);
           // Tokens token = new Tokens(p[1]);
           // list.add(token);
        }
        String[] part = str.split("\\b");
          //check if valid before breaking apart
           if(part.length==0){
               Tokens token = new Tokens(str);
               determineDepthUp(token);
               token.setDepth(depthCounter);
               isLetterandNumberCombo(token);
               if(token.getCommentDepth() == 0){
                   list.add(token);
               }
               determineDepthDown(token);

           }else{
               for (int i = 0; i < part.length; i++){
                   if(part[i].length() > 0 && !onlyLettersandDigits(part[i])){
                       splitOperators(part[i]);

                   }else{
                       Tokens token = new Tokens(part[i]);
                       determineDepthUp(token);
                       token.setDepth(depthCounter);
                       token.setCommentDepth(commentDepth);
                       isLetterandNumberCombo(token);
                       if(token.getCommentDepth() == 0){
                           list.add(token);
                       }
                       determineDepthDown(token);


                   }

               }
           }
    }

    private static void isLetterandNumberCombo(Tokens token) {
        if (token.getContents().matches("(?i)(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+")){
            token.setValid(false);
        }
    }

    private static void determineDepthUp(Tokens token) {
        if (token.getContents().contains("{")){
            depthCounter++;
        }
        if(token.getContents().contains("/*")){
            commentDepth++;
        }
    }

    private static void determineDepthDown(Tokens token) {
        if (token.getContents().contains("}")){
            depthCounter--;
        }
        if(token.getContents().contains("*/")){
            commentDepth--;
        }
    }
//ALL SPECIAL CHARACTERS AND PUNCTUATION COME HERE
    private static void splitOperators(String string){
        if (string.length() > 1) {
            while (string.length() > 0) {
                for (int i = 0; i < opTable.size(); i++) {
                    if (string.startsWith(opTable.get(i))) {
                        Tokens token = new Tokens(opTable.get(i));
                        determineDepthUp(token);
                        token.setDepth(depthCounter);
                        token.setCommentDepth(commentDepth);
                        isLetterandNumberCombo(token);
                        if(token.getCommentDepth() == 0){
                            list.add(token);
                        }
                        determineDepthDown(token);

                        if (opTable.get(i).length() == 1) {
                            string = string.substring(1);
                        } else if (opTable.get(i).length() == 2) {
                            string = string.substring((2)).trim();
                        }
                        break;
                    }else if (i == opTable.size()-1 ){
                        Tokens token = new Tokens(String.valueOf(string.charAt(0)));
                        token.setValid(false);
                        determineDepthUp(token);
                        token.setDepth(depthCounter);
                        token.setCommentDepth(commentDepth);
                        isLetterandNumberCombo(token);
                        if(token.getCommentDepth() == 0){
                            list.add(token);
                        }
                        determineDepthDown(token);

                        if (string.length() > 1){
                            string = string.substring((1)).trim();
                            break;
                        }
                    }
                }
            }
        }else{
            Tokens token = new Tokens(string);
            determineDepthUp(token);
            token.setDepth(depthCounter);
            token.setCommentDepth(commentDepth);
            checkValidity(token);
            isLetterandNumberCombo(token);
            if(token.getCommentDepth() == 0){
                list.add(token);
            }
            determineDepthDown(token);

        }
    }

    private static void checkValidity(Tokens token) {
        for (int i = 0; i < opTable.size() - 1; i++) {
            if (token.getContents().startsWith(opTable.get(i))) {
                return;
            }
        }
        token.setValid(false);
    }

    private static Boolean onlyLettersandDigits(String string){
        return string.matches("^[A-z0-9]+$");
    }


    private static void createOpTable() {
        opTable.add("<=");
        opTable.add(">=");
        opTable.add("==");
        opTable.add("!=");
        opTable.add("/*");
        opTable.add("*/");
        opTable.add("//");
        opTable.add(">");
        opTable.add("<");
        opTable.add(",");
        opTable.add("+");
        opTable.add("-");
        opTable.add("*");
        opTable.add("/");
        opTable.add("=");
        opTable.add(";");
        opTable.add("(");
        opTable.add(")");
        opTable.add("[");
        opTable.add("]");
        opTable.add("{");
        opTable.add("}");

    }

    private static void readFile(String[] args) throws IOException {
        try {
            FileReader fr = new FileReader(args[0]);
         //   File file = new File(args[0]);
            String file = new Scanner(new File(args[0])).useDelimiter("\\Z").next();
          //  System.out.println(file);

            Scanner s = new Scanner(file);

            Stack<Character> stack = new Stack<Character>();
            BufferedReader br = new BufferedReader(fr);
            StringBuffer buffer = new StringBuffer();
            int count = 0;
            String line;
            // REMOVE MULTILINE COMMENTS
            String result=file.replaceAll( "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 ");
            System.out.println(result);
            System.out.println("*************************************************************************");
            System.out.println(file);
            s = new Scanner(file);
            while (s.hasNextLine()) {
                line = s.nextLine();
                if(!line.isEmpty()) {
                    String[] lexemes = line.split(" ");
                    for (String lexeme : lexemes) {
                        if(isSinglelineComment(lexeme)) {
                            break;
                        }
                        if (lexeme.trim().length() > 0){
                            // HANDLES COMMENTS // AT END OF STRING LIKE FUNCTION(INT X)//
                            if (lexeme.endsWith("//") || lexeme.startsWith("//")){
                                break;
                            }
                            //split lexeme up even further---->
                            check(lexeme);
                        }
                    }
                   // System.out.println(Arrays.asList(lexemes));
                   // System.out.println("*************************************************************************");
                }
            }
            for (Tokens token : list) {
                System.out.println(token);
            }
         // System.out.println(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean isSinglelineComment(String s) {
        Pattern regex = Pattern.compile("//.*");
        Matcher matcher = regex.matcher(s);

        return matcher.matches();
    }

}
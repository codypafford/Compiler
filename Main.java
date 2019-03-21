
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
    static ArrayList<Tokens> TokenList = new ArrayList<>();
    static ArrayList<Tokens> tokensForSemantics = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("missing file name...");
            System.exit(0);
        }
        createKeywordTable();
        createOpTable();
        readFile(args);
        new Parser();
        new SemanticAnalyzer();
        SemanticAnalyzer.functionList.printArray();


    }


    private static void readFile(String[] args) throws IOException {
        try {
            String file = new Scanner(new File(args[0])).useDelimiter("\\Z").next();

            Scanner s = new Scanner(file);

            String line = "";
            // REMOVE MULTILINE COMMENTS
            //System.out.println("INPUT:" + file);
           // System.out.println("\n");
            s = new Scanner(file);
            while (s.hasNextLine()) {
                line = s.nextLine();
                if (!line.isEmpty()) {
                    String[] lexemes = line.split(" ");
                    for (String lexeme : lexemes) {
                        if (isSinglelineComment(lexeme)) {
                            break;
                        }
                        if (lexeme.trim().length() > 0) {
                            // HANDLES COMMENTS // AT END OF STRING LIKE FUNCTION(INT X)//
                            if (lexeme.endsWith("//") || lexeme.startsWith("//")) {
                                break;
                            }
                            //split lexeme up even further---->
                            //Lexeme is the chunks split by whitespace
                            check(lexeme);
                        }
                    }
                }
  //              System.out.println("Input: " + line);
                try{
                for (Tokens token : list) {
                    if (token.getType() != null) {
                      //  System.out.println(token.getType() + ":" + token.getContents());
                    } else {
                        if (token.getValid()) {
                          //  System.out.println(token.getContents());
                        } else {
                          //  System.out.println("Error: " + token.getContents());
                        }
                    }
                  //  System.out.println(token);
                }
                    tokensForSemantics.addAll(list);
                    TokenList.addAll(list);
                    list.clear();

            }catch (Exception e){
                    //
                }

              //  System.out.println(token);
                //  System.out.println("");
            }

            // System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // System.out.println("\n\n\n");

    }


    //  STR IS EACH CHUNK SPLIT UP BY WHITESPACE
    private static void check(String str) {
        if (containsFloat(str)) {
            String pattern = "\\d+(?:\\.\\d+)?(E(\\+|-)\\d)?|\\D+";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);

            while (m.find()) {
                if (containsFloat(m.group(0))) {
                    Tokens token = new Tokens(m.group());
                    determineDepthUp(token);
                    token.setDepth(depthCounter);
                    try {
                        int x = Integer.parseInt(m.group(0));
                        token.setType("NUM");
                    } catch (Exception e) {
                        token.setType("FLOAT");
                    }
                    if (token.getCommentDepth() == 0) {
                        list.add(token);
                    }
                    determineDepthDown(token);

                } else {
                    String[] piece = m.group(0).split("\\b");
                    for (String s : piece) {
                        if (s.length() > 0 && !onlyLettersandDigits(s)) {
                            splitOperators(s); //SPLIT UP SPECIAL CHARACTERS EVEN FURTHER

                        } else {
                            //ONLY INTEGERS AND LETTERS COME HERE
                            Tokens token = new Tokens(s);
                            determineDepthUp(token);
                            token.setDepth(depthCounter);
                            //    token.setCommentDepth(commentDepth);
                            isLetterandNumberCombo(token);
                            if (token.getCommentDepth() == 0) {
                                list.add(token);
                            }
                            determineDepthDown(token);
                        }
                    }

                }
            }
        } else {
            //THIS SPLITS BY PUNCTUATION
            String[] part = str.split("\\b|((?<=_)|(?=_))");
            //IF LENGTH IS 0 THEN CREATE A TOKEN OUT OF IT
            if (part.length == 0) {
                Tokens token = new Tokens(str);
                determineDepthUp(token);
                token.setDepth(depthCounter);
                isLetterandNumberCombo(token);
                // checkValidity(token);
                if (token.getCommentDepth() == 0) {
                    list.add(token);
                }
                determineDepthDown(token);

            } else {
                for (int i = 0; i < part.length; i++) {
                    //IF LENGTH IS > 0 AND IT HAS SPECIAL CHARACTERS/PUNCTUATION
                    //SINGLE CHARACTER PUNCTUATION COMES HERE
                    if (part[i].length() > 0 && !onlyLettersandDigits(part[i])) {
                        splitOperators(part[i]); //SPLIT UP SPECIAL CHARACTERS EVEN FURTHER

                    } else {
                        //ONLY INTEGERS AND LETTERS COME HERE
                        Tokens token = new Tokens(part[i]);
                        determineDepthUp(token);
                        token.setDepth(depthCounter);
                        //   token.setCommentDepth(commentDepth);
                        isLetterandNumberCombo(token);
                        if (token.getCommentDepth() == 0) {
                            list.add(token);
                        }
                        determineDepthDown(token);
                    }
                }
            }
        }
    }


    //ALL SPECIAL CHARACTERS AND PUNCTUATION COME HERE
    private static void splitOperators(String string) {
        if (string.length() > 1) {
            while (string.length() > 0) {
                for (int i = 0; i < opTable.size(); i++) {
                    if (string.startsWith(opTable.get(i))) {
                        Tokens token = new Tokens(opTable.get(i));
                        determineDepthUp(token);
                        token.setDepth(depthCounter);
                        isLetterandNumberCombo(token);
                        checkValidity(token);
                        if (token.getCommentDepth() == 0) {
                            list.add(token);
                        }
                        determineDepthDown(token);

                        if (opTable.get(i).length() == 1) {
                            string = string.substring(1);
                        } else if (opTable.get(i).length() == 2) {
                            string = string.substring((2)).trim();
                        }
                        break;

                    } else if (i == opTable.size() - 1) {
                        Tokens token = new Tokens(String.valueOf(string.charAt(0)));
                        token.setValid(false);
                        determineDepthUp(token);
                        token.setDepth(depthCounter);
                        isLetterandNumberCombo(token);
                        if (token.getCommentDepth() == 0) {
                            list.add(token);
                        }
                        determineDepthDown(token);

                        if (string.length() > 1) {
                            string = string.substring((1)).trim();
                            break;
                        }
                        if (i == opTable.size() - 1) {
                            return;
                        }
                    }
                }
            }
        } else {
            Tokens token = new Tokens(string);
            determineDepthUp(token);
            token.setDepth(depthCounter);
            checkValidity(token);
            isLetterandNumberCombo(token);
            if (token.getCommentDepth() == 0) {
                list.add(token);
            }
            determineDepthDown(token);

        }
    }


    private static boolean isSinglelineComment(String s) {
        Pattern regex = Pattern.compile("//.*");
        Matcher matcher = regex.matcher(s);

        return matcher.matches();
    }

    public static Boolean containsFloat(String s) {
        Pattern regex = Pattern.compile(".*\\d+(\\.\\d+)?(E(\\+|-)?\\d+)?.*");
        Matcher matcher = regex.matcher(s);
        return matcher.matches();
    }

    private static void createKeywordTable() {
        keywords.add("else");
        keywords.add("if");
        keywords.add("int");
        keywords.add("return");
        keywords.add("void");
        keywords.add("while");
        keywords.add("float");
    }

    private static void checkValidity(Tokens token) {
        for (int i = 0; i < opTable.size() - 1; i++) {
            if (token.getContents().startsWith(opTable.get(i))) {
                return;
            }
        }
        token.setValid(false);

    }

    private static Boolean onlyLettersandDigits(String string) {
        return string.matches("^[A-Za-z0-9]+$");
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
        opTable.add(".");

    }

    private static void isLetterandNumberCombo(Tokens token) {
        if (token.getContents().matches("(?i)(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+")) {
            token.setValid(false);
        }
    }

    private static void determineDepthUp(Tokens token) {
        if (token.getContents().contains("/*")) {
            commentDepth++;
        }
        token.setCommentDepth(commentDepth);
        if (token.getContents().contains("{") && token.getCommentDepth() == 0) {
            depthCounter++;
        }
    }

    private static void determineDepthDown(Tokens token) {
        if (token.getContents().contains("*/")) {
            commentDepth--;
            if (commentDepth < 0) {
                commentDepth = 0;
            }
        }
        token.setCommentDepth(commentDepth);
        if (token.getContents().contains("}") && token.getCommentDepth() == 0) {
            depthCounter--;
            if (commentDepth < 0) {
                commentDepth = 0;
            }
        }
    }

}
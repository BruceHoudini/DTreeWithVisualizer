import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class LexicalAnalyzer {
    private List<Token> tokenQ = new ArrayList<>();
    private String[] inputs;
    public LexicalAnalyzer(File source){
        try {
            String str;
            Scanner scan = new Scanner(source);
            str = scan.nextLine();
            while(scan.hasNext())
                str += scan.nextLine();

            inputs = str.split("\\b|(?=\\p{Punct})\\B");
            for( int i = 0; i < inputs.length; i++){
                if(inputs[i].length() > 1)
                    inputs[i] = inputs[i].replaceAll("\\s+", "");
            }


            //for (int i = 0; i < inputs.length; i++)
            //    System.out.println("a[" + i + "] = " + inputs[i]);

            populateTokenQueue();

        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void populateTokenQueue(){
        for(int i = 0; i < inputs.length; i++)
            tokenQ.add(makeToken(inputs[i]));
    }
    private Token makeToken(String lexeme){
        TokenType type;
        if(lexeme.length() == 1) {
            switch (lexeme) {
                case "<": type = TokenType.OPENB;
                    break;
                case ">": type = TokenType.CLOSEB;
                    break;
                case "\"": type = TokenType.QUOTE;
                    break;
                case "/": type = TokenType.SLASH;
                    break;
                case "=": type = TokenType.EQUALS;
                    break;
                case " ": type = TokenType.SPACE;
                    break;
                default: type = TokenType.TEXT;
                    break;
            }
        }
        else {
            String temp = lexeme;
            switch (temp.toLowerCase()) {
                case "root": type = TokenType.ROOT;
                    break;
                case "node": type = TokenType.NODE;
                    break;
                case "behavior": type = TokenType.BEHAVIOR;
                    break;
                case "response": type = TokenType.RESPONSE;
                    break;
                default: type = TokenType.TEXT;
                    break;
            }
        }

        return new Token(lexeme, type);
    }
    public Token pop(){
        //System.out.println(tokenQ.get(0).getLexeme());
        return tokenQ.remove(0);
    }
    public Token peek(){
        return tokenQ.get(0);
    }
    public boolean empty(){
        return tokenQ.isEmpty();
    }

}

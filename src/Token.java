/**
 * Created by Weston Ford on 2/12/2016.
 */
public class Token {
    private String lexeme;
    private TokenType tokType;


    public Token(String lexeme, TokenType tokType){
        this.lexeme = lexeme;
        this.tokType = tokType;
    }

    //Getter methods for lexeme and tokType
    public String getLexeme() {
        return lexeme;
    }
    public TokenType getTokType() {
        return tokType;
    }
}

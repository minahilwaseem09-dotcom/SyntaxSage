package lexer;

public class Token {

    // Token type constants
    public static final String KEYWORD    = "KEYWORD";
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String SYMBOL     = "SYMBOL";
    public static final String LITERAL    = "LITERAL";
    public static final String UNKNOWN    = "UNKNOWN";

    public String value;       // actual text: "public", "{", "x", "5"
    public String type;        // one of the constants above
    public int    lineNumber;  // which line of the file this came from

    public Token(String value, String type, int lineNumber) {
        this.value      = value;
        this.type       = type;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "[Line " + String.format("%3d", lineNumber) + "] "
                + String.format("%-12s", type) + " : \"" + value + "\"";
    }

}

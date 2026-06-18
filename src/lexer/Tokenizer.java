package lexer;

import structures.TokenLinkedList;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Tokenizer {

    // All Java reserved keywords loaded at startup
    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        String[] kw = {
                "abstract","assert","boolean","break","byte","case","catch",
                "char","class","const","continue","default","do","double",
                "else","enum","extends","final","finally","float","for",
                "goto","if","implements","import","instanceof","int","interface",
                "long","native","new","package","private","protected","public",
                "return","short","static","strictfp","super","switch",
                "synchronized","this","throw","throws","transient","true",
                "false","null","try","void","volatile","while"
        };
        for (String k : kw) KEYWORDS.add(k);
    }

    // Symbols that should be treated as individual tokens
    private static final String SYMBOL_CHARS = "{}[]();,.<>!=&|+-*/%^~?:";

    // ── Main method ──
    public TokenLinkedList tokenize(String filePath) throws IOException {
        TokenLinkedList tokenList = new TokenLinkedList();
        BufferedReader reader    = new BufferedReader(new FileReader(filePath));
        String          line;
        int             lineNum   = 0;

        while ((line = reader.readLine()) != null) {
            lineNum++;
            // Remove single-line comments before tokenizing
            int commentIdx = line.indexOf("//");
            if (commentIdx >= 0) line = line.substring(0, commentIdx);

            tokenizeLine(line.trim(), lineNum, tokenList);
        }
        reader.close();
        return tokenList;
    }

    // ── Split one line into tokens ──
    private void tokenizeLine(String line, int lineNum, TokenLinkedList list) {
        if (line.isEmpty()) return;

        int i = 0;
        while (i < line.length()) {
            char c = line.charAt(i);

            // Skip whitespace
            if (Character.isWhitespace(c)) { i++; continue; }

            // String literal  "..."
            if (c == '"') {
                StringBuilder sb = new StringBuilder("\"");
                i++;
                while (i < line.length() && line.charAt(i) != '"') {
                    sb.append(line.charAt(i++));
                }
                sb.append("\"");
                if (i < line.length()) i++; // skip closing "
                list.add(new Token(sb.toString(), Token.LITERAL, lineNum));
                continue;
            }

            // Char literal  '.'
            if (c == '\'') {
                StringBuilder sb = new StringBuilder("'");
                i++;
                while (i < line.length() && line.charAt(i) != '\'') {
                    sb.append(line.charAt(i++));
                }
                sb.append("'");
                if (i < line.length()) i++;
                list.add(new Token(sb.toString(), Token.LITERAL, lineNum));
                continue;
            }

            // Number literal
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < line.length() && (Character.isDigit(line.charAt(i)) || line.charAt(i) == '.')) {
                    sb.append(line.charAt(i++));
                }
                list.add(new Token(sb.toString(), Token.LITERAL, lineNum));
                continue;
            }

            // Word — keyword or identifier
            if (Character.isLetter(c) || c == '_') {
                StringBuilder sb = new StringBuilder();
                while (i < line.length() && (Character.isLetterOrDigit(line.charAt(i)) || line.charAt(i) == '_')) {
                    sb.append(line.charAt(i++));
                }
                String word = sb.toString();
                String type = KEYWORDS.contains(word) ? Token.KEYWORD : Token.IDENTIFIER;
                list.add(new Token(word, type, lineNum));
                continue;
            }

            // Symbol (single character)
            if (SYMBOL_CHARS.indexOf(c) >= 0) {
                list.add(new Token(String.valueOf(c), Token.SYMBOL, lineNum));
                i++;
                continue;
            }

            // Anything else — unknown
            list.add(new Token(String.valueOf(c), Token.UNKNOWN, lineNum));
            i++;
        }
    }

    // ── Check if a word is a keyword ──
    public static boolean isKeyword(String word) {

        return KEYWORDS.contains(word.toLowerCase());
    }

    public static Set<String> getKeywords() {

        return KEYWORDS;
    }

}

package analyzers;

import lexer.Token;
import structures.ErrorQueue;
import structures.TokenLinkedList;
import java.util.*;


public class SemicolonAnalyzer {

    private ErrorQueue errorQueue;

    // These keywords start lines that do NOT need semicolons
    private static final Set<String> NO_SEMICOLON_STARTERS = new HashSet<>(Arrays.asList(
            "if","else","for","while","do","switch","case","default",
            "class","interface","enum","try","catch","finally",
            "public","private","protected","{","}"
    ));

    // These keywords on a line alone or at end DO need semicolons
    private static final Set<String> NEEDS_SEMICOLON_AFTER = new HashSet<>(Arrays.asList(
            "return","break","continue","throw"
    ));

    public SemicolonAnalyzer(ErrorQueue errorQueue) {
        this.errorQueue = errorQueue;
    }

    public boolean analyze(TokenLinkedList tokenList) {
        Token[]                     tokens      = tokenList.toArray();
        Map<Integer, List<Token>>   byLine      = groupByLine(tokens);
        boolean                     foundErrors = false;

        for (Map.Entry<Integer, List<Token>> entry : byLine.entrySet()) {
            int          lineNum    = entry.getKey();
            List<Token>  lineTokens = entry.getValue();

            if (lineTokens.isEmpty()) continue;

            Token first = lineTokens.get(0);
            Token last  = lineTokens.get(lineTokens.size() - 1);

            // Skip lines that definitely don't need semicolons
            if (NO_SEMICOLON_STARTERS.contains(first.value)) continue;

            // Skip lines that are just a closing brace
            if (lineTokens.size() == 1 && last.value.equals("}")) continue;
            if (lineTokens.size() == 1 && last.value.equals("{")) continue;

            // Skip annotation lines (@Override, @SuppressWarnings)
            if (first.value.equals("@")) continue;

            // Lines that have an opening brace at end = method/class header
            if (last.value.equals("{")) continue;

            // Lines with only a comment-like structure
            if (lineTokens.size() < 2) continue;

            // Check: if line looks like a statement and last token is NOT semicolon
            if (looksLikeStatement(lineTokens) && !last.value.equals(";")) {
                errorQueue.enqueueError(
                        "Missing semicolon at end of statement",
                        lineNum
                );
                foundErrors = true;
            }
        }

        return !foundErrors;
    }

    // Returns true if this line of tokens looks like a statement needing a semicolon
    private boolean looksLikeStatement(List<Token> lineTokens) {
        if (lineTokens.isEmpty()) return false;
        Token first = lineTokens.get(0);
        Token last  = lineTokens.get(lineTokens.size() - 1);

        // Must end with an identifier, literal, or closing paren — not a brace
        if (last.value.equals("{") || last.value.equals("}")) return false;

        // Starts with a type keyword or identifier → variable declaration or assignment
        if (first.type.equals(Token.KEYWORD) && NEEDS_SEMICOLON_AFTER.contains(first.value)) return true;

        // Has an assignment operator → assignment statement
        for (Token t : lineTokens) {
            if (t.value.equals("=") || t.value.equals("+")
                    || t.value.equals("-") || t.value.equals("*")) {
                return true;
            }
        }
        // Ends with closing paren → method call like System.out.println(...)
        if (last.value.equals(")")) return true;

        return false;
    }

    // Group all tokens by line number
    private Map<Integer, List<Token>> groupByLine(Token[] tokens) {
        Map<Integer, List<Token>> map = new LinkedHashMap<>();
        for (Token t : tokens) {
            map.computeIfAbsent(t.lineNumber, k -> new ArrayList<>()).add(t);
        }
        return map;
    }



}

package analyzers;

import structures.ErrorQueue;
import structures.MyStack;
import structures.TokenLinkedList;
import lexer.Token;

public class BracketAnalyzer {

    private ErrorQueue errorQueue;

    public BracketAnalyzer(ErrorQueue errorQueue) {
        this.errorQueue = errorQueue;
    }

    public boolean analyze(TokenLinkedList tokenList) {
        MyStack stack       = new MyStack(1000);
        Token[] tokens      = tokenList.toArray();
        boolean foundErrors = false;

        for (Token token : tokens) {
            if (!token.type.equals(Token.SYMBOL)) continue;

            String v = token.value;

            // Opening bracket → push onto stack
            if (v.equals("{") || v.equals("[") || v.equals("(")) {
                stack.push(v, token.lineNumber);
            }

            // Closing bracket → pop and check match
            else if (v.equals("}") || v.equals("]") || v.equals(")")) {

                if (stack.isEmpty()) {
                    // Nothing to match —> extra closing bracket
                    errorQueue.enqueueError(
                            "Unexpected closing bracket '" + v + "' — no matching opener",
                            token.lineNumber
                    );
                    foundErrors = true;
                } else {
                    String top     = stack.peek();
                    int    topLine = stack.peekLine();
                    stack.pop();

                    // Check if types match
                    if (!matches(top, v)) {
                        errorQueue.enqueueError(
                                "Mismatched bracket: opened '" + top
                                        + "' on line " + topLine
                                        + " but closed with '" + v + "'",
                                token.lineNumber
                        );
                        foundErrors = true;
                    }
                }
            }
        }

        // Anything left on stack = unclosed bracket
        while (!stack.isEmpty()) {
            int topLine = stack.peekLine();
            String top  = stack.pop();
            errorQueue.enqueueError(
                    "Unclosed bracket '" + top + "' — never closed",
                    topLine
            );
            foundErrors = true;
        }

        return !foundErrors;
    }

    // Returns true if opener and closer are a valid pair
    private boolean matches(String opener, String closer) {
        if (opener.equals("{") && closer.equals("}"))
            return true;
        if (opener.equals("[") && closer.equals("]"))
            return true;
        if (opener.equals("(") && closer.equals(")"))
            return true;

        return false;
    }
}

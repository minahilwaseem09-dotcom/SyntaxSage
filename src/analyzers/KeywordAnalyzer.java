package analyzers;

import lexer.Token;
import structures.ErrorQueue;
import structures.KeywordBST;
import structures.TokenLinkedList;

public class KeywordAnalyzer {

    private ErrorQueue errorQueue;
    private KeywordBST keywordBST;

    // Common typos of Java keywords → what they should be
    private static final String[][] TYPOS = {
            {"pubilc",   "public"},   {"publci",  "public"},
            {"privte",   "private"},  {"priavte", "private"},
            {"retrun",   "return"},   {"retrn",   "return"},
            {"procteted","protected"},{"statc",   "static"},
            {"clss",     "class"},    {"claas",   "class"},
            {"vodi",     "void"},     {"viod",    "void"},
            {"improt",   "import"},   {"imoprt",  "import"},
            {"pakage",   "package"},  {"pakckage","package"},
            {"booelan",  "boolean"},  {"booelna", "boolean"},
            {"sting",    "String"},   {"stirng",  "String"},
            {"intger",   "int"},      {"intt",    "int"},
            {"whlie",    "while"},    {"wihle",   "while"},
            {"fo",       "for"},      {"fro",     "for"},
            {"tyr",      "try"},      {"ctach",   "catch"},
            {"eles",     "else"},     {"esle",    "else"}
    };

    public KeywordAnalyzer(ErrorQueue errorQueue) {
        this.errorQueue = errorQueue;
        this.keywordBST = new KeywordBST();
        this.keywordBST.loadJavaKeywords();
    }

    public boolean analyze(TokenLinkedList tokenList) {
        Token[] tokens      = tokenList.toArray();
        boolean foundErrors = false;

        for (Token token : tokens) {
            // Only check IDENTIFIER tokens — keywords are already labeled
            if (!token.type.equals(Token.IDENTIFIER)) continue;

            String val   = token.value.toLowerCase();
            String typo  = findTypo(val);

            if (typo != null) {
                errorQueue.enqueueError(
                        "Possible keyword typo: \"" + token.value
                                + "\" — did you mean \"" + typo + "\"?",
                        token.lineNumber
                );
                foundErrors = true;
            }
        }

        return !foundErrors;
    }

    // Check if word looks like a misspelled keyword
    private String findTypo(String word) {
        for (String[] pair : TYPOS) {
            if (pair[0].equals(word)) return pair[1];
        }
        return null; // no typo found
    }

    public KeywordBST getBST() {
        return keywordBST;
    }

}

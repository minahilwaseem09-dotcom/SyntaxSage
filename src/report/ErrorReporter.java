package report;

import structures.ErrorQueue;

public class ErrorReporter {

    private ErrorQueue errorQueue;

    public ErrorReporter(ErrorQueue errorQueue) {
        this.errorQueue = errorQueue;
    }

    public void printReport(String filePath, int totalLines, int totalTokens,
                            boolean b, boolean k, boolean s, boolean f) {
        String line = "=".repeat(55);
        String thin = "-".repeat(55);

        System.out.println("\n" + line);
        System.out.println("           SYNTAXSAGE — SCAN REPORT");
        System.out.println(line);
        System.out.printf("  File     : %s%n", filePath);
        System.out.printf("  Lines    : %d%n", totalLines);
        System.out.printf("  Tokens   : %d%n", totalTokens);
        System.out.println(thin);

        // Module status
        System.out.println("  Module Results:");
        System.out.printf("    [1/4] Bracket Check     (Stack)  : %s%n", status(b));
        System.out.printf("    [2/4] Keyword Check     (BST)    : %s%n", status(k));
        System.out.printf("    [3/4] Semicolon Check   (Queue)  : %s%n", status(s));
        System.out.printf("    [4/4] Flow Analysis     (Graph)  : %s%n", status(f));
        System.out.println(thin);

        // Error/Warning counts
        int errors   = errorQueue.getErrorCount();
        int warnings = errorQueue.getWarningCount();

        if (errorQueue.isEmpty()) {
            System.out.println("  No errors found. Code looks clean!");
        } else {
            System.out.println("  Issues Found:");
            System.out.println();
            errorQueue.printAll();
        }

        System.out.println(thin);
        System.out.printf("  Total Errors   : %d%n", errors);
        System.out.printf("  Total Warnings : %d%n", warnings);
        System.out.println(line);

        // Verdict
        if (errors == 0 && warnings == 0) {
            System.out.println("  VERDICT: ✓ Code passed all checks!");
        } else if (errors > 0) {
            System.out.println("  VERDICT: ✗ Fix errors before compiling.");
        } else {
            System.out.println("  VERDICT: ⚠ Warnings found — review recommended.");
        }
        System.out.println(line + "\n");
    }

    private String status(boolean passed) {

        return passed ? "PASSED " : "ISSUES FOUND ";
    }

}

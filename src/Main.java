import analyzers.*;
import lexer.*;
import report.ErrorReporter;
import structures.*;

import java.io.*;
import java.util.Scanner;

public class Main {

    static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        printBanner();

        while (true) {
            System.out.println("  OPTIONS:");
            System.out.println("    1. Scan a source file");
            System.out.println("    2. Run built-in demo (test files)");
            System.out.println("    3. Show keyword BST info");
            System.out.println("    4. Exit");
            System.out.print("\n  Enter choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("  Enter file path: ");
                    String path = input.nextLine().trim();
                    scanFile(path);
                }
                case "2" -> runDemo();
                case "3" -> showBSTInfo();
                case "4" -> {
                    System.out.println("\n  Goodbye!\n");
                    return;
                }
                default  -> System.out.println("  Invalid choice. Try again.\n");
            }
        }
    }

    // ── Core scan method — runs all 4 analyzers ───
    static void scanFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("\n  ERROR: File not found: " + filePath + "\n");
            return;
        }

        System.out.println("\n" + "=".repeat(55));
        System.out.println("  Scanning: " + filePath);
        System.out.println("=".repeat(55));

        try {
            // ── Phase 2: Tokenize ──
            System.out.print("  Tokenizing file...      ");
            Tokenizer       tokenizer = new Tokenizer();
            TokenLinkedList tokens    = tokenizer.tokenize(filePath);
            int             lineCount = countLines(filePath);
            System.out.println("Done  (" + tokens.size() + " tokens, " + lineCount + " lines)");

            // Shared error queue — all analyzers write here
            ErrorQueue errorQueue = new ErrorQueue();

            // ── Phase 3: Bracket Check (Stack) ──
            System.out.print("  [1/4] Bracket Check...     ");
            BracketAnalyzer bracketAnalyzer = new BracketAnalyzer(errorQueue);
            boolean         bracketOk       = bracketAnalyzer.analyze(tokens);
            System.out.println(bracketOk ? "PASSED " : "ISSUES FOUND ✗");

            // ── Phase 4: Keyword Check (BST) ──
            System.out.print("  [2/4] Keyword Check...     ");
            KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer(errorQueue);
            boolean         keywordOk       = keywordAnalyzer.analyze(tokens);
            System.out.println(keywordOk ? "PASSED " : "ISSUES FOUND ✗");

            // ── Phase 5: Semicolon Check (Queue) ──
            System.out.print("  [3/4] Semicolon Check...   ");
            SemicolonAnalyzer semiAnalyzer = new SemicolonAnalyzer(errorQueue);
            boolean           semiOk       = semiAnalyzer.analyze(tokens);
            System.out.println(semiOk ? "PASSED " : "ISSUES FOUND ✗");

            // ── Phase 6: Flow Analysis (Graph) ───
            System.out.print("  [4/4] Flow Analysis...     ");
            FlowAnalyzer flowAnalyzer = new FlowAnalyzer(errorQueue);
            boolean      flowOk       = flowAnalyzer.analyze(tokens);
            System.out.println(flowOk ? "PASSED " : "ISSUES FOUND ✗");

            // ── Phase 7: Print Report ───
            ErrorReporter reporter = new ErrorReporter(errorQueue);
            reporter.printReport(filePath, lineCount, tokens.size(),
                    bracketOk, keywordOk, semiOk, flowOk);

        } catch (IOException e) {
            System.out.println("\n  ERROR reading file: " + e.getMessage() + "\n");
        }
    }

    // ── Demo: runs all 4 built-in test files ──
    static void runDemo() {
        String[] demoFiles = {
                "test_files/ValidCode.java",
                "test_files/MismatchedBrackets.java",
                "test_files/MissingSemicolon.java",
                "test_files/UnreachableCode.java"
        };
        System.out.println("\n  Running demo on all test files...\n");
        for (String f : demoFiles) {
            scanFile(f);
            System.out.println();
        }
    }

    // ── Show BST keyword info ──
    static void showBSTInfo() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("  KEYWORD BST INFO ");
        System.out.println("=".repeat(55));
        KeywordBST bst = new KeywordBST();
        bst.loadJavaKeywords();
        System.out.println("  Total keywords loaded: " + bst.getNodeCount());
        System.out.println("  Search complexity: O(log n) = O(log "
                + bst.getNodeCount() + ") ≈ "
                + (int)(Math.log(bst.getNodeCount()) / Math.log(2)) + " comparisons max");
        System.out.print("\n  ");
        bst.printInOrder();
        System.out.println("=".repeat(55) + "\n");
    }

    static int countLines(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int count = 0;
        while (reader.readLine() != null) count++;
        reader.close();
        return count;
    }

    static void printBanner() {
        System.out.println();
        System.out.println("  ███████╗██╗   ██╗███╗   ██╗████████╗ █████╗ ██╗  ██╗");
        System.out.println("  ██╔════╝╚██╗ ██╔╝████╗  ██║╚══██╔══╝██╔══██╗╚██╗██╔╝");
        System.out.println("  ███████╗ ╚████╔╝ ██╔██╗ ██║   ██║   ███████║ ╚███╔╝ ");
        System.out.println("  ╚════██║  ╚██╔╝  ██║╚██╗██║   ██║   ██╔══██║ ██╔██╗ ");
        System.out.println("  ███████║   ██║   ██║ ╚████║   ██║   ██║  ██║██╔╝ ██╗");
        System.out.println("  ╚══════╝   ╚═╝   ╚═╝  ╚═══╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝");
        System.out.println();
        System.out.println("         Source Code Syntax Validator & Analyzer");
        System.out.println("         DSA Project — Stack | BST | Queue | Graph");
        System.out.println("  " + "─".repeat(53));
        System.out.println();
    }
}

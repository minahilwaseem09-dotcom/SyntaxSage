
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
            System.out.println("    3. Show ALL data structures");
            System.out.println("    4. Exit");
            System.out.print("\n  Enter choice: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("  Tip: paste the full path to your file.");
                    System.out.println("  Example: C:\\Users\\Malai\\Desktop\\Test.java");
                    System.out.print("\n  Enter file path: ");
                    String path = input.nextLine().trim();
                    path = cleanPath(path);
                    scanFile(path);
                }
                case "2" -> runDemo();
                case "3" -> showAllStructures();
                case "4" -> {
                    System.out.println("\n\tGoodbye! Thank you for using SyntaxSage");
                    System.out.println("\tSee you next Time");
                    return;
                }
                default  -> System.out.println("  Invalid choice. Try again.\n");
            }
        }
    }

    // Handles: surrounding quotes, extra spaces, mixed slashes
    static String cleanPath(String path) {
        if (path == null) return "";

        path = path.trim();

        // Remove surrounding double or single quotes
        // (Windows "Copy as path" wraps paths in quotes)
        if (path.length() >= 2) {
            char first = path.charAt(0);
            char last  = path.charAt(path.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                path = path.substring(1, path.length() - 1);
            }
        }

        return path.trim();
    }
    static void scanFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("\n  ERROR: File not found.");
            System.out.println("  You entered: " + filePath);
            System.out.println();
            System.out.println("  Common fixes:");
            System.out.println("    1. Make sure the file ends with .java");
            System.out.println("    2. On Windows, copy the path using");
            System.out.println("       Shift + Right-click → 'Copy as path'");
            System.out.println("    3. Paste it exactly as copied (quotes are OK,");
            System.out.println("       SyntaxSage removes them automatically)");
            System.out.println("    4. Example of a valid path:");
            System.out.println("       C:\\Users\\Malai\\Desktop\\MyCode.java");
            System.out.println();
            return;
        }

        if (file.isDirectory()) {
            System.out.println("\n  ERROR: That path is a folder, not a file.");
            System.out.println("  Please point to a specific .java file.\n");
            return;
        }

        System.out.println("\n" + "=".repeat(55));
        System.out.println("  Scanning: " + filePath);
        System.out.println("=".repeat(55));

        try {
            //  Tokenize
            System.out.print("  Tokenizing file...         ");
            Tokenizer       tokenizer = new Tokenizer();
            TokenLinkedList tokens    = tokenizer.tokenize(filePath);
            int             lineCount = countLines(filePath);
            System.out.println("Done  (" + tokens.size() + " tokens, " + lineCount + " lines)");

            // Shared error queue — all analyzers write here
            ErrorQueue errorQueue = new ErrorQueue();

            //  Bracket Check (Stack)
            System.out.print("  [1/4] Bracket Check...     ");
            BracketAnalyzer bracketAnalyzer = new BracketAnalyzer(errorQueue);
            boolean         bracketOk       = bracketAnalyzer.analyze(tokens);
            System.out.println(bracketOk ? "PASSED ✓" : "ISSUES FOUND ✗");

            //  Keyword Check (BST)
            System.out.print("  [2/4] Keyword Check...     ");
            KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer(errorQueue);
            boolean         keywordOk       = keywordAnalyzer.analyze(tokens);
            System.out.println(keywordOk ? "PASSED ✓" : "ISSUES FOUND ✗");

            // Semicolon Check (Queue)
            System.out.print("  [3/4] Semicolon Check...   ");
            SemicolonAnalyzer semiAnalyzer = new SemicolonAnalyzer(errorQueue);
            boolean           semiOk       = semiAnalyzer.analyze(tokens);
            System.out.println(semiOk ? "PASSED ✓" : "ISSUES FOUND ✗");

            //  Flow Analysis (Graph)
            System.out.print("  [4/4] Flow Analysis...     ");
            FlowAnalyzer flowAnalyzer = new FlowAnalyzer(errorQueue);
            boolean      flowOk       = flowAnalyzer.analyze(tokens);
            System.out.println(flowOk ? "PASSED ✓" : "ISSUES FOUND ✗");

            //  Print Report
            ErrorReporter reporter = new ErrorReporter(errorQueue);
            reporter.printReport(filePath, lineCount, tokens.size(),
                    bracketOk, keywordOk, semiOk, flowOk);

        } catch (IOException e) {
            System.out.println("\n  ERROR reading file: " + e.getMessage() + "\n");
        }
    }

    //  runs all 4 built-in test files
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

    //  Show ALL data structures with live data
    static void showAllStructures() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ALL 5 DATA STRUCTURES IN ACTION");
        System.out.println("=".repeat(60));

        String demoFile = "test_files/MissingSemicolon.java";
        File   file      = new File(demoFile);

        if (!file.exists()) {
            System.out.println("  Demo file not found: " + demoFile);
            System.out.println("  (Make sure test_files folder is in your project root)\n");
            return;
        }

        try {
            //  1. LinkedList
            System.out.println("\n  [1] LINKEDLIST — Token Storage");
            System.out.println("  " + "-".repeat(56));
            Tokenizer       tokenizer = new Tokenizer();
            TokenLinkedList tokens    = tokenizer.tokenize(demoFile);
            System.out.println("  File scanned: " + demoFile);
            System.out.println("  Total tokens stored in LinkedList: " + tokens.size());
            System.out.println("  First 8 tokens (head → forward):");
            for (int i = 0; i < Math.min(8, tokens.size()); i++) {
                System.out.println("    " + tokens.get(i));
            }

            //  2. Stack
            System.out.println("\n  [2] STACK — Bracket Matching");
            System.out.println("  " + "-".repeat(56));
            MyStack demoStack = new MyStack(50);
            System.out.println("  Pushing brackets as they are read:");
            for (Token t : tokens.toArray()) {
                if (t.value.equals("{") || t.value.equals("(") || t.value.equals("[")) {
                    demoStack.push(t.value, t.lineNumber);
                    System.out.println("    push('" + t.value + "')  → " + demoStack);
                } else if (t.value.equals("}") || t.value.equals(")") || t.value.equals("]")) {
                    if (!demoStack.isEmpty()) {
                        String popped = demoStack.pop();
                        System.out.println("    pop()  matched '" + popped + "' with '" + t.value + "'  → " + demoStack);
                    }
                }
            }
            System.out.println("  Final stack state: " + demoStack
                    + (demoStack.isEmpty() ? "  (all brackets matched)" : "  (unclosed brackets remain!)"));

            //  3. BST
            System.out.println("\n  [3] BST — Keyword Dictionary");
            System.out.println("  " + "-".repeat(56));
            KeywordBST bst = new KeywordBST();
            bst.loadJavaKeywords();
            System.out.println("  Total keywords loaded: " + bst.getNodeCount());
            System.out.println("  Search complexity: O(log " + bst.getNodeCount() + ") ≈ "
                    + (int)(Math.log(bst.getNodeCount()) / Math.log(2)) + " comparisons max");
            System.out.println("  Sample searches:");
            String[] testWords = {"public", "class", "pubilc", "while"};
            for (String w : testWords) {
                System.out.println("    search(\"" + w + "\") → " + (bst.search(w) ? "FOUND ✓" : "NOT FOUND ✗"));
            }

            //  4. Queue
            System.out.println("\n  [4] QUEUE — Error Reporting (FIFO)");
            System.out.println("  " + "-".repeat(56));
            ErrorQueue demoQueue = new ErrorQueue();
            BracketAnalyzer    ba = new BracketAnalyzer(demoQueue);
            KeywordAnalyzer    ka = new KeywordAnalyzer(demoQueue);
            SemicolonAnalyzer  sa = new SemicolonAnalyzer(demoQueue);
            ba.analyze(tokens);
            ka.analyze(tokens);
            sa.analyze(tokens);
            System.out.println("  Errors enqueued during analysis: " + demoQueue.size());
            System.out.println("  Dequeuing in FIFO order (first found = first printed):");
            int n = demoQueue.size();
            for (int i = 0; i < n; i++) {
                System.out.println("    dequeue() →" + demoQueue.dequeue());
            }

            // 5. Graph
            System.out.println("\n  [5] GRAPH — Control Flow Analysis");
            System.out.println("  " + "-".repeat(56));
            ErrorQueue   flowQueue = new ErrorQueue();
            FlowAnalyzer fa        = new FlowAnalyzer(flowQueue);
            fa.analyze(tokens);
            ControlFlowGraph graph = fa.getGraph();
            System.out.println("  Total blocks (nodes) created: " + graph.blockCount());
            System.out.println("  Cycle detected (possible infinite loop): "
                    + (graph.hasCycle() ? "YES ⚠" : "NO ✓"));
            graph.printGraph();

            System.out.println("\n" + "=".repeat(60));
            System.out.println("  All 5 structures demonstrated successfully!");
            System.out.println("=".repeat(60) + "\n");

        } catch (IOException e) {
            System.out.println("  ERROR: " + e.getMessage() + "\n");
        }
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
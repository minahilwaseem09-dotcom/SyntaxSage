# SyntaxSage — Source Code Syntax Validator & Analyzer

> A console-based Java tool that reads source code files and detects syntax errors
> using **Stack**, **BST**, **Queue**, **LinkedList**, and **Graph** data structures —
> replicating the core logic of a real compiler.

---


## Project Info

| Field              | Detail                                      |
|--------------------|---------------------------------------------|
| Course             | Data Structures & Algorithms (DSA)          |
| Language           | Java                                        |
| IDE                | IntelliJ IDEA                               |
| Type               | Console-based application                  |
| DSA Structures     | Stack, BST, Queue, LinkedList, Graph        |
| Total Classes      | 14 Java classes across 5 packages           |

---

## What SyntaxSage Does

When you give SyntaxSage a `.java` source file, it runs **4 checks** on it:

```
[1/4]  Bracket Check    → finds mismatched { } [ ] ( )
[2/4]  Keyword Check    → finds keyword typos like "pubilc", "retrun"
[3/4]  Semicolon Check  → finds missing semicolons after statements
[4/4]  Flow Analysis    → finds unreachable code and infinite loops
```

Then prints a complete error report with line numbers.

---

## DSA Structures — Role of Each One

| Structure     | Where Used             | Why This Structure         | Complexity   |
|---------------|------------------------|----------------------------|--------------|
| Stack         | Bracket matching       | LIFO — last opened = first closed | O(n)   |
| BST           | Keyword dictionary     | O(log n) search — faster than list | O(log n) |
| Queue         | Error reporting        | FIFO — errors in order found | O(1)       |
| LinkedList    | Token storage          | Dynamic size, unknown token count | O(n)   |
| Graph + DFS   | Control flow analysis  | Detect cycles and dead nodes | O(V+E)    |

---

## Project Structure

```
SyntaxSage/
├── src/
│   └── syntaxsage/
│       ├── Main.java                   ← Entry point, console menu
│       │
│       ├── lexer/
│       │   ├── Token.java              ← Token object (value, type, lineNumber)
│       │   └── Tokenizer.java          ← Reads file → TokenLinkedList
│       │
│       ├── structures/
│       │   ├── MyStack.java            ← Custom Stack (array-based)
│       │   ├── BSTNode.java            ← BST node
│       │   ├── KeywordBST.java         ← Keyword dictionary, O(log n) search
│       │   ├── ErrorQueue.java         ← FIFO queue for error messages
│       │   ├── TokenLinkedList.java    ← Custom LinkedList for Token objects
│       │   └── CFGraph.java            ← Control Flow Graph + DFS traversal
│       │
│       ├── analyzers/
│       │   ├── BracketAnalyzer.java    ← Uses Stack to match brackets
│       │   ├── KeywordAnalyzer.java    ← Uses BST to detect keyword typos
│       │   ├── SemicolonAnalyzer.java  ← Detects missing semicolons
│       │   └── FlowAnalyzer.java       ← Builds CFG, detects flow issues
│       │
│       └── report/
│           └── ErrorReporter.java      ← Prints final formatted report
│
└── test_files/
    ├── ValidCode.java                  ← Should pass all 4 checks
    ├── MismatchedBrackets.java         ← Contains bracket errors
    ├── MissingSemicolon.java           ← Contains semicolon + keyword typo
    └── UnreachableCode.java            ← Contains unreachable code + infinite loop
```

---

## How to Run in IntelliJ

**Step 1 — Open the project**
```
File → Open → select the SyntaxSage folder
```

**Step 2 — Mark sources root**
```
Right-click src folder → Mark Directory as → Sources Root
```

**Step 3 — Run**
```
Right-click Main.java → Run 'Main.main()'
```

---

## Console Menu

```
  OPTIONS:
    1. Scan a source file
    2. Run built-in demo (test files)
    3. Show keyword BST info
    4. Exit
```

### Option 1 — Scan a source file
Enter any `.java` file path. SyntaxSage runs all 4 analyzers on it and prints the error report.

```
  Enter file path: test_files/MissingSemicolon.java

  Tokenizing file...         Done  (47 tokens, 8 lines)
  [1/4] Bracket Check...     PASSED ✓
  [2/4] Keyword Check...     ISSUES FOUND ✗
  [3/4] Semicolon Check...   ISSUES FOUND ✗
  [4/4] Flow Analysis...     PASSED ✓
```

### Option 2 — Run built-in demo
Automatically scans all 4 test files one by one. Best option to show your teacher during demo.

### Option 3 — Show keyword BST info
Prints the BST contents in alphabetical order (in-order traversal) and shows the O(log n) calculation.

```
  Total keywords loaded: 62
  Search complexity: O(log 62) ≈ 5 comparisons max

  Keywords in BST (alphabetical):
  abstract assert boolean break byte case catch char class ...
```

**Purpose:** Makes the BST visible and provable during viva. Without this, BST works internally but cannot be demonstrated directly.

---

## Sample Output

### Valid file — 0 errors
```
=======================================================
           SYNTAXSAGE — SCAN REPORT
=======================================================
  File     : test_files/ValidCode.java
  Lines    : 18
  Tokens   : 134

  Module Results:
    [1/4] Bracket Check     (Stack)  : PASSED ✓
    [2/4] Keyword Check     (BST)    : PASSED ✓
    [3/4] Semicolon Check   (Queue)  : PASSED ✓
    [4/4] Flow Analysis     (Graph)  : PASSED ✓
  -------------------------------------------------------
  No errors found. Code looks clean!
  -------------------------------------------------------
  Total Errors   : 0
  Total Warnings : 0
  VERDICT: ✓ Code passed all checks!
=======================================================
```

### File with errors
```
=======================================================
           SYNTAXSAGE — SCAN REPORT
=======================================================
  File     : test_files/MissingSemicolon.java
  Lines    : 8
  Tokens   : 47

  Module Results:
    [1/4] Bracket Check     (Stack)  : PASSED ✓
    [2/4] Keyword Check     (BST)    : ISSUES FOUND ✗
    [3/4] Semicolon Check   (Queue)  : ISSUES FOUND ✗
    [4/4] Flow Analysis     (Graph)  : PASSED ✓
  -------------------------------------------------------
  Issues Found:

    [Line   2] ERROR:    Possible keyword typo: "pubilc" — did you mean "public"?
    [Line   3] ERROR:    Missing semicolon at end of statement
    [Line   5] ERROR:    Missing semicolon at end of statement
    [Line   7] ERROR:    Possible keyword typo: "retrun" — did you mean "return"?
  -------------------------------------------------------
  Total Errors   : 4
  Total Warnings : 0
  VERDICT: ✗ Fix errors before compiling.
=======================================================
```

---

## How Each DSA Structure Works

### Stack — Bracket Matching
```
Reads:   {  [  (  )  ]  }
Push {   → Stack: [ { ]
Push [   → Stack: [ {  [ ]
Push (   → Stack: [ {  [  ( ]
Pop  )   → matches ( ✓
Pop  ]   → matches [ ✓
Pop  }   → matches { ✓
Stack empty at end → VALID ✓

But if:
Reads:   {  [  )   ← ) does NOT match [
→ ERROR: Mismatched bracket at line 3
```

### BST — Keyword Dictionary
```
Keywords stored in BST (sorted alphabetically):

           if
          /  \
       class  while
       /        \
     break      void

Search "pubilc" → not in BST → check typo list → "did you mean public?"
Search time: O(log n) = O(log 62) ≈ 5 comparisons
```

### Queue — Error Reporting
```
Error found at line 2  → enqueue → Queue: [ err2 ]
Error found at line 5  → enqueue → Queue: [ err2 → err5 ]
Error found at line 9  → enqueue → Queue: [ err2 → err5 → err9 ]

Print time:
dequeue → print err2
dequeue → print err5
dequeue → print err9
← errors always in order they were found (FIFO)
```

### Graph — Control Flow Analysis
```
Code:
  if (x > 0) {        → node: if-block
      doThis();        → node: if-body
  } else {
      doThat();        → node: else-body
  }
  doFinal();           → node: merge-block

Graph:
  [if-block] → [if-body]   → [merge-block]
             → [else-body] → [merge-block]

DFS finds:
  All nodes reachable → no unreachable code
  No back-edges       → no infinite loops
```

---

## Test Files — Expected Results

| File                    | Bracket | Keyword | Semicolon | Flow   | Errors |
|-------------------------|---------|---------|-----------|--------|--------|
| ValidCode.java          | PASS ✓  | PASS ✓  | PASS ✓    | PASS ✓ | 0      |
| MismatchedBrackets.java | FAIL ✗  | PASS ✓  | PASS ✓    | PASS ✓ | 3      |
| MissingSemicolon.java   | PASS ✓  | FAIL ✗  | FAIL ✗    | PASS ✓ | 4      |
| UnreachableCode.java    | PASS ✓  | PASS ✓  | PASS ✓    | WARN ⚠ | 2 warn |

---

## Viva Questions & Answers

**Q: Why Stack for bracket matching?**
Because bracket matching is a LIFO problem. The last opened bracket must be the first one closed. Stack naturally enforces this order — same technique used by real compilers like javac.

**Q: Why BST instead of ArrayList for keywords?**
ArrayList search is O(n) — checks every element. BST search is O(log n) — eliminates half the options at each step. For 62 keywords, BST needs at most 5 comparisons vs 62 for ArrayList.

**Q: Why Queue for error reporting?**
Errors must appear in the order they occur in the code. Queue is FIFO so the first error found is the first one printed. A Stack would reverse the order which is incorrect.

**Q: What is a Control Flow Graph?**
A directed graph where each node is a code block and each edge is a possible execution path. DFS on this graph finds nodes never visited (unreachable code) and back-edges (infinite loops).

**Q: What is time complexity of your keyword search?**
O(log n) where n is the number of keywords. For 62 keywords, maximum 6 comparisons — much faster than scanning all 62 linearly.

---

## Real-World Connection

> *"This is exactly how production compilers work internally. javac and gcc both use a stack for bracket matching, a symbol table (similar to BST) for keyword lookup, and a control flow graph for optimization and dead code elimination."*

---

*SyntaxSage — DSA Project | University of Central Punjab*
#   S y n t a x S a g e 
 
 

# SyntaxSage 

### Source Code Syntax Validator & Analyzer

A Java-based syntax analysis tool that detects common programming errors using core Data Structures and Algorithms. The project simulates fundamental compiler operations through custom implementations of Stack, Binary Search Tree (BST), Queue, Linked List, and Graph data structures.

---

##  Project Overview

SyntaxSage analyzes Java source files and identifies syntax-related issues before compilation. It demonstrates how real-world compilers use data structures to perform lexical analysis, syntax validation, and control-flow inspection.

### Implemented Data Structures

| Data Structure | Purpose                   |
| -------------- | ------------------------- |
| Stack          | Bracket matching          |
| BST            | Keyword dictionary lookup |
| Queue          | Error reporting system    |
| Linked List    | Token storage             |
| Graph + DFS    | Control flow analysis     |

---

##  Features

* Detects mismatched brackets
* Detects Java keyword typos
* Detects missing semicolons
* Detects unreachable code
* Detects potential infinite loops
* Generates formatted error reports
* Demonstrates practical DSA applications

---

##  Analysis Modules

### 1. Bracket Analysis (Stack)

Validates matching pairs of:

```text
()
[]
{}
```

### 2. Keyword Analysis (BST)

Detects invalid Java keywords such as:

```java
pubilc
retrun
statci
```

and suggests corrections.

### 3. Semicolon Analysis (Queue)

Detects statements that are missing semicolons.

### 4. Flow Analysis (Graph + DFS)

Builds a Control Flow Graph (CFG) and identifies:

* Unreachable code
* Dead execution paths
* Infinite loops

---

##  Architecture

```text
SyntaxSage
│
├── lexer
│   ├── Token
│   └── Tokenizer
│
├── structures
│   ├── MyStack
│   ├── KeywordBST
│   ├── ErrorQueue
│   ├── TokenLinkedList
│   └── CFGraph
│
├── analyzers
│   ├── BracketAnalyzer
│   ├── KeywordAnalyzer
│   ├── SemicolonAnalyzer
│   └── FlowAnalyzer
│
└── report
    └── ErrorReporter
```

---

##  Complexity Analysis

| Operation             | Complexity |
| --------------------- | ---------- |
| Bracket Validation    | O(n)       |
| BST Keyword Search    | O(log n)   |
| Queue Operations      | O(1)       |
| Linked List Traversal | O(n)       |
| DFS Traversal         | O(V + E)   |

---

##  Running the Project

### IntelliJ IDEA

1. Open the project folder.
2. Mark `src` as Sources Root.
3. Run `Main.java`.

```text
Right Click → Main.java → Run Main.main()
```

---

##  Sample Output

```text
[1/4] Bracket Check     PASSED ✓
[2/4] Keyword Check     PASSED ✓
[3/4] Semicolon Check   PASSED ✓
[4/4] Flow Analysis     PASSED ✓

VERDICT: Code passed all checks.
```

---

##  Learning Outcomes

This project demonstrates practical usage of:

* Stack (LIFO processing)
* Binary Search Trees
* Queue-based scheduling
* Linked List storage
* Graph traversal algorithms
* Depth First Search (DFS)
* Compiler design fundamentals

---

##  Author

University of Central Punjab

Data Structures & Algorithms Project

SyntaxSage — Source Code Syntax Validator & Analyzer

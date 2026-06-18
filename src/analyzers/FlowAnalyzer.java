package analyzers;

import lexer.Token;
import structures.ControlFlowGraph;
import structures.ControlFlowGraph.Block;
import structures.ErrorQueue;
import structures.TokenLinkedList;
import java.util.*;

public class FlowAnalyzer {

    private ErrorQueue errorQueue;
    private ControlFlowGraph   graph;

    public FlowAnalyzer(ErrorQueue errorQueue) {
        this.errorQueue = errorQueue;
        this.graph      = new ControlFlowGraph();
    }

    public boolean analyze(TokenLinkedList tokenList) {
        Token[] tokens      = tokenList.toArray();
        boolean foundErrors = false;

        // ── Build the CFG ──────────────────────────────────
        Block  currentBlock   = graph.addBlock("entry", 1);
        Block  prevBlock      = null;
        boolean justReturned  = false;
        int     unreachStart  = -1;
        Set<Integer> loopStartLines = new HashSet<>();

        for (int i = 0; i < tokens.length; i++) {
            Token t = tokens[i];

            // Detect return / break / continue
            if (t.type.equals(Token.KEYWORD)
                    && (t.value.equals("return") || t.value.equals("break")
                    || t.value.equals("continue"))) {
                currentBlock.hasReturn = true;
                justReturned           = true;
                unreachStart           = t.lineNumber + 1;
            }

            // Detect code after return on same or next non-brace line
            else if (justReturned
                    && !t.value.equals("}")
                    && !t.value.equals("{")
                    && !t.value.equals(";")
                    && t.type.equals(Token.KEYWORD)
                    && !t.value.equals("else")) {
                errorQueue.enqueueWarning(
                        "Unreachable code detected after return/break/continue",
                        t.lineNumber
                );
                foundErrors   = true;
                justReturned  = false;
            }

            // Detect if/else → new branches in CFG
            if (t.type.equals(Token.KEYWORD) && t.value.equals("if")) {
                Block ifBlock   = graph.addBlock("if-block",   t.lineNumber);
                Block elseBlock = graph.addBlock("else-block",  t.lineNumber);
                Block mergeBlock= graph.addBlock("merge-block", t.lineNumber);
                graph.addEdge(currentBlock.id, ifBlock.id);
                graph.addEdge(currentBlock.id, elseBlock.id);
                graph.addEdge(ifBlock.id,    mergeBlock.id);
                graph.addEdge(elseBlock.id,  mergeBlock.id);
                prevBlock    = currentBlock;
                currentBlock = mergeBlock;
                justReturned = false;
            }

            // Detect while/for loops → potential cycle in CFG
            if (t.type.equals(Token.KEYWORD)
                    && (t.value.equals("while") || t.value.equals("for"))) {
                Block loopHeader = graph.addBlock("loop-header", t.lineNumber);
                Block loopBody   = graph.addBlock("loop-body",   t.lineNumber);
                Block loopExit   = graph.addBlock("loop-exit",   t.lineNumber);

                graph.addEdge(currentBlock.id, loopHeader.id);
                graph.addEdge(loopHeader.id,   loopBody.id);    // enter loop
                graph.addEdge(loopBody.id,     loopHeader.id);  // back edge (cycle)
                graph.addEdge(loopHeader.id,   loopExit.id);    // exit condition

                loopStartLines.add(t.lineNumber);
                currentBlock = loopExit;
                justReturned = false;
            }
        }

        // ── Check for infinite loops (cycles with no break) ─
        if (graph.hasCycle()) {
            // Find which loop lines have no break nearby
            for (int loopLine : loopStartLines) {
                if (!hasBreakNearLoop(tokens, loopLine)) {
                    errorQueue.enqueueWarning(
                            "Possible infinite loop — no break/return found in loop body",
                            loopLine
                    );
                    foundErrors = true;
                }
            }
        }

        return !foundErrors;
    }

    // Check if there is a break/return within a few lines after loop start
    private boolean hasBreakNearLoop(Token[] tokens, int loopLine) {
        for (Token t : tokens) {
            if (t.lineNumber > loopLine && t.lineNumber <= loopLine + 30) {
                if (t.type.equals(Token.KEYWORD)
                        && (t.value.equals("break") || t.value.equals("return"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public ControlFlowGraph getGraph() {
        return graph;
    }

}

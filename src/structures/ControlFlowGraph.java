package structures;

import java.util.*;

public class ControlFlowGraph {

    // ── Inner class: a block of code (one graph node) ───
    public static class Block {
        public int    id;
        public String label;      // e.g. "if-block", "while-body", "method-body"
        public int    startLine;
        public int    endLine;
        public boolean hasReturn; // block ends with a return/break

        public Block(int id, String label, int startLine) {
            this.id         = id;
            this.label      = label;
            this.startLine  = startLine;
            this.endLine    = startLine;
            this.hasReturn  = false;
        }

        @Override
        public String toString() {
            return "Block[" + id + ": " + label
                    + " (lines " + startLine + "-" + endLine + ")]";
        }
    }

    private List<Block> blocks;         // all nodes
    private Map<Integer, List<Integer>> edges; // adjacency list: blockId → list of successor blockIds
    private int               nextId;

    public ControlFlowGraph() {
        blocks  = new ArrayList<>();
        edges   = new HashMap<>();
        nextId  = 0;
    }

    // ── Add a node (code block) ──
    public Block addBlock(String label, int startLine) {
        Block b = new Block(nextId++, label, startLine);
        blocks.add(b);
        edges.put(b.id, new ArrayList<>());
        return b;
    }

    // ── Add a directed edge: from → to ──
    public void addEdge(int fromId, int toId) {
        if (edges.containsKey(fromId)) {
            edges.get(fromId).add(toId);
        }
    }

    // ── DFS: find all reachable blocks from startId ──
    public Set<Integer> findReachableBlocks(int startId) {
        Set<Integer> visited = new HashSet<>();
        dfsVisit(startId, visited);
        return visited;
    }

    private void dfsVisit(int blockId, Set<Integer> visited) {
        if (visited.contains(blockId)) return;  // already visited
        visited.add(blockId);
        List<Integer> neighbors = edges.getOrDefault(blockId, new ArrayList<>());
        for (int neighbor : neighbors) {
            dfsVisit(neighbor, visited);         // recurse into neighbors
        }
    }

    // ── Cycle detection DFS — finds infinite loops ──
    public boolean hasCycle() {
        Set<Integer> visited    = new HashSet<>();
        Set<Integer> inStack    = new HashSet<>();
        for (Block b : blocks) {
            if (!visited.contains(b.id)) {
                if (dfsCycle(b.id, visited, inStack)) return true;
            }
        }
        return false;
    }

    private boolean dfsCycle(int blockId, Set<Integer> visited, Set<Integer> inStack) {
        visited.add(blockId);
        inStack.add(blockId);

        for (int neighbor : edges.getOrDefault(blockId, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfsCycle(neighbor, visited, inStack)) return true;
            } else if (inStack.contains(neighbor)) {
                return true;  // back-edge found → cycle!
            }
        }
        inStack.remove(blockId);
        return false;
    }

    // ── Find unreachable blocks ──
    public List<Block> findUnreachableBlocks() {
        if (blocks.isEmpty()) return new ArrayList<>();
        Set<Integer> reachable   = findReachableBlocks(0); // start from block 0
        List<Block>  unreachable = new ArrayList<>();
        for (Block b : blocks) {
            if (!reachable.contains(b.id)) {
                unreachable.add(b);
            }
        }
        return unreachable;
    }

    // ── Print the graph structure ──
    public void printGraph() {
        System.out.println("  Control Flow Graph:");
        for (Block b : blocks) {
            System.out.print("    " + b + " → ");
            List<Integer> succs = edges.getOrDefault(b.id, new ArrayList<>());
            if (succs.isEmpty()) System.out.println("[END]");
            else {
                for (int s : succs) System.out.print("Block[" + s + "] ");
                System.out.println();
            }
        }
    }

    public List<Block> getBlocks() {

        return blocks;
    }
    public int blockCount() {

        return blocks.size();
    }

}

package structures;

public class KeywordBST {

    // ── Inner Node Class ───
    static class KeywordNode {
        String      keyword;
        KeywordNode left, right;

        KeywordNode(String keyword) {
            this.keyword = keyword;
            left = right = null;
        }
    }

    // ── BST Operations Class ──
    static class BST {

        KeywordNode root;
        int         nodeCount;

        BST() {
            root      = null;
            nodeCount = 0;
        }

        // Insert keyword — O(log n) average
        public KeywordNode insert(KeywordNode root, String keyword) {
            if (root == null) {
                nodeCount++;
                return new KeywordNode(keyword.toLowerCase());
            }
            int cmp = keyword.toLowerCase().compareTo(root.keyword);
            if (cmp < 0) {
                root.left = insert(root.left, keyword);
            } else if (cmp > 0) {
                root.right = insert(root.right, keyword);
            }
            // equal → duplicate, ignore
            return root;
        }

        // Search keyword
        public boolean search(KeywordNode root, String keyword) {
            if (root == null) {
                return false;
            }
            int cmp = keyword.toLowerCase().compareTo(root.keyword);
            if (cmp == 0) {
                return true;
            }
            if (cmp < 0) {
                return search(root.left, keyword);
            }
            return search(root.right, keyword);
        }

        // Display all keywords in ascending  alphabetically (in-order traversal)
        public void displayInorder(KeywordNode root) {
            if (root == null) {
                return;
            }
            displayInorder(root.left);
            System.out.print(root.keyword + " ");
            displayInorder(root.right);
        }

        // Count total keywords in BST
        public int totalKeywords(KeywordNode root) {
            if (root == null) {
                return 0;
            }
            return totalKeywords(root.left) + totalKeywords(root.right) + 1;
        }

        // Get the first keyword alphabetically (leftmost node)
        public String firstKeyword(KeywordNode root) {
            if (root == null) {
                return "";
            }
            if (root.left == null) {
                return root.keyword;
            }
            return firstKeyword(root.left);
        }

        // Get the last keyword alphabetically (rightmost node)
        public String lastKeyword(KeywordNode root) {
            if (root == null) {
                return "";
            }
            if (root.right == null) {
                return root.keyword;
            }
            return lastKeyword(root.right);
        }

        // Load all Java reserved keywords into BST
        public void loadJavaKeywords() {
            String[] keywords = {
                    "abstract","assert","boolean","break","byte","case","catch",
                    "char","class","const","continue","default","do","double",
                    "else","enum","extends","final","finally","float","for",
                    "goto","if","implements","import","instanceof","int","interface",
                    "long","native","new","package","private","protected","public",
                    "return","short","static","strictfp","super","switch",
                    "synchronized","this","throw","throws","transient","true",
                    "false","null","try","void","volatile","while","string",
                    "system","main","args","out","println","print","scanner"
            };
            for (String kw : keywords) {
                root = insert(root, kw);
            }
        }
    }

    // ── Convenience wrapper (used by KeywordAnalyzer) ───
    private BST bst;

    public KeywordBST() {

        bst = new BST();
    }

    public void loadJavaKeywords() {

        bst.loadJavaKeywords();
    }

    public boolean search(String keyword)
    {

        return bst.search(bst.root, keyword);
    }

    public void insert(String keyword) {

        bst.root = bst.insert(bst.root, keyword);
    }

    public int getNodeCount() {

        return bst.totalKeywords(bst.root);
    }

    public void printInOrder() {
        System.out.print("Keywords in BST (alphabetical): ");
        bst.displayInorder(bst.root);
        System.out.println();
    }

    public BST getBST()       {
        return bst;
    }
    public KeywordNode getRoot() {

        return bst.root;
    }


}

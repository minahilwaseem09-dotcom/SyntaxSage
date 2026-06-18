package structures;

public class BSTNode {
    public String  keyword;
    BSTNode left, right;

    public BSTNode(String keyword) {
        this.keyword = keyword;
        this.left    = null;
        this.right   = null;
    }
}
package structures;

import lexer.Token;

public class TokenLinkedList {

    // Inner node class
    private static class Node {
        Token token;
        Node  next;

        Node(Token token) {
            this.token = token;
            this.next  = null;
        }
    }

    private Node head;   // first node
    private Node tail;   // last node (for O(1) add)
    private int  size;

    public TokenLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Add token at end — O(1)
    public void add(Token token) {
        Node newNode = new Node(token);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail      = newNode;
        }
        size++;
    }

    // Get token by index — O(n)
    public Token get(int index) {
        if (index < 0 || index >= size) return null;
        Node current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.token;
    }

    // Get head node for traversal
    public Node getHead() { return head; }

    public int size()      { return size; }
    public boolean isEmpty(){ return size == 0; }

    // Convert to array for easy iteration in analyzers
    public Token[] toArray() {
        Token[] arr = new Token[size];
        Node current = head;
        int i = 0;
        while (current != null) {
            arr[i++] = current.token;
            current  = current.next;
        }
        return arr;
    }

    // Print all tokens — useful for debugging
    public void printAll() {
        Node current = head;
        while (current != null) {
            System.out.println("  " + current.token);
            current = current.next;
        }
    }

}

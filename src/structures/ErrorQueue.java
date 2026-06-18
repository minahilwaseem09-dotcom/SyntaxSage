package structures;

public class ErrorQueue {

    // Inner node
    private static class Node {
        String message;
        String severity; // "ERROR" or "WARNING"
        int    lineNumber;
        Node   next;

        Node(String message, String severity, int lineNumber) {
            this.message    = message;
            this.severity   = severity;
            this.lineNumber = lineNumber;
            this.next       = null;
        }
    }

    private Node front;  // dequeue from here
    private Node rear;   // enqueue here
    private int  count;
    private int  errorCount;
    private int  warningCount;

    public ErrorQueue() {
        front        = null;
        rear         = null;
        count        = 0;
        errorCount   = 0;
        warningCount = 0;
    }

    // Add error to back of queue — O(1)
    public void enqueue(String message, String severity, int lineNumber) {
        Node newNode = new Node(message, severity, lineNumber);
        if (rear == null) {
            front = newNode;
            rear  = newNode;
        } else {
            rear.next = newNode;
            rear      = newNode;
        }
        count++;
        if (severity.equals("ERROR"))   errorCount++;
        else                             warningCount++;
    }

    // Convenience methods
    public void enqueueError(String message, int lineNumber) {
        enqueue(message, "ERROR", lineNumber);
    }

    public void enqueueWarning(String message, int lineNumber) {
        enqueue(message, "WARNING", lineNumber);
    }

    // Remove and return front message — O(1)
    public String dequeue() {
        if (isEmpty()) return null;
        String msg = buildMessage(front);
        front      = front.next;
        if (front == null) rear = null;
        count--;
        return msg;
    }

    // Look at front without removing
    public String peek() {
        if (isEmpty()) return null;
        return buildMessage(front);
    }

    private String buildMessage(Node node) {
        return String.format("  [Line %3d] %-8s %s",
                node.lineNumber, node.severity + ":", node.message);
    }

    public boolean isEmpty()     { return count == 0; }
    public int size()            { return count; }
    public int getErrorCount()   { return errorCount; }
    public int getWarningCount() { return warningCount; }

    // Print everything — used by ErrorReporter
    public void printAll() {
        Node current = front;
        while (current != null) {
            System.out.println(buildMessage(current));
            current = current.next;
        }
    }
}

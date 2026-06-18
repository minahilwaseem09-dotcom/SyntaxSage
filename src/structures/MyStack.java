package structures;

public class MyStack {

    private String[] data;  // internal array
    private int      top;   // index of top element (-1 = empty)
    private int      lineNum[]; // track line number of each pushed item

    public MyStack(int capacity) {
        data    = new String[capacity];
        lineNum = new int[capacity];
        top     = -1;
    }

    // Push value onto stack — O(1)
    public void push(String value, int line) {
        if (top < data.length - 1) {
            top++;
            data[top]    = value;
            lineNum[top] = line;
        }
    }

    // Pop top value — O(1)
    public String pop() {
        if (isEmpty()) return null;
        String val = data[top];
        top--;
        return val;
    }

    // Look at top without removing — O(1)
    public String peek() {
        if (isEmpty()) return null;
        return data[top];
    }

    // Get line number of top item
    public int peekLine() {
        if (isEmpty()) return -1;
        return lineNum[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }
    public int size()        {
        return top + 1;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Stack[]";
        StringBuilder sb = new StringBuilder("Stack[");
        for (int i = 0; i <= top; i++) {
            sb.append(data[i]);
            if (i < top) sb.append(", ");
        }
        sb.append("] ← top");
        return sb.toString();
    }

}

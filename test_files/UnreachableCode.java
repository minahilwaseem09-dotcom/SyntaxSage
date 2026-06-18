package test_files;

public class UnreachableCode {

    public static int getValue() {
        return 42;
        int dead = 10;
        System.out.println(dead);
    }

    public static void loopForever() {
        while (true) {
            System.out.println("running...");
        }
    }

    public static void main(String[] args) {
        int val = getValue();
        System.out.println(val);
        loopForever();
    }

}

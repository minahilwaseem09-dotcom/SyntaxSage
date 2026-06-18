package test_files;

public class MismatchedBrackets {

    public static void main(String[] args) {
        int x = (5 + 3];
        int[] arr = new int[10);
        System.out.println(x);
        if (x > 0) {
            System.out.println("positive");
        }

}

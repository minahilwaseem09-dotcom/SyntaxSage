package test_files;

public class ValidCode {

    public static void main(String[] args) {
        int x = 10;
        int y = 20;
        int sum = x + y;
        System.out.println(sum);

        if (x > 5) {
            System.out.println("x is greater than 5");
        } else {
            System.out.println("x is not greater than 5");
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            if (i == 3) break;
        }
    }

}

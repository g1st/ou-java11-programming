package labs;

public class HelloWorld {

    public static void main(String[] args) {
        System.out.println("Hello World Java");

        int a = 3, b = 2;

        boolean c = false;

        c = (a > b && ++b == 3);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        c = (a > b && ++b == 3);
        System.out.println("c = " + c);
        System.out.println("b = " + b);
    }
}

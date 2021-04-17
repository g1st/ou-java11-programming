package labs.pm;

public class Practice3 {
    public static void main(String[] args) {
        String teaTxt = "Tea";

        String b = "Tea";

        System.out.println(teaTxt == b); // true

        String c = new String("Tea");

        System.out.println(b == c); // false
        System.out.println(b == c.intern());
        String d = c.intern();
        System.out.println(teaTxt == d);

        c = teaTxt + ' ' + b;
        System.out.println(c);
        System.out.println(c.indexOf('T', 1));
        System.out.println(c.lastIndexOf('T'));
        c = c.toUpperCase();
        System.out.println(c);

        System.out.println(c.substring(c.lastIndexOf('T'), c.lastIndexOf('T') + 2 ));

        StringBuilder txt = new StringBuilder(c);
        System.out.println(txt.length());
        System.out.println(txt.capacity());
        txt.replace(0,3,"What is the price of");
        System.out.println(txt);
        System.out.println(txt.capacity());
    }
}

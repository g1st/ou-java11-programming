package com.capgemini;

public class Main {

    public static void main(String[] args) {
	// write your code here
        byte x = 2, y = 3, z = 5;

        z = (byte) (x + y);

        System.out.println("z = " + z);
        
        float a = (float) x / y;
        System.out.println("a = " + a);
        
        double b = (double) x / y;
        System.out.println("b = " + b);
        b = a;
        System.out.println(b);
        
        double c = Math.round(b * 10000) / 10000.0;

        System.out.println("c = " + c);
        char a1 = 'a', a2 = '\141', a3 = '\u0061';
        System.out.println("a1 = " + a1);
        System.out.println("a1 = " + a1);
        System.out.println("a1 = " + a1);


     }
}

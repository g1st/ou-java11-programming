package labs.pm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        int a = 42;
        Integer b = Integer.valueOf(a);
        int c = Integer.valueOf(a);

        System.out.println("b.doubleValue() = " + b.doubleValue());
        System.out.println(Long.MAX_VALUE);
        BigDecimal x = BigDecimal.valueOf(12.973);
        x = x.setScale(2, RoundingMode.CEILING);
        System.out.println("x = " + x);

        LocalDate today = LocalDate.now();
        System.out.println("today = " + today);

    }
}

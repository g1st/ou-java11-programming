package com.capgemini;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class Practice32 {
    private String a = "aa";

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public static void main(String[] args) {
//        double price = 1.85;
//        double rate = 0.065;
//        price -= price * rate;
//        price = Math.round(price * 100) / 100.0;
//
//        System.out.println(price);
        BigDecimal price = BigDecimal.valueOf(1.85);
        BigDecimal rate = BigDecimal.valueOf(0.065);

        System.out.println(price.subtract(price.multiply(rate)).setScale(2, RoundingMode.HALF_UP));

        Locale locale = Locale.getDefault();
//        Locale locale = Locale.UK;
//        Locale locale = new Locale("ru", "RU");
        System.out.println(locale);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
        percentFormat.setMaximumFractionDigits(2);
        String priceText = currencyFormat.format(price);
        String percentText = percentFormat.format(rate);
        System.out.println(priceText);
        System.out.println(percentText);

        LocalDate today = LocalDate.now();
        DayOfWeek next = today.plusYears(1).getDayOfWeek();
        System.out.println(next);

        LocalTime teaTime = LocalTime.of(17, 00);
        System.out.println(teaTime);

        Duration timeGap = Duration.between(LocalTime.now(), teaTime);
        System.out.println(timeGap);

        Duration timeGap2 = Duration.between(LocalTime.now(), LocalTime.of(15,45));
        System.out.println(timeGap2);

        System.out.println(timeGap.toMinutes());
        System.out.println(timeGap.toMinutesPart());
        System.out.println(timeGap.toHours());
        System.out.println(timeGap.toSeconds());
        System.out.println(timeGap.toSecondsPart());

        LocalDateTime tomorrowTeaTime = LocalDateTime.of(LocalDate.now().plusDays(1), teaTime);
        System.out.println(tomorrowTeaTime);

        // time zones
        ZoneId london = ZoneId.of("Europe/London");
        ZoneId katmandu = ZoneId.of("Asia/Katmandu");

        ZonedDateTime londonTime = ZonedDateTime.of(tomorrowTeaTime, london);

        ZonedDateTime katmanduTime = londonTime.withZoneSameInstant(katmandu);

        System.out.println("londonTime = " + londonTime);
        System.out.println("katmanduTime = " + katmanduTime);

        System.out.println(katmanduTime.getOffset());

        String datePattern = "EE', 'd' of 'MMM yyy' at 'HH:mm z";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(datePattern, locale);
        String timeText = dateFormat.format(katmanduTime);
        System.out.println("timeText = " + timeText);

        // Localization and Format Messages
        ResourceBundle msg = ResourceBundle.getBundle("messages.messages", locale);

        String offerPattern = msg.getString("offer");
        System.out.println("offerPattern = " + offerPattern);
    }
}

package pl.com.bottega.commons.math.formatter;

import java.util.Arrays;

/**
 * Created by Dell on 2016-04-02.
 */
public class FormatterConsoleApplication {
    public static void main(String[] args) {

        Formatter formatter1 = new Formatter(1214352842848942l);
        String[] digit = formatter1.formatDigits(Formatter.FormattingLanguage.EN);
        System.out.println(Arrays.toString(digit));
        System.out.println(formatter1.makeNumber());

        souldCreateForLargeNumber(123124);
        souldCreateForLargeNumber(12352432334576l);//L na końcu to typ long
        souldCreateForLargeNumber(2154234634573457l);

        souldCreateForLargeString("123124");
        souldCreateForLargeString("12352432334576");
        souldCreateForLargeString("2154234634573457");

        shouldFormatDigits();
        shouldGetFirstNumber();
        shouldGetLastNumber();
        shouldFormatNumbers();

        Formatter test = new Formatter(1112734230);
        System.out.println(test.formatNumbers(Formatter.FormattingLanguage.PL));
    }

    private static void souldCreateForLargeNumber(long number) {
        Formatter formatter = new Formatter(number);
    }

    private static void souldCreateForLargeString(String number) {
        Formatter formatter = new Formatter(number);
    }

    private static void shouldFormatDigits(){
        Formatter formatter = new Formatter(123456789123456789l);
        String[] digits = formatter.formatDigits(Formatter.FormattingLanguage.PL);
        System.out.println(Arrays.toString(digits));
    }

    private static void shouldFormatNumbers(){
        Formatter formatter = new Formatter(23491239123l);
        System.out.println(formatter.formatNumbers(Formatter.FormattingLanguage.PL));
        Formatter formatter2 = new Formatter(1000000);
        System.out.println(formatter2.formatNumbers(Formatter.FormattingLanguage.PL));
        Formatter formatter3 = new Formatter(323243553);
        System.out.println(formatter3.formatNumbers(Formatter.FormattingLanguage.PL));
        Formatter formatter4 = new Formatter(300940);
        System.out.println(formatter4.formatNumbers(Formatter.FormattingLanguage.PL));
        Formatter formatter5 = new Formatter(984001013);
        System.out.println(formatter5.formatNumbers(Formatter.FormattingLanguage.PL));
    }


    private static void shouldGetLastNumber() {
        Formatter formatter = new Formatter(123456789123456789l);
        String digit = formatter.getLastDigit(Formatter.FormattingLanguage.PL);
        System.out.println(digit);
    }

    private static void shouldGetFirstNumber() {
        Formatter formatter = new Formatter(123456789123456789l);
        String digit = formatter.getAndReadDigit(1, Formatter.FormattingLanguage.PL);
        System.out.println(digit);
    }

}

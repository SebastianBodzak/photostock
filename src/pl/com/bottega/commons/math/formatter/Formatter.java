package pl.com.bottega.commons.math.formatter;

import java.util.*;

/**
 * Created by Dell on 2016-04-02.
 */
public class Formatter {

    public enum FormattingLanguage {
        PL, EN;
    }

    private List<Byte> digits = new ArrayList<>();
    private boolean godMode = true;


    public Formatter(long number) {
        if (godMode)//iterować jest rzeczą ludzką, rekursja jest rzeczą boską
            recursiveCutter(number);
        else
            iterativeCutter(number);

        Collections.reverse(digits);
    }

    public void recursiveCutter(long number) {
        long digit = number % 10;
        digits.add((byte) digit);
        long newValue = number/10;
        if (newValue != 0)
            recursiveCutter(newValue);
    }

    private void iterativeCutter(long number){
        while (number != 0){
            long digit = number % 10;
            digits.add((byte)digit);
            number = number / 10;//zmiana ALE kopii paramteru metody,
        }
    }

   public Formatter(String number){
        //TODO zaimplementować, ALE NIE jako wywołanie powyższego konstruktora, tylko jako przejśćie po znakach
        char[] digit = number.toCharArray();
//        for (int i = 0; i<digit.length; i++) {
//
//        }
//
//       for (char singleMark : digit) {
//            byte intDigit = singleMark.get;
//            digits.add(intDigit);
//        }
    }
//=====================================================================================================
    /**
     *
     * @param lang
     * @return for 123 return "trzy"
     */
    public String getLastDigit(FormattingLanguage lang) {
        Collections.reverse(digits);
        return getAndReadDigit(1, lang);//TODO
    }

    /**
     *
     * @param lang
     * @return zwraca jeden dwa trzy
     */
    public String[] formatDigits(FormattingLanguage lang) {
        String[] result = new String[digits.size()];
        int nr = 0;
        for (Byte digit : digits) {
            result[nr++] = generateDigit(lang, 0, digit);
        }
        return result;
    }

    /**
     *
     * @param lang
     * @return dla 123 zwraca sto dwadzieśćia trzy
     */
    public String formatNumbers(FormattingLanguage lang) {
        String result = "";
        StringBuilder sB = new StringBuilder(result);
        int numberOfDigits = digits.size();
        int position = 0;
        int bigNumbers = checkGroupOfBigNumbers();

        if ((numberOfDigits % 3) == 1) {
            byte units = digits.get(0);
            sB.append((generateDigit(lang, 0, units)) + " ");
            if (numberOfDigits > 3)
                sB.append(checkPolishGrammarForm(bigNumbers, lang, units, (byte) 0, (byte) 0) + " ");
            position++;
            numberOfDigits--;
            bigNumbers--;
        } else if((numberOfDigits % 3) == 2) {
            byte tens = digits.get(0);
            byte units = digits.get(1);
            if (tens == 1) {
                sB.append(((generateDigit(lang, 1, units)) + " "));
                if (numberOfDigits > 3)
                    sB.append(checkPolishGrammarForm(bigNumbers, lang, units, tens, (byte) 0) + " ");
            } else {
                sB.append(((generateDigit(lang, 2, (byte)(tens - 2))) + " "));
                sB.append(((generateDigit(lang, 0, units)) + " "));
                if (numberOfDigits > 3) {
                    sB.append(checkPolishGrammarForm(bigNumbers, lang, units, tens, (byte) 0) + " ");
                }
            }
            position += 2;
            numberOfDigits -= 2;
            bigNumbers--;
        }

        while(checkIfNumberHasAtLeastThreePosition(numberOfDigits)){
            byte hundreds = digits.get(position);
            byte tens = digits.get(position + 1);
            byte units = digits.get(position + 2);

            if (hundreds != 0) {
                sB.append(generateDigit(lang, 3, (byte) (hundreds - 1)) + " ");
                if ((numberOfDigits > 3) & (tens + units == 0))
                    sB.append(checkPolishGrammarForm(bigNumbers, lang, units, (byte) 0, hundreds) + " ");
            }
            if (tens + units != 0) {
                if (tens == 1) {
                    sB.append(((generateDigit(lang, 1, units)) + " "));
                    if (numberOfDigits > 3)
                        sB.append(checkPolishGrammarForm(bigNumbers, lang, units, tens, hundreds) + " ");
                } else if (hundreds == 0 & tens == 0) {
                    sB.append(((generateDigit(lang, 0, units)) + " "));
                    if (numberOfDigits > 3)
                        sB.append(checkPolishGrammarForm(bigNumbers, lang, units, tens, hundreds) + " ");
                } else {
                    sB.append(((generateDigit(lang, 2, (byte) (tens - 2)) + " ")));
                    if (units != 0)
                        sB.append(((generateDigit(lang, 0, units)) + " "));
                    if (numberOfDigits > 3) {
                        sB.append(checkPolishGrammarForm(bigNumbers, lang, units, tens, hundreds) + " ");
                    }
                }
            }
            bigNumbers--;
            position += 3;
            numberOfDigits -=3;
        }
        result = sB.toString();
        return result;
    }

    private int checkGroupOfBigNumbers() {
        int bigNumbers = 3;
        int digitsSize = digits.size();

        while (digitsSize > 3) {
            digitsSize -= 3;
            bigNumbers++;
        }

        return bigNumbers;
    }

    private String checkPolishGrammarForm(int numberOfBigNumberGroup, FormattingLanguage lang, byte units, byte tens, byte hundreds) {
        byte correctForm;
        if ((units == 0 & tens == 0 & numberOfBigNumberGroup < 4) || (hundreds == 0 & tens == 0 & units == 1)){
            correctForm = 0;
        } else if ((units == 2 || units == 3 || units == 4) & (tens != 1)) {
            correctForm = 1;
        } else if ((units == 0 & tens == 0) & numberOfBigNumberGroup > 3) {
            correctForm = 2;
        } else {
            correctForm = 2;
        }
        String digitGroupString = generateDigit(lang, numberOfBigNumberGroup, correctForm);
        return digitGroupString;
    }

    private boolean checkIfNumberHasAtLeastThreePosition(int numberOfDigits) {
        if (numberOfDigits >= 3)
            return true;
        return false;
    }

    private String generateDigit(FormattingLanguage lang, int position, Byte digit) {
        return DICTIONARY[lang.ordinal()][position][digit];
    }

    private static final String[][][] DICTIONARY = {
            {       {"zero", "jeden", "dwa", "trzy", "cztery", "pięć", "sześć", "siedem", "osiem", "dziewieć"},
                    {"dziesięć", "jedenaście", "dwanaście", "trzynaście", "czternaście", "piętnaście", "szesnaście", "siedemnaście","osiemnaście","dziewiętnaście"},
                    {"dwadzieścia", "trzydzieści", "czterdzieści", "pięćdziesiąt", "sześćdziesiąt", "siedemdziesiąt", "osiemdziesiąt", "dziewięćdziesiąt"},
                    {"sto", "dwieście", "trzysta", "czterysta", "pięćset", "sześćset", "siedemset","osiemset","dziewięćset"},
                    {"tysiąc", "tysiące", "tysięcy"},
                    {"milion", "miliony", "milionów"},
                    {"miliard", "miliardy", "miliardów"},
                    {"bilion", "biliony", "bilionów"},
                    {"biliard", "biliardy", "biliardów"}
            },
            {       {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}}
    };

    public void displayDigit() {
        for (byte digit : digits) {
            System.out.print(digit + " ");
        }
    }

    public byte getDigit(int position, FormattingLanguage lang) {
        Collections.reverse(digits);
        return digits.get(position - 1);
    }

    /**
     *
     * @param position
     * @param lang
     * @return digit at given position
     */
    public String getAndReadDigit(int position, FormattingLanguage lang) {
        Collections.reverse(digits);
        Byte digit = digits.get(position - 1);
        return generateDigit(lang, 0, digit);
    }

    public long makeNumber() {
        long number = 0;
        Collections.reverse(digits);
        long multiple = 1;
        for (byte digit : digits) {
            number += (digit * multiple);
            multiple *= 10;
        }
        return number;
    }
}

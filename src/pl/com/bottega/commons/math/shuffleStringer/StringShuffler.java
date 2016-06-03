package pl.com.bottega.commons.math.shuffleStringer;

import java.util.*;

/**
 * Created by Dell on 2016-04-11.
 */
public class StringShuffler extends StringTokenizer {
    public StringShuffler(String str, String delim, boolean returnDelims) {
        super(str, delim, returnDelims);
    }

    public StringShuffler(String str, String delim) {
        super(str, delim);
    }

    public StringShuffler(String str) {
        super(str);
    }

    public String tokensShuffle() {
        String shuffledString = "";
        while(hasMoreElements()) {
            String token = (String) nextElement();
            List<Character> tokenForShuffle = new LinkedList<>();
            StringBuilder sB = new StringBuilder(shuffledString);

            if(notNumberAndLongENough(token)) {
                for (char c : token.toCharArray())
                    tokenForShuffle.add(c);

                Collections.shuffle(tokenForShuffle.subList(1, tokenForShuffle.size() - 1));
                for (char c : tokenForShuffle) {
                sB.append(c);
                }
            } else {
            sB.append(token);
            }
            shuffledString = sB.toString();
        }
        return shuffledString;
    }

    private boolean notNumberAndLongENough(String string) {
        if (string.length() > 4 && checkIfFirstCharIsNumber(string)) {
            return true;
        }
        return false;
    }

    private boolean checkIfFirstCharIsNumber(String string) {
        char firstLetter = string.charAt(0);
        if(Character.isDigit(firstLetter)){
            return false;
        }
        return true;
    }
}

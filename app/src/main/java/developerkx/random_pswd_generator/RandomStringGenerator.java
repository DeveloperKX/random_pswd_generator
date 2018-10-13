package developerkx.random_pswd_generator;

import java.util.HashSet;
import java.util.Random;

interface IRandomStringGenerator {
    String getRandomString(int length);
}

final class RandomStringGenerator implements IRandomStringGenerator{
    private String charSet;

    RandomStringGenerator(String charSet) {
        this.charSet = "";

        if (charSet != null && !charSet.isEmpty()) {
            HashSet<Character> tmpCharSet = new HashSet<Character>();

            for (int i = 0; i < charSet.length(); i++) {
                tmpCharSet.add(charSet.charAt(i));
            }

            for (Character c : tmpCharSet) {
                this.charSet = this.charSet + c.toString();
            }
        }
    }

    public final String getRandomString(int length) {
        String randomString = "";
        int k, charSetSize = this.charSet.length();
        Random rand = new Random();
        if (charSetSize > 0 && length > 0){
            for (int i = 0; i < length; i++) {
                k = rand.nextInt(charSetSize);
                randomString = randomString + this.charSet.charAt(k);
            }
        }

        return randomString;
    }
}

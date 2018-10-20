package developerkx.random_pswd_generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class PasswordGenerator {
    private static final String LowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private static final String UppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String Digits = "0123456789";
    private static final String SpecialSymbols = "!@#$%^";
    List<IRandomStringGenerator> individualGenerators = new ArrayList<IRandomStringGenerator>();
    IRandomStringGenerator wholeGenerator;

    PasswordGenerator(boolean useLowercaseLetters,
                      boolean useUppercaseLetters,
                      boolean useDigits,
                      boolean useSpecialSymbols){

        String charSet = "";
        if (useLowercaseLetters){
            charSet = charSet + LowercaseLetters;
            this.individualGenerators.add(new RandomStringGenerator(LowercaseLetters));
        }
        if (useUppercaseLetters){
            charSet = charSet + UppercaseLetters;
            this.individualGenerators.add(new RandomStringGenerator(UppercaseLetters));
        }
        if (useDigits){
            charSet = charSet + Digits;
            this.individualGenerators.add(new RandomStringGenerator(Digits));
        }
        if (useSpecialSymbols){
            charSet = charSet + SpecialSymbols;
            this.individualGenerators.add(new RandomStringGenerator(SpecialSymbols));
        }

        this.wholeGenerator = new RandomStringGenerator(charSet);
    }

    public String getRandomPassword(int length) {
        if (length >= this.individualGenerators.size()) {
            String rlt = this.wholeGenerator.getRandomString(length-this.individualGenerators.size());
            for(IRandomStringGenerator generator: this.individualGenerators) {
                char randomChar = generator.getRandomString(1).charAt(0);
                rlt = randomInsert(rlt, randomChar);
            }

            return rlt;
        }

        return "";
    }

    public int getMinLength() {
        return this.individualGenerators.size();
    }

    private String randomInsert(String origin, char addon){
        if (origin.length() == 0 ){
            return String.valueOf(addon);
        }

        StringBuffer buf = new StringBuffer(origin);
        Random rand = new Random();
        int index = rand.nextInt(origin.length()+1);
        buf.insert(index, addon);
        return buf.toString();
    }
}

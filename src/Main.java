import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static AtomicInteger wordsWithLenght3 = new AtomicInteger();
    public static AtomicInteger wordsWithLenght4 = new AtomicInteger();
    public static AtomicInteger wordsWithLenght5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread checkPalindrome = new Thread(() -> {
            for(String text : texts){
                StringBuilder stringBuilder = new StringBuilder(text);
                String[] textArray = text.split("");
                if (text.equals(stringBuilder.reverse().toString())
                && !Arrays.stream(textArray).allMatch(x -> x.equals(textArray[0]))){
                    if(text.length() == 3){
                        wordsWithLenght3.incrementAndGet();
                    } else if(text.length() == 4){
                        wordsWithLenght4.incrementAndGet();
                    }else{
                        wordsWithLenght5.incrementAndGet();
                    }
                }
            }
        });

        Thread checkIdentical = new Thread(() -> {
            for(String text : texts){
                String[] textArray = text.split("");
                if (Arrays.stream(textArray).allMatch(x -> x.equals(textArray[0]))){
                    if(text.length() == 3){
                        wordsWithLenght3.incrementAndGet();
                    } else if(text.length() == 4){
                        wordsWithLenght4.incrementAndGet();
                    }else{
                        wordsWithLenght5.incrementAndGet();
                    }
                }
            }
        });

        Thread checkOrder = new Thread(() -> {
            for(String text : texts){
                StringBuilder stringBuilder = new StringBuilder();
                String[] textArray = text.split("");
                Arrays.stream(text.split(""))
                        .sorted(Comparator.naturalOrder())
                        .forEach(stringBuilder::append);
                if (text.equals(stringBuilder.toString())
                && !Arrays.stream(textArray).allMatch(x -> x.equals(textArray[0]))){
                    if(text.length() == 3){
                        wordsWithLenght3.incrementAndGet();
                    } else if(text.length() == 4){
                        wordsWithLenght4.incrementAndGet();
                    }else{
                        wordsWithLenght5.incrementAndGet();
                    }
                }
            }
        });

        checkPalindrome.start();
        checkIdentical.start();
        checkOrder.start();

        checkPalindrome.join();
        checkIdentical.join();
        checkOrder.join();

        System.out.printf("""
                        Красивых слов с длиной 3: %d шт
                        Красивых слов с длиной 4: %d шт
                        Красивых слов с длиной 5: %d шт""",
                wordsWithLenght3.get(), wordsWithLenght4.get(), wordsWithLenght5.get());



    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

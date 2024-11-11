package es.uva.hilos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class WordVowelCounter implements Runnable {
    private BlockingQueue<String> wordQueue;
    private BlockingQueue<Result> resultQueue;

    // Constructor
    public WordVowelCounter(BlockingQueue<String> wordQueue, BlockingQueue<Result> resultQueue) {
        this.wordQueue = wordQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        // TODO
        try {
            while (!wordQueue.isEmpty()) {
                String word = wordQueue.poll(1, TimeUnit.SECONDS);
                if (word != null) {
                    int vowelCount = countVowels(word);
                    Result result = new Result(word, vowelCount);
                    resultQueue.put(result);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int countVowels(String word) {
        word = word.toLowerCase();
        int count = 0;
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i)=='a' || word.charAt(i)=='e' || word.charAt(i)=='i' || word.charAt(i)=='o' || word.charAt(i)=='u'){
                count++;
            }
        }
        return count;
    }
}
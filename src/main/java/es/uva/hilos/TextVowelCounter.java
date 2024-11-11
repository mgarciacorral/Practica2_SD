package es.uva.hilos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TextVowelCounter {

    // Method that takes a String input and returns an ArrayList of words
    public static ArrayList<String> getWords(String input) {
        return new ArrayList<>(Arrays.asList(input.split(" ")));
    }

    // Method that counts vowels in a string using parallelism
    public static int getVowels(String input, int parallelism) throws InterruptedException {

        // Create queues
        BlockingQueue<String> wordQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Result> resultQueue = new LinkedBlockingQueue<>();

        ArrayList<String> words = getWords(input);
        for(String word : words) {
            wordQueue.put(word);
        }

        // Create and start the worker threads based on the parallelism parameter
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < parallelism; i++) {
            WordVowelCounter worker = new WordVowelCounter(wordQueue, resultQueue);
            Thread workerThread = new Thread(worker);
            workerThread.start();
            workers.add(workerThread);
        }

        // Wait for all worker threads to finish
        for (Thread worker : workers) {
            worker.join();
        }

        // Gather results from resultQueue
        int totalVowels = 0;
        while (!resultQueue.isEmpty()) {
            Result result = resultQueue.poll(1, TimeUnit.SECONDS);
            if (result != null) {
                totalVowels += result.getVowelCount();
            }
        }
        System.out.println(totalVowels);
        return totalVowels;
    }
}
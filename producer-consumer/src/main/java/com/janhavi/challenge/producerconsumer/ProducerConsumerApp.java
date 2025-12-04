package com.janhavi.challenge.producerconsumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application to demonstrate producer-consumer pattern.
 */
public class ProducerConsumerApp {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> source = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> destination = new ArrayList<>();

        int capacity = 3;
        int poisonPill = -1;

        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(capacity);

        Thread producerThread = new Thread(new Producer(source, queue, poisonPill), "ProducerThread");
        Thread consumerThread = new Thread(new Consumer(queue, destination, poisonPill), "ConsumerThread");

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        System.out.println("=== Final Destination Content ===");
        System.out.println(destination);

    }
}

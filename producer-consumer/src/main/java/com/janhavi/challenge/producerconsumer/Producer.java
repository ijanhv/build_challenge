package com.janhavi.challenge.producerconsumer;

import java.util.ArrayList;
import java.util.List;
/**
 * Producer reads from a source container and puts items into the shared queue.
 */
public class Producer implements Runnable {
    /** Source list containing data to be produced */
    private final List<Integer> source;

    /** Shared blocking queue where items are produced */
    private final BoundedBlockingQueue<Integer> queue;

    /** Special termination signal value */
    private final int poisonPill;

    /**
     * Creates a Producer instance.
     *
     * @param source     list of values to be produced
     * @param queue      shared blocking queue
     * @param poisonPill termination signal for consumer
     */
    public Producer(List<Integer> source,
                    BoundedBlockingQueue<Integer> queue,
                    int poisonPill
                    ) {
        this.source = source;
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    /**
     * Iterates over the source list and pushes each item into the queue.
     */
    @Override
    public void run() {
        try {
            for (Integer value: source) {
                queue.put(value);
                System.out.println(Thread.currentThread().getName() + " produced " + value);
                Thread.sleep(50);
            }
            queue.put(poisonPill);
            System.out.println(Thread.currentThread().getName() + " produced " + poisonPill);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted");
        }
    }

}

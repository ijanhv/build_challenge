package com.janhavi.challenge.producerconsumer;

import java.util.ArrayList;
import java.util.List;
/**
 * Producer reads from a source container and puts items into the shared queue.
 */
public class Producer implements Runnable {
    private final List<Integer> source;
    private final BoundedBlockingQueue<Integer> queue;
    private final int poisonPill;

    public Producer(List<Integer> source,
                    BoundedBlockingQueue<Integer> queue,
                    int poisonPill
                    ) {
        this.source = source;
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

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

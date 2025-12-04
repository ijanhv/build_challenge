package com.janhavi.challenge.producerconsumer;

import java.util.List;

/**
 * Consumer reads from the shared queue and stores items into destination container.
 */
public class Consumer implements Runnable {

    private final BoundedBlockingQueue<Integer> queue;
    private final List<Integer> destination;
    private final int poisonPill;

    public Consumer(BoundedBlockingQueue<Integer> queue,
                    List<Integer> destination,
                    int poisonPill) {
        this.queue = queue;
        this.destination = destination;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer value = queue.take();
                if (value == poisonPill) {
                    System.out.println("[Consumer] Received poison pill. Stopping.");
                    break;
                }
                destination.add(value);
                System.out.println("[Consumer] Consumed: " + value);
                Thread.sleep(80); // simulate work
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[Consumer] Interrupted");
        }
    }
}

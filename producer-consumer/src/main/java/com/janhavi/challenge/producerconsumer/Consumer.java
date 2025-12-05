package com.janhavi.challenge.producerconsumer;

import java.util.List;

/**
 * Consumer reads from the shared queue and stores items into destination container.
 */
public class Consumer implements Runnable {

    // Shared blocking queue from which items are consumed
    private final BoundedBlockingQueue<Integer> queue;

    // Destination list where consumed items are stored
    private final List<Integer> destination;

    // Special value used to signal termination
    private final int poisonPill;

    /**
     * Creates a Consumer instance.
     *
     * @param queue       shared blocking queue
     * @param destination list to store consumed items
     * @param poisonPill  special value that signals consumer shutdown
     */
    public Consumer(BoundedBlockingQueue<Integer> queue,
                    List<Integer> destination,
                    int poisonPill) {
        this.queue = queue;
        this.destination = destination;
        this.poisonPill = poisonPill;
    }

    /**
     * Continuously takes elements from the queue and processes them.
     */
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

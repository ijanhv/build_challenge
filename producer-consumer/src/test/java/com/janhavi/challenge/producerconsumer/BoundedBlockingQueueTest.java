package com.janhavi.challenge.producerconsumer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BoundedBlockingQueueTest {

    /**
     * Verifies basic FIFO behavior of the queue in a single-threaded scenario.
     * Ensures that:
     *  - Elements are added correctly
     *  - Queue size is updated
     *  - Elements are removed in correct order
     */
    @Test
    void testSingleThreadPutTake() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);
        queue.put(1);
        queue.put(2);

        // Queue should now be full
        assertEquals(2, queue.size());
        // FIFO order should be preserved
        assertEquals(1, queue.take());
        assertEquals(2, queue.take());

        // Queue should be empty after consuming all items
        assertEquals(0, queue.size());
    }

    /**
     * Integration test using real Producer and Consumer with threading.
     * Ensures that:
     *  - All produced items are eventually consumed
     *  - Poison pill correctly terminates the consumer
     *  - No data is lost in transfer from source to destination
     */
    @Test
    void testProducerConsumerMovesAllItems() throws InterruptedException {
        List<Integer> source = List.of(1, 2, 3, 4, 5);
        List<Integer> destination = new ArrayList<>();
        int poisonPill = -1;

        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);

        // Latch ensures both producer and consumer finish before assertions
        CountDownLatch latch = new CountDownLatch(2);

        Thread producer = new Thread(() -> {
            try {
                new Producer(source, queue, poisonPill).run();
            } finally {
                latch.countDown();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                new Consumer(queue, destination, poisonPill).run();
            } finally {
                latch.countDown();
            }
        });

        producer.start();
        consumer.start();

        // Wait for both threads to complete
        latch.await();

        // Validate that all items were transferred successfully in order
        assertEquals(source, destination);
    }

    /**
     * Duplicate verification of basic queue functionality.
     * This test ensures:
     *  - Put and take operations work correctly
     *  - Queue size becomes zero after consumption
     */
    @Test
    void testPutAndTakeSingleThreaded() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);

        queue.put(1);
        queue.put(2);

        assertEquals(1, queue.take());
        assertEquals(2, queue.take());
        assertEquals(0, queue.size());
    }

    /**
     * End-to-end integration test using real Producer and Consumer classes.
     * Ensures:
     *  - Correct thread coordination
     *  - Proper usage of blocking queue
     *  - Poison pill safely terminates the consumer thread
     */
    @Test
    void testProducerConsumerIntegration() throws InterruptedException {
        List<Integer> source = List.of(1, 2, 3, 4, 5);
        List<Integer> destination = new ArrayList<>();
        int poisonPill = -1;

        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);

        Thread producer = new Thread(new Producer(source, queue, poisonPill));
        Thread consumer = new Thread(new Consumer(queue, destination, poisonPill));

        producer.start();
        consumer.start();

        // Wait for both threads to finish
        producer.join();
        consumer.join();

        // Final consumed output must match produced input
        assertEquals(source, destination);
    }

    /**
     * Verifies that consumer stops execution when poison pill is received.
     * Ensures:
     *  - Consumer exits its loop
     *  - No elements are added to destination list
     */
    @Test
    void testPoisonPillStopsConsumer() throws InterruptedException {
        List<Integer> destination = new ArrayList<>();
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);
        int poisonPill = -1;

        // Insert poison pill without producing any real data
        queue.put(poisonPill);

        Thread consumer = new Thread(new Consumer(queue, destination, poisonPill));
        consumer.start();
        consumer.join();

        // Since poison pill was first element, nothing should be consumed
        assertTrue(destination.isEmpty());
    }

    /**
     * Verifies correctness in multi-producer single-consumer scenario.
     * Ensures:
     *  - Both producers successfully push data into the queue
     *  - Consumer consumes all items
     *  - No data loss occurs due to concurrency
     */
    @Test
    void testMultipleProducersSingleConsumer() throws InterruptedException {
        List<Integer> destination = new ArrayList<>();
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(5);
        int poisonPill = -1;

        List<Integer> src1 = List.of(1, 2, 3);
        List<Integer> src2 = List.of(4, 5, 6);

        Thread p1 = new Thread(new Producer(src1, queue, poisonPill));
        Thread p2 = new Thread(new Producer(src2, queue, poisonPill));
        Thread consumer = new Thread(new Consumer(queue, destination, poisonPill));

        p1.start();
        p2.start();
        consumer.start();

        p1.join();
        p2.join();
        consumer.join();

        // Validate that all data from both producers was consumed
        assertTrue(destination.containsAll(List.of(1, 2, 3, 4, 5, 6)));
        assertEquals(6, destination.size());
    }
}

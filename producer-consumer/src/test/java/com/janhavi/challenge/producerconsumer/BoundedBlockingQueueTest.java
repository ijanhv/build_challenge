package com.janhavi.challenge.producerconsumer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class BoundedBlockingQueueTest {

    @Test
    void testSingleThreadPutTake() throws InterruptedException {
        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);
        queue.put(1);
        queue.put(2);

        assertEquals(2, queue.size());
        assertEquals(1, queue.take());
        assertEquals(2, queue.take());
        assertEquals(0, queue.size());
    }

    @Test
    void testProducerConsumerMovesAllItems() throws InterruptedException {
        List<Integer> source = List.of(1, 2, 3, 4, 5);
        List<Integer> destination = new ArrayList<>();
        int poisonPill = -1;

        BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(2);

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

        latch.await();

        // All source items must be present in destination (order preserved here)
        assertEquals(source, destination);
    }
}

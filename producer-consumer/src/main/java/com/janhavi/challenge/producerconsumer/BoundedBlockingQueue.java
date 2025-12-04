package com.janhavi.challenge.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple bounded blocking queue implementation using wait/notify.
 *
 * @param <T> type of the elements stored in the queue
 */
public class BoundedBlockingQueue<T> {

    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        this.capacity = capacity;
    }

    public void put(T item) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == capacity) {
                wait(); // wait until space is available
            }
            queue.add(item);
            notifyAll(); // notify consumers waiting for items
        }
    }

    public T take() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                wait(); // wait until an item is available
            }
            T item = queue.remove();
            notifyAll(); // notify producers waiting for space
            return item;
        }
    }

    public synchronized int size() {
        return queue.size();
    }
}

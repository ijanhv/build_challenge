package com.janhavi.challenge.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple bounded blocking queue implementation using wait/notify.
 *
 * @param <T> type of the elements stored in the queue
 */
public class BoundedBlockingQueue<T> {

    /** Internal FIFO storage for elements */
    private final Queue<T> queue = new LinkedList<>();

    /** Maximum number of elements the queue can hold */
    private final int capacity;

    /**
     * Constructs a bounded blocking queue with fixed capacity.
     *
     * @param capacity maximum number of elements allowed in the queue
     * @throws IllegalArgumentException if capacity is zero or negative
     */
    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        this.capacity = capacity;
    }

    /**
     * Inserts an item into the queue.
     *
     * @param item the element to be inserted
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void put(T item) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == capacity) {
                wait(); // wait until space is available
            }
            queue.add(item);
            notifyAll(); // notify consumers waiting for items
        }
    }

    /**
     * Removes and returns the head element of the queue.
     *
     * @return the removed element
     * @throws InterruptedException if the thread is interrupted while waiting
     */
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


    /**
     * Returns the current number of elements in the queue.
     *
     * @return size of the queue
     */
    public synchronized int size() {
        return queue.size();
    }
}

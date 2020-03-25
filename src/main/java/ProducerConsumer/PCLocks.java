package ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCLocks {
    private Queue<Item> items;
    private Lock lock;
    private Condition full, empty;
    private int bufferSize;
    private volatile boolean producing;
    private boolean print;

    /**
     * Runs and creates a new producer consumer system with locks.
     *
     * @param producerCount The number of producers in the system.
     * @param consumerCount The number of consumers in the system.
     * @param bufferSize    The limiting size of the buffer.
     * @param itemCount     The number of items each producer should make.
     * @param sleepDelay    How many ms the consumers should sleep for between consuming items.
     * @param print         Whether to print what the system is doing.
     */
    public PCLocks(int producerCount, int consumerCount, int bufferSize, int itemCount, long sleepDelay, boolean print) {
        items = new LinkedList<>();
        lock = new ReentrantLock();
        full = lock.newCondition();
        empty = lock.newCondition();
        this.bufferSize = bufferSize;
        this.print = print;

        Producer[] producers = new Producer[producerCount];
        Consumer[] consumers = new Consumer[consumerCount];

        producing = true;

        for (int i = 0; i < producerCount; i++) {
            producers[i] = new Producer(this, itemCount, (char) (i + 'A'));
        }

        for (int i = 0; i < consumerCount; i++) {
            consumers[i] = new Consumer(this, sleepDelay);
        }

        for(Producer p: producers)
            p.start();
        for(Consumer c: consumers)
            c.start();
        try {
            for (Producer p : producers)
                p.join();
            producing = false;
            for (Consumer c : consumers)
                c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (print)
            System.out.println("Finished");
    }

    /**
     * Method to add an item to the buffer.
     *
     * @param item The item to add.
     */
    public void add(Item item) throws InterruptedException {
        lock.lock();
        while (bufferSize <= items.size()) {
            full.await();
        }
        items.add(item);
        empty.signal();
        lock.unlock();
    }

    /**
     * Method to remove an item from the buffer.
     *
     * @return The item removed.
     */
    public Item remove() throws InterruptedException {
        Item item;
        lock.lock();
        while (items.isEmpty()) {
            if (!producing) {
                lock.unlock();
                return null;
            }
            empty.await();
        }
        item = items.remove();
        full.signal();
        lock.unlock();
        return item;
    }

    /**
     * Consumer thread
     */
    private class Consumer extends Thread {
        private PCLocks controller;
        private long delay;

        /**
         * Consumer constructor.
         *
         * @param controller The PCAtomics object that manages the system.
         * @param delay      The consumers delay between consuming items.
         */
        public Consumer(PCLocks controller, long delay) {
            this.controller = controller;
            this.delay = delay;
        }

        /**
         * Runs the Consumer thread and continues consuming items from the buffer while the system is still running.
         */
        @Override
        public void run() {
            boolean running = true;
            try {
                while (running) {
                    sleep(delay);
                    Item item = controller.remove();
                    if (item == null)
                        running = false;
                    else if (print)
                        System.out.println("Consumed " + item);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Producer thread
     */
    private class Producer extends Thread {
        private int total, produced;
        private PCLocks controller;
        private char id;

        /**
         * Producer constructor.
         *
         * @param controller  The PCAtomics object that controls the system.
         * @param totalToMake The number of items to make.
         * @param id          The producer's id.
         */
        public Producer(PCLocks controller, int totalToMake, char id) {
            total = totalToMake;
            this.controller = controller;
            this.id = id;
        }

        /**
         * Runs the producer thread until its made all of the items.
         */
        @Override
        public void run() {
            try {
                while (produced < total) {
                    Item item = new Item(++produced, id);
                    controller.add(item);
                    if (print)
                        System.out.println("Made " + item);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

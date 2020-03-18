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

    public PCLocks(int producerCount, int consumerCount, int bufferSize, int itemCount, long sleepDelay, boolean print){
        items = new LinkedList<>();
        lock = new ReentrantLock();
        full = lock.newCondition();
        empty = lock.newCondition();
        this.bufferSize = bufferSize;
        this.print = print;

        Producer[] producers = new Producer[producerCount];
        Consumer[] consumers = new Consumer[consumerCount];

        producing = true;

        for(int i=0;i<producerCount;i++){
            producers[i] = new Producer(this, itemCount,(char)(i+'A'));
        }

        for(int i=0;i<consumerCount;i++){
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
        if(print)
            System.out.println("Finished");
    }

    public void add(Item item) throws InterruptedException {
        lock.lock();
        while(bufferSize <= items.size()) {
            full.await();
        }
        items.add(item);
        empty.signal();
        lock.unlock();
    }

    public Item remove() throws InterruptedException {
        Item item = null;
        lock.lock();
        while (items.isEmpty()) {
            if(!producing) {
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

    private class Consumer extends Thread {
        private PCLocks controller;
        private long delay;
        private boolean running;

        public Consumer(PCLocks controller, long delay){
            this.controller = controller;
            this.delay = delay;
        }

        @Override
        public void run() {
            running = true;
            try {
                while (running) {
                    sleep(delay);
                    Item item = controller.remove();
                    if(item==null)
                        running = false;
                    else
                        if(print)
                            System.out.println("Consumed "+item);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Producer extends Thread {
        private int total, produced;
        private PCLocks controller;
        private char id;

        public Producer(PCLocks controller, int totalToMake, char id){
            total = totalToMake;
            this.controller = controller;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (produced < total){
                    Item item = new Item(++produced,id);
                    controller.add(item);
                    if(print)
                        System.out.println("Made "+item);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

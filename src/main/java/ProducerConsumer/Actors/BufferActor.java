package ProducerConsumer.Actors;

import ProducerConsumer.Item;
import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.LinkedList;
import java.util.Queue;

public class BufferActor extends AbstractActor {
    private final int size, producerCount;
    private Queue<Item> queue;
    private int killCount;
    private boolean producing = true;

    /**
     * The public method to create the Buffer actor with certain parameters
     *
     * @param bufferSize    The limiting size of the buffer
     * @param producerCount The number of the producers in the system
     * @return The Props object for Actor creation
     */
    public static Props props(int bufferSize, int producerCount) {
        return Props.create(BufferActor.class, () -> new BufferActor(bufferSize, producerCount));
    }

    /**
     * Private Buffer Actor constructor for the props method
     *
     * @param bufferSize    The limiting size of the buffer
     * @param producerCount The number of producers
     */
    private BufferActor(int bufferSize, int producerCount) {
        size = bufferSize;
        this.producerCount = producerCount;
        queue = new LinkedList<>();
    }

    /**
     * The message handling method, accepts items from producers, a ready command when a consumer is ready,
     * and a kill command from the producers when they are finished.
     *
     * @return The message handler
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Item.class, this::addItem)
                .matchEquals(Commands.READY, this::consume)
                .matchEquals(Commands.KILL, (c) -> kill())
                .build();
    }

    /**
     * Called when a producer finishes producing. The buffer will wait for all producers to finish producing before
     * shutting down the system.
     */
    private void kill() {
        killCount++;
        if (killCount >= producerCount)
            producing = false;
    }

    /**
     * Called when a consumer requests an item, if there are no items the message is sent to the back of the mailbox.
     *
     * @param c The consume command, used when forwarding the item request back to the buffer.
     */
    private void consume(Commands c) {
        if (queue.isEmpty()) {
            if (producing)
                getSelf().forward(c, getContext());
            else {
                getContext().getSystem().terminate();
            }
        } else {
            getSender().tell(queue.remove(), getSelf());
        }
    }

    /**
     * Called when a producer sends and item, tries to add the item to the queue and requests a new one. If the queue is
     * full, the buffer forwards the message back to itself.
     *
     * @param item The item to add.
     */
    private void addItem(Item item) {
        if (queue.size() >= size) {
            getSelf().forward(item, getContext());
        } else {
            queue.add(item);
            getSender().tell(Commands.REQUEST_OBJECT, getSelf());
        }

    }
}
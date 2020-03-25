package ProducerConsumer.Actors;

import ProducerConsumer.Item;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class ConsumerActor extends AbstractActor {
    private final ActorRef buffer;
    private final boolean print;
    private final int delay;

    /**
     * The public props method to create a new Consumer actor.
     *
     * @param bufferActor The buffer actor ref.
     * @param delay       The delay between consuming objects.
     * @param print       Whether to print what this actor is doing.
     * @return The Props object to create a new Consumer actor
     */
    public static Props props(ActorRef bufferActor, int delay, boolean print) {
        return Props.create(ConsumerActor.class, () -> new ConsumerActor(bufferActor, delay, print));
    }

    /**
     * The private ConsumerActor constructor used by the props method.
     *
     * @param bufferActor The buffer actor ref.
     * @param delay       The ms delay between consuming objects.
     * @param print       Whether to print what the actor is doing or not.
     */
    private ConsumerActor(ActorRef bufferActor, int delay, boolean print) {
        this.print = print;
        this.delay = delay;
        buffer = bufferActor;
        buffer.tell(Commands.READY, getSelf());
        getContext().watch(buffer);
    }

    /**
     * The message handling method for the actor, can only receive objects from the buffer to consume.
     *
     * @return The message handler.
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Item.class, this::consume)
                .build();
    }

    /**
     * Called when the actor receives a new item.
     *
     * @param item The item to consume.
     */
    private void consume(Item item) {
        if (print)
            System.out.println("Consumed " + item);
        try {
            Thread.sleep(delay);
            buffer.tell(Commands.READY, getSelf());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
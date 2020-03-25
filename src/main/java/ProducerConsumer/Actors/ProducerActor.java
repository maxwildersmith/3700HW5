package ProducerConsumer.Actors;

import ProducerConsumer.Item;
import akka.actor.AbstractActor;
import akka.actor.Props;

public class ProducerActor extends AbstractActor {
    private final char id;
    private int produced, maxItemCount;
    private final boolean print;
    private boolean running = true;

    /**
     * The public props method to create a new ProducerActor,
     *
     * @param id           The id of the producer.
     * @param maxItemCount The number of items to make.
     * @param print        Whether to print what the actor is doing or not.
     * @return The Props object for actor creation.
     */
    public static Props props(char id, int maxItemCount, boolean print) {
        return Props.create(ProducerActor.class, () -> new ProducerActor(id, maxItemCount, print));
    }

    /**
     * The private constructor used by the props method.
     *
     * @param id           The producers id.
     * @param maxItemCount The number of items to produce.
     * @param print        Whether to print or not.
     */
    private ProducerActor(char id, int maxItemCount, boolean print) {
        this.id = id;
        this.maxItemCount = maxItemCount;
        this.print = print;
        produced = 0;
    }

    /**
     * The message handling method for the actor, can only receive requests to produce and item.
     *
     * @return The message handler.
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Commands.REQUEST_OBJECT, (c) -> {
                    if (running) produce();
                })
                .build();
    }

    /**
     * Called when requested to produce an item. The item is then sent to the buffer, and if this is the last item
     * this producer needs to make, it also sends the kill command.
     */
    private void produce() {
        produced++;
        Item item = new Item(produced, id);
        getSender().tell(item, getSelf());
        if (print)
            System.out.println("Made " + item);
        if (produced >= maxItemCount) {
            getSender().tell(Commands.KILL, getSelf());
            running = false;
        }
    }
}
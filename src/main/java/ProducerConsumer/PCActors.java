package ProducerConsumer;

import ProducerConsumer.Actors.BufferActor;
import ProducerConsumer.Actors.Commands;
import ProducerConsumer.Actors.ConsumerActor;
import ProducerConsumer.Actors.ProducerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@SuppressWarnings("StatementWithEmptyBody")
public class PCActors {
    private volatile boolean running = true;

    /**
     * Runs and creates a new producer consumer system with actors.
     *
     * @param producerCount The number of producers in the system.
     * @param consumerCount The number of consumers in the system.
     * @param bufferSize    The limiting size of the buffer.
     * @param itemCount     The number of items each producer should make.
     * @param sleepDelay    How many ms the consumers should sleep for between consuming items.
     * @param print         Whether to print what the system is doing.
     */
    public PCActors(int producerCount, int consumerCount, int bufferSize, int itemCount, int sleepDelay, boolean print) {
        ActorSystem sys = ActorSystem.create("producer-consumer");

        ActorRef buffer = sys.actorOf(BufferActor.props(bufferSize, producerCount), "buffer");

        for (int i = 0; i < producerCount; i++)
            sys.actorOf(ProducerActor.props((char) ('A' + i), itemCount, print), "producer-" + (char) ('A' + i)).tell(Commands.REQUEST_OBJECT, buffer);


        for (int i = 0; i < consumerCount; i++)
            sys.actorOf(ConsumerActor.props(buffer, sleepDelay, print), "consumer-" + (char) ('A' + i));


        sys.registerOnTermination(() -> {
            running = false;
        });

        while (running) ;
        if (print)
            System.out.println("Finished");

    }
}

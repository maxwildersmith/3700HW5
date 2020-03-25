package Primes;

import ProducerConsumer.Actors.Commands;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


@SuppressWarnings("StatementWithEmptyBody")
public class PrimeActor extends AbstractActor {
    private final int prime;
    private ActorRef nextPrime;

    /**
     * Public method to create and return the Props object for a new prime number actor.
     *
     * @param prime The prime number to base this filter on.
     * @return The Props number for actor creation.
     */
    public static Props props(int prime) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(prime));
    }

    /**
     * Private constructor to create he prime actor from the props method.
     *
     * @param prime The prime number to base this actor on.
     */
    private PrimeActor(int prime) {
        this.prime = prime;
        System.out.print(prime + " ");
    }

    /**
     * Message handler that either receives a new number to process or the kill command.
     *
     * @return The message handler.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, this::newNumber)
                .matchEquals(Commands.KILL, this::kill).build();
    }

    /**
     * Called when the kill command gets received, if this is the last actor, then it terminates the system
     *
     * @param c Kill command to forward.
     */
    private void kill(Commands c) {
        if (nextPrime == null)
            getContext().getSystem().terminate();
        else
            nextPrime.forward(c, getContext());
    }

    /**
     * Called when a new number message is sent to the actor, this is either passed to the next actor or creates a next
     * actor based on this prime.
     *
     * @param i The number.
     */
    private void newNumber(int i) {
        if (i % prime != 0) {
            if (nextPrime == null) {
                nextPrime = getContext().actorOf(PrimeActor.props(i), "prime-actor-" + i);
            } else
                nextPrime.tell(i, ActorRef.noSender());
        }
    }

    private static volatile boolean running = true;

    /**
     * Static method to run prime number generation with Actors.
     *
     * @param maxNum The maximum value to generate primes up to.
     */
    public static void run(int maxNum) {
        ActorSystem sys = ActorSystem.create("primes");
        ActorRef root = sys.actorOf(PrimeActor.props(2), "prime-actor-2");
        for (int i = 3; i < maxNum; i++)
            root.tell(i, ActorRef.noSender());

        root.tell(Commands.KILL, ActorRef.noSender());

        sys.registerOnTermination(() -> {
            running = false;
        });

        while (running) ; //Used to wait for actor system termination.
        System.out.println();
    }
}

package ProducerConsumer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Running Producer Consumer system...");
        locks();
        atomics();
        isolatedSections();

        //TODO: Redo atomics with actual atomics.

    }
    private static void locks(){
        System.out.println("Testing with locks..");
        System.out.println("5 producers, 2 consumers");
        long time = System.currentTimeMillis();
        new PCLocks(5, 2, 10, 100, 10,false);
        time = System.currentTimeMillis() - time;
        System.out.println("Took " + time + "ms");

        System.out.println("2 producers, 5 consumers");
        time = System.currentTimeMillis();
        new PCLocks(2, 5, 10, 100, 10,false);
        time = System.currentTimeMillis()-time;
        System.out.println("Took "+time+"ms");

        time = System.currentTimeMillis();
        new PCLocks(2, 2, 10, 100, 10,false);
        time = System.currentTimeMillis() - time;
        System.out.println("Took " + time + "ms");
    }

    private static void isolatedSections(){
        System.out.println("Testing with isolated sections..");
        System.out.println("5 producers, 2 consumers");
        long time = System.currentTimeMillis();
        new PCIsolated(5, 2, 10, 100, 10,false);
        time = System.currentTimeMillis() - time;
        System.out.println("Took " + time + "ms");

        System.out.println("2 producers, 5 consumers");
        time = System.currentTimeMillis();
        new PCIsolated(2, 5, 10, 100, 100,false);
        time = System.currentTimeMillis()-time;
        System.out.println("Took "+time+"ms");

        time = System.currentTimeMillis();
        new PCIsolated(2, 2, 10, 100, 10,false);
        time = System.currentTimeMillis() - time;
        System.out.println("Took " + time + "ms");
    }

    private static void atomics(){
        System.out.println("Testing with atomics (blocking queue)..");
        System.out.println("5 producers, 2 consumers");
        long time = System.currentTimeMillis();
        new PCAtomics(5, 2, 10, 100, 10,false);
        time = System.currentTimeMillis() - time;
        System.out.println("Took " + time + "ms");

        System.out.println("2 producers, 5 consumers");
        time = System.currentTimeMillis();
        new PCAtomics(2, 5, 10, 100, 10,false);
        time = System.currentTimeMillis()-time;
        System.out.println("Took "+time+"ms");

        time = System.currentTimeMillis();
        new PCAtomics(2, 2, 10, 100, 10,false);
        time = System.currentTimeMillis() - time;
        System.out.println("Took " + time + "ms");
    }
}

package ProducerConsumer;

public class Item {
    private int id;
    private char cID;

    /**
     * Constructor for the Item class made and used by the producer consumer systems.
     *
     * @param id  The items id number.
     * @param cId The character id of who produced this item.
     */
    public Item(int id, char cId) {
        this.id = id;
        this.cID = cId;
    }

    /**
     * Prints the items info along with who made it.
     *
     * @return The string with the Item's number and the id of its producer
     */
    @Override
    public String toString() {
        return "Item #" + id + cID;
    }
}

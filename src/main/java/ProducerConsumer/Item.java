package ProducerConsumer;

public class Item {
    private int id;
    private char cID;

    public Item(int id,char cId){
        this.id = id;
        this.cID = cId;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Item #"+id+cID;
    }
}

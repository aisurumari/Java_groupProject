package shelter.inventory;

public class Item {

    //private int id;
    private String name;
    private int count;

    public Item(String name, int count) {
        //this.id = id;
        this.name = name;
        this.count = count;
    }

   // public Integer getId() {
   //     return id;
   // }

    public String getName() {
        return name;
    }

    public Integer getCount() {
        return count;
    }

    public void addCount(int i) {
        count += i;
    }
}

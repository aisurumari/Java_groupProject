package shelter.inventory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {

    int id;
    Item item;
    LocalDateTime dateOrdered;
    LocalDateTime dateDelivered;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Order(int id, Item item, LocalDateTime dateStart, LocalDateTime dateFinish) {
        this.id = id;
        this.item = item;
        dateOrdered = dateStart;
        dateDelivered = dateFinish;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return item.getName();
    }

    public Integer getQuantity() {
        return item.getCount();
    }

    public String getDateOrdered() {
        return dtf.format(dateOrdered);
    }

    public String getDateDelivered() {
        try {
            return dtf.format(dateDelivered);
        } catch (Exception e) {
            return "";
        }
    }

    public LocalDateTime getDeliveryTimestamp() {
        return dateDelivered;
    }

    public Item getItem() {
        return item;
    }

    public void finishOrder() {
        dateDelivered = LocalDateTime.now();
    }
}

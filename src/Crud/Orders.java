package Crud;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a class for interacting with the order table.
 */
public class Orders extends CrudBase {

    protected String date;

    /**
     * Constructs a new orders object.
     */
    public Orders() {
        super("orders");

        this.addSelectField("OrderID");
    }

    /**
     * Constructs a new orders object.
     *
     * @param date The date to filter on.
     */
    public Orders(String date) {
        this();

        this.date = date;
    }

    /**
     * Gets all orders from table.
     *
     * @return An array list with the selected orders.
     */
    public ArrayList<HashMap<String, String>> all() {
        if (this.date != null && !this.date.isEmpty()) {
            this.bindParam("ExpectedDeliveryDate", this.date);

            return super.all("SELECT * FROM nerdygadgets.orders WHERE ExpectedDeliveryDate = :ExpectedDeliveryDate LIMIT 10");
        }

        return super.all("SELECT * FROM nerdygadgets.orders LIMIT 10");
    }
}

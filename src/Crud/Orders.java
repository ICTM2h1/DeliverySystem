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
        this.addSelectField("CustomerID");
        this.addSelectField("ExpectedDeliveryDate");
        this.addSelectField("CityName");
        this.addSelectField("DeliveryAddressLine1");
        this.addSelectField("DeliveryPostalCode");
        this.addSelectField("DeliveryLocation");
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
        String query = "SELECT * FROM orders O " +
                "INNER JOIN customers CU ON O.CustomerID = CU.CustomerID \n" +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID ";
        if (this.date != null && !this.date.isEmpty()) {
            this.bindParam("ExpectedDeliveryDate", this.date);

            query += " WHERE ExpectedDeliveryDate = :ExpectedDeliveryDate";
        }

        return super.all(query + " LIMIT 10");
    }
}

package Crud;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for interacting with the customers table.
 */
public class Customer extends CrudBase {

    /**
     * Constructs a new customers object.
     */
    public Customer() {
        super("customers", "CustomerID");

        this.addSelectField("CustomerID");
        this.addSelectField("CustomerName");
        this.addSelectField("CityName");
        this.addSelectField("DeliveryAddressLine1");
        this.addSelectField("DeliveryPostalCode");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<LinkedHashMap<String, String>> all() {
        String query = "SELECT * FROM customers CU " +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID ";

        return super.all(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedHashMap<String, String> get(int id) {
        String query = "SELECT * FROM customers CU " +
                "INNER JOIN cities CI ON CU.DeliveryCityID = CI.CityID " +
                "WHERE " + this.primaryKey + " = :" + this.primaryKey;

        this.addCondition(this.primaryKey, String.valueOf(id));

        return super.get(query);
    }

}

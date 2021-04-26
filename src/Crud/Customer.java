package Crud;

/**
 * Provides a class for interacting with the customers table.
 */
public class Customer extends CrudBase {

    /**
     * Constructs a new customers object.
     */
    public Customer() {
        super("customers", "CustomerID");
    }

}

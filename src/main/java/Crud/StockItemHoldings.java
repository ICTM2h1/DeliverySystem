package Crud;

/**
 * Provides a class for interacting with the stock items table.
 */
public class StockItemHoldings extends CrudBase {

    /**
     * Constructs a new stock item object.
     */
    public StockItemHoldings() {
        super("stockitemholdings", "StockItemID");

        this.addSelectField("StockItemID");
        this.addSelectField("QuantityOnHand");
    }

}

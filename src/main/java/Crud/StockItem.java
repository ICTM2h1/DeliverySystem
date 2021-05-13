package Crud;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Provides a class for interacting with the stock items table.
 */
public class StockItem extends CrudBase {

    /**
     * The various levels of the stock item on hand quantity.
     */
    public final static int STOCK_WARNING = 10;
    public final static int STOCK_DANGER = 5;

    /**
     * Constructs a new stock item object.
     */
    public StockItem() {
        super("stockitems", "StockItemID");

        this.addSelectField("StockItemID");
        this.addSelectField("StockItemName");
        this.addSelectField("SellPrice");
        this.addSelectField("QuantityOnHand");
        this.addSelectField("OutOfStock");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<LinkedHashMap<String, String>> all() {
        String query = "SELECT SI.StockItemID, SI.StockItemName, ROUND(TaxRate * RecommendedRetailPrice / 100 + RecommendedRetailPrice,2) as SellPrice, " +
                "SIH.QuantityOnHand, (SELECT COUNT(*) FROM stockitemholdings WHERE QuantityOnHand < 1) AS OutOfStock " +
                "FROM stockitems SI " +
                "JOIN stockitemholdings SIH USING(stockitemid) " +
                "GROUP BY StockItemID";

        return super.all(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedHashMap<String, String> get(int id) {
        String query = "SELECT SI.StockItemID, SI.StockItemName, ROUND(TaxRate * RecommendedRetailPrice / 100 + RecommendedRetailPrice,2) as SellPrice, " +
                "SIH.QuantityOnHand, (SELECT COUNT(*) FROM stockitemholdings WHERE QuantityOnHand < 1) AS OutOfStock " +
                "FROM stockitems SI " +
                "JOIN stockitemholdings SIH USING(stockitemid) " +
                "WHERE SI.StockItemID = :StockItemID " +
                "GROUP BY StockItemID";

        this.addCondition("StockItemID", String.valueOf(id));

        return super.get(query);
    }
}

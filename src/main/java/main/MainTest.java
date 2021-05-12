package main;

import Crud.Customer;
import Crud.Order;
import System.Database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Provides a main class for testing.
 */
public class MainTest {

    private final static int ordersPerCustomer = 2;

    /**
     * Main entry point of this class.
     *
     * @param args The given arguments.
     */
    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());

        LinkedHashMap<String, String> conditions = new LinkedHashMap<>();
        conditions.put("ExpectedDeliveryDate", date);
        Query.delete("orders", conditions);

        String[][] cities = {
            {"Parkweg 2", "3842AD", "Harderwijk", "Henk de Potvis"},
            {"Jan Vermeerstraat 24", "7771WN", "Hardenberg", "Jan Klaasen"},
            {"Wolbrantskerkweg 50B", "1069DA", "Amsterdam", "Klaas-hendrik Berg"},
            {"Sportstraat 21", "7694BA", "Kloosterhaar", "Jessica Dijksma"},
            {"Voermanstraat 110", "9742VA", "Groningen", "Teddy Pleiter"},
            {"Deurloostraat 9", "4335NV", "Middelburg", "Henk de Walvis"},
            {"Bergstraat 33", "3035TB", "Rotterdam", "Teddy Dijksma"},
            {"Rijnstraat 191", "1784BX", "Den Helder", "Jessica Pleiter"},
            {"Hintzenstraat 11", "6216ES", "Maastricht", "Klaas Hendriksen"},
            {"Wibauthof 12", "7604XA", "Almelo", "Jessica van den berg"},
            {"Zuiderzeestraatweg 145", "3849AD", "Hierden", "Jessica van den Pot"},
            {"Kempenaar 29 06", "8231CX", "Lelystad", "Jessica van Pot"},
            {"Zuidoostersingel 3", "8861GB", "Harlingen", "Jessica van den Weg"},
            {"Molenpad 43a", "8921BG", "Leeuwarden", "Jessica van den Boer"},
            {"Naarderstraat 4", "1211AL", "Hilversum", "Jan van den Boer"},
            {"Ceram 53", "3772ZX", "Barneveld", "Jan den Boer"},
            {"Prins Willem-Alexanderpark 317", "3905CK", "Veenendaal", "Pieter Zwat"},
            {"Springfonteinstraat 27", "2572PK", "Den Haag", "Jaap Zwat"},
            {"Van Gentlaan 3", "4819AM", "Breda", "Rick Wit"},
            {"Burgemeester de Raadtsingel 75", "3311JG", "Dordrecht", "Rick Rood"},
            {"Vivaldistraat 12", "5653BR", "Eindhoven", "Coenraad van den Berg"},
            {"Stationsweg 81A", "8401DM", "Gorredijk", "Henk van den Berg"},
            {"Pelikaanstraat 37", "9404CH", "Assen", "Henk Pelikaan"},
        };

        for (String[] city : cities) {
            String customerName = city[4];

            ArrayList<String> selectFields = new ArrayList<>();
            selectFields.add("CustomerID");
            conditions = new LinkedHashMap<>();
            conditions.put("CustomerName", customerName);
            LinkedHashMap<String, String> customerDb = Query.selectFirst("SELECT * FROM customers WHERE CustomerName = :CustomerName", selectFields, conditions);
            if (customerDb == null || customerDb.isEmpty()) {
                createCustomer(city);

                conditions.put("CustomerName", customerName);
                customerDb = Query.selectFirst("SELECT * FROM customers WHERE CustomerName = :CustomerName", selectFields, conditions);
            }

            String customerID = customerDb.get("CustomerID");
            Order order = new Order();
            // nextInt is normally exclusive of the top value, so add 1 to make it inclusive.
            int customerOrders = ThreadLocalRandom.current().nextInt(1, ordersPerCustomer + 1);

            for (int orderAmount = 0; orderAmount < customerOrders; orderAmount++) {
                order.addValue("CustomerID", customerID);
                order.addValue("SalespersonPersonID", "2");
                order.addValue("ContactPersonID", "3032");
                order.addValue("OrderDate", date);
                order.addValue("ExpectedDeliveryDate", date);
                order.addValue("IsUndersupplyBackordered", "0");
                order.addValue("LastEditedWhen", "2013-01-01 00:00:00");
                order.addValue("LastEditedBy", "7");
                order.insert();
            }
        }

        System.out.println("Succesvol test gegevens toegevoegd.");
    }

    /**
     * Creates a new customer.
     *
     * @param city The city data for this customer.
     */
    private static void createCustomer(String[] city) {
        String cityName = city[2];
        String customerName = city[3];

        Customer customer = new Customer();

        ArrayList<String> selectFields = new ArrayList<>();
        selectFields.add("CityID");
        LinkedHashMap<String, String> conditions = new LinkedHashMap<>();
        conditions.put("CityName", cityName);
        LinkedHashMap<String, String> cityDb = Query.selectFirst("SELECT * FROM cities WHERE CityName = :CityName", selectFields, conditions);
        if (cityDb == null || cityDb.isEmpty()) {
            LinkedHashMap<String, String> values = new LinkedHashMap<>();
            values.put("CityName", cityName);
            values.put("StateProvinceID", "39");
            values.put("LastEditedBy", "1");
            values.put("ValidFrom", "2013-01-01 00:00:00");
            values.put("ValidTo", "2013-01-01 00:00:00");

            Query.insert("cities", values);
            conditions.put("CityName", cityName);
            cityDb = Query.selectFirst("SELECT * FROM cities WHERE CityName = :CityName", selectFields, conditions);
        }

        // Just to make the insert possible.
        customer.addValue("BillToCustomerID", "1");
        customer.addValue("CustomerCategoryID", "3");
        customer.addValue("PrimaryContactPersonID", "1001");
        customer.addValue("DeliveryMethodID", "3");
        customer.addValue("PostalCityID", "19586");
        customer.addValue("AccountOpenedDate", "2013-01-01");
        customer.addValue("StandardDiscountPercentage", "3");
        customer.addValue("IsStatementSent", "0");
        customer.addValue("IsOnCreditHold", "30");
        customer.addValue("PaymentDays", "7");
        customer.addValue("PhoneNumber", "(308) 555-0100");
        customer.addValue("FaxNumber", "(308) 555-0100");
        customer.addValue("WebsiteURL", "http://www.tailspintoys.com");
        customer.addValue("PostalAddressLine1", "PO Box 259");
        customer.addValue("PostalPostalCode", "90410");
        customer.addValue("LastEditedBy", "1");
        customer.addValue("ValidFrom", "2013-01-01 00:00:00");
        customer.addValue("ValidTo", "2013-01-01 00:00:00");

        // Important values.
        customer.addValue("CustomerName", customerName);
        customer.addValue("DeliveryAddressLine1", city[0]);
        customer.addValue("DeliveryPostalCode", city[1]);
        customer.addValue("DeliveryCityID", cityDb.get("CityID"));
        customer.insert();
    }

}

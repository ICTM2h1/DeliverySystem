package DeliveryRoute;

import System.Error.SystemError;
import UI.Components.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;

/**
 * Provides a delivery route for deliverers.
 */
public class DeliveryRoute {

    public final static int distancePerDeliverer = 350;

    private final int id;
    private int distance;
    private DeliveryStatus deliveryStatus = DeliveryStatus.NONE;

    private final ArrayList<DeliveryPointBase> deliveryPoints;

    /**
     * Creates a new delivery route.
     *
     * @param id The id of this route.
     */
    public DeliveryRoute(int id) {
        this(id, new ArrayList<>());
    }

    /**
     * Creates a new delivery route.
     *
     * @param id The id of this route.
     * @param deliveryPoints The delivery points of this route.
     */
    public DeliveryRoute(int id, ArrayList<DeliveryPointBase> deliveryPoints) {
        this.id = id;
        this.deliveryPoints = deliveryPoints;
    }

    /**
     * Gets the label of this delivery route.
     *
     * @return The label.
     */
    public String label() {
        return String.format("%s    %skm", id, this.getDistance());
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name of the delivery route.
     *
     * @return The name of the route.
     */
    public String getName() {
        String name = "Unknown";
        if (this.deliveryPoints.isEmpty()) {
            return name;
        }

        int orderCount = this.deliveryPoints.size();
        int middlePointKey = (int) Math.round((double) orderCount / 2) - 1;

        DeliveryPointBase firstPoint = this.deliveryPoints.get(0);
        if (firstPoint != null) {
            name = firstPoint.label() + " - ";
        }

        if (middlePointKey < orderCount) {
            DeliveryPointBase middlePoint = this.deliveryPoints.get(middlePointKey);
            if (middlePoint != null) {
                name += middlePoint.label() + " - ";
            }
        }

        DeliveryPointBase lastPoint = this.deliveryPoints.get(orderCount - 1);
        if (lastPoint != null) {
            name += lastPoint.label();
        }

        return name;
    }

    /**
     * Gets the distance of this delivery route.
     *
     * @return The distance.
     */
    public int getDistance() {
        if (this.distance != 0) {
            return this.distance;
        }

        double distance = 0;
        int counter = 0;
        DeliveryPointBase nextDeliveryPoint;
        for (DeliveryPointBase deliveryPoint : this.deliveryPoints) {
            int nextOrderIndex = counter + 1;
            if (nextOrderIndex >= this.deliveryPoints.size()) {
                break;
            }

            nextDeliveryPoint = this.deliveryPoints.get(nextOrderIndex);

            try {
                distance += deliveryPoint.distance(nextDeliveryPoint);
            } catch (NumberFormatException e) {
                SystemError.handle(e);
            }

            counter++;
        }

        this.distance = (int) Math.round(distance);
        return this.distance;
    }

    /**
     * Renders the delivery route to table.
     *
     * @return The table.
     */
    public Table toTable() {
        Table table = new Table(500, 260);
        table.addColumn("Nr");
        table.addColumn("Vertrekpunt");
        table.addColumn("Aankomstpunt");
        table.addColumn("Afstand");

        int counter = 0;
        DeliveryPointBase nextDeliveryPoint;
        for (DeliveryPointBase deliveryPoint : this.deliveryPoints) {
            int nextOrderIndex = counter + 1;
            if (nextOrderIndex >= this.deliveryPoints.size()) {
                break;
            }

            nextDeliveryPoint = this.deliveryPoints.get(nextOrderIndex);

            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(counter + 1));
            row.add(deliveryPoint.addressLabel());
            row.add(nextDeliveryPoint.addressLabel());
            row.add(String.valueOf(deliveryPoint.distance(nextDeliveryPoint)));

            table.addRow(row);
            counter++;
        }

        table.initializeTable();
        table.setColumnWidth(0, 50);
        table.setColumnWidth(3, 80);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setCellRenderer(0, cellRenderer);

        return table;
    }

    /**
     * Gets the number of delivery points.
     *
     * @return The delivery points.
     */
    public int getDeliveryPointsAmount() {
        return this.deliveryPoints.size();
    }

    /**
     * Gets a delivery point.
     *
     * @param delta The delta of this point
     *
     * @return The delivery point.
     */
    public DeliveryPointBase get(int delta) {
        return this.deliveryPoints.get(delta);
    }

    /**
     * Adds a delivery point to the map.
     *
     * @param deliveryPoint The delivery point.
     */
    public void add(DeliveryPointBase deliveryPoint) {
        this.deliveryPoints.add(deliveryPoint);
    }

    /**
     * Gets the delivery points.
     *
     * @return The delivery points.
     */
    public ArrayList<DeliveryPointBase> getDeliveryPoints() {
        return deliveryPoints;
    }

    /**
     * Gets the delivery points.
     *
     * @return The delivery points.
     */
    public ArrayList<DeliveryPointBase> getDeliveryPointsWithoutStartingPoint() {
        ArrayList<DeliveryPointBase> deliveryPoints = this.deliveryPoints;
        deliveryPoints.remove(0);
        deliveryPoints.remove(deliveryPoints.size() - 1);

        return deliveryPoints;
    }

    /**
     * Sets the delivery status of current instance.
     *
     * @param deliveryStatus ENUM value of delivery status.
     */
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * Gets the string value of delivery status.
     *
     * @return String value of Delivery status
     */
    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }
}

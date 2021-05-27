package DeliveryRoute;

import Crud.Order;
import System.Error.SystemError;
import UI.Panels.JPanelRawListBase;
import net.sf.dynamicreports.adhoc.AdhocManager;
import net.sf.dynamicreports.adhoc.configuration.AdhocColumn;
import net.sf.dynamicreports.adhoc.configuration.AdhocReport;
import net.sf.dynamicreports.adhoc.transformation.AdhocToXmlTransform;
import net.sf.dynamicreports.adhoc.transformation.XmlToAdhocTransform;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Provides a class for generating the delivery routes.
 */
public class DeliveryRoutePanel extends JPanelRawListBase implements ActionListener {

    private final DeliveryPoint StartPoint;
    private final String date;

    private final JButton routeButton, cancelButton, completeButton, printButton;

    private String routeTitle;
    private NearestNeighbour route;

    /**
     * Creates a new delivery route object.
     */
    public DeliveryRoutePanel() {
        this((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
    }

    /**
     * Creates a new delivery route object.
     *
     * @param date The date.
     */
    public DeliveryRoutePanel(String date) {
        this.panelHeight += 25;

        this.StartPoint = new DeliveryPoint(
            "Amsterdam", "1071BR", "P.C. Hooftstraat", 91, 4.8796204,52.3600336, 5
        );
        this.date = date;

        this.routeButton = new JButton("Volgen route");
        this.routeButton.addActionListener(this);

        this.cancelButton = new JButton("Annuleren");
        this.cancelButton.addActionListener(this);

        this.completeButton = new JButton("Afronden");
        this.completeButton.addActionListener(this);

        this.printButton = new JButton("Printen");
        this.printButton.addActionListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTabTitle() {
        return "Bezorgingstrajecten";
    }

    /**
     * Gets the title for this panel.
     *
     * @return The title.
     */
    @Override
    public String getTitle() {
        if (this.getRawListItems().size() == 1) {
            return "1 bezorgingstraject voor vandaag";
        }

        return String.format("%s bezorgingstrajecten voor vandaag", this.getRawListItems().size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addTitleComponent() {
        if (this.getTitle() == null) {
            return;
        }

        if (this.getRawListItems().size() < 1) {
            super.addTitleComponent();
            return;
        }

        JPanel titlePanel = new JPanel();
        titlePanel.setSize(this.getSize());
        titlePanel.setLayout(new BorderLayout());

        this.titleLabel = new JLabel(this.getTitle());
        Font labelFont = this.titleLabel.getFont();
        this.titleLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 20));
        titlePanel.add(this.titleLabel, BorderLayout.WEST);
        titlePanel.add(this.printButton, BorderLayout.EAST);

        this.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.gridBagConstraints.weightx = 0.5;
        this.addFullWidthComponent(titlePanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getNoResultsText() {
        return "Er zijn geen bezorgingstrajecten gevonden voor vandaag.";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<Object> getRawListItems() {
        if (this.listItems != null && !this.listItems.isEmpty()) {
            return this.listItems;
        }

        Order order = new Order();

        ArrayList<LinkedHashMap<String, String>> entities = order.filterOnGeometry();
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<DeliveryRoute> deliveryRoutes = new ArrayList<>();

        int distancePerDeliverer = DeliveryRoute.distancePerDeliverer, deliverer = 0, delta = 0, distance = 0;

        DeliveryPointBase nextDeliveryPoint;
        DeliveryRoute deliveryRoute = new DeliveryRoute(deliverer + 1);
        for (LinkedHashMap<String, String> orderEntity : entities) {
            DeliveryOrderPoint currentDeliveryPoint = new DeliveryOrderPoint(orderEntity);

            // The first distance to be added is the distance from the start point to the first order delivery point.
            if (distance == 0) {
                distance += currentDeliveryPoint.distance(this.StartPoint);
            }

            int nextOrderIndex = delta + 1;
            // Calculates the distance for each next point until there are no points anymore.
            // Then add the last point and route and stop the process.
            if (nextOrderIndex >= entities.size()) {
                deliveryRoute.add(currentDeliveryPoint);
                NearestNeighbour nearestNeighbour = new NearestNeighbour(this.StartPoint, deliveryRoute.getDeliveryPoints());
                DeliveryRoute sortedRoute = new DeliveryRoute(deliveryRoute.getId(), nearestNeighbour.getRoute());

                deliveryRoutes.add(sortedRoute);
                break;
            }

            nextDeliveryPoint = new DeliveryOrderPoint(entities.get(nextOrderIndex));
            distance += currentDeliveryPoint.distance(nextDeliveryPoint);

            // Takes in account that the deliverer must drive back to the start point within the maximum distance.
            int backToStartPointDistance = nextDeliveryPoint.distance(this.StartPoint) + distance;

            // If we reach the maximum distance per deliverer, add route to array and start new route.
            if (backToStartPointDistance >= distancePerDeliverer) {
                NearestNeighbour nearestNeighbour = new NearestNeighbour(this.StartPoint, deliveryRoute.getDeliveryPoints());
                DeliveryRoute sortedRoute = new DeliveryRoute(deliveryRoute.getId(), nearestNeighbour.getRoute());

                deliveryRoutes.add(sortedRoute);

                deliverer++;
                deliveryRoute = new DeliveryRoute(deliverer + 1);
                distance = 0;
            }

            deliveryRoute.add(currentDeliveryPoint);
            delta++;
        }

        return new ArrayList<>(deliveryRoutes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRawListItemLabel(Object listItem) {
        DeliveryRoute deliveryRoute = (DeliveryRoute) listItem;

        return String.format("#%s", deliveryRoute.label());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getListPreviewTitle() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateRawListItemPreview(Object listItem) {
        DeliveryRoute deliveryRoute = (DeliveryRoute) listItem;

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        this.preview.addComponent(new JLabel("Bezorgingstraject:"), true);
        this.preview.addComponent(new JLabel(deliveryRoute.getName()));
        this.routeTitle = deliveryRoute.getName();

        this.preview.addComponent(new JLabel("Startpunt:"), true);
        this.preview.addComponent(new JLabel(this.StartPoint.addressLabel()));

        this.preview.addComponent(new JLabel("Afstand:"), true);
        this.preview.addComponent(new JLabel(String.format("%s km", deliveryRoute.getDistance())));

        JLabel trajectStatusText = new JLabel(deliveryRoute.getDeliveryStatus().toString());
        DeliveryStatus trajectStatus = deliveryRoute.getDeliveryStatus();
        trajectStatusText.setFont(new Font("default", Font.BOLD, 13));
        if (trajectStatus.compareStatus(DeliveryStatus.REJECTED)) {
            trajectStatusText.setFont(new Font("default", Font.ITALIC, 13));
        }
        trajectStatusText.setForeground((trajectStatus.foregroundColor()));
        this.preview.addComponent(new JLabel("Trajectstatus:"), true);
        this.preview.addComponent(trajectStatusText);

        this.preview.gridBagConstraints.insets.top = 10;
        this.preview.addFullWidthComponent(new JLabel(String.format("%s bezorgingspunten", deliveryRoute.getDeliveryPointsAmount() - 1), JLabel.CENTER));
        this.preview.addFullWidthComponent(new JLabel("De afstand is (hemelsbreed) berekend vanaf elke stad tot de volgende stad."));

        this.preview.gridBagConstraints.insets.top = 5;
        if (!deliveryRoute.getDeliveryStatus().compareStatus(DeliveryStatus.NONE) &&
            !deliveryRoute.getDeliveryStatus().compareStatus(DeliveryStatus.NOW_DELIVERING)) {
            this.preview.gridBagConstraints.insets.bottom = 40;
        }

        this.preview.addFullWidthComponent(deliveryRoute.toTable().render(), 3);

        if (deliveryRoute.getDeliveryStatus().compareStatus(DeliveryStatus.NONE) ||
            deliveryRoute.getDeliveryStatus().compareStatus(DeliveryStatus.NOW_DELIVERING)) {
            this.preview.gridBagConstraints.insets.top = 15;
            this.preview.addComponent(this.cancelButton, true);
            this.preview.gridBagConstraints.insets.left = 10;
            this.preview.gridBagConstraints.insets.right = 10;
            this.preview.addComponent(this.routeButton);
            this.preview.gridBagConstraints.insets.left = 0;
            this.preview.gridBagConstraints.insets.right = 0;
            this.preview.addComponent(this.completeButton);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.printButton) {
            this.buildReport();
        }
        DeliveryRoute route = (DeliveryRoute) this.listItems.get(this.list.getSelectedIndex());

        if (e.getSource() == this.routeButton) {
            route.setDeliveryStatus(DeliveryStatus.NOW_DELIVERING);
            ArrayList<DeliveryPointBase> deliveryPoints = route.getDeliveryPoints();

            String[] labels = new String[deliveryPoints.size() - 1];
            int counter = 0;
            DeliveryPointBase nextDeliveryPoint;
            for (DeliveryPointBase deliveryPoint : deliveryPoints) {
                int nextIndex = counter + 1;
                if (nextIndex >= deliveryPoints.size()) {
                    break;
                }

                nextDeliveryPoint = deliveryPoints.get(nextIndex);

                labels[counter] = (counter + 1) + " - " + nextDeliveryPoint.label();
                counter++;
            }

            // We increase every point with one, because the current point is the start point and the next one is
            // the arriving point. Skip the start point and set the status of this point to delivering.
            DeliveryPointBase deliveryPoint = deliveryPoints.get(1);
            if (deliveryPoint.compareStatus(DeliveryStatus.BUSY_WITH_OTHER_DELIVERING)) {
                deliveryPoint.setStatus(DeliveryStatus.NOW_DELIVERING);
            }

            String title = String.format("%s (%skm)", route.getName(), route.getDistance());
            DeliveryFollowDialog deliveryFollowPanel = new DeliveryFollowDialog(new JFrame(), true, deliveryPoints, labels, title);

            if (deliveryFollowPanel.getRouteStatus() != null) {
                if (!deliveryFollowPanel.getRouteStatus().compareStatus(DeliveryStatus.NONE)) {
                    route.setDeliveryStatus(deliveryFollowPanel.getRouteStatus());
                    this.updateRawPreview(route);
                }
            }

        } else if (e.getSource() == this.completeButton) {
            if (JOptionPane.showConfirmDialog(this, "Weet je zeker dat je het hele bezorgingstraject wilt afronden?", "Bezorgingstraject afronden", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                route.setDeliveryStatus(DeliveryStatus.COMPLETED);

                // Set all delivery point statuses to completed
                for (DeliveryPointBase deliveryPoint : route.getDeliveryPoints()) {
                    if (deliveryPoint instanceof DeliveryOrderPoint && (deliveryPoint.compareStatus(DeliveryStatus.NONE) || deliveryPoint.compareStatus(DeliveryStatus.BUSY_WITH_OTHER_DELIVERING))) {
                        LinkedHashMap<String, String> entity = ((DeliveryOrderPoint) deliveryPoint).getOrder();
                        Order order = new Order();
                        order.addCondition("OrderID", entity.get("OrderID"));
                        order.addValue("Status", String.valueOf(DeliveryStatus.COMPLETED.toInteger()));
                        order.update();
                    }
                }

                this.updateRawPreview(route);
            }
        } else if (e.getSource() == this.cancelButton) {
            if (JOptionPane.showConfirmDialog(this, "Weet je zeker dat je het hele bezorgingstraject wilt annuleren?", "Bezorgingstraject annuleren", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                route.setDeliveryStatus(DeliveryStatus.REJECTED);

                // Set all delivery point statuses to rejected.
                for (DeliveryPointBase deliveryPoint : route.getDeliveryPoints()) {
                    if (deliveryPoint instanceof DeliveryOrderPoint && (deliveryPoint.compareStatus(DeliveryStatus.NONE) || deliveryPoint.compareStatus(DeliveryStatus.BUSY_WITH_OTHER_DELIVERING))) {
                        LinkedHashMap<String, String> entity = ((DeliveryOrderPoint) deliveryPoint).getOrder();
                        Order order = new Order();
                        order.addCondition("OrderID", entity.get("OrderID"));
                        order.addValue("Status", String.valueOf(DeliveryStatus.REJECTED.toInteger()));
                        order.update();
                    }
                }

                this.updateRawPreview(route);
            }
        }
    }

    /**
     * Builds the report.
     */
    private void buildReport() {
        AdhocReport report = new AdhocReport();
        AdhocColumn column = new AdhocColumn();
        column.setName("Bezorgingspunt");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("Vertrekpunt");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("Aankomstpunt");
        report.addColumn(column);

        column = new AdhocColumn();
        column.setName("Afstand");
        report.addColumn(column);

        try {
            AdhocManager adhocManager = AdhocManager.getInstance(new AdhocToXmlTransform(), new XmlToAdhocTransform());
            JasperReportBuilder reportBuilder = adhocManager.createReport(report);
            reportBuilder.setDataSource(this.createDataSource());
            reportBuilder.show(false);
        } catch (DRException e) {
            SystemError.handle(e);
        }
    }

    /**
     * Creates the data source for the pdf.
     *
     * @return The data of the pdf.
     */
    private JRDataSource createDataSource() {
        DRDataSource dataSource = new DRDataSource("Bezorgingspunt", "Vertrekpunt", "Aankomstpunt", "Afstand");

        for (Object listItem : this.listItems) {
            DeliveryRoute deliveryRoute = (DeliveryRoute) listItem;

            dataSource.add("--", "--", "--", "--");
            dataSource.add(String.format("Bezorgingstraject %s", deliveryRoute.getId()), "--", "--", "--");

            int counter = 0;
            ArrayList<DeliveryPointBase> deliveryPoints = deliveryRoute.getDeliveryPoints();
            DeliveryPointBase nextDeliveryPoint;
            for (DeliveryPointBase deliveryPoint : deliveryPoints) {
                int nextOrderIndex = counter + 1;
                if (nextOrderIndex >= deliveryPoints.size()) {
                    break;
                }

                nextDeliveryPoint = deliveryPoints.get(nextOrderIndex);

                dataSource.add(String.valueOf(counter + 1), deliveryPoint.getStreet(), nextDeliveryPoint.getStreet(), deliveryPoint.distance(nextDeliveryPoint) + " km");
                dataSource.add("--", deliveryPoint.label() + ", " + deliveryPoint.getPostalCode(), nextDeliveryPoint.label() + ", " + nextDeliveryPoint.getPostalCode(), "--");
                dataSource.add("--", "--", "--", "--");
                counter++;
            }
        }

        return dataSource;
    }

}

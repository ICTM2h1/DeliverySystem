package DeliveryRoute;

import UI.Panels.JPanelRawListBase;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeliveryFollowPanel extends JPanelRawListBase implements ActionListener {

    private JButton confirmButton, rejectButton, completeButton;

    private NearestNeighbour NearestNeighbour;
    private ArrayList<DeliveryPointBase> route;
    private ArrayList<DeliveryPointBase> routeListItems;
    private final String title;

    public DeliveryFollowPanel(NearestNeighbour route, String title) {
        this.NearestNeighbour = route;
        this.route = route.getRoute();
        this.routeListItems = route.getRouteListItems();
        this.title = title;
    }

    private DeliveryPointBase currentlyDelivering;
    private Integer currentlyDeliveringIndex;

    @Override
    public String getTitle() {
        return title + " - " + "(" + NearestNeighbour.getDistance() + "km)";
    }

    @Override
    protected String getListPreviewTitle() {
        return null;
    }

    @Override
    protected ArrayList<Object> getRawListItems() {
        return new ArrayList<> (routeListItems);
    }

    @Override
    protected String getRawListItemLabel(Object listItem) {
        DeliveryPointBase waypoints = (DeliveryPointBase) listItem;

        return waypoints.label();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ListCellRenderer<Object> getListCellRenderer() {

        return new DefaultListCellRenderer() {
            /**
             * {@inheritDoc}
             */
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                try {
                    DeliveryPointBase cityListItem = (DeliveryPointBase) listItemsCopy.get(index);
                    Integer deliveryPointStatus = cityListItem.getStatus();

                    Color background = Color.WHITE;
                    Color foreground = Color.BLACK;

                    // TODO: IS ER EEN MAKKELIJKERE MANIER?
                    if (deliveryPointStatus == DeliveryStatus.BUSY.toInteger()) {
                        foreground = DeliveryStatus.BUSY.foregroundColor();
                    } else if (deliveryPointStatus == DeliveryStatus.DELIVERING.toInteger()) {
                        foreground = DeliveryStatus.DELIVERING.foregroundColor();
                        background = DeliveryStatus.DELIVERING.backgroundColor();
                    } else if (deliveryPointStatus == DeliveryStatus.REJECTED.toInteger()) {
                        foreground = DeliveryStatus.REJECTED.foregroundColor();
                    } else if (deliveryPointStatus == DeliveryStatus.COMPLETED.toInteger()) {
                        foreground = DeliveryStatus.COMPLETED.foregroundColor();
                    }

                    this.setBackground(background);
                    this.setForeground(foreground);

                } catch (Exception exception) {
                    this.setForeground(Color.RED);
                }
                return this;
            }
        };
    }

    @Override
    protected void updateRawListItemPreview(Object listItem) {
        DeliveryPointBase waypoint = (DeliveryPointBase) listItem;

        // previousWaypoint may not exceed the length of testroute.
        DeliveryPointBase previousWaypoint;
        if (route.indexOf(waypoint) == 0) {
            previousWaypoint = route.get(routeListItems.size() - 1);
        } else {
            previousWaypoint = route.get(route.indexOf(waypoint) - 1);
        }

        // Set first (after startpoint) point to status DELIVERING
        if (DeliveryStatus.valueOf(waypoint.getStatus()) == DeliveryStatus.DELIVERING && route.indexOf(waypoint) == 1) {
            waypoint.setStatus(1);
            this.currentlyDelivering = waypoint;
            this.currentlyDeliveringIndex = 1;
        }

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        // Preview title
        this.preview.addComponent(new JLabel("<html><span style='font-size:20px'>"+waypoint.label()+"</span></html>"), true);

        this.preview.gridBagConstraints.insets = new Insets(20, 0, 5, 0);
        this.preview.addComponent(new JLabel("Van:"), true);

        // Add 'startpunt' at end of string incase it is the starting point
        if (previousWaypoint == NearestNeighbour.getStartPoint()) {
            this.preview.addComponent(new JLabel(previousWaypoint.label() + " (STARTPUNT)") );
        } else {
            this.preview.addComponent(new JLabel(previousWaypoint.label()));
        }

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Naar:"), true);

        // Add 'eindpunt' at end of string incase it is the ending point
        if (waypoint == NearestNeighbour.getStartPoint()) {
            this.preview.addComponent(new JLabel(waypoint.label() + " (EINDPUNT)") );
        } else {
            this.preview.addComponent(new JLabel(waypoint.label()));
        }

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Geschatte afstand:"), true);
        this.preview.addComponent(new JLabel(previousWaypoint.distance(waypoint) + " km"));

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Status:"), true);
        this.preview.addComponent(new JLabel(DeliveryStatus.valueOf(waypoint.getStatus()).toString()));

        this.preview.gridBagConstraints.insets = new Insets(15, 0, 5, 0);
        this.preview.addComponent(new JLabel("<html><span style='font-size:13px'>Adresgegevens</span></html>"), true);

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Postcode:"), true);
        this.preview.addComponent(new JLabel(waypoint.getPostalCode() + ", " + waypoint.label()));

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Straat:"), true);
        this.preview.addComponent(new JLabel("HEBBEN WIJ EEN STRAATNAAM?" + waypoint.getHouseNumber()));

        // Currently navigating to endpoint
        if (DeliveryStatus.valueOf(waypoint.getStatus()) == DeliveryStatus.DELIVERING && waypoint.equals(NearestNeighbour.getStartPoint())) {
            this.completeButton = new JButton("Bezorgingstraject afronden");
            this.completeButton.addActionListener(this);
            this.preview.addFullWidthComponent(this.completeButton, 2);
        // Currently delivering (display buttons)
        } else if (DeliveryStatus.valueOf(waypoint.getStatus()) == DeliveryStatus.DELIVERING) {
            this.rejectButton = new JButton("Levering afkeuren");
            this.rejectButton.addActionListener(this);

            this.confirmButton = new JButton("Levering bevestigen");
            this.confirmButton.addActionListener(this);

            this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
            this.preview.addComponent(this.rejectButton, true);
            this.preview.addComponent(this.confirmButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.rejectButton) {
            if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze bestelling wilt annuleren?", "Bevestiging", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                currentlyDelivering.setStatus(DeliveryStatus.REJECTED.toInteger());

                currentlyDeliveringIndex++;
                currentlyDelivering = route.get(currentlyDeliveringIndex);
                currentlyDelivering.setStatus(DeliveryStatus.DELIVERING.toInteger());

                this.list.setSelectedIndex(this.list.getSelectedIndex() + 1);
                this.list.updateUI();
            }
        } else if (e.getSource() == this.confirmButton) {
            if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze bestelling wilt bevestigen?", "Bevestiging", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                currentlyDelivering.setStatus(DeliveryStatus.COMPLETED.toInteger());

                currentlyDeliveringIndex++;
                currentlyDelivering = route.get(currentlyDeliveringIndex);
                currentlyDelivering.setStatus(DeliveryStatus.DELIVERING.toInteger());

                this.list.setSelectedIndex(this.list.getSelectedIndex() + 1);
                this.list.updateUI();
            }
        } else if (e.getSource() == this.completeButton) {
            if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je het bezorgingstraject wilt afronden?", "Bezorgingstraject afronden", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                System.out.println("TODO");
            }
        }
    }
}

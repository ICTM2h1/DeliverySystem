package DeliveryRoute;

import Crud.Order;
import UI.Dialogs.JDialogRawListBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Class to navigate through the route.
 */
public class DeliveryFollowDialog extends JDialogRawListBase implements ActionListener {

    private JButton confirmButton, rejectButton, completeButton;
    public static ArrayList<Object> listItemsCopy;
    private DeliveryStatus routeStatus;

    /**
     * Creates a new delivery follow dialog.
     *
     * @param frame the frame.
     * @param modal is it a modal?
     * @param route the route.
     * @param labels the labels.
     * @param title the title
     */
    public DeliveryFollowDialog(JFrame frame, boolean modal, ArrayList<DeliveryPointBase> route, String[] labels, String title) {
        super(frame, modal, new ArrayList<>(route), labels, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ListCellRenderer<Object> getListCellRenderer() {
        listItemsCopy = this.listItems;

        return new DefaultListCellRenderer() {
            /**
             * {@inheritDoc}
             */
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                try {
                    // We increase every point with one, because the current point is the start point and the next one is
                    // the arriving point.
                    DeliveryPointBase deliveryPoint = (DeliveryPointBase) listItemsCopy.get(index + 1);
                    DeliveryStatus deliveryPointStatus = deliveryPoint.getStatus();

                    Color background = DeliveryStatus.NONE.backgroundColor();
                    Color foreground = DeliveryStatus.NONE.foregroundColor();
                    if (deliveryPointStatus.compareStatus(DeliveryStatus.BUSY_WITH_OTHER_DELIVERING)) {
                        foreground = DeliveryStatus.BUSY_WITH_OTHER_DELIVERING.foregroundColor();
                    } else if (deliveryPointStatus.compareStatus(DeliveryStatus.NOW_DELIVERING)) {
                        foreground = DeliveryStatus.NOW_DELIVERING.foregroundColor();
                        background = DeliveryStatus.NOW_DELIVERING.backgroundColor();
                    } else if (deliveryPointStatus.compareStatus(DeliveryStatus.REJECTED)) {
                        foreground = DeliveryStatus.REJECTED.foregroundColor();
                    } else if (deliveryPointStatus.compareStatus(DeliveryStatus.COMPLETED)) {
                        foreground = DeliveryStatus.COMPLETED.foregroundColor();
                    }

                    this.setBackground(background);
                    this.setForeground(foreground);

                } catch (Exception exception) {
                    this.setForeground(Color.LIGHT_GRAY);
                }
                return this;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateRawListItemPreview(Object listItem) {
        DeliveryPointBase deliveryPoint = (DeliveryPointBase) listItem;
        int nextIndex = this.list.getSelectedIndex() + 1;
        if (nextIndex >= this.listItems.size()) {
            this.preview.addComponent(new JLabel("Error! Er is iets fout gegaan."), true);
            return;
        }

        DeliveryPointBase nextDeliveryPoint = (DeliveryPointBase) this.listItems.get(nextIndex);

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 0, 0);
        this.preview.gridBagConstraints.weightx = 0.5;

        JLabel pointLabel = new JLabel(deliveryPoint.label());
        pointLabel.setFont(new Font("default", Font.BOLD, 20));
        this.preview.addComponent(pointLabel, true);

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Afstand (hemelsbreed):"), true);
        this.preview.addComponent(new JLabel(deliveryPoint.distance(nextDeliveryPoint) + " km"));

        this.preview.gridBagConstraints.insets = new Insets(15, 0, 5, 0);
        JLabel addressFromLabel = new JLabel("Vertrekpunt");
        addressFromLabel.setFont(new Font("default", Font.BOLD, 13));
        this.preview.addComponent(addressFromLabel, true);

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Stad:"), true);
        this.preview.addComponent(new JLabel(deliveryPoint.label()));

        this.preview.addComponent(new JLabel("Straat:"), true);
        this.preview.addComponent(new JLabel(deliveryPoint.getStreet()));

        this.preview.addComponent(new JLabel("Postcode:"), true);
        this.preview.addComponent(new JLabel(deliveryPoint.getPostalCode()));

        this.preview.gridBagConstraints.insets = new Insets(15, 0, 5, 0);
        JLabel addressToLabel = new JLabel("Aankomstpunt");
        addressToLabel.setFont(new Font("default", Font.BOLD, 13));
        this.preview.addComponent(addressToLabel, true);

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Stad:"), true);
        this.preview.addComponent(new JLabel(nextDeliveryPoint.label()));

        this.preview.addComponent(new JLabel("Straat:"), true);
        this.preview.addComponent(new JLabel(nextDeliveryPoint.getStreet()));

        this.preview.addComponent(new JLabel("Postcode:"), true);
        this.preview.addComponent(new JLabel(nextDeliveryPoint.getPostalCode()));

        if (nextDeliveryPoint instanceof DeliveryOrderPoint) {
            this.preview.addComponent(new JLabel("Bestelling:"), true);
            this.preview.addComponent(new JLabel(((DeliveryOrderPoint) nextDeliveryPoint).getOrder().get("OrderID")));
        }

        this.preview.addComponent(new JLabel("Status:"), true);
        JLabel newStatus = new JLabel(nextDeliveryPoint.getStatus().toString());
        newStatus.setFont(new Font("default", Font.BOLD, 13));
        if (nextDeliveryPoint.compareStatus(DeliveryStatus.REJECTED)) {
            newStatus.setFont(new Font("default", Font.ITALIC, 13));
        }
        this.preview.addComponent(newStatus);

        // Decrease the number of list items with 2 because we have to remove the start and end point. These points are
        // not shown as apart buttons in the UI.
        if (this.list.getSelectedIndex() == (this.listItems.size() - 2)) {
            this.completeButton = new JButton("Bezorgingstraject afronden");
            this.completeButton.addActionListener(this);
            this.preview.addFullWidthComponent(this.completeButton, 2);
        // Currently delivering (display buttons)
        } else if (nextDeliveryPoint.compareStatus(DeliveryStatus.NOW_DELIVERING)) {
            this.rejectButton = new JButton("Bestelling annuleren");
            this.rejectButton.addActionListener(this);

            this.confirmButton = new JButton("Bestelling bezorgd");
            this.confirmButton.addActionListener(this);

            this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
            this.preview.addComponent(this.rejectButton, true);
            this.preview.addComponent(this.confirmButton);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.routeStatus = DeliveryStatus.NONE;

        if (e.getSource() == this.rejectButton) {
            if (JOptionPane.showConfirmDialog(this, "Weet je zeker dat je deze bestelling wilt annuleren?", "Bevestiging", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // We increase every point with one, because the current point is the start point and the next one is
                // the arriving point.
                int currentPointIndex = this.list.getSelectedIndex() + 1;
                int nextPointIndex = currentPointIndex + 1;

                // Reject current delivery point and change status of next point to delivering.
                DeliveryPointBase currentDeliveryPoint = (DeliveryPointBase) this.listItems.get(currentPointIndex);
                currentDeliveryPoint.setStatus(DeliveryStatus.REJECTED);

                DeliveryPointBase nextDeliveryPoint = (DeliveryPointBase) this.listItems.get(nextPointIndex);
                nextDeliveryPoint.setStatus(DeliveryStatus.NOW_DELIVERING);

                if (currentDeliveryPoint instanceof DeliveryOrderPoint) {
                    LinkedHashMap<String, String> entity = ((DeliveryOrderPoint) currentDeliveryPoint).getOrder();
                    Order order = new Order();
                    order.addCondition("OrderID", entity.get("OrderID"));
                    order.addValue("Status", String.valueOf(DeliveryStatus.REJECTED.toInteger()));
                    order.update();
                }

                // Go to the next delivery point.
                this.list.setSelectedIndex(this.list.getSelectedIndex() + 1);
                this.list.updateUI();
            }
        } else if (e.getSource() == this.confirmButton) {
            if (JOptionPane.showConfirmDialog(this, "Weet je zeker dat je deze bestelling wilt bevestigen?", "Bevestiging", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int currentPointIndex = this.list.getSelectedIndex() + 1;
                int nextPointIndex = currentPointIndex + 1;

                // Complete current delivery point and change status of next point to delivering.
                DeliveryPointBase currentDeliveryPoint = (DeliveryPointBase) this.listItems.get(currentPointIndex);
                currentDeliveryPoint.setStatus(DeliveryStatus.COMPLETED);

                DeliveryPointBase nextDeliveryPoint = (DeliveryPointBase) this.listItems.get(nextPointIndex);
                nextDeliveryPoint.setStatus(DeliveryStatus.NOW_DELIVERING);

                if (currentDeliveryPoint instanceof DeliveryOrderPoint) {
                    LinkedHashMap<String, String> entity = ((DeliveryOrderPoint) currentDeliveryPoint).getOrder();
                    Order order = new Order();
                    order.addCondition("OrderID", entity.get("OrderID"));
                    order.addValue("Status", String.valueOf(DeliveryStatus.COMPLETED.toInteger()));
                    order.update();
                }

                // Go to the next delivery point.
                this.list.setSelectedIndex(this.list.getSelectedIndex() + 1);
                this.list.updateUI();
            }
        } else if (e.getSource() == this.completeButton) {
            if (JOptionPane.showConfirmDialog(this, "Weet je zeker dat je het bezorgingstraject wilt afronden?", "Bezorgingstraject afronden", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.routeStatus = DeliveryStatus.COMPLETED;
                this.dispose();
            }
        }
    }

    /**
     * Gets the delivery status for current route.
     *
     * @return DeliveryStatus for current route.
     */
    public DeliveryStatus getRouteStatus() {
        return routeStatus;
    }
}

package DeliveryRoute;

import UI.Dialogs.JDialogRawListBase;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeliveryFollowPanel extends JDialogRawListBase implements ActionListener {

    private JButton confirmButton, rejectButton, completeButton;
    public static ArrayList<Object> listItemsCopy;
    private boolean isStartingPoint = true;

    public DeliveryFollowPanel(JFrame frame, boolean modal, ArrayList<DeliveryPointBase> route, DeliveryPointBase startingPoint, String[] labels, String title) {
        super(frame, modal, new ArrayList<> (route), startingPoint, labels, title);
        startingPoint.setStatus(DeliveryStatus.BUSY);
    }

    private DeliveryPointBase currentlyDelivering;
    private Integer currentlyDeliveringIndex;

    @Override
    protected String getListPreviewTitle() {
        return null;
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
                DeliveryPointBase cityListItem = (DeliveryPointBase) listItemsCopy.get(index);
                DeliveryStatus deliveryPointStatus = cityListItem.getStatus();

                Color background = Color.WHITE;
                Color foreground = Color.BLACK;

                // TODO: IS ER EEN MAKKELIJKERE MANIER?
                if (deliveryPointStatus.compareStatus(DeliveryStatus.BUSY)) {
                    foreground = DeliveryStatus.BUSY.foregroundColor();
                } else if (deliveryPointStatus.compareStatus(DeliveryStatus.DELIVERING)) {
                    foreground = DeliveryStatus.DELIVERING.foregroundColor();
                    background = DeliveryStatus.DELIVERING.backgroundColor();
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

    @Override
    protected void updateRawListItemPreview(Object listItem) {
        DeliveryPointBase waypoint = (DeliveryPointBase) listItem;

        // previousWaypoint may not exceed the length of route.
        DeliveryPointBase previousWaypoint = (DeliveryPointBase) startingPoint;
        int previousPointIndex = this.list.getSelectedIndex() - 1;

        if (previousPointIndex > 0 && previousPointIndex <= this.listItems.size()) {
            previousWaypoint = (DeliveryPointBase) this.listItems.get(previousPointIndex);
        }

        // Set first (after startpoint) point to status DELIVERING
        if (waypoint.compareStatus(DeliveryStatus.DELIVERING) && this.list.getSelectedIndex() == 0) {
            waypoint.setStatus(DeliveryStatus.DELIVERING);
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
        if (previousWaypoint instanceof DeliveryPoint && isStartingPoint) {
            this.preview.addComponent(new JLabel(previousWaypoint.addressLabel() + " (STARTPUNT)") );
            // Makes sure that we only have one starting point.
            this.isStartingPoint = false;
        } else {
            this.preview.addComponent(new JLabel(previousWaypoint.addressLabel()));
        }

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Naar:"), true);

        // Add 'eindpunt' at end of string incase it is the ending point
        if (waypoint instanceof DeliveryPoint) {
            this.preview.addComponent(new JLabel(waypoint.addressLabel() + " (EINDPUNT)") );
        } else {
            this.preview.addComponent(new JLabel(waypoint.addressLabel()));
        }

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Geschatte afstand:"), true);
        this.preview.addComponent(new JLabel(previousWaypoint.distance(waypoint) + " km"));

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Status:"), true);
        this.preview.addComponent(new JLabel(waypoint.getStatus().toString()));

        this.preview.gridBagConstraints.insets = new Insets(15, 0, 5, 0);
        this.preview.addComponent(new JLabel("<html><span style='font-size:13px'>Adresgegevens</span></html>"), true);

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Postcode:"), true);
        this.preview.addComponent(new JLabel(waypoint.getPostalCode()));

        this.preview.addComponent(new JLabel("Straat:"), true);
        this.preview.addComponent(new JLabel(waypoint.getStreet()));

        this.preview.addComponent(new JLabel("Stad:"), true);
        this.preview.addComponent(new JLabel(waypoint.label()));

        // Currently navigating to endpoint
        if (waypoint.compareStatus(DeliveryStatus.DELIVERING) && waypoint instanceof DeliveryPoint) {
            this.completeButton = new JButton("Bezorgingstraject afronden");
            this.completeButton.addActionListener(this);
            this.preview.addFullWidthComponent(this.completeButton, 2);
        // Currently delivering (display buttons)
        } else if (waypoint.compareStatus(DeliveryStatus.DELIVERING)) {
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
                currentlyDelivering.setStatus(DeliveryStatus.REJECTED);

                currentlyDelivering = (DeliveryPointBase) listItemsCopy.get(currentlyDeliveringIndex);
                currentlyDelivering.setStatus(DeliveryStatus.DELIVERING);
                currentlyDeliveringIndex++;

                this.list.setSelectedIndex(this.list.getSelectedIndex() + 1);
                this.list.updateUI();
            }
        } else if (e.getSource() == this.confirmButton) {
            if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze bestelling wilt bevestigen?", "Bevestiging", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                currentlyDelivering.setStatus(DeliveryStatus.COMPLETED);

                currentlyDelivering = (DeliveryPointBase) listItemsCopy.get(currentlyDeliveringIndex);
                currentlyDelivering.setStatus(DeliveryStatus.DELIVERING);
                currentlyDeliveringIndex++;

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

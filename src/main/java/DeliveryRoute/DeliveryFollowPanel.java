package DeliveryRoute;

import UI.Dialogs.JDialogRawListBase;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeliveryFollowPanel extends JDialogRawListBase implements ActionListener {

    private JButton confirmButton, rejectButton, completeButton;
    private int indexCounter;
    public static ArrayList<Object> listItemsCopy;

    public DeliveryFollowPanel(JFrame frame, boolean modal, ArrayList<DeliveryPointBase> route, DeliveryPointBase startingPoint, String[] labels, String title) {
        super(frame, modal, new ArrayList<> (route), startingPoint, labels, title);
        indexCounter = 0;
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
        DeliveryPointBase previousWaypoint;
//        if (listItemsCopy.get(0).equals(listItems.indexOf(waypoint))) {
        if (listItemsCopy.indexOf(waypoint) == 0 && indexCounter == 0) {
            previousWaypoint = (DeliveryPointBase) startingPoint;
        } else {
            previousWaypoint = (DeliveryPointBase) listItemsCopy.get(indexCounter - 1);
        }

        // Set first (after startpoint) point to status DELIVERING
        if (DeliveryStatus.valueOf(waypoint.getStatus()) == DeliveryStatus.DELIVERING && indexCounter == 0) {
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
        if (previousWaypoint instanceof DeliveryPoint) {
            this.preview.addComponent(new JLabel(previousWaypoint.label() + " (STARTPUNT)") );
        } else {
            this.preview.addComponent(new JLabel(previousWaypoint.label()));
        }

        this.preview.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.preview.addComponent(new JLabel("Naar:"), true);

        // Add 'eindpunt' at end of string incase it is the ending point
        if (waypoint instanceof DeliveryPoint) {
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
        this.preview.addComponent(new JLabel("HEBBEN WIJ EEN STRAATNAAM...." + waypoint.getHouseNumber()));

        // Currently navigating to endpoint
        if (DeliveryStatus.valueOf(waypoint.getStatus()) == DeliveryStatus.DELIVERING && waypoint instanceof DeliveryPoint) {
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
        indexCounter++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.rejectButton) {
            if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze bestelling wilt annuleren?", "Bevestiging", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                currentlyDelivering.setStatus(DeliveryStatus.REJECTED.toInteger());

                currentlyDelivering = (DeliveryPointBase) listItemsCopy.get(currentlyDeliveringIndex);
                currentlyDelivering.setStatus(DeliveryStatus.DELIVERING.toInteger());
                currentlyDeliveringIndex++;

                this.list.setSelectedIndex(this.list.getSelectedIndex() + 1);
                this.list.updateUI();
            }
        } else if (e.getSource() == this.confirmButton) {
            if (JOptionPane.showConfirmDialog(null, "Weet je zeker dat je deze bestelling wilt bevestigen?", "Bevestiging", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                currentlyDelivering.setStatus(DeliveryStatus.COMPLETED.toInteger());

                currentlyDelivering = (DeliveryPointBase) listItemsCopy.get(currentlyDeliveringIndex);
                currentlyDelivering.setStatus(DeliveryStatus.DELIVERING.toInteger());
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

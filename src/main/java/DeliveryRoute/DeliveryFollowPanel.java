package DeliveryRoute;

import Authenthication.User;
import Crud.StockItem;
import UI.Panels.JPanelBase;
import UI.Panels.JPanelListBase;
import UI.Panels.JPanelRawListBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class DeliveryFollowPanel extends JPanelRawListBase implements ActionListener {

    private JButton addButton, editButton, deleteButton;
    private final String routeName = "KLOOSTERHAAR - GRONINGEN - ALMELO";
    public static DeliveryPoint startpunt = new DeliveryPoint ("Kloosterhaar",52.5107628, 6.6406951, 1);
    public static final ArrayList<DeliveryPoint> stedenlijst = new ArrayList<DeliveryPoint>(Arrays.asList(
            new DeliveryPoint("Amsterdam",  52.3546449, 4.8339212, 1),
            new DeliveryPoint("Groningen", 53.2216999, 6.530674, 1),
            new DeliveryPoint("Middelburg", 51.5050983, 3.58268, 1),
            new DeliveryPoint("Rotterdam", 51.9279653, 4.4207889, 1),
            new DeliveryPoint("Maastricht", 50.8577758, 5.6308644, 1),
            new DeliveryPoint("Hardenberg", 52.5736686, 6.6010214, 1)
    ));

    /**
     * Creates a new list panel.
     *
     * @param user The user.
     */
    public DeliveryFollowPanel(User user) {
        super(user);
    }


    @Override
    public String getTitle() {
        return "TITEL VOOR LATER";
    }

    @Override
    protected String getListPreviewTitle() {
        return null;
    }

    @Override
    protected ArrayList<Object> getRawListItems() {
        return new ArrayList<> (stedenlijst);
    }

    @Override
    protected String getRawListItemLabel(Object listItem) {
        DeliveryPoint test = (DeliveryPoint) listItem;

        return test.label();
    }

    @Override
    protected void updateRawListItemPreview(Object listItem) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

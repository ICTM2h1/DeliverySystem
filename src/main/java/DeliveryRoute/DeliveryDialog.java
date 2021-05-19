package DeliveryRoute;

import javax.swing.JOptionPane;

public class DeliveryDialog {

    public static void main(String[] args) {

        int input = JOptionPane.showConfirmDialog(null, "Do you like bacon?");
        System.out.println(input);
    }
}
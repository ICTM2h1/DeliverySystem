package DeliveryRoute;

import java.awt.*;
import java.util.LinkedHashMap;

public enum DeliveryStatus {
    BUSY(0) {
        @Override
        public String toString() {
            return "Bezig met andere bezorging";
        }

        public Color foregroundColor() {
            return Color.BLACK;
        }

        public int toInteger() {
            return 0;
        }
    },
    DELIVERING(1) {
        @Override
        public String toString() {
            return "Nu aan het bezorgen";
        }

        public Color foregroundColor() {
            return Color.WHITE;
        }

        public Color backgroundColor() { return Color.BLUE ;}

        public int toInteger() {
            return 1;
        }
    },
    REJECTED(2) {
        @Override
        public String toString() {
            return "Bestelling geannuleerd";
        }

        public Color foregroundColor() {
            return Color.RED;
        }

        public int toInteger() {
            return 2;
        }
    },
    COMPLETED(3) {
        @Override
        public String toString() {
            return "Bestelling afgeleverd";
        }

        public Color foregroundColor() {
            return Color.GREEN;
        }

        public int toInteger() {
            return 3;
        }
    };

    private int status;
    private static final LinkedHashMap<Integer, DeliveryStatus> statusMap = new LinkedHashMap<>();

    DeliveryStatus(int status) {
        this.status = status;
    }

    /* Create a static map with all defined user roles. */
    static {
        for (DeliveryStatus deliveryStatus : DeliveryStatus.values()) {
            statusMap.put(deliveryStatus.status, deliveryStatus);
        }
    }

    /**
     * Returns the ENUM-tag by it's given value.
     *
     * @param status integer necessary to identify status.
     * @return DeliveryStatus with corresponding value.
     */
    public static DeliveryStatus valueOf(int status) {
        return statusMap.get(status);
    }

    /**
     * String value of delivery status.
     *
     * @return String with name of status.
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Returns foreground color for corresponding delivery status.
     *
     * @return Color of corresponding delivery status (foreground).
     */
    public Color foregroundColor() {
        return Color.BLACK;
    }

    /**
     * Returns background color for corresponding delivery status.
     *
     * @return Color of corresponding delivery status (background).
     */
    public Color backgroundColor() {
        return Color.WHITE;
    }

    /**
     * Returns the integer value of the status.
     *
     * @return integer value of the status.
     */
    public int toInteger() {
        return 0;
    }

    public boolean compareStatus(DeliveryStatus compareStatus) {
        return this.status == compareStatus.toInteger();
    }}

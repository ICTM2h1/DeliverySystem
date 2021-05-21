package DeliveryRoute;

import java.awt.*;
import java.util.LinkedHashMap;

/**
 * Enum provides the value for delivery statuses.
 */
public enum DeliveryStatus {
    NONE(-1) {
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Geen status bekend";
        }

        /**
         * {@inheritDoc}
         */
        public int toInteger() {
            return -1;
        }
    },
    BUSY_WITH_OTHER_DELIVERING(0) {
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Bezig met andere bezorging";
        }

        /**
         * {@inheritDoc}
         */
        public Color foregroundColor() {
            return Color.BLACK;
        }

        /**
         * {@inheritDoc}
         */
        public int toInteger() {
            return 0;
        }
    },
    NOW_DELIVERING(1) {
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Nu aan het bezorgen";
        }

        /**
         * {@inheritDoc}
         */
        public Color foregroundColor() {
            return Color.WHITE;
        }

        /**
         * {@inheritDoc}
         */
        public Color backgroundColor() { return Color.BLUE ;}

        /**
         * {@inheritDoc}
         */
        public int toInteger() {
            return 1;
        }
    },
    REJECTED(2) {
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Bestelling geannuleerd";
        }

        /**
         * {@inheritDoc}
         */
        public Color foregroundColor() {
            return Color.RED;
        }

        /**
         * {@inheritDoc}
         */
        public int toInteger() {
            return 2;
        }
    },
    COMPLETED(3) {
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Bestelling afgeleverd";
        }

        /**
         * {@inheritDoc}
         */
        public Color foregroundColor() {
            return Color.GREEN;
        }

        /**
         * {@inheritDoc}
         */
        public int toInteger() {
            return 3;
        }
    };

    private int status;
    private static final LinkedHashMap<Integer, DeliveryStatus> statusMap = new LinkedHashMap<>();

    /**
     * Creates a new delivery status.
     *
     * @param status the status.
     */
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

    /**
     * Determines if statuses are equal.
     *
     * @param compareStatus status to be compared.
     * @return whether the status is equal or not.
     */
    public boolean compareStatus(DeliveryStatus compareStatus) {
        return this.status == compareStatus.toInteger();
    }
}
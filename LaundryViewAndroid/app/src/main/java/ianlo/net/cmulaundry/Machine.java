package ianlo.net.cmulaundry;

public class Machine {
    // Machine states.
    public static final int AVAILABLE = 0;
    public static final int RUNNING = 1;
    public static final int ENDED = 2;
    public static final int OUTOFSERVICE = 3;
    public static final int UNKNOWN = 4;

    // Dryer or Washer.
    public static final int DRYER = 0;
    public static final int WASHER = 1;

    // Local fields.
    private int type;
    private int status;
    private int number;

    // Time remaining in minutes.
    private int timeRemaining;

    /**
     * Creates a new laundry machine with a washer or dryer type and the machine number written on the machine.
     *
     * @param type   one of the type constants given by this class (i.e. WASHER, DRYER).
     * @param number the laundry machine number that is written on the machine.
     */
    public Machine(int type, int number) {
        this.type = type;
        this.status = AVAILABLE;
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getStringStatus() {
        switch (status) {
            case AVAILABLE:
                return "Available";
            case RUNNING:
                return "Running (" + timeRemaining + " min left)";
            case ENDED:
                return "Cycle Ended";
            case OUTOFSERVICE:
                return "Out of Service";
            case UNKNOWN:
                return "Status Unknown";
            default:
                return "Status Unknown";
        }
    }

    public void setStatus(int status) {
        this.status = status;
        if (status != RUNNING) timeRemaining = 0;
    }

    // Time remaining in minutes.
    public int getTimeRemaining() {
        return timeRemaining;
    }

    // When you set the time remaining, it automatically sets the machine to running.
    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
        this.status = RUNNING;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Sets the status and time remaining of the machine using the String from the LaundryView page.
     *
     * @param machineText The description string from the LaundryView page.
     */
    public void setStatusWithString(String machineText) {
        // Set the status of the machines based on what is in the scraped text.
        if (machineText.contains("available")) {
            setStatus(Machine.AVAILABLE);
        }
        else if (machineText.contains("time remaining")) {
            setStatus(Machine.RUNNING);
            // Scrape the time remaining from the text and set it to the machine's time remaining.
            int startIndex = machineText.indexOf("remaining") + 10;
            int endIndex = machineText.indexOf("min") - 1;
            setTimeRemaining(Integer.parseInt(machineText.substring(startIndex, endIndex)));
        }
        else if (machineText.contains("out of service")) {
            setStatus(Machine.OUTOFSERVICE);
        }
        else if (machineText.contains("ended")) {
            setStatus(Machine.ENDED);
        }
        else if (machineText.contains("unknown")) {
            setStatus(Machine.UNKNOWN);
        }
        else {
            setStatus(Machine.UNKNOWN);
        }
    }
}

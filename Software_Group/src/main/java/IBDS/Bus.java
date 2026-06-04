package IBDS;

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType;

    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    // Converts bus object into a human-readable TXT file format.
    public String toFileString() {
        return busID + "," + capacity + "," + fuelLevel + "," + fuelType;
    }

    // Recreates a Bus object from one line in the TXT file.
    public static Bus fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");

        if (parts.length != 4) {
            return null;
        }

        try {
            String busID = parts[0];
            int capacity = Integer.parseInt(parts[1]);
            double fuelLevel = Double.parseDouble(parts[2]);
            String fuelType = parts[3];

            return new Bus(busID, capacity, fuelLevel, fuelType);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
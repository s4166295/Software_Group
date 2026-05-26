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

    public String getBusID() { return busID; }
    public int getCapacity() { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType() { return fuelType; }

    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setFuelLevel(double fuelLevel) { this.fuelLevel = fuelLevel; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String toFileString() {
        return busID + "," + capacity + "," + fuelLevel + "," + fuelType;
    }
}
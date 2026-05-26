package IBDS;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BusRepository {
    private File file;

    public BusRepository(String filePath) {
        this.file = new File(filePath);
    }

    public boolean addBus(Bus bus) {
        if (!isValidBusID(bus.getBusID())) return false;
        if (retrieveBus(bus.getBusID()) != null) return false;

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(bus.toFileString() + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Bus retrieveBus(String busID) {
        for (Bus bus : getAllBuses()) {
            if (bus.getBusID().equals(busID)) {
                return bus;
            }
        }
        return null;
    }

    public boolean updateBus(Bus updatedBus) {
        List<Bus> buses = getAllBuses();

        for (int i = 0; i < buses.size(); i++) {
            Bus existing = buses.get(i);

            if (existing.getBusID().equals(updatedBus.getBusID())) {
                if (updatedBus.getCapacity() > existing.getCapacity()) return false;

                buses.set(i, updatedBus);
                saveAllBuses(buses);
                return true;
            }
        }
        return false;
    }

    public int countBuses() {
        return getAllBuses().size();
    }

    private List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();

        if (!file.exists()) return buses;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length == 4) {
                    buses.add(new Bus(
                            data[0],
                            Integer.parseInt(data[1]),
                            Double.parseDouble(data[2]),
                            data[3]
                    ));
                }
            }
        } catch (Exception e) {
            return buses;
        }

        return buses;
    }

    private void saveAllBuses(List<Bus> buses) {
        try (FileWriter writer = new FileWriter(file, false)) {
            for (Bus bus : buses) {
                writer.write(bus.toFileString() + "\n");
            }
        } catch (IOException e) {
            // ignored for assignment simplicity
        }
    }

    public boolean isValidBusID(String busID) {
        if (busID == null || busID.length() != 8) return false;
        return busID.matches("\\d{8}");
    }

    public boolean canDriverOperateBus(Driver driver, Bus bus) {
        int age = calculateAge(driver.getBirthdate());

        if (age > 50 && bus.getCapacity() >= 50) return false;

        if (bus.getFuelType().equalsIgnoreCase("Electricity") &&
                driver.getExperienceYears() < 5) return false;

        if ((bus.getFuelType().equalsIgnoreCase("Electricity") ||
                bus.getFuelType().equalsIgnoreCase("Hybrid")) &&
                !(driver.getLicenseType().equalsIgnoreCase("Heavy") ||
                        driver.getLicenseType().equalsIgnoreCase("PublicTransport"))) {
            return false;
        }

        return true;
    }
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate dob = LocalDate.parse(birthdate, formatter);
        return Period.between(dob, LocalDate.now()).getYears();
    }
}
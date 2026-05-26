package IBDS;

import java.io.*;
import java.util.*;

public class DriverRepository {
    private File file;

    public DriverRepository(String filePath) {
        this.file = new File(filePath);
    }

    public boolean addDriver(Driver driver) {
        if (!isValidDriverID(driver.getDriverID())) return false;
        if (!isValidAddress(driver.getAddress())) return false;
        if (!isValidBirthdate(driver.getBirthdate())) return false;
        if (retrieveDriver(driver.getDriverID()) != null) return false;

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(driver.toFileString() + "\n");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Driver retrieveDriver(String driverID) {
        for (Driver driver : getAllDrivers()) {
            if (driver.getDriverID().equals(driverID)) {
                return driver;
            }
        }
        return null;
    }

    public boolean updateDriver(Driver updatedDriver) {
        List<Driver> drivers = getAllDrivers();

        for (int i = 0; i < drivers.size(); i++) {
            Driver existing = drivers.get(i);

            if (existing.getDriverID().equals(updatedDriver.getDriverID())) {
                if (!existing.getName().equals(updatedDriver.getName())) return false;
                if (existing.getExperienceYears() > 10 &&
                        !existing.getLicenseType().equals(updatedDriver.getLicenseType())) return false;

                drivers.set(i, updatedDriver);
                saveAllDrivers(drivers);
                return true;
            }
        }
        return false;
    }

    public int countDrivers() {
        return getAllDrivers().size();
    }

    private List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();

        if (!file.exists()) return drivers;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length == 6) {
                    drivers.add(new Driver(
                            data[0],
                            data[1],
                            Integer.parseInt(data[2]),
                            data[3],
                            data[4],
                            data[5]
                    ));
                }
            }
        } catch (Exception e) {
            return drivers;
        }

        return drivers;
    }

    private void saveAllDrivers(List<Driver> drivers) {
        try (FileWriter writer = new FileWriter(file, false)) {
            for (Driver driver : drivers) {
                writer.write(driver.toFileString() + "\n");
            }
        } catch (IOException e) {
        }
    }

    public boolean isValidDriverID(String driverID) {
        if (driverID == null || driverID.length() != 10) return false;

        if (!Character.isDigit(driverID.charAt(0)) || !Character.isDigit(driverID.charAt(1))) return false;
        if (driverID.charAt(0) < '2' || driverID.charAt(0) > '9') return false;
        if (driverID.charAt(1) < '2' || driverID.charAt(1) > '9') return false;

        int specialCount = 0;
        for (int i = 2; i <= 7; i++) {
            char c = driverID.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }

        if (specialCount < 2) return false;

        return Character.isUpperCase(driverID.charAt(8)) &&
                Character.isUpperCase(driverID.charAt(9));
    }

    public boolean isValidAddress(String address) {
        if (address == null) return false;
        return address.split("\\|").length == 5;
    }

    public boolean isValidBirthdate(String birthdate) {
        if (birthdate == null) return false;
        return birthdate.matches("\\d{2}-\\d{2}-\\d{4}");
    }
}
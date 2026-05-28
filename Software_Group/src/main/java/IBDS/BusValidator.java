package IBDS;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Set;

public class BusValidator {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    // B1: busID must be unique, exactly 8 characters long, and all digits.
    public static boolean isValidBusId(String busID, Set<String> existingBusIds) {
        if (busID == null || !busID.matches("\\d{8}")) {
            return false;
        }

        return existingBusIds == null || !existingBusIds.contains(busID);
    }

    // B2: bus capacity cannot increase during update.
    public static boolean canUpdateCapacity(int oldCapacity, int newCapacity) {
        return oldCapacity > 0 && newCapacity > 0 && newCapacity <= oldCapacity;
    }

    // B3: drivers older than 50 cannot drive buses with capacity 50 or more.
    public static boolean satisfiesDriverAgeRestriction(
            String birthdate,
            int busCapacity,
            LocalDate currentDate
    ) {
        if (birthdate == null || currentDate == null || busCapacity <= 0) {
            return false;
        }

        LocalDate birthDate = LocalDate.parse(birthdate, DATE_FORMAT);
        int age = Period.between(birthDate, currentDate).getYears();

        return !(age > 50 && busCapacity >= 50);
    }

    // B4: only drivers with at least 5 years experience can drive electric buses.
    public static boolean satisfiesElectricBusExperienceRestriction(
            int experienceYears,
            String fuelType
    ) {
        if (fuelType == null || experienceYears < 0) {
            return false;
        }

        if (fuelType.equalsIgnoreCase("Electricity")) {
            return experienceYears >= 5;
        }

        return true;
    }

    // B5: only Heavy or PublicTransport licence holders can operate electric/hybrid buses.
    public static boolean satisfiesLicenceRestriction(
            String licenceType,
            String fuelType
    ) {
        if (licenceType == null || fuelType == null) {
            return false;
        }

        String normalisedLicence = licenceType.replaceAll("\\s+", "").toLowerCase();
        String normalisedFuel = fuelType.toLowerCase();

        boolean electricOrHybrid =
                normalisedFuel.equals("electricity") || normalisedFuel.equals("hybrid");

        if (!electricOrHybrid) {
            return true;
        }

        return normalisedLicence.equals("heavy")
                || normalisedLicence.equals("publictransport");
    }
}
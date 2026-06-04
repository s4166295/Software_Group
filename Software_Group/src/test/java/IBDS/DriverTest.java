package IBDS;

import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    // D1 - Driver ID Rules

    @Test
    public void validDriverIDShouldReturnTrue() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertTrue(repo.isValidDriverID("23@@abcdAB"));
    }

    @Test
    public void driverIDWithWrongLengthShouldReturnFalse() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertFalse(repo.isValidDriverID("23@@abcAB"));
    }

    @Test
    public void driverIDWithoutTwoSpecialCharactersShouldReturnFalse() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertFalse(repo.isValidDriverID("23abcdefAB"));
    }

    // D2 - Address Format

    @Test
    public void validAddressShouldReturnTrue() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertTrue(repo.isValidAddress("12|Main Street|Melbourne|VIC|Australia"));
    }

    @Test
    public void addressWithMissingFieldsShouldReturnFalse() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertFalse(repo.isValidAddress("12|Main Street|Melbourne|VIC"));
    }
//demo
    @Test
    public void nullAddressShouldReturnFalse() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertFalse(repo.isValidAddress(null));
    }

    // D3 - Birthdate Format

    @Test
    public void validBirthdateShouldReturnTrue() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertTrue(repo.isValidBirthdate("25-12-2000"));
    }

    @Test
    public void birthdateWithWrongSeparatorShouldReturnFalse() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertFalse(repo.isValidBirthdate("25/12/2000"));
    }

    @Test
    public void birthdateWithWrongOrderShouldReturnFalse() {
        DriverRepository repo = new DriverRepository("test-drivers.txt");
        assertFalse(repo.isValidBirthdate("2000-12-25"));
    }

    // D4 - License Update Restriction

    @Test
    public void driverWithMoreThan10YearsExperienceCannotChangeLicense() {
        File file = new File("driver-test-d4-1.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "23@@abcdAB",
                "John Smith",
                12,
                "Heavy",
                "12|Main Street|Melbourne|VIC|Australia",
                "10-10-1980"
        );

        Driver updated = new Driver(
                "23@@abcdAB",
                "John Smith",
                12,
                "PublicTransport",
                "12|Main Street|Melbourne|VIC|Australia",
                "10-10-1980"
        );

        repo.addDriver(original);
        assertFalse(repo.updateDriver(updated));

        file.delete();
    }

    @Test
    public void driverWith10YearsExperienceCanChangeLicense() {
        File file = new File("driver-test-d4-2.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "24@@abcdAB",
                "Sarah Lee",
                10,
                "Medium",
                "15|King Street|Sydney|NSW|Australia",
                "12-05-1990"
        );

        Driver updated = new Driver(
                "24@@abcdAB",
                "Sarah Lee",
                10,
                "Heavy",
                "15|King Street|Sydney|NSW|Australia",
                "12-05-1990"
        );

        repo.addDriver(original);
        assertTrue(repo.updateDriver(updated));

        file.delete();
    }

    @Test
    public void driverWithLessThan10YearsExperienceCanChangeLicense() {
        File file = new File("driver-test-d4-3.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "25@@abcdAB",
                "Michael Tan",
                5,
                "Light",
                "20|Queen Street|Brisbane|QLD|Australia",
                "01-01-1995"
        );

        Driver updated = new Driver(
                "25@@abcdAB",
                "Michael Tan",
                5,
                "Medium",
                "20|Queen Street|Brisbane|QLD|Australia",
                "01-01-1995"
        );

        repo.addDriver(original);
        assertTrue(repo.updateDriver(updated));

        file.delete();
    }

    // D5 - Immutable Fields

    @Test
    public void driverIDCannotBeChangedDuringUpdate() {
        File file = new File("driver-test-d5-1.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "26@@abcdAB",
                "David Brown",
                4,
                "Medium",
                "50|Collins Street|Melbourne|VIC|Australia",
                "20-08-1992"
        );

        Driver updated = new Driver(
                "27@@abcdAB",
                "David Brown",
                4,
                "Medium",
                "50|Collins Street|Melbourne|VIC|Australia",
                "20-08-1992"
        );

        repo.addDriver(original);
        assertFalse(repo.updateDriver(updated));

        file.delete();
    }

    @Test
    public void driverNameCannotBeChangedDuringUpdate() {
        File file = new File("driver-test-d5-2.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "28@@abcdAB",
                "Emma Wilson",
                6,
                "Heavy",
                "40|George Street|Sydney|NSW|Australia",
                "15-03-1988"
        );

        Driver updated = new Driver(
                "28@@abcdAB",
                "Emma Watson",
                6,
                "Heavy",
                "40|George Street|Sydney|NSW|Australia",
                "15-03-1988"
        );

        repo.addDriver(original);
        assertFalse(repo.updateDriver(updated));

        file.delete();
    }

    @Test
    public void mutableFieldsCanBeChangedWhenIDAndNameStaySame() {
        File file = new File("driver-test-d5-3.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "29@@abcdAB",
                "Olivia Green",
                3,
                "Light",
                "30|Old Street|Perth|WA|Australia",
                "22-11-1998"
        );

        Driver updated = new Driver(
                "29@@abcdAB",
                "Olivia Green",
                4,
                "Medium",
                "99|New Street|Perth|WA|Australia",
                "22-11-1998"
        );

        repo.addDriver(original);
        assertTrue(repo.updateDriver(updated));

        file.delete();
    }
}
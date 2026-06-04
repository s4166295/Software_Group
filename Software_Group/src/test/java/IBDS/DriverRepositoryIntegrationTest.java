package IBDS;

import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class DriverRepositoryIntegrationTest {

    // DI1:
    // Verifies that a valid driver record can be successfully stored
    // in the TXT file and later retrieved from the repository.
    @Test
    void validDriverShouldBeStoredAndRetrieved() {
        File file = new File("driver-integration1.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver driver = new Driver(
                "23@@abcdAB",
                "John Smith",
                5,
                "Medium",
                "12|Main Street|Melbourne|VIC|Australia",
                "10-10-1995"
        );

        assertTrue(repo.addDriver(driver));

        Driver retrieved = repo.retrieveDriver("23@@abcdAB");

        assertNotNull(retrieved);

        file.delete();
    }

    // DI2:
    // Verifies that a driver with an invalid Driver ID is rejected
    // and is not stored in the TXT file.
    @Test
    void invalidDriverShouldNotBeStored() {
        File file = new File("driver-integration2.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver driver = new Driver(
                "123",
                "John Smith",
                5,
                "Medium",
                "12|Main Street|Melbourne|VIC|Australia",
                "10-10-1995"
        );

        assertFalse(repo.addDriver(driver));
        assertEquals(0, repo.countDrivers());

        file.delete();
    }

    // DI3:
    // Verifies that updates to an existing driver are successfully
    // saved and persisted within the repository storage.
    @Test
    void updatedDriverShouldPersist() {
        File file = new File("driver-integration3.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        Driver original = new Driver(
                "24@@abcdAB",
                "Sarah Lee",
                5,
                "Medium",
                "12|Main Street|Melbourne|VIC|Australia",
                "10-10-1995"
        );

        repo.addDriver(original);

        Driver updated = new Driver(
                "24@@abcdAB",
                "Sarah Lee",
                6,
                "Heavy",
                "99|New Street|Melbourne|VIC|Australia",
                "10-10-1995"
        );

        assertTrue(repo.updateDriver(updated));

        file.delete();
    }

    // DI4:
    // Verifies that the repository correctly updates and maintains
    // the total number of stored driver records.
    @Test
    void driverCountShouldUpdateCorrectly() {
        File file = new File("driver-integration4.txt");
        file.delete();

        DriverRepository repo = new DriverRepository(file.getPath());

        repo.addDriver(new Driver(
                "25@@abcdAB",
                "A",
                5,
                "Medium",
                "1|A|Melbourne|VIC|Australia",
                "10-10-1995"
        ));

        repo.addDriver(new Driver(
                "26@@abcdAB",
                "B",
                5,
                "Medium",
                "2|B|Melbourne|VIC|Australia",
                "10-10-1995"
        ));

        assertEquals(2, repo.countDrivers());

        file.delete();
    }
}
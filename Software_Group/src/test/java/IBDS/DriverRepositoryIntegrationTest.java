package IBDS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DriverRepositoryIntegrationTest {

    @TempDir
    Path tempDir;

    private Path driverFile;
    private DriverRepository driverRepository;
//demo comment
    @BeforeEach
    void setUp() {
        driverFile = tempDir.resolve("drivers.txt");
        driverRepository = new DriverRepository(driverFile.toString());
    }

    private Driver createValidDriver(String driverID) {
        return new Driver(
                driverID,
                "John Smith",
                6,
                "Heavy",
                "25|Collins Street|Melbourne|VIC|Australia",
                "15-08-1988"
        );
    }

    @Test
    @DisplayName("ITD-01: Valid driver is stored correctly and can be retrieved from TXT file")
    void validDriverIsStoredCorrectlyAndRetrievedFromFile() {
        Driver driver = createValidDriver("23@@abcdAB");

        boolean result = driverRepository.addDriver(driver);

        assertTrue(result);
        assertTrue(Files.exists(driverFile));

        DriverRepository reloadedRepository = new DriverRepository(driverFile.toString());
        Driver storedDriver = reloadedRepository.retrieveDriver("23@@abcdAB");

        assertNotNull(storedDriver);
        assertEquals("23@@abcdAB", storedDriver.getDriverID());
        assertEquals("John Smith", storedDriver.getName());
        assertEquals(6, storedDriver.getExperienceYears());
        assertEquals("Heavy", storedDriver.getLicenseType());
        assertEquals("25|Collins Street|Melbourne|VIC|Australia", storedDriver.getAddress());
        assertEquals("15-08-1988", storedDriver.getBirthdate());
    }

    @Test
    @DisplayName("ITD-02: Invalid driver is rejected and is not written to TXT file")
    void invalidDriverIsRejectedAndNotStored() {
        Driver invalidDriver = new Driver(
                "13abcdefG",
                "Invalid Driver",
                4,
                "Medium",
                "10|King Street|Melbourne|VIC|Australia",
                "22-05-1992"
        );

        boolean result = driverRepository.addDriver(invalidDriver);

        assertFalse(result);
        assertEquals(0, driverRepository.countDrivers());

        DriverRepository reloadedRepository = new DriverRepository(driverFile.toString());
        assertNull(reloadedRepository.retrieveDriver("13abcdefG"));
    }

    @Test
    @DisplayName("ITD-03: Driver update is persisted correctly in TXT file")
    void driverUpdateIsPersistedCorrectly() {
        Driver originalDriver = createValidDriver("34##wxyzCD");
        assertTrue(driverRepository.addDriver(originalDriver));

        Driver updatedDriver = new Driver(
                "34##wxyzCD",
                "John Smith",
                8,
                "PublicTransport",
                "99|Swanston Street|Melbourne|VIC|Australia",
                "15-08-1988"
        );

        boolean updateResult = driverRepository.updateDriver(updatedDriver);

        assertTrue(updateResult);

        DriverRepository reloadedRepository = new DriverRepository(driverFile.toString());
        Driver storedDriver = reloadedRepository.retrieveDriver("34##wxyzCD");

        assertNotNull(storedDriver);
        assertEquals(8, storedDriver.getExperienceYears());
        assertEquals("PublicTransport", storedDriver.getLicenseType());
        assertEquals("99|Swanston Street|Melbourne|VIC|Australia", storedDriver.getAddress());
    }

    @Test
    @DisplayName("ITD-04: Driver count updates correctly after adding multiple valid drivers")
    void driverCountUpdatesCorrectlyAfterAddingDrivers() {
        Driver driverOne = createValidDriver("45!!abcdEF");

        Driver driverTwo = new Driver(
                "56$$wxyzGH",
                "Sarah Lee",
                3,
                "Medium",
                "7|Bourke Street|Melbourne|VIC|Australia",
                "02-11-1995"
        );

        assertEquals(0, driverRepository.countDrivers());

        assertTrue(driverRepository.addDriver(driverOne));
        assertEquals(1, driverRepository.countDrivers());

        assertTrue(driverRepository.addDriver(driverTwo));
        assertEquals(2, driverRepository.countDrivers());

        DriverRepository reloadedRepository = new DriverRepository(driverFile.toString());
        assertEquals(2, reloadedRepository.countDrivers());
    }

    @Test
    @DisplayName("ITD-05: Duplicate driver ID is rejected and count does not increase")
    void duplicateDriverIdIsRejectedAndCountDoesNotIncrease() {
        Driver firstDriver = createValidDriver("67%%abcdIJ");

        Driver duplicateDriver = new Driver(
                "67%%abcdIJ",
                "Another Person",
                5,
                "Heavy",
                "12|Elizabeth Street|Melbourne|VIC|Australia",
                "09-04-1990"
        );

        assertTrue(driverRepository.addDriver(firstDriver));
        assertFalse(driverRepository.addDriver(duplicateDriver));

        assertEquals(1, driverRepository.countDrivers());

        DriverRepository reloadedRepository = new DriverRepository(driverFile.toString());
        assertEquals(1, reloadedRepository.countDrivers());
    }

    @Test
    @DisplayName("ITD-06: Driver with more than 10 years experience cannot change licence type")
    void experiencedDriverCannotChangeLicenceTypeDuringUpdate() {
        Driver experiencedDriver = new Driver(
                "78@@abcdKL",
                "Michael Brown",
                12,
                "Heavy",
                "50|La Trobe Street|Melbourne|VIC|Australia",
                "19-03-1980"
        );

        assertTrue(driverRepository.addDriver(experiencedDriver));

        Driver invalidUpdate = new Driver(
                "78@@abcdKL",
                "Michael Brown",
                12,
                "PublicTransport",
                "50|La Trobe Street|Melbourne|VIC|Australia",
                "19-03-1980"
        );

        boolean updateResult = driverRepository.updateDriver(invalidUpdate);

        assertFalse(updateResult);

        DriverRepository reloadedRepository = new DriverRepository(driverFile.toString());
        Driver storedDriver = reloadedRepository.retrieveDriver("78@@abcdKL");

        assertNotNull(storedDriver);
        assertEquals("Heavy", storedDriver.getLicenseType());
    }
}
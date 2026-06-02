package IBDS;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    private BusRepository createRepo() throws Exception {
        File tempFile = File.createTempFile("buses", ".txt");
        tempFile.deleteOnExit();
        return new BusRepository(tempFile.getAbsolutePath());
    }

    // B1: Bus ID Rules

    @Test
    void B1_validBusId_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");

        assertTrue(repo.addBus(bus));
    }

    @Test
    void B1_busIdTooShort_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        Bus bus = new Bus("1234567", 40, 80.0, "Diesel");

        assertFalse(repo.addBus(bus));
    }

    @Test
    void B1_busIdWithLetters_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        Bus bus = new Bus("1234A678", 40, 80.0, "Diesel");

        assertFalse(repo.addBus(bus));
    }

    @Test
    void B1_duplicateBusId_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        Bus bus1 = new Bus("12345678", 40, 80.0, "Diesel");
        Bus bus2 = new Bus("12345678", 45, 70.0, "Hybrid");

        assertTrue(repo.addBus(bus1));
        assertFalse(repo.addBus(bus2));
    }

    // B2: Capacity Update Restriction

    @Test
    void B2_decreasingCapacity_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        repo.addBus(new Bus("11111111", 60, 80.0, "Diesel"));

        Bus updatedBus = new Bus("11111111", 50, 70.0, "Diesel");

        assertTrue(repo.updateBus(updatedBus));
    }

    @Test
    void B2_sameCapacity_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        repo.addBus(new Bus("22222222", 60, 80.0, "Diesel"));

        Bus updatedBus = new Bus("22222222", 60, 70.0, "Hybrid");

        assertTrue(repo.updateBus(updatedBus));
    }

    @Test
    void B2_increasingCapacity_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        repo.addBus(new Bus("33333333", 50, 80.0, "Diesel"));

        Bus updatedBus = new Bus("33333333", 60, 70.0, "Diesel");

        assertFalse(repo.updateBus(updatedBus));
    }

    // B3: Driver Age Restriction

    @Test
    void B3_driverUnder50LargeBus_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 10, "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("44444444", 50, 80.0, "Diesel");

        assertTrue(repo.canDriverOperateBus(driver, bus));
    }

    @Test
    void B3_driverOver50Capacity50_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 20, "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1970");
        Bus bus = new Bus("55555555", 50, 80.0, "Diesel");

        assertFalse(repo.canDriverOperateBus(driver, bus));
    }

    @Test
    void B3_driverOver50Capacity49_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 20, "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1970");
        Bus bus = new Bus("66666666", 49, 80.0, "Diesel");

        assertTrue(repo.canDriverOperateBus(driver, bus));
    }

    // B4: Electric Bus Experience Restriction

    @Test
    void B4_fiveYearsExperienceElectricBus_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 5, "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("77777777", 40, 80.0, "Electricity");

        assertTrue(repo.canDriverOperateBus(driver, bus));
    }

    @Test
    void B4_fourYearsExperienceElectricBus_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 4, "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("88888888", 40, 80.0, "Electricity");

        assertFalse(repo.canDriverOperateBus(driver, bus));
    }

    @Test
    void B4_zeroExperienceDieselBus_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 0, "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("99999999", 40, 80.0, "Diesel");

        assertTrue(repo.canDriverOperateBus(driver, bus));
    }

    // B5: Driver Licence Restriction

    @Test
    void B5_heavyLicenceElectricBus_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 10, "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("12121212", 40, 80.0, "Electricity");

        assertTrue(repo.canDriverOperateBus(driver, bus));
    }

    @Test
    void B5_publicTransportLicenceHybridBus_shouldPass() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 10, "PublicTransport", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("34343434", 40, 80.0, "Hybrid");

        assertTrue(repo.canDriverOperateBus(driver, bus));
    }

    @Test
    void B5_mediumLicenceHybridBus_shouldFail() throws Exception {
        BusRepository repo = createRepo();
        Driver driver = new Driver("2345@@AB", "Ali", 10, "Medium", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");
        Bus bus = new Bus("56565656", 40, 80.0, "Hybrid");

        assertFalse(repo.canDriverOperateBus(driver, bus));
    }
}
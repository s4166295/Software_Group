package IBDS;

import org.junit.jupiter.api.Test;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class BusRepositoryIntegrationTest {

    // BI1 checks that valid bus data is saved and retrieved using the real TXT file.
    @Test
    void validBusShouldBeStoredAndRetrieved() {
        File file = new File("bus-integration1.txt");
        file.delete();

        BusRepository repo = new BusRepository(file.getPath());

        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");

        assertTrue(repo.addBus(bus));

        Bus retrieved = repo.retrieveBus("12345678");

        assertNotNull(retrieved);
        assertEquals("12345678", retrieved.getBusID());

        file.delete();
    }
    // BI2 checks that invalid bus IDs are rejected and not written to storage.
    @Test
    void invalidBusShouldNotBeStored() {
        File file = new File("bus-integration2.txt");
        file.delete();

        BusRepository repo = new BusRepository(file.getPath());

        Bus bus = new Bus("1234ABCD", 40, 80.0, "Diesel");

        assertFalse(repo.addBus(bus));
        assertEquals(0, repo.countBuses());

        file.delete();
    }
    // BI3 checks that bus updates are persisted after repository operations.
    @Test
    void updatedBusShouldPersist() {
        File file = new File("bus-integration3.txt");
        file.delete();

        BusRepository repo = new BusRepository(file.getPath());

        Bus original = new Bus("87654321", 50, 80.0, "Diesel");
        repo.addBus(original);

        Bus updated = new Bus("87654321", 40, 70.0, "Diesel");

        assertTrue(repo.updateBus(updated));

        Bus retrieved = repo.retrieveBus("87654321");

        assertEquals(40, retrieved.getCapacity());

        file.delete();
    }
    // BI4 checks that bus record counts update correctly after adding buses.
    @Test
    void busCountShouldUpdateCorrectly() {
        File file = new File("bus-integration4.txt");
        file.delete();

        BusRepository repo = new BusRepository(file.getPath());

        repo.addBus(new Bus("11112222", 40, 80.0, "Diesel"));
        repo.addBus(new Bus("33334444", 45, 70.0, "Hybrid"));

        assertEquals(2, repo.countBuses());

        file.delete();
    }
}
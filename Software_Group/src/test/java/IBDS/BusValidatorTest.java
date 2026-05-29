package IBDS;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BusValidatorTest {

    private static final LocalDate TEST_DATE = LocalDate.of(2026, 5, 28);

    // B1: Bus ID Rules

    @Test
    void B1_validUniqueBusId_shouldPass() {
        assertTrue(BusValidator.isValidBusId("12345678", Set.of("87654321")));
    }

    @Test
    void B1_busIdWithSevenDigits_shouldFail() {
        assertFalse(BusValidator.isValidBusId("1234567", Set.of()));
    }

    @Test
    void B1_busIdWithLetter_shouldFail() {
        assertFalse(BusValidator.isValidBusId("1234A678", Set.of()));
    }

    @Test
    void B1_duplicateBusId_shouldFail() {
        assertFalse(BusValidator.isValidBusId("12345678", Set.of("12345678")));
    }

    // B2: Capacity Update Restriction

    @Test
    void B2_decreasingCapacity_shouldPass() {
        assertTrue(BusValidator.canUpdateCapacity(60, 50));
    }

    @Test
    void B2_sameCapacity_shouldPass() {
        assertTrue(BusValidator.canUpdateCapacity(60, 60));
    }

    @Test
    void B2_increasingCapacity_shouldFail() {
        assertFalse(BusValidator.canUpdateCapacity(50, 60));
    }

    // B3: Driver Age Restriction

    @Test
    void B3_driverUnder50WithLargeBus_shouldPass() {
        assertTrue(BusValidator.satisfiesDriverAgeRestriction("01-01-1990", 50, TEST_DATE));
    }

    @Test
    void B3_driverOver50WithCapacity50_shouldFail() {
        assertFalse(BusValidator.satisfiesDriverAgeRestriction("01-01-1970", 50, TEST_DATE));
    }

    @Test
    void B3_driverOver50WithCapacity49_shouldPass() {
        assertTrue(BusValidator.satisfiesDriverAgeRestriction("01-01-1970", 49, TEST_DATE));
    }

    // B4: Electric Bus Restriction

    @Test
    void B4_fiveYearsExperienceElectricBus_shouldPass() {
        assertTrue(BusValidator.satisfiesElectricBusExperienceRestriction(5, "Electricity"));
    }

    @Test
    void B4_fourYearsExperienceElectricBus_shouldFail() {
        assertFalse(BusValidator.satisfiesElectricBusExperienceRestriction(4, "Electricity"));
    }

    @Test
    void B4_zeroExperienceDieselBus_shouldPass() {
        assertTrue(BusValidator.satisfiesElectricBusExperienceRestriction(0, "Diesel"));
    }

    // B5: Driver Licence Restriction

    @Test
    void B5_heavyLicenceElectricBus_shouldPass() {
        assertTrue(BusValidator.satisfiesLicenceRestriction("Heavy", "Electricity"));
    }

    @Test
    void B5_publicTransportLicenceHybridBus_shouldPass() {
        assertTrue(BusValidator.satisfiesLicenceRestriction("PublicTransport", "Hybrid"));
    }

    @Test
    void B5_mediumLicenceHybridBus_shouldFail() {
        assertFalse(BusValidator.satisfiesLicenceRestriction("Medium", "Hybrid"));
    }
}
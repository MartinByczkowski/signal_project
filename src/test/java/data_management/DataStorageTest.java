package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // Mock DataReader implementation
        DataReader reader = dataStorage -> {
            dataStorage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
            dataStorage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);
        };

        // Initialize DataStorage with the mock reader
        DataStorage storage = DataStorage.getInstance();
        try {
            reader.readData(storage);
        } catch (Exception e) {
            System.err.println("Error reading data: " + e.getMessage());
        }

        // Retrieve records and validate
        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(200.0, records.get(1).getMeasurementValue()); // Validate second record
    }
}

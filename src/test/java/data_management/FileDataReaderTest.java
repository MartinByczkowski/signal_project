package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

class FileDataReaderTest {
    private File testFile;
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary test file
        testFile = File.createTempFile("test_patient_data", ".csv");
        
        // Write test data to the file
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("1,98.6,Temperature,1700000000000\n");
            writer.write("1,120.0,HeartRate,1700000001000\n");
            writer.write("2,130.0,SystolicBP,1700000002000\n");
            writer.write("2,97.0,BloodSaturation,1700000003000\n");
        }
        
        dataStorage = DataStorage.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Delete the temporary file
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testReadData() throws IOException {
        // Create FileDataReader with the test file
        FileDataReader reader = new FileDataReader(testFile.getAbsolutePath());
        
        // Read data into storage
        reader.readData(dataStorage);
        
        // Verify patient 1 data
        List<PatientRecord> patient1Records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(2, patient1Records.size(), "Patient 1 should have 2 records");
        
        // Verify temperature record
        boolean foundTemperature = false;
        boolean foundHeartRate = false;
        
        for (PatientRecord record : patient1Records) {
            if ("Temperature".equals(record.getRecordType())) {
                assertEquals(98.6, record.getMeasurementValue(), 0.001, "Temperature value should be 98.6");
                assertEquals(1700000000000L, record.getTimestamp(), "Temperature timestamp should match");
                foundTemperature = true;
            } else if ("HeartRate".equals(record.getRecordType())) {
                assertEquals(120.0, record.getMeasurementValue(), 0.001, "Heart rate value should be 120.0");
                assertEquals(1700000001000L, record.getTimestamp(), "Heart rate timestamp should match");
                foundHeartRate = true;
            }
        }
        
        assertTrue(foundTemperature, "Temperature record should be found");
        assertTrue(foundHeartRate, "Heart rate record should be found");
        
        // Verify patient 2 data
        List<PatientRecord> patient2Records = dataStorage.getRecords(2, 0, Long.MAX_VALUE);
        assertEquals(2, patient2Records.size(), "Patient 2 should have 2 records");
        
        // Verify blood pressure record
        boolean foundBloodPressure = false;
        boolean foundBloodSaturation = false;
        
        for (PatientRecord record : patient2Records) {
            if ("SystolicBP".equals(record.getRecordType())) {
                assertEquals(130.0, record.getMeasurementValue(), 0.001, "Blood pressure value should be 130.0");
                assertEquals(1700000002000L, record.getTimestamp(), "Blood pressure timestamp should match");
                foundBloodPressure = true;
            } else if ("BloodSaturation".equals(record.getRecordType())) {
                assertEquals(97.0, record.getMeasurementValue(), 0.001, "Blood saturation value should be 97.0");
                assertEquals(1700000003000L, record.getTimestamp(), "Blood saturation timestamp should match");
                foundBloodSaturation = true;
            }
        }
        
        assertTrue(foundBloodPressure, "Blood pressure record should be found");
        assertTrue(foundBloodSaturation, "Blood saturation record should be found");
    }
    
    @Test
    void testReadDataWithInvalidFormat() throws IOException {
        // Create a file with invalid data
        File invalidFile = File.createTempFile("invalid_data", ".csv");
        try (FileWriter writer = new FileWriter(invalidFile)) {
            writer.write("1,98.6,Temperature,1700000000000\n"); // Valid line
            writer.write("invalid line\n"); // Invalid line
            writer.write("2,not_a_number,BloodPressure,1700000002000\n"); // Invalid number
        }
        
        // Create FileDataReader with the invalid file
        FileDataReader reader = new FileDataReader(invalidFile.getAbsolutePath());
        
        // Read data into storage
        reader.readData(dataStorage);
        
        // Verify that valid data was read
        List<PatientRecord> patient1Records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(1, patient1Records.size(), "Only valid lines should be processed");
        
        // Clean up
        invalidFile.delete();
    }
}
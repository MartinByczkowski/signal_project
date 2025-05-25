package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implementation of the DataReader interface that reads patient data from a file.
 * The file should contain patient data in a specific format:
 * patientId,measurementValue,recordType,timestamp
 */
public class FileDataReader implements DataReader {
    private String filePath;

    /**
     * Constructs a FileDataReader with the specified file path.
     *
     * @param filePath the path to the file containing patient data
     */
    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads patient data from the file and stores it in the provided DataStorage.
     * Each line in the file should be in the format: patientId,measurementValue,recordType,timestamp
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the file
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Parse the line and add data to storage
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        int patientId = Integer.parseInt(parts[0].trim());
                        double measurementValue = Double.parseDouble(parts[1].trim());
                        String recordType = parts[2].trim();
                        long timestamp = Long.parseLong(parts[3].trim());
                        
                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        }
    }
}
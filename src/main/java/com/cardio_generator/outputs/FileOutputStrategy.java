package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements an output strategy that writes patient data to files.
 * Each label corresponds to a separate file in the specified base directory.
 * This strategy creates files on demand and appends new data to existing files.
 */
public class FileOutputStrategy implements OutputStrategy {

    /**
     * The base directory where all output files will be stored.
     */
    private final String baseDirectory;

    /**
     * A thread-safe map that caches file paths for each label to avoid
     * recalculating paths for repeated labels.
     */
    private final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a FileOutputStrategy with the specified base directory.
     * The directory will be created if it does not exist when data is first output.
     *
     * @param baseDirectory The directory where output files will be created and stored
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs patient data to a file corresponding to the given label.
     * If the file does not exist, it is created. Data is appended to the file.
     * Each piece of data is written on a new line in a formatted way.
     *
     * @param patientId The unique identifier of the patient
     * @param timestamp The time when the data was recorded (in milliseconds since epoch)
     * @param label     A descriptive label for the data (e.g., "ECG", "Blood Pressure")
     * @param data      The actual health data as a string
     * @throws RuntimeException If an error occurs during file operations, it is caught and logged,
     *                          but not propagated
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the base directory if it does not exist
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Determine the file path for the given label
        String filePath = fileMap.computeIfAbsent(
            label, k -> Paths.get(baseDirectory, label + ".txt").toString()
        );

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
            Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", 
                patientId, timestamp, label, data);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}

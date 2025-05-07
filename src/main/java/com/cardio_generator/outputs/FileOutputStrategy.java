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
 */
public class FileOutputStrategy implements OutputStrategy {

    private final String baseDirectory;
    private final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a FileOutputStrategy with the specified base directory.
     *
     * @param baseDirectory the directory where output files will be created
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs patient data to a file corresponding to the given label.
     * If the file does not exist, it is created. Data is appended to the file.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label     the label identifying the type of data
     * @param data      the data to be written
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
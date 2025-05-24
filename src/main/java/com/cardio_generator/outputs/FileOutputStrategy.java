package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Outputs patient data to text files, one file per data label.
 * Files are stored in the specified base directory.
 */
public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory;

    /** Maps each data label to its corresponding file path. */
    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * Creates a new FileOutputStrategy.
     *
     * @param baseDirectory the directory where output files will be saved
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes patient data to a file named after the data label.
     * Creates the directory if it doesn't exist.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time the data was recorded
     * @param label the type of data
     * @param data the data value
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        String filePath = file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}

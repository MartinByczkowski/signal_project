package com.cardio_generator.outputs;

/**
 * Implementation of the OutputStrategy interface that outputs patient data to the console.
 * This strategy formats and prints the data to standard output using System.out.
 */
public class ConsoleOutputStrategy implements OutputStrategy {
    /**
     * Outputs the patient data to the console in a formatted way.
     *
     * @param patientId The unique identifier of the patient
     * @param timestamp The time when the data was recorded (in milliseconds since epoch)
     * @param label     A descriptive label for the data (e.g., "ECG", "Blood Pressure")
     * @param data      The actual health data as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    }
}

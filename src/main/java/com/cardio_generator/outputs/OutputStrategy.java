package com.cardio_generator.outputs;

/**
 * Interface defining a strategy for outputting patient health data.
 * Implementations of this interface determine how and where the data is output,
 * such as to the console, files, network, etc.
 */
public interface OutputStrategy {
    /**
     * Outputs patient health data according to the specific strategy implementation.
     *
     * @param patientId The unique identifier of the patient
     * @param timestamp The time when the data was recorded (in milliseconds since epoch)
     * @param label     A descriptive label for the data (e.g., "ECG", "Blood Pressure")
     * @param data      The actual health data as a string
     */
    void output(int patientId, long timestamp, String label, String data);
}

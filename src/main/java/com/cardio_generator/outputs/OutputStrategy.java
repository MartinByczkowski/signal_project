package com.cardio_generator.outputs;

/**
 * Interface for defining how patient data should be output.
 */
public interface OutputStrategy {

    /**
     * Outputs patient data.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time the data was recorded
     * @param label the type of data (e.g., heart rate)
     * @param data the data value
     */
    void output(int patientId, long timestamp, String label, String data);
}

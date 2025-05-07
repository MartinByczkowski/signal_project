package com.alerts;

/**
 * Represents an alert triggered for a patient.
 * This class encapsulates details about the alert, including the patient ID,
 * the condition that triggered the alert, and the timestamp of the alert.
 */
public class Alert {

    private String patientId;
    private String condition;
    private long timestamp;

    /**
     * Constructs an {@code Alert} with the specified details.
     *
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition that triggered the alert
     * @param timestamp the time at which the alert was triggered, in milliseconds
     *                  since the Unix epoch
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Returns the ID of the patient associated with the alert.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Returns the condition that triggered the alert.
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the timestamp of when the alert was triggered.
     *
     * @return the timestamp in milliseconds since the Unix epoch
     */
    public long getTimestamp() {
        return timestamp;
    }
}

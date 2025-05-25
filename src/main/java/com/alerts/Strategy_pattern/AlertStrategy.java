package com.alerts.Strategy_pattern;

public interface AlertStrategy {
    /**
     * Checks if an alert should be triggered based on the provided data.
     * @param patientId The patient identifier.
     * @param data The relevant health metric value.
     * @return true if an alert should be triggered, false otherwise.
     */
    boolean checkAlert(String patientId, double data);
}
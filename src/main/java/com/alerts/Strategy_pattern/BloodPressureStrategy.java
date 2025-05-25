package com.alerts.Strategy_pattern;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(String patientId, double systolicPressure) {
        // Example: Alert if systolic pressure is out of normal range
        return systolicPressure < 90 || systolicPressure > 140;
    }
}
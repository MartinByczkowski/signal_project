package com.alerts.Strategy_pattern;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(String patientId, double heartRate) {
        // Example: Alert if heart rate is out of normal range
        return heartRate < 50 || heartRate > 120;
    }
}
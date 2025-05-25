package com.alerts.Strategy_pattern;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public boolean checkAlert(String patientId, double oxygenSaturation) {
        // Example: Alert if oxygen saturation drops below 92%
        return oxygenSaturation < 92.0;
    }
}
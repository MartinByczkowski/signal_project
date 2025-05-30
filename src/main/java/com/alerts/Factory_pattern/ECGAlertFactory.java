package com.alerts.Factory_pattern;

import com.alerts.Alert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
package com.alerts.Factory_pattern;

import com.alerts.Alert;

public abstract class AlertFactory {
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
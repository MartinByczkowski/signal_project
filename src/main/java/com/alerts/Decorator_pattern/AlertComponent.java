package com.alerts.Decorator_pattern;

public interface AlertComponent {
    String getPatientId();
    String getCondition();
    long getTimestamp();
    String getDetails();
}
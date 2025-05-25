package com.alerts.Decorator_pattern;

public abstract class AlertDecorator implements AlertComponent {
    protected AlertComponent decoratedAlert;

    public AlertDecorator(AlertComponent decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    public String getPatientId() { return decoratedAlert.getPatientId(); }
    public String getCondition() { return decoratedAlert.getCondition(); }
    public long getTimestamp() { return decoratedAlert.getTimestamp(); }
    public String getDetails() { return decoratedAlert.getDetails(); }
}
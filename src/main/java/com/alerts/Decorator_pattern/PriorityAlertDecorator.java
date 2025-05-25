package com.alerts.Decorator_pattern;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priority;

    public PriorityAlertDecorator(AlertComponent decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    @Override
    public String getDetails() {
        return decoratedAlert.getDetails() + " | Priority: " + priority;
    }
}
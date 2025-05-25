package com.alerts.Decorator_pattern;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatCount;

    public RepeatedAlertDecorator(AlertComponent decoratedAlert, int repeatCount) {
        super(decoratedAlert);
        this.repeatCount = repeatCount;
    }

    @Override
    public String getDetails() {
        return decoratedAlert.getDetails() + " | Repeated " + repeatCount + " times";
    }
}
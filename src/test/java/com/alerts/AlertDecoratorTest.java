package com.alerts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.alerts.Decorator_pattern.*;

public class AlertDecoratorTest {
    @Test
    void testPriorityAndRepeatedDecorator() {
        AlertComponent alert = new Alert("1", "High BP", System.currentTimeMillis());
        alert = new PriorityAlertDecorator(alert, "HIGH");
        alert = new RepeatedAlertDecorator(alert, 3);
        assertTrue(alert.getDetails().contains("Priority: HIGH"));
        assertTrue(alert.getDetails().contains("Repeated 3 times"));
    }
}
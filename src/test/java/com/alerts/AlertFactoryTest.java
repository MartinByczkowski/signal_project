package com.alerts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.alerts.Factory_pattern.*;

public class AlertFactoryTest {
    @Test
    void testBloodPressureAlertFactory() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("1", "High BP", System.currentTimeMillis());
        assertTrue(alert instanceof BloodPressureAlert);
    }
}
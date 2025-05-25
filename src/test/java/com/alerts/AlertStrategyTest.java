package com.alerts;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.alerts.Strategy_pattern.*;

public class AlertStrategyTest {
    @Test
    void testBloodPressureStrategy() {
        AlertStrategy strategy = new BloodPressureStrategy();
        assertTrue(strategy.checkAlert("1", 150.0));
        assertFalse(strategy.checkAlert("1", 120.0));
    }
}
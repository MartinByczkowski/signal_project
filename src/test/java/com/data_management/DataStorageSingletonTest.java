package com.data_management;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class DataStorageSingletonTest {
    @Test
    void testSingletonInstance() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();
        assertSame(instance1, instance2, "Both instances should be the same (singleton)");
    }
}
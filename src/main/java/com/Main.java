package com;
import com.data_management.DataStorage;
import com.cardio_generator.HealthDataSimulator;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.main(new String[]{});
        } else {
            try {
                HealthDataSimulator.main(new String[]{});
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}

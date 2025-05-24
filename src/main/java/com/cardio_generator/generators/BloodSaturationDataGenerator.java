package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood saturation data for patients.
 * Values fluctuate slightly and stay within a realistic range (90–100%).
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Initializes the generator with baseline saturation values for each patient.
     *
     * @param patientCount number of patients to generate data for
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Between 95 and 100
        }
    }

    /**
     * Generates and outputs a new saturation value for the given patient.
     *
     * @param patientId the ID of the patient
     * @param outputStrategy strategy used to output the data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            int variation = random.nextInt(3) - 1; // -1, 0, or 1
            int newSaturationValue = lastSaturationValues[patientId] + variation;
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    newSaturationValue + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace();
        }
    }
}

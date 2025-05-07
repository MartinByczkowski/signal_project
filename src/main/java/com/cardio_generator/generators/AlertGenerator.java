package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alerts for patients based on random probabilities.
 * Alerts can either be triggered or resolved, and the state of each patient's
 * alert is tracked.
 * 
 * This class implements the {@link PatientDataGenerator} interface and outputs
 * alert data using the specified {@link OutputStrategy}.
 */
public class AlertGenerator implements PatientDataGenerator {

    private static final Random RANDOM_GENERATOR = new Random();
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructs an {@code AlertGenerator} for a specified number of patients.
     * 
     * @param patientCount the total number of patients to track alerts for
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert data for a specific patient.
     * If the patient currently has an active alert, there is a 90% chance the alert
     * will be resolved.
     * If the patient does not have an active alert, a new alert may be triggered
     * based on a random probability.
     * 
     * @param patientId      the ID of the patient for whom alert data is generated
     * @param outputStrategy the {@link OutputStrategy} used to output the alert
     *                       data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the triggered alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}

package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generator for simulated Electrocardiogram (ECG) data.
 * This class creates realistic ECG waveforms for patients by simulating
 * the P wave, QRS complex, and T wave components of a cardiac cycle.
 */
public class ECGDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private double[] lastEcgValues;
    private static final double PI = Math.PI;

    /**
     * Constructs a new ECGDataGenerator for the specified number of patients.
     * Initializes the last ECG value for each patient to zero.
     *
     * @param patientCount the number of patients for whom ECG data will be generated
     */
    public ECGDataGenerator(int patientCount) {
        lastEcgValues = new double[patientCount + 1];
        // Initialize the last ECG value for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastEcgValues[i] = 0; // Initial ECG value can be set to 0
        }
    }

    /**
     * Generates ECG data for a specific patient and sends it to the specified output.
     * The method simulates an ECG waveform based on the patient's last ECG value,
     * and handles any exceptions that might occur during generation.
     *
     * @param patientId       the unique identifier of the patient for whom data is being generated
     * @param outputStrategy  the strategy used to output the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        // TODO Check how realistic this data is and make it more realistic if necessary
        try {
            double ecgValue = simulateEcgWaveform(patientId, lastEcgValues[patientId]);
            outputStrategy.output(patientId, System.currentTimeMillis(), "ECG", Double.toString(ecgValue));
            lastEcgValues[patientId] = ecgValue;
        } catch (Exception e) {
            System.err.println("An error occurred while generating ECG data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }

    /**
     * Simulates an ECG waveform for a patient based on their last ECG value.
     * The method creates a realistic ECG signal by combining sinusoidal waves
     * that represent the P wave, QRS complex, and T wave of a cardiac cycle.
     *
     * @param patientId     the unique identifier of the patient
     * @param lastEcgValue  the last ECG value generated for this patient
     * @return              the new ECG value
     */
    private double simulateEcgWaveform(int patientId, double lastEcgValue) {
        // Simplified ECG waveform generation based on sinusoids
        double hr = 60.0 + random.nextDouble() * 20.0; // Simulate heart rate variability between 60 and 80 bpm
        double t = System.currentTimeMillis() / 1000.0; // Use system time to simulate continuous time
        double ecgFrequency = hr / 60.0; // Convert heart rate to Hz

        // Simulate different components of the ECG signal
        double pWave = 0.1 * Math.sin(2 * PI * ecgFrequency * t);
        double qrsComplex = 0.5 * Math.sin(2 * PI * 3 * ecgFrequency * t); // QRS is higher frequency
        double tWave = 0.2 * Math.sin(2 * PI * 2 * ecgFrequency * t + PI / 4); // T wave is offset

        return pWave + qrsComplex + tWave + random.nextDouble() * 0.05; // Add small noise
    }
}

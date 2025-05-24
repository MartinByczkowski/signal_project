package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating simulated patient health data.
 * Implementations of this interface generate different types of health data
 * such as ECG readings, blood pressure, blood saturation, etc.
 */
public interface PatientDataGenerator {
    /**
     * Generates health data for a specific patient and sends it to the specified output.
     *
     * @param patientId       the unique identifier of the patient for whom data is being generated
     * @param outputStrategy  the strategy used to output the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}

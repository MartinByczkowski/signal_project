package com.alerts;

import java.util.List;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method defines the specific conditions under which an
     * alert will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Get all records for the patient
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);

        // Trigger an alert if the patient has no records
        if (records.isEmpty()) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "No Data", System.currentTimeMillis()));
            return;
        }

        // Check for abnormal heart rate
        checkHeartRateAlerts(patient, records);

        // Check for abnormal blood pressure
        checkBloodPressureAlerts(patient, records);

        // Check for abnormal temperature
        checkTemperatureAlerts(patient, records);

        // Check for abnormal blood saturation
        checkBloodSaturationAlerts(patient, records);
    }

    /**
     * Checks for abnormal heart rate values and triggers alerts if necessary.
     * 
     * @param patient the patient to check
     * @param records the patient's records
     */
    private void checkHeartRateAlerts(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if ("HeartRate".equals(record.getRecordType())) {
                double heartRate = record.getMeasurementValue();

                // Alert for bradycardia (low heart rate)
                if (heartRate < 60) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Bradycardia: Heart rate " + heartRate + " bpm", 
                                          record.getTimestamp()));
                }

                // Alert for tachycardia (high heart rate)
                if (heartRate > 100) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Tachycardia: Heart rate " + heartRate + " bpm", 
                                          record.getTimestamp()));
                }
            }
        }
    }

    /**
     * Checks for abnormal blood pressure values and triggers alerts if necessary.
     * 
     * @param patient the patient to check
     * @param records the patient's records
     */
    private void checkBloodPressureAlerts(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if ("SystolicBP".equals(record.getRecordType())) {
                double systolic = record.getMeasurementValue();

                // Alert for hypertension (high blood pressure)
                if (systolic > 140) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Hypertension: Systolic BP " + systolic + " mmHg", 
                                          record.getTimestamp()));
                }

                // Alert for hypotension (low blood pressure)
                if (systolic < 90) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Hypotension: Systolic BP " + systolic + " mmHg", 
                                          record.getTimestamp()));
                }
            }
        }
    }

    /**
     * Checks for abnormal temperature values and triggers alerts if necessary.
     * 
     * @param patient the patient to check
     * @param records the patient's records
     */
    private void checkTemperatureAlerts(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if ("Temperature".equals(record.getRecordType())) {
                double temperature = record.getMeasurementValue();

                // Alert for fever
                if (temperature > 100.4) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Fever: Temperature " + temperature + " °F", 
                                          record.getTimestamp()));
                }

                // Alert for hypothermia
                if (temperature < 95.0) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Hypothermia: Temperature " + temperature + " °F", 
                                          record.getTimestamp()));
                }
            }
        }
    }

    /**
     * Checks for abnormal blood saturation values and triggers alerts if necessary.
     * 
     * @param patient the patient to check
     * @param records the patient's records
     */
    private void checkBloodSaturationAlerts(Patient patient, List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if ("BloodSaturation".equals(record.getRecordType())) {
                double saturation = record.getMeasurementValue();

                // Alert for low blood oxygen
                if (saturation < 95) {
                    triggerAlert(new Alert(String.valueOf(patient.getPatientId()), 
                                          "Low Blood Oxygen: Saturation " + saturation + "%", 
                                          record.getTimestamp()));
                }
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: Patient ID: " + alert.getPatientId() + ", Condition: " + alert.getCondition()
                + ", Timestamp: " + alert.getTimestamp());
    }
}

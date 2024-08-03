package lemon.hospitaltable.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TreatmentGenerator {
    private static final String[] DIAGNOSES = {
            "Fracture", "Sprain", "Arthritis", "Dislocation", "Osteoporosis",                //Orthopedics
            "Scoliosis", "Tendinitis", "Bursitis", "Ligament", "Meniscus",                    //Orthopedics
            "Angina", "Arrhythmia", "Cardiomyopathy", "Hypertension", "Atherosclerosis",     //Cardiology
            "Myocarditis", "Endocarditis", "Tachycardia", "Bradycardia", "Pericarditis",      //Cardiology
            "Appendicitis", "Hernia", "Cholecystitis", "Abscess", "Ulcer",                   //Surgery
            "Diverticulitis", "Hemorrhoids", "Fistula", "Trauma", "Tumor",                    //Surgery
            "Epilepsy", "Migraine", "Stroke", "Meningitis", "Encephalitis",                    //Neurology
            "Neuropathy", "Parkinsonism", "Dementia", "Myelopathy", "Neuralgia"                //Neurology
    };

    public static void main(String[] args) {
        // Define constants for the range of patient IDs
        int patientIdStart = 1;
        int patientIdFinish = 2000;

        // Define arrays for doctor IDs and ward IDs
        int[] doctorsIDs = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        int[] wardsIDs = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        // Define dates of beginning treatments
        String startDate = "2023-12-20";
        String endDate = "2024-12-20";

        // Define durations of treatments
        int minDays = 3;
        int maxDays = 33;

        String sqlFileName = "src/main/java/lemon/hospitaltable/generators/insert_treatments.sql";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        generateSqlInsertFile(
                sqlFileName,
                patientIdStart,
                patientIdFinish,
                doctorsIDs,
                wardsIDs,
                startDate,
                endDate,
                minDays,
                maxDays,
                dateFormat
        );
        System.out.println("SQL statements written to " + sqlFileName);
    }

    private static void generateSqlInsertFile(
            String fileName,
            int patientIdStart,
            int patientIdEnd,
            int[] doctorIds,
            int[] wardIds,
            String startDate,
            String endDate,
            int minDays,
            int maxDays,
            SimpleDateFormat dateFormat
    ) {
        List<String> records;
        try {
            records = generateRecords(
                    patientIdStart,
                    patientIdEnd,
                    doctorIds,
                    wardIds,
                    startDate,
                    endDate,
                    minDays,
                    maxDays,
                    dateFormat
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(
                    "INSERT INTO treatments (patient_id, doctor_id, ward_id, date_in, date_out, diagnosis, notation) VALUES\n"
            );
            for (int i = 0; i < records.size(); i++) {
                writer.write(records.get(i));

                if (i < records.size() - 1) {
                    writer.write(",\n");
                } else {
                    writer.write(";\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> generateRecords(
            int patientIdStart,
            int patientIdEnd,
            int[] doctorIds,
            int[] wardIds,
            String startDate,
            String endDate,
            int minDays,
            int maxDays,
            SimpleDateFormat dateFormat
    ) {
        List<String> records = new ArrayList<>();
        Random random = new Random();

        for (int patientId = patientIdStart; patientId <= patientIdEnd; patientId++) {
            int doctorId = doctorIds[random.nextInt(doctorIds.length)];
            int wardId = wardIds[random.nextInt(wardIds.length)];
            Date dateIn;
            try {
                dateIn = randomDate(dateFormat.parse(startDate), dateFormat.parse(endDate));

                int treatmentDuration = minDays + random.nextInt(maxDays);
                Date dateOut = new Date(dateIn.getTime() + (treatmentDuration * 86400000L)); // 86400000 milliseconds in a day
                String diagnosis = DIAGNOSES[random.nextInt(DIAGNOSES.length)];

                String record = String.format(
                        "(%d, %d, %d, '%s', '%s', '%s', NULL)",
                        patientId, doctorId, wardId, dateFormat.format(dateIn), dateFormat.format(dateOut), diagnosis
                );
                records.add(record);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return records;
    }

    private static Date randomDate(Date start, Date end) {
        long randomTime = ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime());
        return new Date(randomTime);
    }
}

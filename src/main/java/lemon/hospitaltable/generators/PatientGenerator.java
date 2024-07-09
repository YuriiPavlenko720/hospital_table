package lemon.hospitaltable.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientGenerator {

    private static final List<String> FIRST_NAMES_MALE = Arrays.asList(
            "Bogdan", "Nestor", "Ivan", "George", "Taras", "Noah", "Panas", "Oliver", "Dmytro", "Elijah", "Vasyl",
            "James", "Grygoriy", "Donald", "Ostap", "William", "Svyatoslav", "Benjamin", "Igor", "Lucas", "Olexandr",
            "Henry", "Pavlo", "Pedro", "Petro", "Joe", "Mykola", "Vsevolod", "Gnat", "Danylo", "Kyrylo", "Kuzma",
            "Levko", "Mykyta", "Marko", "Myron", "Oleg", "Roman", "Stepan", "Tymofiy", "Tyhon", "Patrick", "John"
            );
    private static final List<String> FIRST_NAMES_FEMALE = Arrays.asList(
            "Hanna", "Olivia", "Olena", "Emma", "Olga", "Charlotte", "Lina", "Amelia", "Maria", "Ava", "Oksana",
            "Sophia", "Paraska", "Isabella", "Virka", "Mia", "Fekla", "Evelyn", "Motrya", "Harper", "Yulia", "Alla",
            "Halyna", "Svitlana", "Alice", "Virginia", "Vanda", "Gorpyna", "Daryna", "Diana", "Eva", "Ivanna",
            "Iryna", "Irma", "Maya", "Marta", "Melania", "Odarka", "Pavlina", "Polina", "Ruslana", "Solomia", "Tina"
    );
    private static final List<String> LAST_NAMES = Arrays.asList(
            "Petrenko", "Smith", "Korolenko", "Johnson", "Sydorenko", "Williams", "Tarasenko", "Jones", "Ivanenko",
            "Brown", "Panasenko", "Dmytrenko", "Davis", "Vasylenko", "Miller", "Grygorenko", "Wilson", "Grinchenko",
            "Moore", "Ostapenko", "Danylenko", "Taylor", "Kuchma", "Kyrylenko", "Kuzmenko", "Lukianenko", "Bovt",
            "Anderson", "Yuschenko", "Thomas", "Poroshenko", "Jackson", "Kravchuk", "White", "Tymoshenko", "Harris",
            "Martin", "Mykytenko", "Thompson", "Myronenko", "Garcia", "Romanenko", "Martinez", "Stepanenko", "Robinson",
            "Tyhonenko", "Linenko", "Virchynok", "Hvesenko", "Gudz", "Ivasenko", "Tsubenko", "Sosiura", "Vynnychenko",
            "Petlura", "Tutunyk", "Machno", "Nestaiko", "Slastion", "Gogol", "Shevchenko", "Bulba", "Tymchuk", "Chumak",
            "Bondarenko", "Babenko", "Goncharenko", "Kasianenko", "Parchomenko", "Symonenko", "Tytarenko", "Tkachenko",
            "Gavrylenko", "Lutsenko", "Boyko", "Lyashko", "Lyashenko", "Nalyvaiko", "Sirko", "Kryvonis", "Petruk",
            "Gonchar", "Goncharuk", "Kolisnyk", "Kovalenko", "Kovalchuk", "Melnyk", "Vinnyk", "Melnuchuk", "Somko"
    );
    private static final List<String> CITIES = Arrays.asList(
            "Kyiv", "New York", "Chernihiv", "Los Angeles", "Zhytomyr", "Chicago", "Chernivtsi", "Houston", "Odesa",
            "Cherkasy", "Philadelphia", "Ivano-Frankivsk", "Lutsk", "San Diego", "Rivne", "Dallas", "Mykolaiv",
            "London", "Dnipro", "Berlin", "Poltava", "Madrid", "Kharkiv", "Rome", "Ternopil", "Paris", "Berdychiv",
            "Vienna", "Zhmerinka", "Amsterdam", "Kozyatyn", "Brussels", "Kolomyia", "Lisbon", "Lviv", "Copenhagen",
            "Tokyo", "Uzhhorod", "Seoul", "Bangkok", "Singapore", "Sydney", "Melbourne", "Dubai", "Toronto"
    );
    private static final List<String> INTERESTS = Arrays.asList(
            "reading", "traveling", "cooking", "sports", "music", "movies", "hiking", "gardening", "painting",
            "photography", "fencing", "dancing", "writing", "gaming", "history", "knitting", "yoga", "cycling",
            "swimming", "surfing", "climbing", "running", "scuba diving", "skydiving", "archery", "camping",
            "bird watching", "shopping", "collecting", "pottery", "sculpting", "origami", "astronomy", "coding",
            "robotics", "magic", "theater", "martial arts", "volunteering", "brewing", "baking", "cheese making",
            "woodworking", "metalworking", "jewelry making", "tattooing", "cosplaying", "blacksmithing", "weaving",
            "whittling", "lock picking", "hunting", "fishing", "football", "hockey", "horses", "dogs", "cats", "movies",
            "languages", "anime", "writing", "poetry", "art", "steam machines", "cars", "bikes", "animals"
    );

    public static void main(String[] args) {
        int numberOfPatients = 20; // Number of patients for generation
        String startDate = "1946-02-03";
        String endDate = "2004-07-09";
        int minInterests = 0; // Min number of interests
        int maxInterests = 4; // Max number of interests

        generateInsertStatement(numberOfPatients, startDate, endDate, minInterests, maxInterests);
    }

    private static void generateInsertStatement(
            int numberOfPatients, String startDateStr, String endDateStr, int minInterests, int maxInterests
    ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate;
        Date endDate;

        try {
            startDate = dateFormat.parse(startDateStr);
            endDate = dateFormat.parse(endDateStr);
        } catch (ParseException e) {
            System.err.println("Date parsing error: " + e.getMessage());
            return;
        }

        Random random = new Random();
        StringBuilder sql = new StringBuilder(
                "INSERT INTO patients (name, birth, sex, phone, interests, address, email, status, notation) \n VALUES "
        );
        Set<String> usedEntries = new HashSet<>();

        int patientsGenerated = 0;
        while (patientsGenerated < numberOfPatients) {
            String gender = generateGender(random);
            String name = generateName(random, gender);
            String birth = generateDateOfBirth(random, startDate, endDate, dateFormat);
            String entry = name + "|" + birth; // Create a unique identifier for name and birth

            // Check if this combination has already been used
            if (usedEntries.contains(entry)) {
                continue; // Skip this iteration and regenerate
            }

            String interests = generateInterests(random, minInterests, maxInterests);
            String address = generateAddress(random);
            sql.append(String.format(
                    "('%s', '%s', '%s', NULL, '%s', '%s', NULL, 'new', NULL)", name, birth, gender, interests, address)
            );
            sql.append(",\n");

            usedEntries.add(entry);
            patientsGenerated++;

            if (patientsGenerated < numberOfPatients) {
                sql.append(", ");
            }
        }

        sql.append(";");

        // Write SQL query to file
        String sqlFileName = "src/main/java/lemon/hospitaltable/generators/insert_treatments.sql";
        try (FileWriter fileWriter = new FileWriter(sqlFileName)) {
            fileWriter.write(sql.toString());
            System.out.println("SQL statements written to " + sqlFileName);
        } catch (IOException e) {
            System.err.println("Writing error: " + e.getMessage());
        }
    }

    private static String generateGender(Random random) {
        double prob = random.nextDouble();
        if (prob < 0.49) {
            return "Male";
        } else if (prob < 0.94) {
            return "Female";
        } else if (prob < 0.95) {
            return "Non-binary";
        } else {
            return "Other";
        }
    }

    private static String generateName(Random random, String gender) {
        if ("Male".equals(gender)) {
            return FIRST_NAMES_MALE.get(random.nextInt(FIRST_NAMES_MALE.size())) + " " +
                    LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        } else if ("Female".equals(gender)) {
            return FIRST_NAMES_FEMALE.get(random.nextInt(FIRST_NAMES_FEMALE.size())) + " "
                    + LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        } else {
            // For non-binary or other genders, randomly select from both lists
            List<String> allNames = Arrays.asList(
                    FIRST_NAMES_MALE.get(random.nextInt(FIRST_NAMES_MALE.size())) + " "
                            + LAST_NAMES.get(random.nextInt(LAST_NAMES.size())),
                    FIRST_NAMES_FEMALE.get(random.nextInt(FIRST_NAMES_FEMALE.size())) + " "
                            + LAST_NAMES.get(random.nextInt(LAST_NAMES.size()))
            );
            return allNames.get(random.nextInt(allNames.size()));
        }
    }

    private static String generateDateOfBirth(
            Random random, Date startDate, Date endDate, SimpleDateFormat dateFormat
    ) {
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        long randomMillis = startMillis + (long) (random.nextDouble() * (endMillis - startMillis));
        return dateFormat.format(new Date(randomMillis));
    }

    private static String generateInterests(Random random, int minInterests, int maxInterests) {
        int interestCount = random.nextInt(maxInterests - minInterests + 1) + minInterests; // Random number of interests within range
        StringBuilder interests = new StringBuilder();
        for (int i = 0; i < interestCount; i++) {
            if (i > 0) {
                interests.append(", ");
            }
            interests.append(INTERESTS.get(random.nextInt(INTERESTS.size())));
        }
        return interests.toString();
    }

    private static String generateAddress(Random random) {
        return CITIES.get(random.nextInt(CITIES.size()));
    }
}

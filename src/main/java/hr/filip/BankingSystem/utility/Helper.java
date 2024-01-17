package hr.filip.BankingSystem.utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class Helper {

    public static List<Long> generateRandomLongs(int numberOfNumbers, long lowerBound, long upperBound) {
        if (numberOfNumbers < 0 || lowerBound >= upperBound) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        Set<Long> randomNumbers = new HashSet<>();
        Random random = new Random();
        while (randomNumbers.size() < numberOfNumbers) {
            long randomNumber = lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
            randomNumbers.add(randomNumber);
        }
        return new ArrayList<>(randomNumbers);
    }

    public static Long generateRandomLong(long lowerBound, long upperBound) {
        if (lowerBound < 0 || upperBound < 0 || lowerBound >= upperBound) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        Random random = new Random();
        return lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound));
    }

    public static double generateRandomDouble(double lowerBound, double upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("Invalid range");
        }
        Random random = new Random();
        return lowerBound + (upperBound - lowerBound) * random.nextDouble();
    }

    public static Instant calculateTimeBack(LocalDate minusTime) {
        LocalDateTime oneMonthAgoDateTime = LocalDateTime.of(minusTime, LocalDateTime.now().toLocalTime());
        return oneMonthAgoDateTime.toInstant(ZoneOffset.UTC);
    }
}

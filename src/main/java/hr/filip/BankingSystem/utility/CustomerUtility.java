package hr.filip.BankingSystem.utility;

import hr.filip.BankingSystem.model.Customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CustomerUtility {

    public static List<Customer> createCustomers(int numberOfCustomers) {
        List<String> randomNames = new ArrayList<>(Arrays.asList("Tony", "Mark", "Anna", "Christine", "Tiffany", "Paul", "John", "Maria"));
        List<String> randomSurnames = new ArrayList<>(Arrays.asList("Johnson", "Smith", "Brown", "Roy", "Lee", "Moore", "Martinez", "Gomez"));
        List<String> randomStreetFirstName = new ArrayList<>(Arrays.asList("Madison", "Aurora", "Oak", "Pine", "Wilson", "Elm"));
        List<String> randomStreetSecondName = new ArrayList<>(Arrays.asList("Street", "Ave", "Lane", "Road", "Avenue"));
        List<Customer> customers = new ArrayList<>();
        long lowerBoundCustomerId = 1000;
        long upperBoundCustomerId = 99999;
        List<Long> customerIds = Helper.generateRandomLongs(numberOfCustomers, lowerBoundCustomerId, upperBoundCustomerId);
        for(int i = 0; i < numberOfCustomers; i++){
            Random r = new Random();
            String randomFullName = randomNames.get(r.nextInt(randomNames.size())) + " " +
                    randomSurnames.get(r.nextInt(randomSurnames.size()));
            String randomStreetName =  randomStreetFirstName.get(r.nextInt(randomStreetFirstName.size())) + " " +
                    randomStreetSecondName.get(r.nextInt(randomStreetSecondName.size())) + " " + r.nextInt(100);
            String[] fullNameSplit = randomFullName.strip().split(" ");
            String email = fullNameSplit[0].toLowerCase() + fullNameSplit[1].toLowerCase() + r.nextInt(1000) +"@gmail.com";
            String phoneNumber = "+938" + Helper.generateRandomLong(10000000, 99999999);
            customers.add(new Customer(customerIds.get(i), randomFullName, randomStreetName, email, phoneNumber, new ArrayList<>()));
        }
        return customers;
    }

}

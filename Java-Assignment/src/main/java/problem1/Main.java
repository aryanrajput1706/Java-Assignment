package problem1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        List<Car> carList = new ArrayList<>();
        List<Accessories> accessoriesList = new ArrayList<>();

        String accessoryRangeRegex = "^₹(.*) - ₹(.*)";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode carsNode;
        JsonNode accessoriesNode;

        try {
            carsNode = mapper.readTree(new File("C:\\Users\\AryanRajput\\IdeaProjects\\Java-Assignment\\src\\main\\java\\problem1\\cars.json"));
            accessoriesNode = mapper.readTree(new File("C:\\Users\\AryanRajput\\IdeaProjects\\Java-Assignment\\src\\main\\java\\problem1\\accessories.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        carsNode.fieldNames().forEachRemaining(carName -> {
                    // Get the details (variants, price_range) for each car
                    JsonNode carDetails = carsNode.get(carName);

                    // Get variants and price range arrays
                    ArrayNode variants = (ArrayNode) carDetails.get("variants");
                    ArrayNode priceRange = (ArrayNode) carDetails.get("price_range");
                    List<String> variantsList = new ArrayList<>();
                    List<String> priceList = new ArrayList<>();
                    for (JsonNode variant : variants) {
                        variantsList.add(variant.asText());
                    }
                    for (JsonNode price : priceRange) {
                        priceList.add(price.asText());
                    }

                    carList.add(new Car(carName, variantsList, priceList));
                }
        );

        accessoriesNode.fieldNames().forEachRemaining(accessoriesCategory ->{
                    JsonNode accessoriesDetails = accessoriesNode.get(accessoriesCategory);

            accessoriesDetails.fieldNames().forEachRemaining(accessoryName -> {
                // Get the price range for the accessory
                String priceRange = accessoriesDetails.get(accessoryName).asText();

                // Use regex to split the price range into lower and upper values
                Pattern pattern = Pattern.compile("₹([\\d,]+) - ₹([\\d,]+)");
                Matcher matcher = pattern.matcher(priceRange);

                int lowerPrice = 0;
                int upperPrice = 0;

                if (matcher.find()) {
                    // Extract and parse the price values (removing commas and converting to integers)
                    lowerPrice = Integer.parseInt(matcher.group(1).replace(",", ""));
                    upperPrice = Integer.parseInt(matcher.group(2).replace(",", ""));
                }

                accessoriesList.add(new Accessories(accessoriesCategory, accessoryName, lowerPrice, upperPrice));
            });


        });




        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your budget");
        int budget = sc.nextInt(); // Customer Budget
        float priceCount = 0; // for Adding money

        List<Car> cars = new ArrayList<>();

        for (Car c : carList) {
            for (String i : c.range) {
                float price = 100000 * Float.parseFloat(i);
                if ((float) budget >= price) {
                    cars.add(c);
                    break;
                }
            }
        }

        System.out.println("Following cars fits under you budget :\n");

        for (int i = 0; i < cars.size(); i++) {
            System.out.println(i + 1 + "-" + cars.get(i).name);
        }

        System.out.println("\nSelect a car no. from the above list");

        int choice = sc.nextInt();
        --choice;

        System.out.println("\nThese are the variants of " + cars.get(choice).getName() + " that fits under your budget");
        Map<String, Float> mp = cars.get(choice).getMp();

        for (Map.Entry<String, Float> entry : mp.entrySet()) {
            if (entry.getValue() <= budget) System.out.println(entry.getKey() + " " + entry.getValue());
        }

        System.out.println("Insert the varient that you wanna buy from the above varients : ");

        String str = sc.next();

        float remainingBudget = budget - mp.get(str);
        priceCount = mp.get(str);

        Map<Integer, Accessories> acsMap = new HashMap<>();
        int index = 1;

        for (Accessories acs : accessoriesList) {
            if (acs.getKnow((int) remainingBudget)) {
                acsMap.put(index++, acs);
            }
        }

        System.out.println("Customizations that can fit within your budget are :" + remainingBudget);

        for (int key : acsMap.keySet()) {
            System.out.println(key + " - " + acsMap.get(key).getName() + " ---> Price " + acsMap.get(key).getLowerRange() + "-" + acsMap.get(key).getUpperRange());
        }

        System.out.println("Do you wanna purchase any accessories");

        while (remainingBudget >= 200) {
            String response = sc.next();

            if (response.equals("no")) break;
            else {
                System.out.println("Enter the Sr.No. of accessories that you wanna add and the cost within the range");
                int serialNo = sc.nextInt();
                int costbyCustomer = sc.nextInt();

                priceCount += costbyCustomer;
                remainingBudget -= costbyCustomer;

                if (remainingBudget >= 200) {
                    System.out.println("Do you wanna add more accessories?\n Enter yes or no");
                }
            }
        }

        double finalPrice = priceCount + (priceCount * 0.21);

        System.out.println("Your final price including accessories, registration,insurance, and handling fees is :" + finalPrice);

    }
}
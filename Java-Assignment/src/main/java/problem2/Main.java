package problem2;


import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        //Loan related information
        System.out.println("Enter the cost of the property : ");
        double propertyCost = sc.nextDouble();

        System.out.println("Enter the rate of interest : ");
        double loanRate = sc.nextDouble();

        System.out.println("Enter the loan duration(in year) :");
        int loanYears = sc.nextInt();

        System.out.println("Enter the annual aprreciation rate :");
        double propertyAppRate = sc.nextDouble();

        System.out.println("Enter the annual depreciation rate :");
        double propertyDepRate = sc.nextDouble();

        //SIP related information
        System.out.println("Enter the monthly SIP in Rs. :");
        double monthlySIP = sc.nextDouble();

        System.out.println("Enter the expected annual return rate for SIP :");
        double sipRate = sc.nextDouble();

        //Calculating EMI

        double monthlyLoanRate = (loanRate/12)/100;
        int totalLoanMonths = loanYears * 12;
        double monthlyEMIPayment = (propertyCost * monthlyLoanRate * Math.pow(1+monthlyLoanRate,totalLoanMonths))/(Math.pow(1+monthlyLoanRate,totalLoanMonths)-1);

        System.out.println("Your montly EMI is :" + monthlyEMIPayment);


        double depreciatedValue = propertyCost;
        double appreciatedValue = propertyCost;
        double sipValue = 0;

        System.out.println("Year\tDepreciated Value\tAppreciatedValue\t SIP Value");

        for(int year = 1;year<=loanYears;year++){
            depreciatedValue *= (1- propertyDepRate/100);
            appreciatedValue *= (1+ propertyAppRate/100);

            sipValue = calculateSIPValue(monthlySIP,sipRate,year*12);

            System.out.println(year+"year\tRs."+ df.format(depreciatedValue)+"\t\tRs." +df.format(appreciatedValue)+ "\t\tRs." + df.format(sipValue));
        }


        System.out.println("\nCosts after " + loanYears + "Years :");
        System.out.println("Total Depreciated Value of the property : Rs. " + df.format(depreciatedValue));
        System.out.println("Total Appreciated Value of the property : Rs. " + df.format(appreciatedValue));
        System.out.println("Final SIP Value : Rs. "+ df.format(sipValue));

    }

    private static double calculateSIPValue(double monthyContribution, double annualReturnRate,int months){
        double monthlyRate = (annualReturnRate/12)/100;
        return monthyContribution * (Math.pow(1+monthlyRate,months)-1)/monthlyRate*(1+monthlyRate);
    }
}

package customer_utills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Customer {

    public static class Movement {

        private int yazTimeAccured;
        private double amount;
        private char outOrIn;
        private double balanceBeforeAction;
        private double balanceAfterAction;

        @Override
        public String toString() {
            return "yaz: " + yazTimeAccured +
                    "\namount: " + String.format("%.1f" , amount) + outOrIn +
                    "\ncurrent balance:" + String.format("%.1f" , balanceAfterAction);
        }

        public void setYazTimeAccured(int time){this.yazTimeAccured = time;}

        public void setAmount(double amount){this.amount = amount;}

        public void setOutOrIn(char sign){this.outOrIn = sign;}

        public void setBalanceBeforeAction(double balanceBeforeAction){this.balanceBeforeAction = balanceBeforeAction;}

        public void setBalanceAfterAction(double balanceAfterAction){this.balanceAfterAction = balanceAfterAction;}
    }

    private double balance = 0;
    private String name;

    private List<Movement> movementList = new ArrayList<>();

    private List<Loan> loanerLoans = new ArrayList<>();

    private List<Loan> lenderLoans = new ArrayList<>();

    private List<String> notifications = new ArrayList<>();


    public void setBalance(double balance){this.balance = balance;}

    public double getBalance() {return balance;}

    public void setName(String name){this.name = name;}

    public String getName(){return name;}

    public List<Movement> getUnmodifiableMovementList() {
        return Collections.unmodifiableList(movementList);
    }

    @Override
    public String toString() {
        String res = "Customer information :\n" +
                " name : " + name +
                ";\n balance : " + String.format("%.1f" , balance) +";\n movements in the account :{\n";
        for (Movement movement : movementList){
            res += movement.toString() + "; \n";
        }
        res += "};\n loans as a loner :{";
        int i=1;
        for(Loan loan : loanerLoans){
            res += "loan #" + i + loan.printDataForCustomerLoaner() + ";\n";
            i++;
        }
        res += "};\n loans as a lender :{";
        i=1;
        for (Loan loan : lenderLoans){
            res += "loan #" + i + loan.printDataForCustomerLender(name) + ";\n";
            i++;
        }
        res += "};\n";

        return res;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getName().equals(customer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }



}

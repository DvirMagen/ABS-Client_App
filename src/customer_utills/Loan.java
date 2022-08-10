package customer_utills;

import java.io.Serializable;
import java.util.*;

public class Loan implements Serializable {


    public static class Lender implements Serializable {
        public String name;
        public double invest;

        public Lender() {
            this.name = ""; this.invest = 0;
        }

        public Lender(String name, double invest) {
            this.name = name;
            this.invest = invest;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getInvest() {
            return invest;
        }

        public void setInvest(double invest) {
            this.invest = invest;
        }

        @Override
        public String toString() {
            return "Lender{" +
                    "name='" + name + '\'' +
                    ", invest=" + invest +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Lender)) return false;
            Lender lender = (Lender) o;
            return getInvest() == lender.getInvest() && getName().equals(lender.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getInvest());
        }
    }

    public static class Payment{
        public int timeUnitPayed;
        public double fundPartPayed;
        public double intristPartPayed;
        public double paymentTotal;
        public boolean isPayed = false;
        public String lenderName;
        public double currentlyPayedMoney;
        public double currentlyLeftToPay;

        public Payment() {
        }

        public Payment(int timeUnitPayed, double fundPartPayed, double intristPartPayed, double paymentTotal, boolean isPayed, String lenderName, double currentlyPayedMoney, double currentlyLeftToPay) {
            this.timeUnitPayed = timeUnitPayed;
            this.fundPartPayed = fundPartPayed;
            this.intristPartPayed = intristPartPayed;
            this.paymentTotal = paymentTotal;
            this.isPayed = isPayed;
            this.lenderName = lenderName;
            this.currentlyPayedMoney = currentlyPayedMoney;
            this.currentlyLeftToPay = currentlyLeftToPay;
        }

        public int getTimeUnitPayed() {
            return timeUnitPayed;
        }

        public void setTimeUnitPayed(int timeUnitPayed) {
            this.timeUnitPayed = timeUnitPayed;
        }

        public double getFundPartPayed() {
            return fundPartPayed;
        }

        public void setFundPartPayed(double fundPartPayed) {
            this.fundPartPayed = fundPartPayed;
        }

        public double getIntristPartPayed() {
            return intristPartPayed;
        }

        public void setIntristPartPayed(double intristPartPayed) {
            this.intristPartPayed = intristPartPayed;
        }

        public double getPaymentTotal() {
            return paymentTotal;
        }

        public void setPaymentTotal(double paymentTotal) {
            this.paymentTotal = paymentTotal;
        }

        public boolean isPayed() {
            return isPayed;
        }

        public void setPayed(boolean payed) {
            isPayed = payed;
        }

        public String getLenderName() {
            return lenderName;
        }

        public void setLenderName(String lenderName) {
            this.lenderName = lenderName;
        }

        public double getCurrentlyPayedMoney() {
            return currentlyPayedMoney;
        }

        public void setCurrentlyPayedMoney(double currentlyPayedMoney) {
            this.currentlyPayedMoney = currentlyPayedMoney;
        }

        public double getCurrentlyLeftToPay() {
            return currentlyLeftToPay;
        }

        public void setCurrentlyLeftToPay(double currentlyLeftToPay) {
            this.currentlyLeftToPay = currentlyLeftToPay;
        }

        @Override
        public String toString() {
            return "Payment{" +
                    "timeUnitPayed=" + timeUnitPayed +
                    ", fundPartPayed=" + fundPartPayed +
                    ", intristPartPayed=" + intristPartPayed +
                    ", paymentTotal=" + paymentTotal +
                    ", isPayed=" + isPayed +
                    ", lenderName='" + lenderName + '\'' +
                    ", currentlyPayedMoney=" + currentlyPayedMoney +
                    ", currentlyLeftToPay=" + currentlyLeftToPay +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Payment)) return false;
            Payment payment = (Payment) o;
            return getTimeUnitPayed() == payment.getTimeUnitPayed() && Double.compare(payment.getFundPartPayed(), getFundPartPayed()) == 0 && Double.compare(payment.getIntristPartPayed(), getIntristPartPayed()) == 0 && Double.compare(payment.getPaymentTotal(), getPaymentTotal()) == 0 && isPayed() == payment.isPayed() && Double.compare(payment.getCurrentlyPayedMoney(), getCurrentlyPayedMoney()) == 0 && Double.compare(payment.getCurrentlyLeftToPay(), getCurrentlyLeftToPay()) == 0 && Objects.equals(getLenderName(), payment.getLenderName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTimeUnitPayed(), getFundPartPayed(), getIntristPartPayed(), getPaymentTotal(), isPayed(), getLenderName(), getCurrentlyPayedMoney(), getCurrentlyLeftToPay());
        }
    }

    public String owner;
    public String category;
    public int capital;
    public int totalYazTime;
    public int paysEveryYaz;
    public int intristPerPayment;
    public String id;
    public final Map<Lender,Double> lenders = new HashMap<>(); // lender -> how much not payed
    public int yazTimeActive;
    public int yazTimeLeft;
    public double intristPayed;
    public double intristLeftToPay;
    public double loanPayed = 0;
    public double loanLeftToPay;
    public int nextYazTimePayment;
    public int lastYazTimePayment;
    public String status = "new";
    public List<Payment> payments = new ArrayList<>();
    public boolean wasMoneyTaken = false;
    public boolean isOnSale = false;

    public Loan() {
    }

    public Loan(String owner, String category, int capital, int totalYazTime, int paysEveryYaz, int intristPerPayment, String id, int yazTimeActive, int yazTimeLeft, double intristPayed, double intristLeftToPay, double loanPayed, double loanLeftToPay, int nextYazTimePayment, int lastYazTimePayment, String status, List<Payment> payments, boolean wasMoneyTaken, boolean isOnSale) {
        this.owner = owner;
        this.category = category;
        this.capital = capital;
        this.totalYazTime = totalYazTime;
        this.paysEveryYaz = paysEveryYaz;
        this.intristPerPayment = intristPerPayment;
        this.id = id;
        this.yazTimeActive = yazTimeActive;
        this.yazTimeLeft = yazTimeLeft;
        this.intristPayed = intristPayed;
        this.intristLeftToPay = intristLeftToPay;
        this.loanPayed = loanPayed;
        this.loanLeftToPay = loanLeftToPay;
        this.nextYazTimePayment = nextYazTimePayment;
        this.lastYazTimePayment = lastYazTimePayment;
        this.status = status;
        this.payments = payments;
        this.wasMoneyTaken = wasMoneyTaken;
        this.isOnSale = isOnSale;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public int getTotalYazTime() {
        return totalYazTime;
    }

    public void setTotalYazTime(int totalYazTime) {
        this.totalYazTime = totalYazTime;
    }

    public int getPaysEveryYaz() {
        return paysEveryYaz;
    }

    public void setPaysEveryYaz(int paysEveryYaz) {
        this.paysEveryYaz = paysEveryYaz;
    }

    public int getIntristPerPayment() {
        return intristPerPayment;
    }

    public void setIntristPerPayment(int intristPerPayment) {
        this.intristPerPayment = intristPerPayment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Lender, Double> getLenders() {
        return lenders;
    }

    public int getYazTimeActive() {
        return yazTimeActive;
    }

    public void setYazTimeActive(int yazTimeActive) {
        this.yazTimeActive = yazTimeActive;
    }

    public int getYazTimeLeft() {
        return yazTimeLeft;
    }

    public void setYazTimeLeft(int yazTimeLeft) {
        this.yazTimeLeft = yazTimeLeft;
    }

    public double getIntristPayed() {
        return intristPayed;
    }

    public void setIntristPayed(double intristPayed) {
        this.intristPayed = intristPayed;
    }

    public double getIntristLeftToPay() {
        return intristLeftToPay;
    }

    public void setIntristLeftToPay(double intristLeftToPay) {
        this.intristLeftToPay = intristLeftToPay;
    }

    public double getLoanPayed() {
        return loanPayed;
    }

    public void setLoanPayed(double loanPayed) {
        this.loanPayed = loanPayed;
    }

    public double getLoanLeftToPay() {
        return loanLeftToPay;
    }

    public void setLoanLeftToPay(double loanLeftToPay) {
        this.loanLeftToPay = loanLeftToPay;
    }

    public int getNextYazTimePayment() {
        return nextYazTimePayment;
    }

    public void setNextYazTimePayment(int nextYazTimePayment) {
        this.nextYazTimePayment = nextYazTimePayment;
    }

    public int getLastYazTimePayment() {
        return lastYazTimePayment;
    }

    public void setLastYazTimePayment(int lastYazTimePayment) {
        this.lastYazTimePayment = lastYazTimePayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public boolean isWasMoneyTaken() {
        return wasMoneyTaken;
    }

    public void setWasMoneyTaken(boolean wasMoneyTaken) {
        this.wasMoneyTaken = wasMoneyTaken;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "owner='" + owner + '\'' +
                ", category='" + category + '\'' +
                ", capital=" + capital +
                ", totalYazTime=" + totalYazTime +
                ", paysEveryYaz=" + paysEveryYaz +
                ", intristPerPayment=" + intristPerPayment +
                ", id='" + id + '\'' +
                ", lenders=" + lenders +
                ", yazTimeActive=" + yazTimeActive +
                ", yazTimeLeft=" + yazTimeLeft +
                ", intristPayed=" + intristPayed +
                ", intristLeftToPay=" + intristLeftToPay +
                ", loanPayed=" + loanPayed +
                ", loanLeftToPay=" + loanLeftToPay +
                ", nextYazTimePayment=" + nextYazTimePayment +
                ", lastYazTimePayment=" + lastYazTimePayment +
                ", status='" + status + '\'' +
                ", payments=" + payments +
                ", wasMoneyTaken=" + wasMoneyTaken +
                ", isOnSale=" + isOnSale +
                '}';
    }

    public int leftToFund(){
        if(lenders.isEmpty())
            return capital;

        int leftToFund = capital;
        for(Loan.Lender lender : lenders.keySet()){
            leftToFund -= lender.getInvest();
        }
        return leftToFund;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return getCapital() == loan.getCapital() && getTotalYazTime() == loan.getTotalYazTime() && getPaysEveryYaz() == loan.getPaysEveryYaz() && getIntristPerPayment() == loan.getIntristPerPayment() && getYazTimeActive() == loan.getYazTimeActive() && getYazTimeLeft() == loan.getYazTimeLeft() && Double.compare(loan.getIntristPayed(), getIntristPayed()) == 0 && Double.compare(loan.getIntristLeftToPay(), getIntristLeftToPay()) == 0 && Double.compare(loan.getLoanPayed(), getLoanPayed()) == 0 && Double.compare(loan.getLoanLeftToPay(), getLoanLeftToPay()) == 0 && getNextYazTimePayment() == loan.getNextYazTimePayment() && getLastYazTimePayment() == loan.getLastYazTimePayment() && isWasMoneyTaken() == loan.isWasMoneyTaken() && isOnSale() == loan.isOnSale() && Objects.equals(getOwner(), loan.getOwner()) && Objects.equals(getCategory(), loan.getCategory()) && Objects.equals(getId(), loan.getId()) && Objects.equals(getLenders(), loan.getLenders()) && Objects.equals(getStatus(), loan.getStatus()) && Objects.equals(getPayments(), loan.getPayments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner(), getCategory(), getCapital(), getTotalYazTime(), getPaysEveryYaz(), getIntristPerPayment(), getId(), getLenders(), getYazTimeActive(), getYazTimeLeft(), getIntristPayed(), getIntristLeftToPay(), getLoanPayed(), getLoanLeftToPay(), getNextYazTimePayment(), getLastYazTimePayment(), getStatus(), getPayments(), isWasMoneyTaken(), isOnSale());
    }

    public String printDataForCustomerLoaner(){
        return "loan id : '" + id +"'; loan category : " + category + "; original loan amount : " + capital +
                "; payment is being done every " + paysEveryYaz + " yaz time; intrist for every payment : " + intristPerPayment +
                "; total amount to pay for the loan : " + String.format("%.1f" , (((double)(100+intristPerPayment))/100)*capital) + "; loan status: " + printStatusForCustomerLoaner();
    }

    public String printDataForCustomerLender(String name){
        String res = "";
        for (Lender lender : lenders.keySet()){
            if(lender.name.equalsIgnoreCase(name)){
                res += "loan id : " + id + "; loan category : " + category + "; original invest : " + lender.invest +
                        ";  payment is being done every " + paysEveryYaz + " yaz time; intrist for every payment : " + intristPerPayment
                        + "; total amount for the loan :" + String.format("%.1f" , (lender.invest + lender.invest*(((double)intristPerPayment)/100))) + "; loan status : " + printStatusForCustomerLender(lender.name);
            }
        }
        return res;
    }

    public String printStatusForCustomerLoaner(){
        if(status.equalsIgnoreCase("pending")){
            double totalPayed = 0;
            for(Lender lender : lenders.keySet()){
                totalPayed += lender.invest;
            }
            return " -PENDING LOAN- left to fund for the loan to become active=" + (capital-totalPayed) + ".";
        }

        else if(status.equalsIgnoreCase("active")){
            return " -ACTIVE LOAN- next payment is at the " + nextYazTimePayment + "time unit; - the payment would be " +
                    String.format("%.1f" , ((((double)capital)/(totalYazTime/paysEveryYaz))*(((double) intristPerPayment+100)/100))) + "at total.";
        }

        else if(status.equalsIgnoreCase("risk")){
            int counter = 0;
            for (Payment payment : payments) {
                if (!payment.isPayed)
                    counter++;
                else
                    counter = 0;
            }
            return " -RISK LOAN- there were " + counter + " payments that had not happened, at total amount of " +
                    String.format("%.1f" , ((((double)capital)/(totalYazTime/paysEveryYaz))*(((double) intristPerPayment+100)/100))*counter) +".";
        }

        else if(status.equalsIgnoreCase("finished")){
            return " -FINISHED LOAN- became active at " + yazTimeActive + " yaz time, and was fully payed at " + lastYazTimePayment + "yaz time.";
        }
        else if(status.equalsIgnoreCase("new")){
            return "new";
        }

        return "";
    }

    public String printStatusForCustomerLender(String name){
        if(status.equalsIgnoreCase("pending")){
            double totalPayed = 0;
            for(Lender lender : lenders.keySet()){
                totalPayed += lender.invest;
            }
            return " -PENDING LOAN- left to fund for the loan to become active=" + (capital-totalPayed) + ".";
        }

        else if(status.equalsIgnoreCase("active")){
            for (Lender lender : lenders.keySet()) {
                if(lender.name.equalsIgnoreCase(name)) {
                    return " -ACTIVE LOAN- next payment is at the " + nextYazTimePayment+ "time unit; - the payment would be " +
                            String.format("%.1f" , ((((double)lender.invest) / (totalYazTime/paysEveryYaz)) * (((double) intristPerPayment+100) / 100))) +
                            "at total.";
                }
            }
        }

        else if(status.equalsIgnoreCase("risk")){
            int counter = 0;
            for (Payment payment : payments) {
                if (!payment.isPayed)
                    counter++;
                else
                    counter = 0;
            }
            for(Lender lender : lenders.keySet()) {
                if (lender.name.equalsIgnoreCase(name)) {
                    return " -RISK LOAN- there were " + counter + " payments that had not happened, at total amount of " +
                            String.format("%.1f" , ((((double)lender.invest) / (totalYazTime/paysEveryYaz)) * (((double) intristPerPayment+100) / 100))*counter) + ".";
                }
            }
        }

        else if(status.equalsIgnoreCase("finished")){
            return " -FINISHED LOAN- became active at " + yazTimeActive + " yaz time, and was fully payed at " + lastYazTimePayment + "yaz time.";
        }

        return "";
    }
}
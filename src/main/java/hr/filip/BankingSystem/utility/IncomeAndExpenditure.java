package hr.filip.BankingSystem.utility;

public class IncomeAndExpenditure {

    private double income;
    private double expenditure;

    public IncomeAndExpenditure(double income, double expenditure) {
        this.income = income;
        this.expenditure = expenditure;
    }

    public double getIncome() {
        return income;
    }

    public IncomeAndExpenditure setIncome(double income) {
        this.income = income;
        return this;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public IncomeAndExpenditure setExpenditure(double expenditure) {
        this.expenditure = expenditure;
        return this;
    }
}

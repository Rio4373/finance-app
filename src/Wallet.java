import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

// Класс Wallet - управление кошельком пользователя

public class Wallet implements Serializable {
    private double balance;
    private final Map<String, Double> income;
    private final Map<String, Double> expense;
    private final Map<String, Double> budgets;

    public Wallet() {
        this.balance = 0;
        this.income = new HashMap<>();
        this.expense = new HashMap<>();
        this.budgets = new HashMap<>();
    }

    public double getBalance() {
        return balance;
    }

    // Метод для добавления дохода
    public void addIncome(Scanner scanner) {
        System.out.print("Введите категорию дохода: ");
        String category = scanner.nextLine();
        System.out.print("Введите сумму: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Сумма дохода должна быть положительной.");
            return;
        }

        addIncomeDirect(category, amount);
        System.out.println("Доход добавлен.");
    }

    // Метод для добавления расхода
    public void addExpense(Scanner scanner) {
        System.out.print("Введите категорию расхода: ");
        String category = scanner.nextLine();
        System.out.print("Введите сумму: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Сумма расхода должна быть положительной.");
            return;
        }

        addExpenseDirect(category, amount);
        System.out.println("Расход добавлен.");
    }

    // Прямое добавление дохода
    public void addIncomeDirect(String category, double amount) {
        income.put(category, income.getOrDefault(category, 0.0) + amount);
        balance += amount;
    }

    // Прямое добавление расхода
    public void addExpenseDirect(String category, double amount) {
        expense.put(category, expense.getOrDefault(category, 0.0) + amount);
        balance -= amount;

        if (budgets.containsKey(category) && expense.get(category) > budgets.get(category)) {
            System.out.println("Внимание: Вы превысили бюджет по категории " + category + "!");
        }
    }

    // Метод для установки бюджета
    public void setBudget(Scanner scanner) {
        System.out.print("Введите категорию: ");
        String category = scanner.nextLine();
        System.out.print("Введите бюджет: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Бюджет должен быть положительным.");
            return;
        }

        budgets.put(category, amount);
        System.out.println("Бюджет установлен.");
    }

    // Метод для отображения статистики
    public void displayStats() {
        System.out.println("Общий баланс: " + balance);
        System.out.println("Доходы: " + income);
        System.out.println("Расходы: " + expense);
        System.out.println("Бюджеты: " + budgets);

        budgets.forEach((category, budget) -> {
            double spent = expense.getOrDefault(category, 0.0);
            System.out.println("Категория: " + category + ", Бюджет: " + budget + ", Осталось: " + (budget - spent));
        });
    }
}


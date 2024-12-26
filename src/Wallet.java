import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.*;

// Класс, представляющий кошелек пользователя
public class Wallet implements Serializable {
    private double balance; // Баланс кошелька
    private final Map<String, Double> income; // Доходы по категориям
    private final Map<String, Double> expense; // Расходы по категориям
    private final Map<String, Double> budgets; // Бюджеты по категориям

    public Wallet() {
        this.balance = 0;
        this.income = new HashMap<>();
        this.expense = new HashMap<>();
        this.budgets = new HashMap<>();
    }

    public double getBalance() {
        return balance;
    }

    // Добавление дохода
    public void addIncome(Scanner scanner) {
        System.out.print("Введите категорию дохода: ");
        String category = scanner.nextLine();
        System.out.print("Введите сумму: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Сумма должна быть положительной.");
            return;
        }

        addIncomeDirect(category, amount);
        System.out.println("Доход добавлен.");
    }

    // Добавление расхода
    public void addExpense(Scanner scanner) {
        System.out.print("Введите категорию расхода: ");
        String category = scanner.nextLine();
        System.out.print("Введите сумму: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Сумма должна быть положительной.");
            return;
        }

        addExpenseDirect(category, amount);
        System.out.println("Расход добавлен.");
    }

    // Добавление дохода напрямую
    public void addIncomeDirect(String category, double amount) {
        income.put(category, income.getOrDefault(category, 0.0) + amount);
        balance += amount;
    }

    // Добавление расхода напрямую
    public void addExpenseDirect(String category, double amount) {
        expense.put(category, expense.getOrDefault(category, 0.0) + amount);
        balance -= amount;

        if (budgets.containsKey(category) && expense.get(category) > budgets.get(category)) {
            System.out.println("Внимание: Вы превысили бюджет по категории " + category + "!");
        }
    }

    // Установка бюджета
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

    // Отображение статистики
    public void displayStats() {
        System.out.println("Общий баланс: " + balance);
        System.out.println("Общие доходы: " + income.values().stream().mapToDouble(Double::doubleValue).sum());
        System.out.println("Доходы по категориям: " + income);
        System.out.println("Общие расходы: " + expense.values().stream().mapToDouble(Double::doubleValue).sum());
        System.out.println("Расходы по категориям: " + expense);
        System.out.println("Бюджеты по категориям: " + budgets);

        budgets.forEach((category, budget) -> {
            double spent = expense.getOrDefault(category, 0.0);
            System.out.println("Категория: " + category + ", Бюджет: " + budget + ", Осталось: " + (budget - spent));
        });
    }
}


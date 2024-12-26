import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

// Класс FinanceApp - основная логика приложения
public class FinanceApp {
    private Map<String, User> users;
    private User loggedInUser;
    private final Scanner scanner;

    // Конструктор приложения финансового управления
    public FinanceApp() {
        this.scanner = new Scanner(System.in);
        loadUsers();
    }

    // Основной цикл работы приложения
    public void run() {
        while (true) {
            if (loggedInUser == null) {
                System.out.println("1. Войти\n2. Зарегистрироваться\n3. Выйти");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": login(); break;
                    case "2": register(); break;
                    case "3": saveUsers(); return;
                    default: System.out.println("Неверная команда.");
                }
            } else {
                userMenu();
            }
        }
    }

    // Метод для авторизации пользователя
    private void login() {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUser = user;
            System.out.println("Успешный вход!");
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }

    // Метод для регистрации нового пользователя
    private void register() {
        System.out.print("Введите новый логин: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Логин уже существует.");
            return;
        }
        System.out.print("Введите новый пароль: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        users.put(username, newUser);
        System.out.println("Регистрация успешна!");
    }

    // Меню авторизованного пользователя
    private void userMenu() {
        System.out.println("1. Добавить доход\n2. Добавить расход\n3. Установить бюджет\n4. Показать статистику\n5. Перевести средства другому пользователю\n6. Выйти");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1": loggedInUser.getWallet().addIncome(scanner); break;
            case "2": loggedInUser.getWallet().addExpense(scanner); break;
            case "3": loggedInUser.getWallet().setBudget(scanner); break;
            case "4": loggedInUser.getWallet().displayStats(); break;
            case "5": transferFunds(); break;
            case "6": loggedInUser = null; break;
            default: System.out.println("Неверная команда.");
        }
    }

    // Метод для перевода средств между пользователями
    private void transferFunds() {
        System.out.print("Введите логин пользователя, которому хотите перевести средства: ");
        String recipientUsername = scanner.nextLine();
        User recipient = users.get(recipientUsername);
        if (recipient == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Введите сумму перевода: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Сумма перевода должна быть положительной.");
            return;
        }

        if (loggedInUser.getWallet().getBalance() >= amount) {
            loggedInUser.getWallet().addExpenseDirect("Перевод пользователю " + recipientUsername, amount);
            recipient.getWallet().addIncomeDirect("Перевод от пользователя " + loggedInUser.getUsername(), amount);
            System.out.println("Перевод выполнен успешно.");
        } else {
            System.out.println("Недостаточно средств для перевода.");
        }
    }

    // Метод сохранения данных пользователей
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
            System.out.println("Данные сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // Метод загрузки данных пользователей
    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (Map<String, User>) ois.readObject();
            System.out.println("Данные загружены.");
        } catch (FileNotFoundException e) {
            users = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
            users = new HashMap<>();
        }
    }
}

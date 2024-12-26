import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

// Класс для управления приложением
public class FinanceApp {
    private Map<String, User> users; // Хранилище пользователей
    private User loggedInUser; // Текущий авторизованный пользователь
    private final Scanner scanner; // Сканнер для ввода данных

    public FinanceApp() {
        this.scanner = new Scanner(System.in); // Инициализация сканера
        loadUsers(); // Загрузка данных пользователей
    }

    // Основной цикл работы приложения
    public void run() {
        while (true) {
            if (loggedInUser == null) {
                System.out.println("1. Войти\n2. Зарегистрироваться\n3. Выйти");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": login(); break; // Вход
                    case "2": register(); break; // Регистрация
                    case "3": saveUsers(); return; // Сохранение данных и выход
                    default: System.out.println("Неверная команда.");
                }
            } else {
                userMenu(); // Меню пользователя
            }
        }
    }

    // Метод для входа пользователя
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

    // Главное меню пользователя
    private void userMenu() {
        System.out.println("1. Добавить доход\n2. Добавить расход\n3. Установить бюджет\n4. Показать статистику\n5. Перевести средства другому пользователю\n6. Выйти");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1": loggedInUser.getWallet().addIncome(scanner); break; // Добавление дохода
            case "2": loggedInUser.getWallet().addExpense(scanner); break; // Добавление расхода
            case "3": loggedInUser.getWallet().setBudget(scanner); break; // Установка бюджета
            case "4": loggedInUser.getWallet().displayStats(); break; // Показ статистики
            case "5": transferFunds(); break; // Перевод средств
            case "6": loggedInUser = null; break; // Выход из аккаунта
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

    // Метод для сохранения данных пользователей
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
            System.out.println("Данные сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    // Метод для загрузки данных пользователей
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

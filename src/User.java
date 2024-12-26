import java.io.Serializable;

// Класс, представляющий пользователя
public class User implements Serializable {
    private final String username; // Логин пользователя
    private final String password; // Пароль пользователя
    private final Wallet wallet; // Кошелек пользователя

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet(); // Инициализация кошелька
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }
}

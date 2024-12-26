import java.io.Serializable;

// Класс User - модель пользователя
public class User implements Serializable {
    private final String username;
    private final String password;
    private final Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
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

package fun.gen.readme;


public class User {

    final String login;
    final String name;
    final String password;


    public User(String login,
                String name,
                String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

package fun.gen.readme;


public class User {

    String login;
    String name;
    String password;
   // Set<Rating> ratings;
   // Set<User> friends;

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

package Example_Constructor;

public class UserSummary {

    String name;
    String role;

    UserSummary(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}

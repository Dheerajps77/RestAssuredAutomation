package Example_Constructor;

public class Users {

    int id;
    String name;
    String role;

    public Users(int id, String name, String role) {
        this.id = id;
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

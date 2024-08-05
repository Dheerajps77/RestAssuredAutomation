package Example_Constructor;

import java.util.ArrayList;
import java.util.List;

public class RoleGroup {

    String role;
    List<Users> userSummaries;

    RoleGroup(String role) {
        this.role = role;
        this.userSummaries = new ArrayList<Users>();
    }

    public void addUser(Users user) {
        this.userSummaries.add(user);
    }


    public String getRole() {
        return role;
    }

    public List<Users> getUserSummaries() {
        return userSummaries;
    }
}

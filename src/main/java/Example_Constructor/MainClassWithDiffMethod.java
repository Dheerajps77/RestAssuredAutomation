package Example_Constructor;

import java.util.HashMap;
import java.util.Map;

public class MainClassWithDiffMethod {

    public static void main(String[] args) {

        Users users1 = new Users(1, "Mahesh", "admin");
        Users users2 = new Users(2, "Mayur", "developer");
        Users users3 = new Users(3, "Amruta", "hr");
        Users users4 = new Users(4, "Vijay", "developer");
        Users users5 = new Users(5, "Sneha", "hr");


        Users[] usersSummary = {
                (users1), (users2), (users3), (users4), (users5),
        };

        for (int i = 0; i < usersSummary.length; i++) {
            int id = usersSummary[i].id;
            System.out.println("id: " + usersSummary[i].id +
                    ", name:" + usersSummary[i].getName() +
                    ", role: " + usersSummary[i].role);
        }

        System.out.println();


        Map<String, RoleGroup> stringRoleGroupMap = new HashMap<String, RoleGroup>();

        for (Users users6 : usersSummary) {
            String role = users6.getRole();
            RoleGroup roleGroup = stringRoleGroupMap.get(role);
            if (roleGroup == null) {
                roleGroup = new RoleGroup(role);
                stringRoleGroupMap.put(role, roleGroup);
            }
            roleGroup.addUser(users6);
        }
        // Print the result

        System.out.println("from array to create new this one");
        System.out.println();
        for (RoleGroup group : stringRoleGroupMap.values()) {
            System.out.println("Role: " + group.getRole());
            for (Users user : group.getUserSummaries()) {
                System.out.println("  User: " + user.getName() + ", Role: " + user.getRole());
            }
        }
    }
}

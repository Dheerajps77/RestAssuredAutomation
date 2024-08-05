package Example_Constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainClassWithOriginalData {

    public static void main(String[] args) {


        Users users1 = new Users(1, "Mahesh", "admin");
        Users users2 = new Users(2, "Mayur", "developer");
        Users users3 = new Users(3, "Amruta", "hr");
        Users users4 = new Users(4, "Vijay", "developer");
        Users users5 = new Users(5, "Sneha", "hr");


        List<Map<String, Object>> usersList = new ArrayList<>();
        usersList.add(Map.of("id", 1, "name", "Mahesh", "role", "admin"));
        usersList.add(Map.of("id", 2, "name", "Mayur", "role", "developer"));
        usersList.add(Map.of("id", 3, "name", "Amruta", "role", "hr"));
        usersList.add(Map.of("id", 4, "name", "Vijay", "role", "developer"));
        usersList.add(Map.of("id", 5, "name", "Sneha", "role", "hr"));


        // Map to store grouped users
        Map<String, List<Map<String, Object>>> groupedUsers = new HashMap<>();

        // Group users by role
        for (Map<String, Object> user : usersList) {
            String role = (String) user.get("role");
            if (!groupedUsers.containsKey(role)) {
                groupedUsers.put(role, new ArrayList<>());
            }
            groupedUsers.get(role).add(Map.of("name", user.get("name"), "role", user.get("role")));
        }

        // Create the desired output format
        List<Map<String, Object>> result = new ArrayList<>();
        for (String role : groupedUsers.keySet()) {
            Map<String, Object> roleGroup = new HashMap<>();
            roleGroup.put("role", role);
            roleGroup.put("users", groupedUsers.get(role));
            result.add(roleGroup);
        }

        // Print the result
        System.out.println(result);
    }
}

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static spark.Spark.*;
import java.sql.*;
import  java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        Gson gson = new GsonBuilder().create();
        //Statement stmt = conn.createStatement();
        //Statement s = conn.createStatement();


        //stmt.execute("CREATE TABLE if not exists `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `employee_id` text,`client_id` text,`owner_id` text);");
        //s.execute("CREATE TABLE if not exists `owner` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` text);");

       /* PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO `owner` (name) VALUES (?)"
        );

        PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO `users` (employee_id,client_id,owner_id ) VALUES (?, ?, ?)"
        );

        ps2.setString(1, "9"); // вместо первого "?" подставляется строка "Ivan"
        ps2.setString(2, "8"); // вместо первого "?" подставляется строка "Ivan"
        ps2.setString(3, "5"); // вместо первого "?" подставляется строка "Ivan"
        ps2.executeUpdate();
        ps1.setString(1, "Ivan"); // вместо первого "?" подставляется строка "Ivan"
        ps1.executeUpdate();
    }
*/


        get("/users", (request,response) -> {

            response.type("application/json");
            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `users`"
            );

            ResultSet rs = stmnt.executeQuery();
            List<User> users = new ArrayList<>();

            while (rs.next()){
                users.add(new User(
                        rs.getInt("id"), rs.getString("employee_id"), rs.getString("client_id"), rs.getString("owner_id")));
            }
            return users;
        }, gson::toJson);


        get("/users/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));

            response.type("application/json");
            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `users` WHERE `id` = ?"
            );

            stmnt.setInt(1, userID);
            ResultSet rs = stmnt.executeQuery();

            User user = new User(rs.getInt("id"),rs.getString("employee_id"), rs.getString("client_id"), rs.getString("owner_id"));

            return user;
        },gson::toJson);

        delete("/users/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));
            response.type("application/json");

            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `users` WHERE `id` = ?"
            );

            stmnt.setInt(1, userID);

            stmnt = conn.prepareStatement(
                    "DELETE FROM `users` WHERE id= ?"
            );
            stmnt.setInt(1, userID);
            stmnt.executeUpdate();

            return "Удалили!";

        },gson::toJson);

        post("/users",(request,response)->{

            User user = gson.fromJson(request.body(), User.class);
            response.type("application/json");

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `users` (`id`, `employee_id`, `client_id`, `owner_id`) VALUES (?, ?, ?, ?)"
            );

            stmnt.setInt(1, user.id);
            stmnt.setString(2, user.employeeID);
            stmnt.setString(3, user.clientID);
            stmnt.setString(4, user.ownerID);

            stmnt.executeUpdate();

            return user;

        },gson::toJson);

        put("/users/:id",(request,response)->{

            int userID = Integer.parseInt(request.params("id"));
            User user = gson.fromJson(request.body(), User.class);
            response.type("application/json");

            PreparedStatement stmnt = conn.prepareStatement(
                    "UPDATE `users` SET `id`=?, `employee_id`=?, `client_id`=?, `owner_id`=? WHERE `id`=?"
            );

            stmnt.setInt(1, user.id);
            stmnt.setString(2, user.employeeID);
            stmnt.setString(3, user.clientID);
            stmnt.setString(4, user.ownerID);
            stmnt.setInt(5, userID);

            stmnt.executeUpdate();

            return user;

        },gson::toJson);



    }
}

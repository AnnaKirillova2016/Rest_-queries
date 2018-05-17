import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static spark.Spark.*;

public class Main1 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");
        Gson gson = new GsonBuilder().create();
        // GET /users


        get("/owner", (request,response) -> {
            response.type("application/json");
            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `owner`"
            );
            ResultSet rs = stmnt.executeQuery();
            List<Owner> users = new ArrayList<>();
            while (rs.next()){
                users.add(new Owner(
                        rs.getInt("id"),
                        rs.getString("name")));
            }
            return users;
        }, gson::toJson);

        // GET users/id

        get("/owner/:id",(request,response)->{
            int userID = Integer.parseInt(request.params("id"));

            response.type("application/json");
            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `owner` WHERE `id` = ?"
            );

            stmnt.setInt(1, userID);

            ResultSet rs = stmnt.executeQuery();

            Owner user = new Owner(rs.getInt("id"),rs.getString("name"));

            return user;

        },gson::toJson);

        // DELETE /users/:id

        delete("/owner/:id",(request,response)->{

            int userID = Integer.parseInt(request.params("id"));
            response.type("application/json");

            PreparedStatement stmnt = conn.prepareStatement(
                    "SELECT * FROM `owner` WHERE `id` = ?"
            );

            stmnt.setInt(1, userID);

            stmnt = conn.prepareStatement(
                    "DELETE FROM `owner` WHERE id= ?"
            );

            stmnt.setInt(1, userID);
            stmnt.executeUpdate();

            return "Delete!";

        },gson::toJson);

        // POST /users

        post("/owner",(request,response)->{

            Owner user = gson.fromJson(request.body(), Owner.class);
            response.type("application/json");

            PreparedStatement stmnt = conn.prepareStatement(
                    "INSERT INTO `owner` (`name`) VALUES (?)"
            );

            stmnt.setString(1, user.name);

            stmnt.executeUpdate();

            return user;

        },gson::toJson);


        // PUT /users/:id

        put("/owner/:id",(request,response)->{

            int userID = Integer.parseInt(request.params("id"));
            Owner user = gson.fromJson(request.body(), Owner.class);
            response.type("application/json");

            PreparedStatement stmnt = conn.prepareStatement(
                    "UPDATE `owner` SET `id`=?, `name`=? WHERE `id`=?"
            );

            stmnt.setInt(1, user.id);
            stmnt.setString(2, user.name);
            stmnt.setInt(3, userID);

            stmnt.executeUpdate();

            return user;

        },gson::toJson);


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

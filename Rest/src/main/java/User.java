public class User {
    public int id;
    public String employeeID;
    public String clientID;
    public String ownerID;

    public int errorCode;

    public User(int id, String employeeID, String clientID, String ownerID) {
        this.id = id;
        this.employeeID = employeeID;
        this.clientID = clientID;
        this.ownerID = ownerID;
    }

    public User() {

    }
}

package carservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String username;

    private String password;

    private UserRole userRole;

    public User() {}

    public User(String firstName,
                String lastName,
                String username,
                String password,
                UserRole userRole) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.username  = username;
        this.password  = password;
        this.userRole  = userRole;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password= password; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

}

package userservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user entity stored in MongoDB, including personal details,
 * login credentials, and assigned role.
 */
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

    /**
     * Default constructor required by persistence frameworks.
     */
    public User() {}

    /**
     * Constructs a new User with the specified details.
     *
     * @param firstName user's first name
     * @param lastName  user's last name
     * @param username  login name of the user (must be unique)
     * @param password  encoded password of the user
     * @param userRole  role assigned to the user (e.g., ADMIN, USER, TESTER)
     */
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

    /**
     * @return unique identifier of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id identifier to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return login username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the login username of the user.
     *
     * @param username the username to set (must be unique)
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return encoded password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the encoded password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return role assigned to the user
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param userRole the user role to set
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}

package com.carrental.model;

import jakarta.persistence.*;

/**
 * Entity representing an application user.
 * <p>
 * Maps to the "users" table and stores personal credentials and roles.
 */
@Entity
@Table(name = "users")
public class User {

    /** Unique identifier of the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** First name of the user. */
    @Column(nullable = false)
    private String firstName;

    /** Last name of the user. */
    @Column(nullable = false)
    private String lastName;

    /** Unique username used for authentication. */
    @Column(nullable = false)
    private String username;

    /** Encrypted password for the user account. */
    @Column(nullable = false)
    private String password;

    /** Role assigned to the user (e.g., ADMIN, USER). */
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    /** Default constructor required by JPA. */
    public User() {}

    /**
     * Constructs a new User with the specified details.
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param username  unique username for login
     * @param password  encoded password for the account
     * @param userRole  role assigned to the user
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
     * Returns the unique identifier of the user.
     *
     * @return user ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id user ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the first name of the user.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the username used for authentication.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username used for authentication.
     *
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the encoded password of the user.
     *
     * @return encoded password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the encoded password of the user.
     *
     * @param password encoded password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the role assigned to the user.
     *
     * @return user role
     */
    public UserRole getUserRole() {
        return userRole;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param userRole role to set
     */
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}

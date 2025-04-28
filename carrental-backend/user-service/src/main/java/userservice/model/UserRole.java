package userservice.model;

/**
 * Enumeration of roles that can be assigned to a user within the system.
 */
public enum UserRole {

    /** Administrator with full access to all operations. */
    ADMIN,

    /** Regular user with standard access permissions. */
    USER,

    /** Tester role intended for QA and testing activities. */
    TESTER
}

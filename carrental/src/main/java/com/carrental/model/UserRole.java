package com.carrental.model;

/**
 * Enumeration of application user roles.
 * <p>
 * Defines the various roles that can be assigned to a user,
 * which determine their permissions within the system.
 */
public enum UserRole {

    /** Administrator role with full system privileges. */
    ADMIN,

    /** Standard user role with basic access rights. */
    USER,

    /** Tester role used for quality assurance and testing purposes. */
    TESTER

}

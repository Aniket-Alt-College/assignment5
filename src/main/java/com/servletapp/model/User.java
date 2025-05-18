package com.servletapp.model;

/**
 * User model class
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;  // This field was causing the issues
    private String email;

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * Constructor with all fields
     * @param id User ID
     * @param username Username
     * @param password Password
     * @param fullName Full name
     * @param email Email address
     */
    public User(int id, String username, String password, String fullName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    /**
     * Get user ID
     * @return User ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set user ID
     * @param id User ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get username
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username
     * @param username Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get password
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password
     * @param password Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get full name
     * @return Full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set full name
     * @param fullName Full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Get email
     * @return Email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email
     * @param email Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * User details in string format
     * @return String representation of user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
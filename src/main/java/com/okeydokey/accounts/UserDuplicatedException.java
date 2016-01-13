package com.okeydokey.accounts;

/**
 * @author okeydokey
 */
public class UserDuplicatedException extends RuntimeException {
    String username;
    public UserDuplicatedException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

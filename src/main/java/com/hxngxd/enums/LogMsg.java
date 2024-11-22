package com.hxngxd.enums;

public enum LogMsg {

    GENERAL_IS_NULL("%s is null"),
    GENERAL_SUCCESS("Successfully %s"),
    GENERAL_FAIL("Failed to %s"),
    GENERAL_SOMETHING_WENT_WRONG("Something went wrong while %s"),

    USER_EXIST("Username or Email already in use"),
    USER_NOT_FOUND("User not found"),
    USER_NOT_LOGGED_IN("No user is logged in"),
    USER_NOT_LOGGED_OUT("The current user has not logged out"),
    USER_DONT_HAVE_TO("The current user does not need to perform this action"),
    USER_WRONG_PASSWORD("Incorrect password"),
    USER_NOT_ALLOWED("The current user does not have permission to %s"),
    USER_CANNOT("You cannot %s"),
    USER_SUSPENDED("Account has been suspended"),
    USER_BANNED("Account has been banned"),

    FILE_NOT_FOUND("File not found: %s"),
    FILE_CANNOT_OPEN("Cannot open file: %s"),
    FILE_IO_ERROR("Error reading or writing file: %s"),
    FILE_IS_NULL("File is null"),
    FILE_PATH_INVALID("File path is invalid: %s"),

    DATABASE_NO_CONNECTION("Database is not connected"),
    DATABASE_READ_ERROR("Error reading from database: %s"),
    DATABASE_WRITE_ERROR("Error writing to database: %s"),
    DATABASE_QUERY_FAILED("Query failed: %s"),

    VALIDATION_EMAIL_NOT_VALID("Email is not valid"),
    VALIDATION_INFO_TOO_LONG("Some information is too long"),
    VALIDATION_INFO_IS_MISSING("Some information is missing"),
    VALIDATION_INVALID_INPUT("Invalid input");

    private final String message;

    LogMsg(String message) {
        this.message = message;
    }

    public String msg(Object... params) {
        return String.format(this.message, params);
    }
    
}
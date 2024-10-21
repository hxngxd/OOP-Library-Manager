package com.hxngxd.enums;

public class LogMessages {
    public enum General {
        IS_NULL("%s is null"),
        SUCCESS("Successfully %s"),
        FAIL("Failed to %s"),
        SOMETHING_WENT_WRONG("Something went wrong while %s");

        private final String message;

        General(String message) {
            this.message = message;
        }

        public String getMessage(String param) {
            return String.format(this.message, param);
        }
    }

    public enum User {
        USER_EXIST("User already exists"),
        USER_NOT_FOUND("User does not exist"),
        USER_NOT_LOGGED_IN("No user is logged in"),
        USER_NOT_LOGGED_OUT("The current user has not logged out yet"),
        USER_DONT_HAVE_TO("The current user does not have to do this"),
        WRONG_PASSWORD("Wrong password"),
        USER_NOT_ALLOWED("The current user does not have permission to %s"),
        USER_CANNOT("You can't %s");

        private final String message;

        User(String message) {
            this.message = message;
        }

        public String getMessage(String param) {
            return String.format(this.message, param);
        }

        public String getMessage() {
            return this.message;
        }
    }

    public enum File {
        FILE_NOT_FOUND("File not found: %s"),
        FILE_CANNOT_OPEN("File cannot be opened: %s"),
        FILE_IO_ERROR("Error reading or writing file: %s"),
        FILE_IS_NULL("File is null"),
        FILE_PATH_INVALID("File path is invalid: %s");

        private final String message;

        File(String message) {
            this.message = message;
        }

        public String getMessage(String filePath) {
            return String.format(this.message, filePath);
        }

        public String getMessage() {
            return this.message;
        }
    }

    public enum Database {
        NO_DB_CONNECTION("The database is not connected"),
        DB_READ_ERROR("Error reading from database: %s"),
        DB_WRITE_ERROR("Error writing to database: %s"),
        DB_QUERY_FAILED("Query failed: %s");

        private final String message;

        Database(String message) {
            this.message = message;
        }

        public String getMessage(String query) {
            return String.format(this.message, query);
        }

        public String getMessage() {
            return this.message;
        }
    }

    public enum Validation {
        EMAIL_NOT_VALID("Email is not valid"),
        INFO_TOO_LONG("Some information is too long"),
        INFO_IS_MISSING("Some information is missing"),
        INVALID_INPUT("Invalid input");

        private final String message;

        Validation(String message) {
            this.message = message;
        }

        public String getMessage(String param) {
            return String.format(this.message, param);
        }

        public String getMessage() {
            return this.message;
        }
    }
}

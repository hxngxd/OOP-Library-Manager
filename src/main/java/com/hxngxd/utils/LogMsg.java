package com.hxngxd.utils;

import com.hxngxd.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogMsg {

    public static final String tryAgain = "Please try again";
    public static final String userExist = "User already exists";
    public static final String userNotFound = "User does not exist";
    public static final String userNotLogIn = "No user is logged in";
    public static final String userNotLogOut = "The current user has not logged out yet";
    public static final String userDontHaveTo = "The current user does not have to do this";
    public static final String wrongPW = "Wrong password";
    public static final String noDBConnection = "The database is not connected";
    public static final String emailNotValid = "Email is not valid";
    public static final String infoTooLong = "Some information is too long";
    public static final String infoIsMissing = "Some information is missing";

    private static final Logger logger = LogManager.getLogger(LogMsg.class);

    private LogMsg() {
    }

    public static String sthwr(String work) {
        return "Something when wrong while " + work;
    }

    public static String userNotAllowTo(String work) {
        return "The current user does not have permission to " + work;
    }

    public static String userCant(String work) {
        return "You can't " + work;
    }

    public static String success(String work) {
        return "Successfully " + work;
    }

    public static String fail(String work) {
        return "Failed to " + work;
    }

    public static boolean logResult(boolean result, String action) {
        if (result) {
            logger.info(success(action));
        } else {
            logger.error(fail(action));
        }
        return result;
    }
}

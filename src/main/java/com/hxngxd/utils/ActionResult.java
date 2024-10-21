package com.hxngxd.utils;

import com.hxngxd.enums.LogMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionResult {
    private static final Logger log = LogManager.getLogger(ActionResult.class);
    private final boolean success;
    private final String message;

    public ActionResult(boolean success, String action, String message) {
        this.success = success;
        this.message = message;
        if (success) {
            log.info(LogMessages.General.SUCCESS.getMessage(action));
        } else {
            log.error(LogMessages.General.FAIL.getMessage(action));
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
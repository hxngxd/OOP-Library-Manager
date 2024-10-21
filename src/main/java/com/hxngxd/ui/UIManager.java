package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.utils.LogMsg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class UIManager {
    private static final Logger logger = LogManager.getLogger(UIManager.class);
    public static final HashMap<UI, Parent> fxmlCache = new HashMap<>();


    private UIManager() {
    }

    public static Parent loadOnce(UI ui) {
        if (fxmlCache.containsKey(ui)) {
            return fxmlCache.get(ui);
        } else {
            return load(ui);
        }
    }

    public static Parent load(UI ui) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    UIManager.class.getResource(ui.getPath())
            );
            Parent root = loader.load();
            fxmlCache.put(ui, root);
            return root;
        } catch (IOException e) {
            logger.error(LogMsg.fail("load fxml: " + ui.name()), e);
        }
        return null;
    }
}

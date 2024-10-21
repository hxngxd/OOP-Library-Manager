package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.utils.LogMsg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class UIManager {
    private static final Logger logger = LogManager.getLogger(UIManager.class);
    public static final HashMap<UI, Scene> Scenes = new HashMap<>();
    public static final HashMap<UI, FXMLLoader> Loaders = new HashMap<>();


    private UIManager() {
    }

    public static Scene loadScene(UI ui) {
        if (!Scenes.containsKey(ui)) {
            try {
                Scenes.put(ui, new Scene(loadOnce(ui).load()));
            } catch (IOException e) {
                logger.error(LogMsg.fail("load scene: " + ui.name()), e);
                return null;
            }
        }
        return Scenes.get(ui);
    }

    public static FXMLLoader loadOnce(UI ui) {
        if (!Loaders.containsKey(ui)) {
            Loaders.put(ui, load(ui));
        }
        return Loaders.get(ui);
    }

    public static FXMLLoader load(UI ui) {
        return new FXMLLoader(
                UIManager.class.getResource(ui.getPath())
        );
    }
}

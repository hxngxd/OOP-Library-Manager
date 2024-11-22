package com.hxngxd.ui;

import com.hxngxd.enums.UI;
import com.hxngxd.enums.LogMsg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public final class UIManager {

    private static final Logger log = LogManager.getLogger(UIManager.class);

    public static final HashMap<UI, Scene> Scenes = new HashMap<>();

    public static final HashMap<UI, FXMLLoader> Loaders = new HashMap<>();

    public static UI currentScene = null;

    private UIManager() {
    }

    public static Scene loadScene(UI ui) {
        if (!Scenes.containsKey(ui)) {
            try {
                Scenes.put(ui, new Scene(Objects.requireNonNull(loadOnce(ui)).getRoot()));
            } catch (NullPointerException e) {
                log.error(LogMsg.GENERAL_FAIL.msg("load scene: " + ui.name()), e);
                return null;
            }
        }
        return Scenes.get(ui);
    }

    public static FXMLLoader loadOnce(UI ui) {
        if (!Loaders.containsKey(ui)) {
            FXMLLoader loader = load(ui);
            if (loader == null) {
                return null;
            }
            Loaders.put(ui, loader);
        }
        return Loaders.get(ui);
    }

    public static FXMLLoader load(UI ui) {
        FXMLLoader loader = new FXMLLoader(
                UIManager.class.getResource(ui.getPath())
        );
        try {
            loader.load();
        } catch (IOException e) {
            log.error(LogMsg.GENERAL_FAIL.msg("load ui: " + ui.name()), e);
            return null;
        }
        return loader;
    }

    public static <T> T getControllerOnce(UI ui) {
        return loadOnce(ui).getController();
    }

    public static <T> T getRootOnce(UI ui) {
        return loadOnce(ui).getRoot();
    }
}
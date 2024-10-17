package com.hxngxd.controller;

import com.hxngxd.enums.SceneType;
import com.hxngxd.utils.LogMsg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class SceneManager {
    private final StageManager stageManager = StageManager.getInstance();
    private final Logger logger = LogManager.getLogger(SceneManager.class);
    public static final HashMap<SceneType, Scene> Scenes = new HashMap<>();

    private SceneManager() {
    }

    private static class SingletonHolder {
        private static final SceneManager instance = new SceneManager();
    }

    public static SceneManager getInstance() {
        return SingletonHolder.instance;
    }

    public Scene loadScene(SceneType sceneType) {
        if (!Scenes.containsKey(sceneType)) {
            try {
                Parent parent = FXMLLoader.load(
                        Objects.requireNonNull(
                                getClass().getResource(sceneType.name() + ".fxml"))
                );
                Scenes.put(sceneType, new Scene(parent));
            } catch (IOException e) {
                logger.error(LogMsg.fail("load scene " + sceneType.name()), e);
                return null;
            }
        }
        return Scenes.get(sceneType);
    }
}

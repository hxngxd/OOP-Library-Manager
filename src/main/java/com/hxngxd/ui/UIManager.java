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
import java.util.Objects;

public class UIManager {
    private static final Logger logger = LogManager.getLogger(UIManager.class);
    private final StageManager stageManager = StageManager.getInstance();
    public static final HashMap<UI, FXMLLoader> Loaders = new HashMap<>();


    private UIManager() {
    }

    public static void load(UI ui) {
        if (!Loaders.containsKey(ui)) {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(
                            UIManager.class.getResource(ui.getPath())
                    ));
            Loaders.put(ui, loader);
        }
    }
}

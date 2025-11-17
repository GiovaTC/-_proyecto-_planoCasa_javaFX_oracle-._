package com.plano.planocasa;

import com.plano.planocasa.dao.HouseDAO;
import com.plano.planocasa.dao.RoomDAO;
import com.plano.planocasa.model.House;
import com.plano.planocasa.model.Room;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class MainApp extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private House currentHouse;

    @Override
    public void start(Stage primaryStage) {
        //
    }
}

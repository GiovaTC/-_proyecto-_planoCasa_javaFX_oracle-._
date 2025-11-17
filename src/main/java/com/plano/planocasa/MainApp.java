package com.plano.planocasa;

import com.plano.planocasa.dao.HouseDAO;
import com.plano.planocasa.dao.RoomDAO;
import com.plano.planocasa.model.House;
import com.plano.planocasa.model.Room;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

public class MainApp extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private House currentHouse;

    @Override
    public void start(Stage primaryStage) {
        //
        primaryStage.setTitle("Plano de casa - JavaFX + oracle");

        BorderPane root = new BorderPane();

        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        drawGrid();

        // panel lateral para crear casa / habitaciones
        VBox side = new VBox(10);
        side.setPadding(new Insets(10));
        TextField houseName = new TextField();
        houseName.setPromptText("Nombre de la casa");
        TextField houseAddr = new TextField();
        houseAddr.setPromptText("Dirección");
        Button btnCreateHouse = new Button("Crear casa");

        ListView<String> housesList = new ListView<>();
        Button btnLoadHouses = new Button("Cargar casas");

        // controler para agregar habitación (clic - drag en canvas tambien disponible)
        TextField roomName = new TextField();
        roomName.setPromptText("Nombre habitación");
        Button btnAddRoom = new Button("Añadir habitación (ejemplo)");

        side.getChildren().addAll(new Label("Casa:"), houseName, houseAddr, btnCreateHouse, btnLoadHouses, new Label("Casas guardadas:"), housesList,
              new Separator(), new Label("Habitación:"),  roomName, btnAddRoom);

        HBox bottom = new HBox(10);
        bottom.setPadding(new Insets(10));
        Label info = new Label("Haz click-drag en el canvas para dibujar una habitación (botón izquierdo).");
        bottom.getChildren().add(info);

        root.setCenter(canvas);
        root.setRight(side);
        root.setBottom(bottom);

        // Eventos
        btnCreateHouse.setOnAction(e -> {
            try {
                House h = new House();
                h.setName(houseName.getText());
                h.setAddress(houseAddr.getText());
                Long id = HouseDAO.save(h);
                h.setId(id);
                currentHouse = h;
                housesList.getItems().add(String.format("%d - %s", id, h.getName()));
                info.setText("Casa creada con id=" + id);
            } catch (SQLException ex) {
                info.setText("Error al crear casa: " + ex.getMessage());
            }
        });

        btnLoadHouses.setOnAction(e -> {
            housesList.getItems().clear();
            try {
                List<House> hs = HouseDAO.findAll();
                for (House h : hs) {
                    housesList.getItems().add(String.format("%d - %s", h.getId(), h.getName()));
                }
                info.setText("Casas cargadas: " + hs.size());
            } catch (SQLException ex) {
                info.setText("Error al cargar casas: " + ex.getMessage());
            }
        });

        housesList.setOnMouseClicked(ev -> {
            String sel = housesList.getSelectionModel().getSelectedItem();
            if (sel != null) {
                String[] parts = sel.split(" - ");
                try {
                    long id = Long.parseLong(parts[0]);
                    currentHouse = new House();
                    currentHouse.setId(id);
                    currentHouse.setName(parts[1]);
                    // cargar rooms
                    List<Room> rooms = RoomDAO.findByHouseId(id);
                    currentHouse.setRooms(rooms);
                    redrawRooms();
                    info.setText("Casa cargada: id=" + id + " con " + rooms.size() + " habitaciones");
                } catch (Exception ex) {
                    info.setText("Error al seleccionar casa: " + ex.getMessage());
                }
            }
        });

        // Añadir habitación manual simple
        btnAddRoom.setOnAction(e -> {
            if (currentHouse == null || currentHouse.getId() == null) {
                info.setText("Primero crea o carga una casa.");
                return;
            }
            String rn = roomName.getText();
            if (rn == null || rn.isEmpty()) rn = "Habitación";
            Room r = new Room(rn, 50, 50, 200, 150);
            r.setHouseId(currentHouse.getId());
            try {
                RoomDAO.save(r);
                currentHouse.getRooms().add(r);
                redrawRooms();
                info.setText("Habitación guardada: " + rn);
            } catch (SQLException ex) {
                info.setText("Error guardando habitación: " + ex.getMessage());
            }
        });

        // Dibujo con mouse: click-drag para crear habitación
        final double[] start = new double[2];
        canvas.setOnMousePressed(ev -> {
            if (ev.getButton() == MouseButton.PRIMARY) {
                start[0] = ev.getX(); start[1] = ev.getY();
            }
        });
        canvas.setOnMouseReleased(ev -> {
            if (ev.getButton() == MouseButton.PRIMARY) {
                double endX = ev.getX(); double endY = ev.getY();
                double x = Math.min(start[0], endX);
                double y = Math.min(start[1], endY);
                double w = Math.abs(endX - start[0]);
                double h = Math.abs(endY - start[1]);
                // crear habitación
                if (w > 10 && h > 10 && currentHouse != null && currentHouse.getId() != null) {
                    Room r = new Room("Hab " + (currentHouse.getRooms().size() + 1), x, y, w, h);
                    r.setHouseId(currentHouse.getId());
                    try {
                        RoomDAO.save(r);
                        currentHouse.getRooms().add(r);
                        redrawRooms();
                        info.setText("Habitación creada y guardada: " + r.getName());
                    } catch (SQLException ex) {
                        info.setText("Error guardando habitación: " + ex.getMessage());
                    }
                }
            }
        });

        primaryStage.setScene(new Scene(root, 1100, 640));
        primaryStage.show();
    }

    private void drawGrid() {
        gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        // fondo
        gc.fillText("Plano (grid)", 10, 12);
        for (int x = 0; x < canvas.getWidth(); x += 20) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        for (int y = 0; y < canvas.getHeight(); y += 20) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
    }

    private void redrawRooms() {
        drawGrid();
        if (currentHouse == null) return;
        gc.fillText("Casa: " + currentHouse.getName(), 10, 30);
        for (Room r : currentHouse.getRooms()) {
            gc.strokeRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
            gc.fillText(r.getName(), r.getX() + 5, r.getY() + 15);
            gc.fillText(String.format("%.1f m2", r.getArea()), r.getX() + 5, r.getY() + 30);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


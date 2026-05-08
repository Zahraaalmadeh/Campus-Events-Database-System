import javafx.application.Application;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.layout.BorderPane;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

public class MyApplication extends Application {
    public static Stable stable = new Stable();
    public static Horse horseTable = new Horse();
    public static Owner ownerTable = new Owner();
    public static Owns ownsTable = new Owns();
    public static Trainer trainerTable = new Trainer();
    public static Track track = new Track();
    public static Race race = new Race();
    public static RaceResults rr = new RaceResults();
    public static Old_info oldInfo=new Old_info();
    static Connection reservationConn;
    static Connection con;

    public static void main(String[] args) {
        try {
            reservationConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "Fatimah", "Fatimah@1029");
            Statement stmt = reservationConn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS reservation");

            stmt.close();
            reservationConn.close();

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservation", "Fatimah", "Fatimah@1029");
            stable.CreateTable(con);
            horseTable.CreateTable(con);
            ownerTable.CreateTable(con);
            ownsTable.CreateTable(con);
            trainerTable.CreateTable(con);
            track.CreateTable(con);
            race.CreateTable(con);
            rr.CreateTable(con);
            oldInfo.CreateTable(con);

            ownerTable.CreateDeleteOwnerProcedure(con);
            oldInfo.CreateTrigger(con);
            launch(args);
        } catch (SQLException e) {
            System.out.println("error: " + e.getMessage());
        }
    }
    @Override
    public void start(Stage primaryStage) {
        Text title = new Text("Horse Racing");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-fill: #896C6C; -fx-font-weight: bold;");

        Button adminButton = new Button("Admin");
        adminButton.setPrefSize(120, 50);
        adminButton.setFont(Font.font("Arial", 18));
        adminButton.setStyle(" -fx-background-color: #7A7A73;-fx-text-fill: #FCF9EA; ");

        Button guestButton = new Button("Guest");
        guestButton.setPrefSize(120, 50);
        guestButton.setFont(Font.font("Arial", 18));
        guestButton.setStyle(" -fx-background-color: #7A7A73;-fx-text-fill: #FCF9EA; ");

        adminButton.setOnAction(e -> AdminInterface(primaryStage));
        guestButton.setOnAction(e -> GuestInterface(primaryStage));

        HBox buttonLayout = new HBox(50, adminButton, guestButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(30, title, buttonLayout);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #FCF9EA; -fx-padding: 50;");

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("Horse Racing");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    // admin Interfaces
    private void AdminInterface(Stage stage) {
        VBox leftMenu = new VBox(20);
        leftMenu.setPadding(new Insets(20));
        leftMenu.setStyle("-fx-background-color: #7A7A73;");

        Button addRaceBtn = new Button("Add new race");
        addRaceBtn.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button deleteOwner = new Button("Delete owner");
        deleteOwner.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button moveHorse = new Button("Move horse");
        moveHorse.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button approveTrainer = new Button("Approve new Trainer");
        approveTrainer.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button exit = new Button("Exit");
        exit.setStyle("-fx-background-color: #842A3B;-fx-text-fill: #FCF9EA; ");

        addRaceBtn.setPrefWidth(150);
        deleteOwner.setPrefWidth(150);
        moveHorse.setPrefWidth(150);
        approveTrainer.setPrefWidth(150);
        exit.setPrefWidth(150);

        leftMenu.getChildren().addAll(addRaceBtn, deleteOwner, moveHorse, approveTrainer, exit);

        Pane addRaceForm = addRaceForm();

        BorderPane adminLayout = new BorderPane();
        adminLayout.setLeft(leftMenu);
        adminLayout.setCenter(addRaceForm);

        addRaceBtn.setOnAction(e -> adminLayout.setCenter(addRaceForm));
        deleteOwner.setOnAction(e -> adminLayout.setCenter(deleteOwner()));
        moveHorse.setOnAction(e -> adminLayout.setCenter(moveHorse()));
        approveTrainer.setOnAction(e -> adminLayout.setCenter(approveTrainer()));
        exit.setOnAction(e -> start(stage));

        Scene adminScene = new Scene(adminLayout, 1000, 600);
        stage.setScene(adminScene);
    }
    private Pane addRaceForm() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #FCF9EA;");

        Label title = new Label("Add New Race with Results");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);
        form.setPadding(new Insets(20, 0, 20, 0));

        Label raceIdLabel = new Label("Race ID *");
        raceIdLabel.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
        form.add(raceIdLabel, 0, 1);
        TextField raceIdField = new TextField();
        raceIdField.setPrefWidth(250);
        form.add(raceIdField, 1, 1);
        Label horseIdLabel = new Label("Horse ID *");
        horseIdLabel.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
        form.add(horseIdLabel, 0, 2);
        TextField horseIdField = new TextField();
        horseIdField.setPrefWidth(250);
        form.add(horseIdField, 1, 2);
        String[] optionalLabels = {
                "Race Name",
                "Track Name",
                "Race Date (yyyy-mm-dd)",
                "Race Time (HH:mm)",
                "Results",
                "Prize Amount"
        };
        TextField[] optionalFields = new TextField[optionalLabels.length];

        for (int i = 0; i < optionalLabels.length; i++) {
            Label lbl = new Label(optionalLabels[i]);
            lbl.setStyle("-fx-text-fill: #896C6C; -fx-font-weight: bold;");
            form.add(lbl, 0, i + 4);
            optionalFields[i] = new TextField();
            optionalFields[i].setPrefWidth(250);
            form.add(optionalFields[i], 1, i + 4);

            if (optionalLabels[i].contains("Date")) {
                optionalFields[i].setPromptText("e.g., 2024-01-15");
            } else if (optionalLabels[i].contains("Time")) {
                optionalFields[i].setPromptText("e.g., 14:30");
            } else if (optionalLabels[i].contains("Prize")) {
                optionalFields[i].setPromptText("e.g., 50000.00");
            }
        }

        Button submitBtn = new Button("Add Race & Results");
        submitBtn.setPrefWidth(200);
        submitBtn.setStyle(" -fx-background-color: #7A7A73;-fx-text-fill: #FCF9EA; -fx-font-weight: bold;");

        Label message = new Label();
        message.setVisible(false);

        submitBtn.setOnAction(e -> {
            String raceID = raceIdField.getText().trim();
            String horseId = horseIdField.getText().trim();
            String raceName = optionalFields[0].getText().trim();
            String trackName = optionalFields[1].getText().trim();
            String raceDate = optionalFields[2].getText().trim();
            String raceTime = optionalFields[3].getText().trim();
            String results = optionalFields[4].getText().trim();
            String prize = optionalFields[5].getText().trim();

            message.setVisible(false);

            if (raceID.isEmpty()) {
                showError(message, "Race ID is required");
                raceIdField.requestFocus();
                return;
            }
            if (horseId.isEmpty()) {
                showError(message, "Horse ID is required");
                horseIdField.requestFocus();
                return;
            }
            try {
                con.setAutoCommit(false);
                try {
                    if (!race.raceExists(con, raceID)) {
                        boolean raceAdded = race.addNewRace(con, raceID, raceName, trackName, raceDate, raceTime);
                        boolean resultAdded = rr.addRaceResult(con, raceID, horseId, results, prize);
                        con.commit();
                        if (raceAdded && resultAdded) {
                            showSuccess(message, "Race and Results Added Successfully!");
                            raceIdField.clear();
                            horseIdField.clear();
                            for (TextField field : optionalFields) {
                                field.clear();
                            }
                        }
                    } else {
                        showError(message, "Race ID already exists. Please use a different ID.");
                    }
                } catch (SQLException ex) {
                    con.rollback();
                    throw ex;
                } finally {
                    con.setAutoCommit(true);
                }
            } catch (SQLException s) {
                showError(message, s.getMessage());
            }
        });

        layout.getChildren().addAll(title, form, submitBtn, message);
        return layout;
    }
    private void showError(Label message, String text) {
        message.setText(text);
        message.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14px;");
        message.setVisible(true);
    }
    private void showSuccess(Label message, String text) {
        message.setText(text);
        message.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14px;");
        message.setVisible(true);
    }
    private Pane deleteOwner() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(80, 50, 50, 50));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FCF9EA; -fx-padding: 50;");

        Label title = new Label("Delete Owner");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        Label lbl = new Label("Enter owner ID");
        lbl.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");

        TextField input = new TextField();
        input.setPrefWidth(300);
        Button deleteBtn = new Button("Delete");
        deleteBtn.setPrefWidth(120);
        deleteBtn.setStyle(" -fx-background-color: #7A7A73;-fx-text-fill: #FCF9EA; ");
        Label message = new Label();
        message.setVisible(false);

        deleteBtn.setOnAction(e -> {
            String ID = input.getText();
            message.setVisible(false);
            if (ID.isEmpty()) {
                message.setText("Enter an Owner ID");
                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            } else {
                try {
                    if (ownerTable.ownerExists(con, ID)) {
                        if(numOfOwner(con,ID)>0){
                            boolean deleted = ownerTable.OwnerDeletionOnly(con, ID);
                            if (deleted) {
                                message.setText("The Owner has been deleted successfully");
                                message.setStyle("-fx-font-size: 15px; -fx-text-fill: #5D866C; -fx-font-weight: bold;");
                                input.clear();
                            } else {
                                message.setText("Failed to delete owner");
                                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                            }
                            message.setVisible(true);
                            String adding="INSERT INTO old_info VALUES (?,?,?,?,?,?)";
                            String oldHorse = "SELECT * FROM Horse WHERE horseId NOT IN (SELECT DISTINCT horseId FROM Owns)";
                            PreparedStatement old = con.prepareStatement(oldHorse);
                            ResultSet r = old.executeQuery();

                            while (r.next()) {
                                String horseId = r.getString("horseId");
                                String horseName = r.getString("horseName");
                                String age = r.getString("age");
                                String gender = r.getString("gender");
                                String registration = r.getString("registration");
                                String stableId = r.getString("stableId");

                                PreparedStatement ps = con.prepareStatement(adding);
                                ps.setString(1, horseId);
                                ps.setString(2, horseName);
                                ps.setString(3, age);
                                ps.setString(4, gender);
                                ps.setString(5, registration);
                                ps.setString(6, stableId);
                                ps.executeQuery();

                            }
                            String query="DELETE FROM Horse " +
                            "    WHERE horseId NOT IN (SELECT DISTINCT horseId FROM Owns); ";
                            PreparedStatement ps = con.prepareStatement(query);
                            ps.execute();
                        }
                        else{
                            boolean deleted = ownerTable.deleteOwner(con, ID);
                            if (deleted) {
                                message.setText("The Owner has been deleted successfully");
                                message.setStyle("-fx-font-size: 15px; -fx-text-fill: #5D866C; -fx-font-weight: bold;");
                                input.clear();
                            }
                            else {
                                message.setText("Failed to delete owner");
                                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                            }
                            message.setVisible(true);
                        }
                    } else {
                        message.setText("Owner Id does not exist");
                        message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                        message.setVisible(true);
                    }
                } catch (SQLException s) {
                    message.setText("Error: " + s.getMessage());
                    message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                    message.setVisible(true);
                }
            }
        });

        layout.getChildren().addAll(title,lbl, input, deleteBtn, message);
        return layout;
    }
    private int numOfOwner(Connection conn, String ownerId) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT ownerId) as ownerCount FROM Owns WHERE horseId IN " +
                "(SELECT horseId FROM Owns WHERE ownerId = ?) AND ownerId != ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ownerId);
        ps.setString(2, ownerId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("ownerCount");
        }
        return 0;
    }
    private Pane moveHorse() {
        VBox layout = new VBox(25);
        layout.setPadding(new Insets(80, 50, 50, 50));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FCF9EA; -fx-padding: 50;");

        Label title = new Label("Move Horse");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        Label horseLabel = new Label("Enter horse ID");
        horseLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        TextField horseInput = new TextField();
        horseInput.setPrefWidth(300);
        Label stableLabel = new Label("Enter new stable ID");
        stableLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        TextField stableInput = new TextField();
        stableInput.setPrefWidth(300);
        Button moveBtn = new Button("Move");
        moveBtn.setPrefWidth(120);
        moveBtn.setStyle(" -fx-background-color: #7A7A73;-fx-text-fill: #FCF9EA; ");
        Label message = new Label();
        message.setVisible(false);

        moveBtn.setOnAction(e -> {
            String horseID = horseInput.getText().trim();
            String stableID = stableInput.getText().trim();
            message.setVisible(false);
            if (horseID.isEmpty()) {
                message.setText("Enter a horse ID");
                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            } else if (stableID.isEmpty()) {
                message.setText("Enter a stable ID");
                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            } else {
                try {
                    boolean moved = horseTable.moveHorse(con, horseID, stableID);

                    if (moved) {
                        message.setText("Success! " + horseID + " has been moved to " + stableID);
                        message.setStyle("-fx-font-size: 15px; -fx-text-fill: #5D866C; -fx-font-weight: bold;");
                        horseInput.clear();
                        stableInput.clear();
                    } else {
                        message.setText("Failed to move horse");
                        message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                    }
                    message.setVisible(true);
                } catch (SQLException ex) {
                    message.setText( ex.getMessage());
                    message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                    message.setVisible(true);
                }
            }
        });

        layout.getChildren().addAll(title,horseLabel, horseInput, stableLabel, stableInput, moveBtn, message);
        return layout;
    }
    private Pane approveTrainer() {
        VBox layout = new VBox(25);
        layout.setPadding(new Insets(80, 50, 50, 50));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #FCF9EA; -fx-padding: 50;");

        Label title = new Label("Approve Trainer");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        Label lbl = new Label("Enter Trainer ID");
        lbl.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        TextField input = new TextField();
        input.setPrefWidth(300);
        Label lbl2 = new Label("Enter Stable ID");
        lbl2.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        TextField input2 = new TextField();
        input2.setPrefWidth(300);
        Button approve = new Button("Approve");
        approve.setPrefWidth(120);
        approve.setStyle(" -fx-background-color: #7A7A73;-fx-text-fill: #FCF9EA; ");
        Label message = new Label();
        message.setVisible(false);

        approve.setOnAction(e -> {
            String trainerID = input.getText().trim();
            String stableID = input2.getText().trim();
            message.setVisible(false);
            if (trainerID.isEmpty()) {
                message.setText("Enter Trainer ID");
                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            } else if (stableID.isEmpty()) {
                message.setText("Enter Stable ID");
                message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            } else {
                try {
                    boolean approved = trainerTable.approveTrainer(con, trainerID, stableID);
                    if (approved) {
                        message.setText("Success! " + trainerID + " has been approved for " + stableID);
                        message.setStyle("-fx-font-size: 15px; -fx-text-fill: #5D866C; -fx-font-weight: bold;");
                        input.clear();
                        input2.clear();
                    } else {
                        message.setText("Failed to approve trainer");
                        message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                    }
                    message.setVisible(true);
                } catch (SQLException ex) {
                    message.setText(ex.getMessage());
                    message.setStyle("-fx-font-size: 15px; -fx-text-fill: Red; -fx-font-weight: bold;");
                    message.setVisible(true);
                }
            }
        });

        layout.getChildren().addAll(title,lbl, input, lbl2, input2, approve, message);
        return layout;
    }
    // guest Interfaces
    private void GuestInterface(Stage stage) {
        VBox leftMenu = new VBox(16);
        leftMenu.setPadding(new Insets(20));
        leftMenu.setStyle("-fx-background-color: #7A7A73;");

        Button btnHorsesByOwner = new Button("Browse Horses By Owner");
        btnHorsesByOwner.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button btnWinningTrainers = new Button("Winning Trainers");
        btnWinningTrainers.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button btnTrainerWinnings = new Button("Trainer Total Wins");
        btnTrainerWinnings.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button btnTracksCounts = new Button("Tracks With Counts");
        btnTracksCounts.setStyle("-fx-background-color: #FCF9EA;-fx-text-fill: #595959; ");
        Button exit = new Button("Exit");
        exit.setStyle("-fx-background-color: #842A3B;-fx-text-fill: #FCF9EA; ");

        for (Button b : new Button[]{btnHorsesByOwner, btnWinningTrainers, btnTrainerWinnings, btnTracksCounts, exit}) {
            b.setPrefWidth(150);
        }
        leftMenu.getChildren().addAll(btnHorsesByOwner, btnWinningTrainers, btnTrainerWinnings, btnTracksCounts, exit);

        BorderPane root = new BorderPane();
        root.setLeft(leftMenu);


        Pane horsesPane = horsesByOwnerPane();
        Pane winningPane = winningTrainersPane();
        Pane winningsPane = trainerTotalWinningsPane();
        Pane tracksPane = tracksWithCountsPane();


        root.setCenter(horsesPane);

        btnHorsesByOwner.setOnAction(e -> root.setCenter(horsesPane));
        btnWinningTrainers.setOnAction(e -> root.setCenter(winningPane));
        btnTrainerWinnings.setOnAction(e -> root.setCenter(winningsPane));
        btnTracksCounts.setOnAction(e -> root.setCenter(tracksPane));
        exit.setOnAction(e -> start(stage));

        stage.setScene(new Scene(root, 1000, 600));
    }
    private Pane horsesByOwnerPane() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(30));
        form.setHgap(15);
        form.setVgap(10);
        form.setStyle("-fx-background-color: #FCF9EA; -fx-padding: 50;");

        Label title = new Label("Browse Horses by Owner Last Name");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");
        form.add(title, 0, 0, 2, 1);

        TextField ownerLastName = new TextField();
        ownerLastName.setPromptText("Owner last name");
        ownerLastName.setPrefWidth(400);
        Button search = new Button("Search");
        search.setStyle("-fx-background-color: #7A7A73; -fx-text-fill: white;");
        form.add(ownerLastName, 0, 1);
        form.add(search, 1, 1);

        Label message = new Label();
        message.setVisible(false);
        form.add(message, 0, 2, 2, 1);

        TableView<HorseByOwnerRow> table = new TableView<>();
        table.setPlaceholder(new Label("Results will appear here..."));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<HorseByOwnerRow, String> horseCol = new TableColumn<>("Horse Name");
        horseCol.setCellValueFactory(new PropertyValueFactory<>("horseName"));
        TableColumn<HorseByOwnerRow, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<HorseByOwnerRow, String> tfCol = new TableColumn<>("Trainer First");
        tfCol.setCellValueFactory(new PropertyValueFactory<>("trainerFirstName"));
        TableColumn<HorseByOwnerRow, String> tlCol = new TableColumn<>("Trainer Last");
        tlCol.setCellValueFactory(new PropertyValueFactory<>("trainerLastName"));
        table.getColumns().addAll(horseCol, ageCol, tfCol, tlCol);

        form.add(table, 0, 3, 2, 1);

        search.setOnAction(e -> {
            String name = ownerLastName.getText() == null ? "" : ownerLastName.getText().trim();
            message.setVisible(false);
            if (name.isEmpty()) {
                message.setText("Enter an owner last name");
                message.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
                return;
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT h.horseName, h.age, t.fname AS trainerFirstName, t.lname AS trainerLastName " +
                            "FROM Owner o JOIN Owns ow ON ow.ownerId=o.ownerId " +
                            "JOIN Horse h ON h.horseId=ow.horseId " +
                            "LEFT JOIN Trainer t ON t.stableId=h.stableId " +
                            "WHERE LOWER(o.lname)=LOWER(?) " +
                            "ORDER BY h.horseName, t.lname, t.fname")) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    java.util.List<HorseByOwnerRow> rows = new java.util.ArrayList<>();
                    while (rs.next()) {
                        rows.add(new HorseByOwnerRow(
                                rs.getString("horseName"),
                                rs.getInt("age"),
                                rs.getString("trainerFirstName"),
                                rs.getString("trainerLastName")));
                    }
                    table.setItems(FXCollections.observableArrayList(rows));
                    message.setText("Loaded " + rows.size() + " rows");
                    message.setStyle("-fx-text-fill: #5D866C; -fx-font-weight: bold;");
                    message.setVisible(true);
                }
            } catch (SQLException ex) {
                message.setText(ex.getMessage());
                message.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            }
        });

        return form;
    }
    private Pane winningTrainersPane() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #FCF9EA;");

        Label title = new Label("Winning Trainers");
        title.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");

        Button load = new Button("Load");
        load.setStyle("-fx-background-color: #7A7A73; -fx-text-fill: white;");

        Label message = new Label();
        message.setVisible(false);

        TableView<WinningTrainerRow> table = new TableView<>();
        table.setPlaceholder(new Label("Click Load"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<WinningTrainerRow, String> tn = new TableColumn<>("Trainer Name");
        tn.setCellValueFactory(new PropertyValueFactory<>("trainerName"));
        TableColumn<WinningTrainerRow, String> wh = new TableColumn<>("Winning Horse");
        wh.setCellValueFactory(new PropertyValueFactory<>("winningHorse"));
        TableColumn<WinningTrainerRow, String> wr = new TableColumn<>("Winning Race");
        wr.setCellValueFactory(new PropertyValueFactory<>("winningRace"));
        table.getColumns().addAll(tn, wh, wr);

        load.setOnAction(e -> {
            message.setVisible(false);
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT t.fname AS trainerFirstName, t.lname AS trainerLastName, " +
                                 "h.horseName AS winningHorse, r.raceName AS winningRace, r.raceDate AS raceDate " +
                                 "FROM RaceResults rr " +
                                 "JOIN Horse h ON h.horseId = rr.horseId " +
                                 "JOIN Trainer t ON t.stableId = h.stableId " +
                                 "JOIN Race r ON r.raceId = rr.raceId " +
                                 "WHERE rr.results = 'first' " +
                                 "ORDER BY t.lname, t.fname, h.horseName")) {
                java.util.List<WinningTrainerRow> rows = new java.util.ArrayList<>();
                while (rs.next()) {
                    rows.add(new WinningTrainerRow(
                            rs.getString("trainerFirstName"),
                            rs.getString("trainerLastName"),
                            rs.getString("winningHorse"),
                            rs.getString("winningRace"),
                            rs.getString("raceDate")
                    ));
                }
                table.setItems(FXCollections.observableArrayList(rows));
                message.setText("Loaded " + rows.size() + " rows");
                message.setStyle("-fx-text-fill: #5D866C; -fx-font-weight: bold;");
                message.setVisible(true);
            } catch (SQLException ex) {
                message.setText(ex.getMessage());
                message.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            }
        });

        layout.getChildren().addAll(title, load, message, table);
        return layout;
    }
    private Pane trainerTotalWinningsPane() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #FCF9EA;");

        Label title = new Label("Trainer Total Winnings");
        title.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");

        Button load = new Button("Load");
        load.setStyle("-fx-background-color: #7A7A73; -fx-text-fill: white;");

        Label message = new Label();
        message.setVisible(false);

        TableView<TrainerWinningsRow> table = new TableView<>();
        table.setPlaceholder(new Label("Click Load"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TrainerWinningsRow, String> tn = new TableColumn<>("Trainer Name");
        tn.setCellValueFactory(new PropertyValueFactory<>("trainerName"));
        TableColumn<TrainerWinningsRow, Number> tw = new TableColumn<>("Total Winnings");
        tw.setCellValueFactory(new PropertyValueFactory<>("totalWinnings"));
        table.getColumns().addAll(tn, tw);

        load.setOnAction(e -> {
            message.setVisible(false);
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT t.fname AS trainerFirstName, t.lname AS trainerLastName, " +
                                 "SUM(CASE WHEN rr.prize IS NULL THEN 0 ELSE rr.prize END) AS totalWinnings " +
                                 "FROM Trainer t " +
                                 "LEFT JOIN Horse h ON h.stableId = t.stableId " +
                                 "LEFT JOIN RaceResults rr ON rr.horseId = h.horseId " +
                                 "GROUP BY t.trainerId, t.fname, t.lname " +
                                 "ORDER BY totalWinnings DESC, t.lname, t.fname")) {
                java.util.List<TrainerWinningsRow> rows = new java.util.ArrayList<>();
                while (rs.next()) {
                    rows.add(new TrainerWinningsRow(
                            rs.getString("trainerFirstName"),
                            rs.getString("trainerLastName"),
                            rs.getDouble("totalWinnings")
                    ));
                }
                table.setItems(FXCollections.observableArrayList(rows));
                message.setText("Loaded " + rows.size() + " rows");
                message.setStyle("-fx-text-fill: #5D866C; -fx-font-weight: bold;");
                message.setVisible(true);
            } catch (SQLException ex) {
                message.setText(ex.getMessage());
                message.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            }
        });

        layout.getChildren().addAll(title, load, message, table);
        return layout;
    }
    private Pane tracksWithCountsPane() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #FCF9EA;");

        Label title = new Label("Tracks with Race & Participant Counts");
        title.setStyle("-fx-font-size: 22px; -fx-text-fill: #896C6C; -fx-font-weight: bold;");

        Button load = new Button("Load");
        load.setStyle("-fx-background-color: #7A7A73; -fx-text-fill: white;");

        Label message = new Label();
        message.setVisible(false);

        TableView<TrackCountsRow> table = new TableView<>();
        table.setPlaceholder(new Label("Click Load"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TrackCountsRow, String> tc = new TableColumn<>("Track Name");
        tc.setCellValueFactory(new PropertyValueFactory<>("trackName"));
        TableColumn<TrackCountsRow, Number> rc = new TableColumn<>("Race Count");
        rc.setCellValueFactory(new PropertyValueFactory<>("raceCount"));
        TableColumn<TrackCountsRow, Number> pc = new TableColumn<>("Participant Count");
        pc.setCellValueFactory(new PropertyValueFactory<>("participantCount"));
        table.getColumns().addAll(tc, rc, pc);

        load.setOnAction(e -> {
            message.setVisible(false);
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT tr.trackName, " +
                                 "       COUNT(DISTINCT r.raceId) AS raceCount, " +
                                 "       COUNT(p.horseId)        AS participantCount " +
                                 "FROM Track AS tr " +
                                 "LEFT JOIN Race AS r " +
                                 "  ON r.trackName = tr.trackName " +
                                 "LEFT JOIN ( " +
                                 "    SELECT rr.raceId, rr.horseId " +
                                 "    FROM RaceResults rr " +
                                 "    GROUP BY rr.raceId, rr.horseId " +
                                 "    HAVING SUM(CASE " +
                                 "                 WHEN LOWER(rr.results) IN " +
                                 "                      ('first','second','third','fourth','fifth','sixth','seventh','eighth','ninth','tenth','last')" +
                                 "                 THEN 1 ELSE 0 " +
                                 "               END) > 0 " +
                                 " ) AS p " +
                                 "  ON p.raceId = r.raceId " +
                                 "GROUP BY tr.trackName " +
                                 "ORDER BY tr.trackName")) {

                java.util.List<TrackCountsRow> rows = new java.util.ArrayList<>();
                while (rs.next()) {
                    rows.add(new TrackCountsRow(
                            rs.getString("trackName"),
                            rs.getInt("raceCount"),
                            rs.getInt("participantCount")
                    ));
                }
                table.setItems(FXCollections.observableArrayList(rows));
                message.setText("Loaded " + rows.size() + " rows");
                message.setStyle("-fx-text-fill: #5D866C; -fx-font-weight: bold;");
                message.setVisible(true);
            } catch (SQLException ex) {
                message.setText(ex.getMessage());
                message.setStyle("-fx-text-fill: Red; -fx-font-weight: bold;");
                message.setVisible(true);
            }

        });

        layout.getChildren().addAll(title, load, message, table);
        return layout;
    }
    public static class HorseByOwnerRow {
        private final String horseName; private final int age;
        private final String trainerFirstName; private final String trainerLastName;
        public HorseByOwnerRow(String horseName, int age, String tf, String tl) {
            this.horseName = horseName; this.age = age; this.trainerFirstName = tf; this.trainerLastName = tl;
        }
        public String getHorseName() { return horseName; }
        public int getAge() { return age; }
        public String getTrainerFirstName() { return trainerFirstName; }
        public String getTrainerLastName() { return trainerLastName; }
    }
    public static class WinningTrainerRow {
        private final String trainerName; private final String winningHorse; private final String winningRace; private final String raceDate;
        public WinningTrainerRow(String f, String l, String horse, String race, String date) {
            this.trainerName = ((f==null?"":f) + " " + (l==null?"":l)).trim();
            this.winningHorse = horse; this.winningRace = race; this.raceDate = date;
        }
        public String getTrainerName() { return trainerName; }
        public String getWinningHorse() { return winningHorse; }
        public String getWinningRace() { return winningRace; }
        public String getRaceDate() { return raceDate; }
    }
    public static class TrainerWinningsRow {
        private final String trainerName; private final double totalWinnings;
        public TrainerWinningsRow(String f, String l, double total) {
            this.trainerName = ((f==null?"":f) + " " + (l==null?"":l)).trim();
            this.totalWinnings = total;
        }
        public String getTrainerName() { return trainerName; }
        public double getTotalWinnings() { return totalWinnings; }
    }
    public static class TrackCountsRow {
        private final String trackName; private final int raceCount; private final int participantCount;
        public TrackCountsRow(String name, int races, int participants) {
            this.trackName = name; this.raceCount = races; this.participantCount = participants;
        }
        public String getTrackName() { return trackName; }
        public int getRaceCount() { return raceCount; }
        public int getParticipantCount() { return participantCount; }
    }
}
package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        GiftWrapper points = new GiftWrapper();
        Button giftWrap = new Button("Wrap Gift");
        //Create the initial set of rectangles.
        Group pointsGroup = new Group(points.getPoints());
        Group lines = new Group(points.getLines());
        pointsGroup.getChildren().addAll(lines, giftWrap);
        primaryStage.setScene(new Scene(pointsGroup, 800, 600, Color.BLACK));
        primaryStage.show();

        giftWrap.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                points.wrapGift();
                //System.out.println("The button has been pressed!");
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

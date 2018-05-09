package Controller;

import Main.MainApplication;
import Service.MainService;
import Utils.Screen;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class MedicDashboardController implements ControlledScreensInterface {

    @FXML
    private Label fullnameLabel;

    @FXML
    private Button cerereSangeButton;

    @FXML
    private Button istoricCereriButton;

    @FXML
    private Button stareActualaButton;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane leftAnchorPane;

    @FXML
    private AnchorPane meniuPane;

    @FXML
    private AnchorPane burgerPane;

    private double prefBorderPaneWidth;
    private double prefBurgerPaneWidth;
    private double prefLeftPaneWidth;
    private double animationSpeed;
    private MainService mainService;
    private ControllerScreens controller;

    @FXML
    private void showLeftMenu(){

        leftAnchorPane.getChildren().remove(burgerPane);
        Timeline timeline = new Timeline();
        double pref = prefLeftPaneWidth;
        System.out.println(pref);

        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(animationSpeed),
                        new KeyValue(leftAnchorPane.prefWidthProperty()
                                , pref))

        );
        timeline.play();
        timeline.setOnFinished(
                x-> {
                    leftAnchorPane.getChildren().add(meniuPane);
                    resizeCentralPane();
                }
        );
    }

    @FXML
    private BorderPane borderPane;
    @FXML
    private BorderPane centralPane;
    @FXML
    private void CerereSangeClicked(){

        centralPane.setCenter(controller.getScreen("CERERE_SANGE"));

        mainPane.setPrefWidth(900);
        resizeCentralPane();
    }
    @FXML
    private void IstoricCereriClicked(){

        centralPane.setCenter(controller.getScreen("ISTORIC_CERERI"));
        mainPane.setPrefWidth(900);
        resizeCentralPane();
    }
    @FXML
    private void stareActualaClicked(){
        centralPane.setCenter(controller.getScreen("STARE_PACIENTI"));
        mainPane.setPrefWidth(900);
        resizeCentralPane();
    }

    @FXML
    private void homeButtonClicked(){
        centralPane.setCenter(mainPane);
        mainPane.setPrefWidth(740);
        resizeCentralPane();
    }

    @FXML
    private void hideLeftMenu(){

        leftAnchorPane.getChildren().remove(meniuPane);
        Timeline timeline = new Timeline();
        double pref = prefBurgerPaneWidth;

       // borderPane.getScene().getWindow().setWidth(prefCentral);

        Window a = borderPane.getScene().getWindow();
        System.out.println(pref);
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(animationSpeed),
                        new KeyValue(leftAnchorPane.prefWidthProperty()
                                , pref))

        );
        timeline.play();
        timeline.setOnFinished(
                x-> {
                    leftAnchorPane.getChildren().add(burgerPane);
                    System.out.println(prefBorderPaneWidth);
                    resizeCentralPane();
                    //resizeCentralPane(prefCentral);
                }
        );

    }


    private double getLeftSize(){
        return leftAnchorPane.getPrefWidth();
    }

    double prefWindowSize;

    private void resizeCentralPane(){
        Timeline timeline = new Timeline();

        prefWindowSize = mainPane.getPrefWidth() + getLeftSize();

        borderPane.getScene().getWindow().setWidth(prefWindowSize);
        System.out.println(getLeftSize());
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(animationSpeed),
                        new KeyValue(borderPane.prefWidthProperty(),
                                prefWindowSize))
        );
        timeline.play();
        timeline.setOnFinished(
                x-> {

                }
        );
    }
    @FXML
    private AnchorPane mainPane;


    @FXML
    private void initialize(){
        animationSpeed = 150;
        prefLeftPaneWidth = leftAnchorPane.getPrefWidth();
        prefBurgerPaneWidth = burgerPane.getPrefWidth();
        prefBorderPaneWidth = borderPane.getPrefWidth();
        prefWindowSize = prefBorderPaneWidth;
        leftAnchorPane.getChildren().remove(burgerPane);
    }

    public void setMainService(MainService mainService){
        this.mainService = mainService;
    }

    @Override
    public void setScreenParent(ControllerScreens screenParent) {
        this.controller = screenParent;
    }

    @FXML
    private void logout(){
        loadLogin();
    }

    private void loadLogin() {
        controller.setScreen(Screen.LOGIN_SCREEN);
    }
}

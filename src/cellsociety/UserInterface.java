package cellsociety;




import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserInterface extends Application {

    public static final int DEFAULT_FRAMES_PER_SECOND = 1;

    private Group UIroot;
    private Stage UIstage;

    private int stepsPerSecond;
    private Simulation mySim;


    @Override
    public void start(Stage primaryStage) {
        UIroot = new Group();
        UIstage = new Stage();
        stepsPerSecond = DEFAULT_FRAMES_PER_SECOND;
        Configuration config = new Configuration(getFileName());
        mySim = config.getInitSim();
        createController();
    }

    private String getFileName(){
        FileTextPrompt fileInput = new FileTextPrompt();
        return fileInput.getResult();
    }

    private void createController(){
        HBox controls = new HBox();
        Button stopButton = new Button();
        stopButton.setOnAction(e -> mySim.resetKeyFrame(0));
        stopButton.setText("Stop");
        controls.getChildren().add(stopButton);
        UIroot.getChildren().add(controls);
        Scene controllerScreen = new Scene(UIroot, 500, 500);
        UIstage.setScene(controllerScreen);
        UIstage.show();
    }

    private void setStepsPerSecond(int sps){
        stepsPerSecond = sps;
    }

    public int getStepsPerSecond(){
        return stepsPerSecond;
    }




    class FileTextPrompt {
        private final String result;
        FileTextPrompt() {
            final Stage dialog = new Stage();

            dialog.setTitle("Enter Missing Text");
            dialog.initStyle(StageStyle.UTILITY);
            dialog.initModality(Modality.WINDOW_MODAL);


            final TextField textField = new TextField();
            final Button submitButton = new Button("Submit");
            submitButton.setDefaultButton(true);
            submitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent t) {
                    if(isValidFile()) {
                        dialog.close();
                    }
                    else{
                        displayErrorMessage();
                    }
                }
            });
            textField.setMinHeight(TextField.USE_PREF_SIZE);

            final VBox layout = new VBox(10);
            layout.setAlignment(Pos.CENTER_RIGHT);
            layout.setStyle("-fx-background-color: azure; -fx-padding: 10;");
            layout.getChildren().setAll(
                    textField,
                    submitButton
            );

            dialog.setScene(new Scene(layout));
            dialog.showAndWait();

            result = textField.getText();
        }

        private void displayErrorMessage() {
            return;
        }

        private boolean isValidFile() {
            return true;
        }

        private String getResult() {
            return result;
        }
    }
}

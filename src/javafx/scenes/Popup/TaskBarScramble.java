package javafx.scenes.Popup;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class TaskBarScramble  implements Initializable {
        @FXML
        public Label progress_status_label;
        @FXML
        public Label progress_precent_label;
        @FXML
        public ProgressBar progressbar;
        @FXML
        public Button ok_btn;

        String[] texts = new String[]{"Loans","Borrower","Optinal Loans","Optinal invests"};

        @Override
        public void initialize(URL location, ResourceBundle resources) {
        }

        public void showProgress(boolean isGood, Consumer<Boolean> consumer) {
                int MAX = 100;
                progress_status_label.setText("Verify");
                Task<Object> task = new Task<Object>() {
                        @Override
                        protected Object call() {

                                if (!isGood){
                                        for (int i = 0; i < 5; i++) {
                                                progressbar.setProgress((double)i/10);
                                                try {
                                                        Thread.sleep(40);
                                                } catch (InterruptedException ignored) {
                                                }
                                        }
                                        Platform.runLater(() -> progress_status_label.setText("Error!"));
                                        ok_btn.setDisable(false);
                                        progressbar.setProgress(0);
                                        Platform.runLater(() -> consumer.accept(false));
                                        return null;
                                }

                                final int[] j = {0};
                                for (int i = 0; i <= MAX; i++) {
                                        int finalI = i;
                                        Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                        progress_precent_label.setText(finalI+"%");
                                                }
                                        });

                                        progressbar.setProgress((double)(i%10) /10);
                                        if (i%(MAX/texts.length) == 0){
                                                Platform.runLater(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                                if (j[0] < texts.length)
                                                                        progress_status_label.setText("Loading "+texts[j[0]++]);
                                                        }
                                                });

                                        }
                                        try {
                                                Thread.sleep(40);
                                        } catch (InterruptedException ignored) {
                                        }
                                }


                                Platform.runLater(() -> progress_status_label.setText("Done!"));
                                progressbar.setProgress(1);

                                ok_btn.setDisable(false);
                                Platform.runLater(() -> consumer.accept(true));
                                return null;
                        }};
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();
        }

        @FXML
        public void onCloseButtonClick(ActionEvent event){
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
        }
}
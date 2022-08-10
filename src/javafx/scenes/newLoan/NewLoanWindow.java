package javafx.scenes.newLoan;

import customer_utills.Constants;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static customer_utills.Constants.GSON_INSTANCE;
import static java.lang.Character.isDigit;

public class NewLoanWindow  implements Initializable {

    private Runnable runnable;

    @FXML
    public TextField loan_id_textview;
    @FXML
    public ComboBox loan_category_combobox;
    @FXML
    public TextField loan_capital_textview;
    @FXML
    public TextField loan_total_yaz_time_textview;
    @FXML
    public TextField loan_payment_every_yaz_time_textview;
    @FXML
    public Slider loan_instint_per_payment_slider;
    @FXML
    public Label loan_instint_per_payment_label;
    @FXML
    public Button cancel_new_loan_btn;
    @FXML
    public Button admit_new_loan_btn;
    @FXML
    public TextField instint_textfield;
    @FXML
    public Button enter_instint_by_textfield_btn;

    /**/
    public void openWindow(Stage primaryStage) throws Exception {
       /* FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(Constants.CUSTOMER_NEW_LOAN_WINDOW_FXML_RESOURCE_LOCATION);
        fxmlLoader.setLocation(url);
        Parent playersRoot = fxmlLoader.load(fxmlLoader.getLocation().openStream());
        Scene scene = new Scene(playersRoot);
        primaryStage.setTitle("Create New Loan Window");
        primaryStage.setScene(scene);
        Image mini_icon = new Image("javafx/resources/images/main_mini_logo.png");
        primaryStage.getIcons().add(mini_icon);
        primaryStage.show();*/
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateCategoriesView();
        this.instint_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                instint_textfield.setText(oldValue+"");
            }
            else if(!newValue.isEmpty() && Double.parseDouble(newValue) > 100)
                instint_textfield.setText(oldValue+"");
        });
        this.loan_total_yaz_time_textview.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                loan_total_yaz_time_textview.setText(oldValue+"");
            }
        });
        this.loan_payment_every_yaz_time_textview.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                loan_payment_every_yaz_time_textview.setText(oldValue+"");
            }
        });
    }

    @FXML
    public void onCloseButtonClick(ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onAdmitButtonClick(ActionEvent event){
        if(true /* TODO check validation */) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            String finalUrl = HttpUrl
                    .parse(Constants.CREATE_NEW_LOAN)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .addQueryParameter("loan_id", this.loan_id_textview.getText())
                    .addQueryParameter("loan_category", this.loan_category_combobox.getValue().toString() + "")
                    .addQueryParameter("loan_capital", this.loan_capital_textview.getText())
                    .addQueryParameter("loan_total_yaz_time", this.loan_total_yaz_time_textview.getText())
                    .addQueryParameter("loan_payment_every_yaz_time", this.loan_payment_every_yaz_time_textview.getText())
                    .addQueryParameter("loan_instint_per_payment", Double.toString(this.loan_instint_per_payment_slider.getValue()))
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println("onResponse onAdmitButtonClick");
                    System.out.println("Response: " + response.code());
                    if (response.code() == 200) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stage.close();
                                runnable.run();
                            }
                        });
                    } else if (response.code() == 400) {

                    }
                }
            });
        }// TODO error popup

    }

    @FXML
    protected void changeSliderLabelHandler(MouseEvent e)
    {
        this.loan_instint_per_payment_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int newValueInt = (int)Math.round(newValue.doubleValue());
            loan_instint_per_payment_slider.setValue(newValueInt);
            loan_instint_per_payment_label.setText("Loan Instint Per Payment is " + newValueInt + "%");
        });
    }

    @FXML
    protected void enterInstintByTextField(ActionEvent event)
    {
        String instint_str = this.instint_textfield.getText() +"";
        Double instint_per_payment = Double.parseDouble(instint_str);
        this.loan_instint_per_payment_slider.setValue(instint_per_payment);
        this.loan_instint_per_payment_label.setText("Loan Instint Per Payment is " + instint_str + "%");
    }

    private void updateCategoriesView(){
        String finalUrl = HttpUrl
                .parse(Constants.CATEGORIES)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse");
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response: " + jsonArrayOfUsersNames);
                String[] categoriesfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                for (String category : categoriesfromJson) {
                    System.out.println(category);
                }
                Platform.runLater(() -> {
                    loan_category_combobox.getItems().clear();
                    loan_category_combobox.getItems().addAll(categoriesfromJson);
                });
            }
        });


    }

    protected boolean isDoubleInput(String input)
    {
        int g = 0;
        if(isDigit(input.charAt(0))) {
            for(int i = 1; i<input.length(); i++)
            {
                if(input.charAt(i) == '.')
                {
                    g++;
                }
                else if(!isDigit(input.charAt(i)) && input.charAt(i) != '.')
                    return false;
                if(g > 1)
                    return false;
            }
        }
        else
            return false;
        return true;
    }

    public void setCallback(Runnable runnable) {
        this.runnable = runnable;
    }
}

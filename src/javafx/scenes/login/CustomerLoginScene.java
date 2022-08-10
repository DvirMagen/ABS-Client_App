package javafx.scenes.login;

import customer_utills.Constants;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static customer_utills.Constants.GSON_INSTANCE;

public class CustomerLoginScene implements Initializable {
    @FXML
    public TextField customer_username_textfield_login_scene;
    @FXML
    public Label customer_error_label_login_scene;
    @FXML
    public Button customer_enter_btn_login_scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    protected void onClickEnterButton(ActionEvent event)
    {
        String username = customer_username_textfield_login_scene.getText();
        if(username.isEmpty())
        {
            customer_error_label_login_scene.setText("Please Enter Username");
            customer_error_label_login_scene.setTextFill(Color.RED);
            customer_error_label_login_scene.setVisible(true);
        } else{
            String finalUrl = HttpUrl
                    .parse(customer_utills.Constants.LOGIN_PAGE)
                    .newBuilder()
                    .addQueryParameter("username",username )
                    .build()
                    .toString();

            // updateHttpStatusLine("New request is launched for: " + finalUrl);
            customer_error_label_login_scene.setText("");
            customer_error_label_login_scene.setVisible(false);
            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        customer_error_label_login_scene.setVisible(true);
                        customer_error_label_login_scene.setText("Something went wrong: " + e.getMessage());
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        String responseBody = response.body().string();
                        Platform.runLater(() -> {
                            customer_error_label_login_scene.setVisible(true);
                            customer_error_label_login_scene.setText("Something went wrong: " + responseBody);
                        });
                        System.out.println(responseBody);
                    } else {
                        String finalUrl = HttpUrl
                                .parse(Constants.BALANCE)
                                .newBuilder()
                                .addQueryParameter("username",username )
                                .build()
                                .toString();
                        HttpClientUtil.runAsync(finalUrl, new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Platform.runLater(() -> {
                                    customer_error_label_login_scene.setVisible(true);
                                    customer_error_label_login_scene.setText("Something went wrong: " + e.getMessage());
                                });
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String json = response.body().string();
                                HashMap<String,Object> mapfromJson = GSON_INSTANCE.fromJson(json, HashMap.class);
                                Platform.runLater(() -> {
                                    //
                                    ABS_Client.current_customer.setName(username);
                                    ABS_Client.current_customer.setBalance((Double)mapfromJson.get("balance"));
                                    try {
                                        FXMLLoader loader = new FXMLLoader();
                                        URL url = getClass().getResource(Constants.CUSTOMER_DASHBOARD_PAGE_FXML_RESOURCE_LOCATION);
                                        loader.setLocation(url);
                                        Parent root = loader.load();
                                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        stage.setScene(new Scene(root));
                                        stage.show();
                                        //adminLoadFileScene.updateWelcomeAdminLabel(username_admin_login.getText());
                                    } catch (IOException ex) {
                                        System.err.println(ex);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }
}

package javafx;

import customer_utills.Constants;
import customer_utills.Customer;
import customer_utills.http.HttpClientUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class ABS_Client extends Application {

    public static Customer current_customer = new Customer();
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(Constants.CUSTOMER_LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        fxmlLoader.setLocation(url);

        Parent playersRoot = fxmlLoader.load(fxmlLoader.getLocation().openStream());
//        MainScene mainController = (MainScene) fxmlLoader.getController();
        Scene scene = new Scene(playersRoot);
        primaryStage.setTitle("A.B.S - Alternative Banking System");
        primaryStage.setScene(scene);
        Image mini_icon = new Image("javafx/resources/images/main_mini_logo.png");
        primaryStage.getIcons().add(mini_icon);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.runAsync(Constants.LOGOUT, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // chatCommands.updateHttpLine("Logout request ended with failure...:(");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() || response.isRedirect()) {
                    HttpClientUtil.removeCookiesOf(Constants.BASE_DOMAIN);
                    current_customer = new Customer();
                    HttpClientUtil.shutdown();
                }
            }
        });

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

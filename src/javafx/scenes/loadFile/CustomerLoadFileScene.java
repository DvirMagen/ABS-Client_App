package javafx.scenes.loadFile;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static customer_utills.Constants.GSON_INSTANCE;

public class CustomerLoadFileScene implements Initializable {
    @FXML
    public TextField customer_file_path_textfield;
    @FXML
    public Button customer_analyze_btn_load_file_page;
    @FXML
    public Button customer_choose_file_btn_load_file_page;
    @FXML
    public Label customer_error_label_load_file_scene;
    @FXML
    public Button customer_enter_btn_load_file_scene;
    @FXML
    public Label welcome_client_username_load_file_scene;

    private File selectedFile =null;

    protected String xmlPath = "";

    protected boolean xmlPathValid = false;

    protected boolean is_valid = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcome_client_username_load_file_scene.setText("Welcome " + ABS_Client.current_customer.getName());
    }

    @FXML
    protected void onClickCustomerSelectFile(ActionEvent event){
        Stage mainStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        selectedFile = fileChooser.showOpenDialog(mainStage);

        customer_analyze_btn_load_file_page.setDisable(selectedFile == null);
        customer_file_path_textfield.setText(selectedFile != null? selectedFile.getAbsolutePath() : "");
    }

    @FXML
    protected void onClickAnalyzeFile(ActionEvent event) throws JAXBException {

        if (selectedFile != null) {

            customer_error_label_load_file_scene.setVisible(false);
            customer_enter_btn_load_file_scene.setDisable(false);
            System.out.println(selectedFile.getAbsolutePath());
            String xmlPath = selectedFile.getAbsolutePath();
            try{
                loadXmlPath(xmlPath);
            }catch (Exception e){
                is_valid = false;
                //update_user_text = e.getMessage();
                customer_error_label_load_file_scene.setText(e.getMessage());
                customer_error_label_load_file_scene.setVisible(true);
                return;
            }
            String finalUrl = HttpUrl
                    .parse(Constants.LOAD_XML)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .addQueryParameter("xmlPath", xmlPath)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure onClickAnalyzeFile");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println("onResponse");

                    String jsonArrayOfUsersNames = response.body().string();
                    System.out.println("Response: " + jsonArrayOfUsersNames);
                    if(response.code() ==  200) {

//                        Loan[] loansfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Loan[].class);
//                        for (Loan loan : loansfromJson) {
//                            System.out.println(loan);
//                        }
                        Platform.runLater(() -> onClickEnter(event));


                    } else if (response.code() == 400) {
                        HashMap<String,String> mapfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, HashMap.class);

                        for (Map.Entry<String, String> stringStringEntry : mapfromJson.entrySet()) {
                            System.out.println(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                        }
                    }
                }
            });


           // listener.ContextListener.engine.loadDataFromClientXmlFile(xml_file, xmlPath);
           //setViewByEngine(); //TODO remove false
        }

    }

    public void loadXmlPath(String xmlPath) throws Exception{
        if (!xmlPath.endsWith("xml")) {
            throw new Exception("Xml Path Is Does Not End As Needed");
        }
        this.xmlPath = xmlPath;
        this.xmlPathValid = true;
    }



    @FXML
    protected void onClickEnter(ActionEvent event){

        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getResource(Constants.CUSTOMER_DASHBOARD_PAGE_FXML_RESOURCE_LOCATION);
            loader.setLocation(url);
            Parent root = loader.load();

            //   CustomerScene scene2Controller = loader.getController();
            //  scene2Controller.setEngineAndSelectedCustomer(this.engine,this.selectedCustomer);
               Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(root));
              stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    public boolean isXmlPathValid(){
        return xmlPathValid;
    }




}

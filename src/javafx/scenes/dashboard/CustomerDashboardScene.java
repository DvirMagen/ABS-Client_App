package javafx.scenes.dashboard;

import com.google.gson.reflect.TypeToken;
import customer_utills.Constants;
import customer_utills.Loan;
import customer_utills.PaymentView;
import customer_utills.XmlPathIsDoesNotEndAsNeededException;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scenes.Popup.TaskBarScramble;
import javafx.scenes.newLoan.NewLoanWindow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static customer_utills.Constants.GSON_INSTANCE;
import static java.lang.Character.isDigit;
import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;

public class CustomerDashboardScene implements Initializable {
    @FXML
    public Label welcome_customer_label_dashboard;
    @FXML
    public Tab information_tab;
    @FXML
    public TableView customer_transaction_tableview;
    @FXML
    public TableColumn<String, String> customer_transaction_tablecolumn;
    @FXML
    public TableView customer_borrower_tableview;
    @FXML
    public TableColumn customer_borrower_id_column;
    @FXML
    public TableColumn customer_borrower_category_column;
    @FXML
    public TableColumn customer_borrower_payed_column;
    @FXML
    public TableColumn customer_borrower_status_column;
    @FXML
    public TableColumn customer_borrower_capital_column;
    @FXML
    public TableColumn customer_borrower_total_time_column;
    @FXML
    public TableView customer_lender_tableview;
    @FXML
    public TableColumn customer_lender_id_column;
    @FXML
    public TableColumn investment_owner_column;
    @FXML
    public TableColumn customer_lender_category_column;
    @FXML
    public TableColumn customer_lender_payed_column;
    @FXML
    public TableColumn customer_lender_status_column;
    @FXML
    public TableColumn customer_lender_capital_column;
    @FXML
    public TableColumn customer_lender_total_time_column;
    @FXML
    public Tab scramble_tab;
    @FXML
    public TextField amount_for_round_textfield;
    @FXML
    public TextField minimum_interest_rate_textfield;
    @FXML
    public TextField min_yaz_textfield;
    @FXML
    public ComboBox<String> categories_combobox;
    @FXML
    public TextField max_ownership_textfield;
    @FXML
    public TextField max_open_loans_textfield;
    @FXML
    public Label error_label;
    @FXML
    public Button filter_btn;
    @FXML
    public TableView scramble_tableview;
    @FXML
    public TableColumn scramble_loan_id_column;
    @FXML
    public TableColumn scramble_loan_owner_column;
    @FXML
    public TableColumn scramble_loan_category_column;
    @FXML
    public TableColumn scramble_loan_status_column;
    @FXML
    public TableColumn scramble_loan_capital_column;
    @FXML
    public TableColumn scramble_loan_total_time_column;
    @FXML
    public Tab payment_tab;
    @FXML
    public TableView payment_tableview;
    @FXML
    public TableColumn payment_loan_id_column;
    @FXML
    public TableColumn payment_owner_column;
    @FXML
    public TableColumn payment_interest_rate_column;
    @FXML
    public TableColumn payment_total_invest_column;
    @FXML
    public TableColumn payment_status_column;
    @FXML
    public TableColumn payment_upcoming_pay_column;
    @FXML
    public TableColumn payment_checkbox_column;
    @FXML
    public Button clear_all_btn;
    @FXML
    public Button pay_btn;
    @FXML
    public TextArea notifications_text_erea;
    @FXML
    public Label current_yaz_time_label_customer_dashboard;
    @FXML
    public Label error_label_load_file;
    @FXML
    public TextField file_path_textfield_load_file;
    @FXML
    public Button analyze_btn_load_file;
    @FXML
    public Button choose_file_btn_load_file;
    @FXML
    public TabPane tab_pane_id;
    @FXML
    public Label balance_customer_label_dashboard;
    @FXML
    public Tab loans_for_sale_tab;
    @FXML
    public TableView<Loan> loans_for_sale_tableview;
    @FXML
    public Button customer_logout_btn;
    @FXML
    public Button create_new_loan_btn;
    @FXML
    public TableColumn loans_for_sale_id_column;
    @FXML
    public TableColumn loans_for_sale_owner_column;
    @FXML
    public TableColumn loans_for_sale_category_column;
    @FXML
    public TableColumn loans_for_sale_capital_column;
    @FXML
    public TableColumn loans_for_sale_interist_rate_column;
    @FXML
    public Button withdraw_btn;
    @FXML
    public Button charge_btn;
    @FXML
    public Label dashboard_error_label;
    private File selectedFile =null;
    protected boolean is_valid = true;
    protected String xmlPath = "";
    protected ObservableList<Loan> filter_loans_observableList = FXCollections.observableArrayList();
    protected ObservableList<Loan> loans_as_lender_observableList = FXCollections.observableArrayList();
    private final ObservableList<Loan> customer_borrower_ObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> customer_movements_ObservableList = FXCollections.observableArrayList();
    private final ObservableList<PaymentView> loans_payment_observableList = FXCollections.observableArrayList();
    private final ObservableList<Loan> loans_for_sale_observableList = FXCollections.observableArrayList();
    protected Double amount_invest_for_round;
    protected String amount_invest_for_round_str = "";
    protected String min_interest_rate_str = "";
    protected String min_yaz_time_for_loan_str = "";
    protected String loan_category_str = "";
    protected String max_percent_own_str = "";
    protected String max_open_loans_str = "";
    protected boolean xmlPathValid = false;

    private HashMap<PaymentView,CheckBox> paymentViews = new HashMap<>();
    private TimerTask timeUnitRefresher;
    private TimerTask borrowerRefresher;
    private TimerTask lenderRefresher;
    private TimerTask paymentsRefresher;
    private TimerTask notificationsRefresher;
    private TimerTask balanceRefresher;
    private TimerTask transactionsRefresher;
    private TimerTask loansForSaleRefresher;
    private TimerTask  rewindRefresher;
    private Timer timeUnit_Timer;
    private Timer borrowerRefresh_Timer;
    private Timer lenderRefresh_Timer;
    private Timer paymentsRefresh_Timer;
    private Timer balanceRefresh_Timer;
    private Timer transactionsRefresh_Timer;
    private Timer loansForSaleRefresh_Timer;
    private Timer notificationsRefresh_Timer;
    private Timer rewindRefresher_Timer;
    protected Integer current_TimeUnit;
    protected BooleanProperty isRewindMode = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        isRewindMode.addListener((observable, oldValue, newValue) -> {
            System.out.println("isRewindMode update from: "+oldValue+" to: "+newValue);
            pay_btn.setDisable(newValue);
            clear_all_btn.setDisable(newValue);
            create_new_loan_btn.setDisable(newValue);
            analyze_btn_load_file.setDisable(newValue);
            choose_file_btn_load_file.setDisable(newValue);
            charge_btn.setDisable(newValue);
            withdraw_btn.setDisable(newValue);
        });


        this.customer_borrower_tableview.setItems(this.customer_borrower_ObservableList);
        this.customer_transaction_tableview.setItems(this.customer_movements_ObservableList);
        this.customer_lender_tableview.setItems(this.loans_as_lender_observableList);
        this.scramble_tableview.setItems(this.filter_loans_observableList);
        this.loans_for_sale_tableview.setItems(this.loans_for_sale_observableList);
        this.payment_tableview.setItems(this.loans_payment_observableList);

        this.customer_borrower_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.customer_borrower_category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        this.customer_borrower_payed_column.setCellValueFactory(new PropertyValueFactory<>("loanPayed"));
        this.customer_borrower_status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.customer_borrower_capital_column.setCellValueFactory(new PropertyValueFactory<>("capital"));
        this.customer_borrower_total_time_column.setCellValueFactory(new PropertyValueFactory<>("totalYazTime"));

        this.customer_lender_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.investment_owner_column.setCellValueFactory(new PropertyValueFactory<>("owner"));
        this.customer_lender_category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        this.customer_lender_payed_column.setCellValueFactory(new PropertyValueFactory<>("loanPayed"));
        this.customer_lender_status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.customer_lender_capital_column.setCellValueFactory(new PropertyValueFactory<>("capital"));
        this.customer_lender_total_time_column.setCellValueFactory(new PropertyValueFactory<>("totalYazTime"));

        this.scramble_loan_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.scramble_loan_owner_column.setCellValueFactory(new PropertyValueFactory<>("owner"));
        this.scramble_loan_category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        this.scramble_loan_status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.scramble_loan_capital_column.setCellValueFactory(new PropertyValueFactory<>("capital"));
        this.scramble_loan_total_time_column.setCellValueFactory(new PropertyValueFactory<>("totalYazTime"));

        this.payment_loan_id_column.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        this.payment_owner_column.setCellValueFactory(new PropertyValueFactory<>("lenderName"));
        this.payment_interest_rate_column.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        this.payment_status_column.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));
        this.payment_total_invest_column.setCellValueFactory(new PropertyValueFactory<>("totalInvest"));
        this.payment_upcoming_pay_column.setCellValueFactory(new PropertyValueFactory<>("upcomingPayment"));


        this.loans_for_sale_id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.loans_for_sale_owner_column.setCellValueFactory(new PropertyValueFactory<>("owner"));
        this.loans_for_sale_category_column.setCellValueFactory(new PropertyValueFactory<>("category"));
        this.loans_for_sale_capital_column.setCellValueFactory(new PropertyValueFactory<>("capital"));
        this.loans_for_sale_interist_rate_column.setCellValueFactory(new PropertyValueFactory<>("intristPerPayment"));

        this.balance_customer_label_dashboard.setText("Balance: " + ABS_Client.current_customer.getBalance());
        this. welcome_customer_label_dashboard.setText("Welcome " + ABS_Client.current_customer.getName()+ "!" );

//        updateLoansInfoView();
        updateCategoriesView();
        updateMovementView();
//        updateLenderInfoView();
//        String finalUrl = HttpUrl
//                .parse(Constants.YAZ_TIME)
//                .newBuilder()
//                .build()
//                .toString();
//        HttpClientUtil.runAsync(finalUrl, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                System.out.println("onFailure");
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                System.out.println("onResponse");
//                String jsonSt = response.body().string();
//                System.out.println("Response: " + jsonSt);
//                if(response.code() ==  200) {
//                    HashMap<String,Object> hashJson = GSON_INSTANCE.fromJson(jsonSt, HashMap.class);
//                    int yaz_time = (int) (Math.floor((Double) hashJson.get("yaz_time")));
//                    current_TimeUnit = yaz_time;
//                    Platform.runLater(() -> {
//                        System.out.println("Time Unit: "+ current_TimeUnit);
//                        current_yaz_time_label_customer_dashboard.setText("Time Unit: " + current_TimeUnit.toString());
//                    });
////                        }
//
//                } /*else {
//
//                }*/
//            }
//        });


        tab_pane_id.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
            error_label_load_file.setText("");
            error_label_load_file.setVisible(false);
            if (newValue.getId().equals(information_tab.getId())) {
                // reload info tab
               // showLoans();
                updateLoansInfoView();
                updateLenderInfoView();
                updateCategoriesView();

            } else if (newValue.getId().equals(scramble_tab.getId())) {
                // reload scramble tab
                //clearScrambleTab();
                //updateScrambleTableView();


            } else if (newValue.getId().equals(payment_tab.getId())) {
                // reload payment tab
                //paymentViews.clear();
               // updateViewPaymentTableView();
            }
        }));
        this.amount_for_round_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d+\\.\\d*|\\d+")) {
                amount_for_round_textfield.setText(oldValue+"");
            }else if (!newValue.isEmpty() && Double.parseDouble(newValue) > ABS_Client.current_customer.getBalance()){
                amount_for_round_textfield.setText(oldValue+"");
            }
        });
        this.minimum_interest_rate_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d+\\.\\d*|\\d+")) {
                minimum_interest_rate_textfield.setText(oldValue+"");
            }
            else if(!newValue.isEmpty() && Double.parseDouble(newValue) > 100)
                minimum_interest_rate_textfield.setText(oldValue+"");
        });
        this.max_ownership_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d+\\.\\d*|\\d+")) {
                max_ownership_textfield.setText(oldValue+"");
            }
            else if (!newValue.isEmpty() && Double.parseDouble(newValue) > 100){
                max_ownership_textfield.setText(oldValue+"");
            }
        });
        this.min_yaz_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                min_yaz_textfield.setText(oldValue+"");
            }
        });
        this.max_open_loans_textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                max_open_loans_textfield.setText(oldValue+"");
            }
        });
        this.scramble_tableview.getColumns().add(0,new TableRowExpanderColumn<>(this::createScrambleEditor));
        {
            ObservableList<TableColumn<Loan, ?>> columns = scramble_tableview.getColumns();
            for (TableColumn<Loan, ?> column : columns) {
                column.setMaxWidth(1f * Integer.MAX_VALUE * (96 / (columns.size() - 1))); // 30% width
            }
            columns.get(0).setMaxWidth(1f * Integer.MAX_VALUE * 4); // 30% width
        }

        this.customer_borrower_tableview.getColumns().add(0,new TableRowExpanderColumn<>(this::createBorrowerInfoEditor));
        {
            ObservableList<TableColumn<Loan, ?>> columns = customer_borrower_tableview.getColumns();
            for (TableColumn<Loan, ?> column : columns) {
                column.setMaxWidth(1f * Integer.MAX_VALUE * (96 / (columns.size() - 1))); // 30% width
            }
            columns.get(0).setMaxWidth(1f * Integer.MAX_VALUE * 4); // 30% width
        }
        this.customer_lender_tableview.getColumns().add(0,new TableRowExpanderColumn<>(this::createLenderInfoEditor));
        {
            ObservableList<TableColumn<Loan, ?>> columns = customer_lender_tableview.getColumns();
            for (TableColumn<Loan, ?> column : columns) {
                column.setMaxWidth(1f * Integer.MAX_VALUE * (96 / (columns.size() - 1))); // 30% width
            }
            columns.get(0).setMaxWidth(1f * Integer.MAX_VALUE * 4); // 30% width
        }
        this.loans_for_sale_tableview.getColumns().add(0, new TableRowExpanderColumn<>(this::createLoansForSaleEditor));
        {
            ObservableList<TableColumn<Loan, ?>> columns = loans_for_sale_tableview.getColumns();
            for (TableColumn<Loan, ?> column : columns) {
                column.setMaxWidth(1f * Integer.MAX_VALUE * (96 / (columns.size() - 1))); // 30% width
            }
            columns.get(0).setMaxWidth(1f * Integer.MAX_VALUE * 4); // 30% width
        }
        payment_tableview.getColumns().add(0,new TableRowExpanderColumn<>(this::createPaymentEditor));
        {
            ObservableList<TableColumn<PaymentView, ?>> columns = payment_tableview.getColumns();
            for (TableColumn<PaymentView, ?> column : columns) {
                column.setMaxWidth(1f * Integer.MAX_VALUE * (96 / (columns.size() - 1))); // 30% width
            }
            columns.get(0).setMaxWidth(1f * Integer.MAX_VALUE * 2); // 30% width
            columns.get(7).setMaxWidth(1f * Integer.MAX_VALUE * 2); // 30% width
        }


        this.customer_transaction_tablecolumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        this.addCheckBoxToTable(this.payment_checkbox_column, new BiConsumer<PaymentView, CheckBox>() {
            @Override
            public void accept(PaymentView paymentView, CheckBox checkBox) {
                //System.out.println(paymentView);
                //System.out.println(checkBox.isSelected());

                if (checkBox.isSelected()){
                    paymentViews.put(paymentView,checkBox);
                }else{
                    paymentViews.remove(paymentView);
                }

                pay_btn.setDisable(paymentViews.size()==0);
            }
        });
        if(ABS_Client.current_customer.getName() != null) {
            startNotificationsRefresher();
            startPaymentsRefresher();
            startBorrowerRefresher();
            startLenderRefresher();
            startYazRefresher();
            startBalanceRefresher();
            startTransacionsRefresher();
            startLoansForSaleRefresher();
            startRewindRefresher();
        }
    }

    private GridPane createBorrowerInfoEditor(TableRowExpanderColumn.TableRowDataFeatures<Loan> param) {
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        Loan loan = param.getValue();
        Button finish_loan_button = new Button("Finish Loan");
        Button sale_loan_button = new Button("Sale Loan");
        if (!(loan.getStatus().equalsIgnoreCase("active") || loan.getStatus().equalsIgnoreCase("risk"))){
            //finish_loan_button.setVisible(false);
            finish_loan_button.setDisable(true);
        }
        else
            finish_loan_button.setDisable(isRewindMode.getValue());
        if(!loan.getStatus().equalsIgnoreCase("active") || loan.isOnSale())
            sale_loan_button.setDisable(true);
        else
            sale_loan_button.setDisable(isRewindMode.getValue());
        finish_loan_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                finishLoan(loan.getId());
                StringBuilder notifictionMessage = new StringBuilder();
                notifictionMessage.append("Current Time Unit: ");
                notifictionMessage.append("\n");
                notifictionMessage.append("Loan Id: ");
                notifictionMessage.append(loan.getId());
                notifictionMessage.append("Loan Status: ");
                notifictionMessage.append(loan.getStatus());
                notifictionMessage.append("\n");
                notifictionMessage.append("------------------------------------------\n");
                addNotification(ABS_Client.current_customer.getName(), notifictionMessage.toString());
            }
        });
        isRewindMode.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!loan.getStatus().equalsIgnoreCase("active") || loan.isOnSale())
                    sale_loan_button.setDisable(true);
                else
                    sale_loan_button.setDisable(newValue);
                if (!(loan.getStatus().equalsIgnoreCase("active") || loan.getStatus().equalsIgnoreCase("risk"))){
                    //finish_loan_button.setVisible(false);
                    finish_loan_button.setDisable(true);
                }
                else
                    finish_loan_button.setDisable(newValue);
            }
        });

        sale_loan_button.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if (!isRewindMode.getValue())
                    putLoanOnSale(loan.getId());
            }

        });
        int i=0;
        editor.addRow(i++, new Label("\t"), new Label("Loan is Active " + loan.getYazTimeActive() + " Yaz"));
        editor.addRow(i++, new Label("\t"), new Label("Loan Left " + loan.getYazTimeLeft() + " Yaz"));
        editor.addRow(i++, new Label("\t"), new Label("Loan Last Time Payment  " + loan.getLastYazTimePayment()+ " Yaz"));
//        editor.addRow(i++, new Label("\t"), new Label("Loan Upcoming Payment  " + loan.getTheUpcomingPayment()));
        editor.addRow(i++, new Label("\t"), new Label("Loan Left  " + loan.getLoanLeftToPay()));
        if(!loan.getLenders().isEmpty()) {
            TableView<Loan.Lender> lendersTableView = new TableView<>();

            TableColumn<Loan.Lender, String> lenderNameTableColumn = new TableColumn<>("Name");
            TableColumn<Loan.Lender, Integer> lenderInvestTableColumn = new TableColumn<>("Invest");
            lenderNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            lenderInvestTableColumn.setCellValueFactory(new PropertyValueFactory<>("invest"));
            lendersTableView.getColumns().addAll(lenderNameTableColumn, lenderInvestTableColumn);
            GridPane.setHgrow(lendersTableView, Priority.ALWAYS);
            lendersTableView.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
            ObservableList<Loan.Lender> lenders = FXCollections.observableArrayList();
            lenders.addAll(loan.getLenders().keySet());
            lendersTableView.setItems(lenders);
            editor.addRow(i++, new Label("\t") ,new Label("Lenders: "));
            editor.addRow(i++, new Label("\t"), lendersTableView);
        }else{
            editor.addRow(i++, new Label("\t"),new Label("Lenders:  No Lenders"));
        }
        editor.addRow(i++, new Label("\n"));
        editor.addRow(i++, finish_loan_button,new Label(" "), sale_loan_button);
        return editor;
    }

    private GridPane createLenderInfoEditor(TableRowExpanderColumn.TableRowDataFeatures<Loan> param){
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        Loan loan = param.getValue();
        int i=0;
        editor.addRow(i++, new Label("\t"), new Label("Loan is Active " + loan.getYazTimeActive() + " Yaz"));
        editor.addRow(i++, new Label("\t"), new Label("Loan Left " + loan.getYazTimeLeft() + " Yaz"));
        editor.addRow(i++, new Label("\t"), new Label("Loan Last Time Payment  " + loan.getLastYazTimePayment()+ " Yaz"));
//        editor.addRow(i++, new Label("\t"), new Label("Loan Upcoming Payment  " + loan.getTheUpcomingPayment()));
        editor.addRow(i++, new Label("\t"), new Label("Loan Left  " + loan.getLoanLeftToPay()));
        if(!loan.getLenders().isEmpty()) {
            TableView<Loan.Lender> lendersTableView = new TableView<>();

            TableColumn<Loan.Lender, String> lenderNameTableColumn = new TableColumn<>("Name");
            TableColumn<Loan.Lender, Integer> lenderInvestTableColumn = new TableColumn<>("Invest");
            lenderNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            lenderInvestTableColumn.setCellValueFactory(new PropertyValueFactory<>("invest"));
            lendersTableView.getColumns().addAll(lenderNameTableColumn, lenderInvestTableColumn);
            GridPane.setHgrow(lendersTableView, Priority.ALWAYS);
            lendersTableView.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
            ObservableList<Loan.Lender> lenders = FXCollections.observableArrayList();
            lenders.addAll(loan.getLenders().keySet());
            lendersTableView.setItems(lenders);
            editor.addRow(i++, new Label("\t") ,new Label("Lenders: "));
            editor.addRow(i++, new Label("\t"), lendersTableView);
        }else{
            editor.addRow(i++, new Label("\t"),new Label("Lenders:  No Lenders"));
        }
        editor.addRow(i++, new Label("\n"));
        return editor;
    }
    private GridPane createScrambleEditor(TableRowExpanderColumn.TableRowDataFeatures<Loan> param) {
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        Loan loan = param.getValue();
        Button button = new Button("Apply");
        TextField textField = new TextField();
        button.setDisable(isRewindMode.getValue());
        isRewindMode.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                button.setDisable(isRewindMode.getValue() || textField.getText().isEmpty());
            }
        });
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d+\\.\\d*|\\d+")) {
                textField.setText(oldValue+"");
            }else if (!newValue.isEmpty() && (Double.parseDouble(newValue) > amount_invest_for_round ||
                    Double.parseDouble(newValue) > loan.leftToFund() ||
                    Double.parseDouble(newValue) > ABS_Client.current_customer.getBalance())){
                textField.setText(oldValue+"");
            }
            button.setDisable(isRewindMode.getValue());
        });

        button.setOnMouseClicked(event -> {
            if (textField.getText().isEmpty()) return;
            param.setExpanded(false);
            Double v = Double.parseDouble(textField.getText());
            if (v == 0) return;
            textField.setText("");
            amount_invest_for_round = amount_invest_for_round - v;
            amount_for_round_textfield.setText(amount_invest_for_round.toString());
            //updateBalanceView("withdraw" ,v.toString());
            investMoneyInLoan(v.toString(), loan.getId());
            updateScrambleView();
            updateLenderInfoView();
            updateBalanceView("withdraw", v.toString());
            //showLoans();
//            // TODO update user balance
        });

        editor.addRow(1, new Label("\tLoan is Active " + loan.getYazTimeActive() + " Yaz"));
        editor.addRow(2, new Label("\tInterest Per Payment " + loan.getIntristPerPayment() + "%"));
        editor.addRow(3, new Label("\tLoan Payment is Every " + loan.getPaysEveryYaz() + " Yaz"));
        editor.addRow(4, new Label("\tLoan Left To Fund " + loan.leftToFund() + " "));
        editor.addRow(5,new Label("\tEnter Investment Amount: "),textField , new Label("  "),button);
        return editor;
    }

    private GridPane createPaymentEditor(TableRowExpanderColumn.TableRowDataFeatures<PaymentView> param) {
        PaymentView paymentView = param.getValue();
        param.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!paymentView.getLoanStatus().equals("risk")) {
                    param.setExpanded(false);
                }
            }
        });
        if (!paymentView.getLoanStatus().equals("risk")) {
            param.setExpanded(false);
        }
        GridPane gridPane=new GridPane();
        TextField textField = new TextField();
        Button button=new Button("Submit Custom Pay");
        gridPane.addRow(0,new Label("\t"),textField);
        gridPane.addRow(1,new Label("\t"),button);

        if (!paymentView.getLoanStatus().equals("risk")) {
            textField.setDisable(true);
            textField.setVisible(false);
            button.setDisable(isRewindMode.getValue());
            button.setVisible(false);
        }

        isRewindMode.addListener((observable, oldValue, newValue) ->
                button.setDisable(newValue || !paymentView.getLoanStatus().equals("risk")));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d+\\.\\d*|\\d+")) {
                textField.setText(oldValue+"");
            }else if (!newValue.isEmpty() && Double.parseDouble(newValue) > ABS_Client.current_customer.getBalance() &&
                    Double.parseDouble(newValue) > paymentView.getUpcomingPayment()){
                textField.setText(oldValue+"");
            }
        });
        button.setOnMouseClicked(event -> {

            String text = textField.getText();
            if (text.isEmpty()) return;
            if (text.charAt(text.length()-1)=='.'){
                text+='0';
            }
            double aDouble = Double.parseDouble(text);
            if (aDouble <= 0) return;

            payPayment(paymentView.getLoanId(),paymentView.getLenderName(),text);
            StringBuilder notifictionMessage = new StringBuilder();
            if (paymentViews.containsKey(paymentView)) {
                CheckBox checkBox = paymentViews.get(paymentView);
                checkBox.setSelected(false);
                paymentViews.remove(paymentView);
            }

            param.setExpanded(false);
            textField.setText("");
            notifictionMessage.append("Current Time Unit: ");
            notifictionMessage.append(current_TimeUnit.toString());
            notifictionMessage.append("\n");
            notifictionMessage.append("Loan Id: ");
            notifictionMessage.append(paymentView.getLoanId());
            notifictionMessage.append("\n");
            notifictionMessage.append("Lender Name: ");
            notifictionMessage.append(paymentView.getLenderName());
            notifictionMessage.append("\n");
            notifictionMessage.append("Loan Status: ");
            notifictionMessage.append(paymentView.getLoanStatus());
            notifictionMessage.append("\n");
            notifictionMessage.append("Total Invest: ");
            notifictionMessage.append(paymentView.getTotalInvest());
            notifictionMessage.append("\n");
            notifictionMessage.append("Payment is: ");
            notifictionMessage.append(aDouble);
            notifictionMessage.append("\n");
            notifictionMessage.append("---------------------------------\n");
           addNotification(ABS_Client.current_customer.getName(), notifictionMessage.toString());
            StringBuilder notifictionMessage2 = new StringBuilder();
            notifictionMessage2.append("Current Time Unit: ");
            notifictionMessage2.append(current_TimeUnit.toString());
            notifictionMessage2.append("\n");
            notifictionMessage2.append("Loan Id: ");
            notifictionMessage2.append(paymentView.getLoanId());
            notifictionMessage2.append("\n");
            notifictionMessage2.append("Lender Name: ");
            notifictionMessage2.append(paymentView.getLenderName());
            notifictionMessage2.append("\n");
            notifictionMessage2.append("Loan Status: ");
            notifictionMessage2.append(paymentView.getLoanStatus());
            notifictionMessage2.append("\n");
            notifictionMessage2.append("Total Invest: ");
            notifictionMessage2.append(paymentView.getTotalInvest());
            notifictionMessage2.append("\n");
            notifictionMessage2.append("Totally Left To Pay: ");
            notifictionMessage2.append(paymentView.getUpcomingPayment()-aDouble);
            notifictionMessage2.append("\n");
            notifictionMessage2.append("---------------------------------\n");
           addNotification(ABS_Client.current_customer.getName(), notifictionMessage2.toString());
            pay_btn.setDisable(true);

        });

        return gridPane;
    }
    private GridPane createLoansForSaleEditor(TableRowExpanderColumn.TableRowDataFeatures<Loan> param)
    {
        GridPane editor = new GridPane();
        editor.setPadding(new Insets(10));
        Loan loan = param.getValue();
        Button button = new Button("Buy Loan");
        isRewindMode.addListener((observable, oldValue, newValue) -> button.setDisable(newValue));
        button.setOnMouseClicked(event -> {
            param.setExpanded(false);
            buyLoan(param.getValue().getId(), ABS_Client.current_customer.getName(), param.getValue().getOwner());
        });

        editor.addRow(1, new Label("\tLoan is Active " + loan.getYazTimeActive() + " Yaz"));
        editor.addRow(2, new Label("\tLoan Payment is Every " + loan.getPaysEveryYaz() + " Yaz"));
        editor.addRow(3, new Label("\tLoan Payed:  " + loan.getLoanPayed() + "\n"));
        editor.addRow(4, new Label("\t", button));


        return editor;
    }
    protected void investMoneyInLoan(String investMoney, String loanId)
    {
        String finalUrl = HttpUrl
                .parse(Constants.INVEST_LOAN)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .addQueryParameter("invested_amount", investMoney)
                .addQueryParameter("loan_id", loanId)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse investMoneyInLoan");
                String jsonSt = response.body().string();
                System.out.println("Response investMoneyInLoan: " + jsonSt);
                if(response.code() ==  200) {
                    Type listType = new TypeToken<List<Loan>>(){}.getType();

                    List<Loan> loans = Constants.GSON_INSTANCE.fromJson(jsonSt, listType);
                    Platform.runLater(() -> {
                        System.out.println(loans);
                        for (Loan loan : loans) {
                            System.out.println(loan);
                        }
                        loans_as_lender_observableList.clear();
                        loans_as_lender_observableList.setAll(loans);
                        customer_lender_tableview.refresh();


                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });

    }

    protected void buyLoan(String loan_id, String newOwner, String oldOwner)
    {
        String finalUrl = HttpUrl
                .parse(Constants.BUY_LOAN)
                .newBuilder()
                .addQueryParameter("loan_id", loan_id)
                .addQueryParameter("new_owner", newOwner)
                .addQueryParameter("old_owner", oldOwner)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse buyLoan");
                String jsonSt = response.body().string();
                System.out.println("Response buyLoan: " + jsonSt);
                if(response.code() ==  200) {
                }
                else{
                    HashMap<String,String> mapfromJson = GSON_INSTANCE.fromJson(jsonSt, HashMap.class);

                    for (Map.Entry<String, String> stringStringEntry : mapfromJson.entrySet()) {
                        System.out.println(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error!");
                            alert.setHeaderText(stringStringEntry.getKey());
                            alert.setContentText(stringStringEntry.getValue());
                            alert.showAndWait();

                        });
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });


    }
    public void loadXmlPath(String xmlPath) throws XmlPathIsDoesNotEndAsNeededException {
        if (!xmlPath.endsWith("xml")) {
            throw new XmlPathIsDoesNotEndAsNeededException();
        }
        this.xmlPath = xmlPath;
        this.xmlPathValid = true;
    }

    /*Update Methods*/
    protected void updateBalanceView(String action, String amount)
    {
        String finalUrl = HttpUrl
                .parse(Constants.UPDATE_BALANCE)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .addQueryParameter("action", action)
                .addQueryParameter("amount", amount)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure updateBalanceView");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse");
                Platform.runLater(() -> {
                    analyze_btn_load_file.setDisable(true);
                    file_path_textfield_load_file.setText("");
                });
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response updateBalanceView: " + jsonArrayOfUsersNames);
                if (response.code() == 200) {
                    HashMap<String, Object> mapfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, HashMap.class);
                    ABS_Client.current_customer.setBalance((double) mapfromJson.get("balance"));
                    Platform.runLater(() -> {
                        tab_pane_id.getSelectionModel().select(information_tab);
                        balance_customer_label_dashboard.setText("Balance: " + ABS_Client.current_customer.getBalance());
                        updateMovementView();
                    });
                } else if (response.code() == 400) {
                    HashMap<String, String> mapfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, HashMap.class);

                    for (Map.Entry<String, String> stringStringEntry : mapfromJson.entrySet()) {
                        System.out.println(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                        Platform.runLater(() -> {
                            error_label_load_file.setText(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                            error_label_load_file.setVisible(true);
                            error_label_load_file.setTextFill(Color.RED);
                            error_label_load_file.setMinWidth(50);
                            error_label_load_file.setMinHeight(50);
                        });
                    }
                }
            }
        });
    }
    private void updateYazTimeLabel(String json_timeUnit){
        Map<String,Object> map = GSON_INSTANCE.fromJson(json_timeUnit, Map.class);
        Integer yaz_time = ((Double) map.get("yaz_time")).intValue();

        if(!yaz_time.equals(current_TimeUnit)) {
            current_TimeUnit = yaz_time;
            current_yaz_time_label_customer_dashboard.setText("Time Unit: " + current_TimeUnit.toString());
        }
    }

    private void updateNotificationsView(String json_notifications) {
        Map<String,Object> map = GSON_INSTANCE.fromJson(json_notifications, Map.class);
        String notifications = ((String) map.get("notifications")).toString();
        if(!notifications.equalsIgnoreCase(notifications_text_erea.getText())) {
            Platform.runLater(() -> {
                this.notifications_text_erea.setText(notifications);
                this.notifications_text_erea.setScrollTop(Double.MAX_VALUE);
            });
        }
    }

    private void updatePaymentsView(PaymentView[] paymentViews_arr)
    {
        List<PaymentView> paymentViews_list = Arrays.asList(paymentViews_arr);
        paymentViews_list.sort(Comparator.comparing(PaymentView::getLoanId));
        if (!loans_payment_observableList.equals(paymentViews_list)) {
            System.out.println("updatePaymentsView");
            System.out.println(paymentViews_list);
            for (PaymentView paymentView : paymentViews_list) {
                System.out.println(paymentView);
            }
            loans_payment_observableList.clear();
            loans_payment_observableList.setAll(paymentViews_list);
            payment_tableview.refresh();
            this.payment_checkbox_column.setVisible(!loans_payment_observableList.isEmpty());
        }
    }
    private void updateLoansInfoView(){

        String finalUrl = HttpUrl
                .parse(Constants.LOAN)
                .newBuilder()
                .addQueryParameter("owner", ABS_Client.current_customer.getName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure updateLoansInfoView");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse updateLoansInfoView");
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response updateLoansInfoView: " + jsonArrayOfUsersNames);
                Loan[] loansfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Loan[].class);
                for (Loan loan : loansfromJson) {
                    System.out.println(loan);
                }
                Platform.runLater(() -> {
                    customer_borrower_ObservableList.clear();
                    customer_borrower_ObservableList.setAll(loansfromJson);
                    customer_borrower_tableview.refresh();
                });
            }
        });
        this.customer_lender_tableview.refresh();
    }
    private void updateLenderInfoView()
    {
        String finalUrl = HttpUrl
                .parse(Constants.INVESTED_LOANS)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure updateLenderInfoView");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                System.out.println("onResponse updateLenderInfoView");
                String jsonArrayOfUsersNames = response.body().string();
//                System.out.println("Response updateLenderInfoView: " + jsonArrayOfUsersNames);
                Loan[] loansfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Loan[].class);
//                for (Loan loan : loansfromJson) {
//                    System.out.println(loan);
//                }
                Platform.runLater(() -> {
                    loans_as_lender_observableList.clear();
                    loans_as_lender_observableList.setAll(loansfromJson);
                    customer_lender_tableview.refresh();
                });
            }
        });
        this.customer_lender_tableview.refresh();
    }

    private void updateBorrowerView(Loan[] loans){
        List<Loan> loans_list = Arrays.stream(loans)
                .sorted(Comparator.comparing(Loan::getId))
                .collect(Collectors.toList());
        if (!customer_borrower_ObservableList.equals(loans_list)) {
            System.out.println("updateBorrowerView");
            System.out.println(loans_list);
            for (Loan loan : loans_list) {
                System.out.println(loan);
            }
            customer_borrower_ObservableList.clear();
            customer_borrower_ObservableList.setAll(loans_list);
            customer_borrower_tableview.refresh();
        }
    }

    private void updateLender(Loan[] loans)
    {
        List<Loan> loans_list = Arrays.stream(loans)
                .sorted(Comparator.comparing(Loan::getId))
                .collect(Collectors.toList());
        if(!loans_as_lender_observableList.equals(loans_list)) {
            System.out.println("updateLender");
            System.out.println(loans_list);
            for (Loan loan : loans_list) {
                System.out.println(loan);
            }
            loans_as_lender_observableList.clear();
            loans_as_lender_observableList.setAll(loans_list);
            customer_lender_tableview.refresh();
        }
    }

    private void updateBalanceRefresh(Double balance)
    {
        if(!balance_customer_label_dashboard.getText().equalsIgnoreCase("Balance: " + balance))
        {
            System.out.println("updateBalanceRefresh: "+balance);
            ABS_Client.current_customer.setBalance(balance);
            balance_customer_label_dashboard.setText("Balance: " + balance);
        }
    }

    private void updateTransactionsRefresh(List<String> transactionsList)
    {
        if (!customer_movements_ObservableList.equals(transactionsList)) {
            System.out.println("updateTransactionsRefresh");
            System.out.println(transactionsList);
            customer_movements_ObservableList.clear();
            customer_movements_ObservableList.setAll(transactionsList);
            customer_transaction_tableview.refresh();
        }
    }

    private  void updateLoansForSaleView(Loan[] loans)
    {
        List<Loan> loans_list = Arrays.stream(loans)
                .sorted(Comparator.comparing(Loan::getId))
                .collect(Collectors.toList());
        if (!loans_for_sale_observableList.equals(loans_list)) {
            System.out.println("updateLoansForSaleView");
            System.out.println(loans_list);
            for (Loan loan : loans_list) {
                System.out.println(loan);
            }
            loans_for_sale_observableList.clear();
            loans_for_sale_observableList.setAll(loans_list);
            loans_for_sale_tableview.refresh();
        }
    }

    private void updateRewindMode(Boolean isRewindMode)
    {
        if(this.isRewindMode.getValue() != isRewindMode){
            System.out.println("updateRewindMode: "+isRewindMode);
            this.isRewindMode.set(isRewindMode);
        }

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
                System.out.println("onFailure updateCategoriesView");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse Categories");
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response Categories: " + jsonArrayOfUsersNames);
                String[] categoriesfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                for (String category : categoriesfromJson) {
                    System.out.println(category);
                }
                Platform.runLater(() -> {
                    categories_combobox.getItems().clear();
                    categories_combobox.getItems().addAll(categoriesfromJson);
                });
            }
        });


    }
    private void updateMovementView(){
        System.out.println("updateMovementView");
        String finalUrl = HttpUrl
                .parse(Constants.MOVEMENTS)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure updateMovementView");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse Movements");
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response Movements: " + jsonArrayOfUsersNames);
                Map<String,Object> map = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Map.class);
                List<String> movements = (List<String>) map.getOrDefault("movements",new ArrayList<String>());
                System.out.println("movements: " + movements.toString());
                Platform.runLater(() -> {
                    customer_movements_ObservableList.clear();
                    customer_movements_ObservableList.setAll(movements);
                    customer_transaction_tableview.refresh();
                });
            }
        });
    }
    private void updateScrambleView()
    {
        HttpUrl.Builder builder = HttpUrl
                .parse(Constants.UPDATE_SCRAMBLE)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .addQueryParameter("amount_invest", amount_invest_for_round_str);
        if (!min_interest_rate_str.isEmpty())
            builder = builder.addQueryParameter("min_interest_rate",min_interest_rate_str);
        if (!min_yaz_time_for_loan_str.isEmpty())
            builder = builder.addQueryParameter("min_yaz_for_loan",min_yaz_time_for_loan_str);
        if (loan_category_str != null && !loan_category_str.isEmpty()) {
            System.out.println("123456 add to builder category named: "+loan_category_str);
            builder = builder.addQueryParameter("loan_category", loan_category_str);
        }
        if (!max_percent_own_str.isEmpty())
            builder = builder.addQueryParameter("max_percent_ownership",max_percent_own_str);
        if (!max_open_loans_str.isEmpty())
            builder = builder.addQueryParameter("max_open_loans",max_open_loans_str);


        String finalUrl =
                builder
                        .build()
                        .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse updateFilterView");
                String jsonSt = response.body().string();
                System.out.println("Response updateFilterView: " + jsonSt);
                if(response.code() ==  200) {
                    Type listType = new TypeToken<List<Loan>>(){}.getType();

                    List<Loan> loans = Constants.GSON_INSTANCE.fromJson(jsonSt, listType);
                    Platform.runLater(() -> {
                        System.out.println(loans);
                        for (Loan loan : loans) {
                            System.out.println(loan);
                        }
                        filter_loans_observableList.clear();
                        filter_loans_observableList.setAll(loans);
                        scramble_tableview.refresh();
                        amount_invest_for_round = Double.parseDouble(amount_invest_for_round_str);

                    });
                }

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    /*OnClick Methods*/
    @FXML
    protected void onClickCustomerSelectFile(ActionEvent event){

        if (isRewindMode.getValue()){
            return;
        }

        Stage mainStage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        selectedFile = fileChooser.showOpenDialog(mainStage);

        analyze_btn_load_file.setDisable(selectedFile == null);
        file_path_textfield_load_file.setText(selectedFile != null? selectedFile.getAbsolutePath() : "");
    }
    @FXML
    protected void onClickAnalyzeFile(ActionEvent event) {

        if (isRewindMode.getValue()){
            return;
        }

        if (selectedFile != null) {

            error_label_load_file.setVisible(false);
            System.out.println(selectedFile.getAbsolutePath());
            String xmlPath = selectedFile.getAbsolutePath();
            try{
                loadXmlPath(xmlPath);
            }catch (XmlPathIsDoesNotEndAsNeededException e){
                is_valid = false;
                //update_user_text = e.getMessage();
                error_label_load_file.setText(e.getMessage());
                error_label_load_file.setVisible(true);
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
                    System.out.println("onResponse onClickAnalyzeFile");
                    analyze_btn_load_file.setDisable(true);
                    file_path_textfield_load_file.setText("");
                    String jsonArrayOfUsersNames = response.body().string();
                    System.out.println("Response onClickAnalyzeFile: " + jsonArrayOfUsersNames);
                    if(response.code() ==  200) {

//                        Loan[] loansfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Loan[].class);
//                        for (Loan loan : loansfromJson) {
//                            System.out.println(loan);
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource(Constants.POPUP_LOADING_TASKBAR_WINDOW_PATH));
                                Parent root = loader.load();
                                TaskBarScramble taskBarScramble = loader.getController();

                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setAlwaysOnTop(true);
                                stage.setResizable(false);
                                stage.show();
                                taskBarScramble.showProgress(xmlPathValid, new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) {
                                        tab_pane_id.getSelectionModel().select(information_tab);
                                        error_label_load_file.setText("File Loaded Successfully!");
                                        updateCategoriesView();
                                        error_label_load_file.setVisible(true);
                                        error_label_load_file.setTextFill(Color.GREEN);
                                        error_label_load_file.setMinWidth(100);
                                        error_label_load_file.setMinHeight(100);
                                    }
                                });
                            } catch (IOException ex) {
                                System.err.println(ex);
                            }

                        });

//                        }

                    } else if (response.code() == 400) {
                        HashMap<String,String> mapfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, HashMap.class);

                        for (Map.Entry<String, String> stringStringEntry : mapfromJson.entrySet()) {
                            System.out.println(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                            Platform.runLater(() -> {
                                error_label_load_file.setText(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                                error_label_load_file.setVisible(true);
                                error_label_load_file.setTextFill(Color.RED);
                                error_label_load_file.setMinWidth(50);
                                error_label_load_file.setMinHeight(50);
                            });
                        }
                    }
                }
            });

        }

    }
    @FXML
    void logoutClicked(ActionEvent event) {
       // chatCommands.updateHttpLine(Constants.LOGOUT);
        HttpClientUtil.runAsync(Constants.LOGOUT, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               // chatCommands.updateHttpLine("Logout request ended with failure...:(");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() || response.isRedirect()) {
                    HttpClientUtil.removeCookiesOf(customer_utills.Constants.BASE_DOMAIN);
                    Platform.runLater(() -> {
                        try {
                            ABS_Client.current_customer.setName(null);
                            FXMLLoader loader = new FXMLLoader();
                            URL url = getClass().getResource(customer_utills.Constants.CUSTOMER_LOGIN_PAGE_FXML_RESOURCE_LOCATION);
                            loader.setLocation(url);
                            Parent root = loader.load();
                            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                            //adminLoadFileScene.updateWelcomeAdminLabel(username_admin_login.getText());
                        } catch (IOException ex) {
                            System.err.println(ex);
                        }

                    });
                }
            }
        });

    }
    @FXML
    protected void onClickChargeBtn(final ActionEvent a_event)
    {
        if (isRewindMode.getValue()){
            return;
        }
        TextInputDialog td = new TextInputDialog("");
        ImageView icon = new ImageView("javafx/resources/images/charge_icon.png");
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        td.setGraphic(icon);
        td.setHeight(250);
        td.setWidth(250);
        td.setTitle("Charge");
        Stage stage = (Stage) td.getDialogPane().getScene().getWindow();
        Image mini_icon = new Image("javafx/resources/images/charge_mini_icon.png");
        stage.getIcons().add(mini_icon);
        td.setHeaderText("Charge: ");
        Optional<String> s = td.showAndWait();
        s.ifPresent(new Consumer<String>() {
            @Override
            public void accept(String s) {
                //TODO Check string validation
                System.out.println("chargeMoney = "+s +" for customer = "+ ABS_Client.current_customer.getName());
                if(!isDoubleInput(s))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error!!!");
                    alert.setContentText("Ooops, You don't konw how to eneter number?!?!?!!");

                    alert.showAndWait();
                    return;
                }
                System.out.println("sending request");
                updateBalanceView("charge", s);
            }
        });

    }
    @FXML
    protected void onClickWithdrawBtn(final ActionEvent a_event) {
        if (isRewindMode.getValue()){
            return;
        }
        TextInputDialog td = new TextInputDialog("");
        ImageView icon = new ImageView("javafx/resources/images/withdraw_icon.png");
        td.setTitle("Withdraw");
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        td.setGraphic(icon);
        td.setHeight(250);
        td.setWidth(250);
        Stage stage = (Stage) td.getDialogPane().getScene().getWindow();
        Image mini_icon = new Image("javafx/resources/images/withdraw_mini_icon.png");
        stage.getIcons().add(mini_icon);
        td.setHeaderText("Withdraw: ");
        Optional<String> s = td.showAndWait();
        s.ifPresent(new Consumer<String>() {
            @Override
            public void accept(String s) {
                if (!isDoubleInput(s)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error!!!");
                    alert.setContentText("Ooops, You don't know how to enter number!");

                    alert.showAndWait();
                    return;
                }

                //TODO Check string validation
                double money_withdraw = Double.parseDouble(s);
                if (money_withdraw > ABS_Client.current_customer.getBalance()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error!!!");
                    alert.setContentText("Ooops, You don't have enough money");

                    alert.showAndWait();
                    return;
                }
                updateBalanceView("withdraw", s);
            }
        });
    }
    @FXML
    protected void onClickNewLoanButton(final ActionEvent event ) {
        if (isRewindMode.getValue()){
            return;
        }
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(customer_utills.Constants.CUSTOMER_NEW_LOAN_WINDOW_FXML_RESOURCE_LOCATION));
                Parent root = loader.load();
                NewLoanWindow newLoanWindow = loader.getController();
                newLoanWindow.setCallback(new Runnable(){
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            updateLoansInfoView();
                        });
                    }
                });
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setAlwaysOnTop(true);
                stage.setResizable(false);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex);
            }
            tab_pane_id.getSelectionModel().select(information_tab);
        });
    }
    @FXML
    protected void onClickFilter(ActionEvent event)
    {
        String amount_invest_for_round_s = this.amount_for_round_textfield.getText()+"";
        String min_interest_rate = this.minimum_interest_rate_textfield.getText()+"";
        String min_yaz_time_for_loan = this.min_yaz_textfield.getText()+"";
        String loan_category = this.categories_combobox.getValue() +"";
        if(loan_category.equalsIgnoreCase("none") || loan_category.equalsIgnoreCase("null")) {
            loan_category = null;
        }
        String max_percent_own = this.max_ownership_textfield.getText()+"";
        String max_open_loans = this.max_open_loans_textfield.getText()+"";
        if(!amount_invest_for_round_s.isEmpty()) {
            amount_invest_for_round_str = amount_invest_for_round_s;
            HttpUrl.Builder builder = HttpUrl
                    .parse(Constants.UPDATE_SCRAMBLE)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .addQueryParameter("amount_invest", amount_invest_for_round_s);
            if (!min_interest_rate.isEmpty()) {
                builder = builder.addQueryParameter("min_interest_rate", min_interest_rate);
                min_interest_rate_str = min_interest_rate;
            }
            if (!min_yaz_time_for_loan.isEmpty()) {
                builder = builder.addQueryParameter("min_yaz_for_loan", min_yaz_time_for_loan);
                min_yaz_time_for_loan_str = min_yaz_time_for_loan;
            }
            if (loan_category != null && !loan_category.isEmpty()) {
                builder = builder.addQueryParameter("loan_category", loan_category);
                System.out.println("add to builder category named: #"+loan_category+"#");
                loan_category_str = loan_category;
            }
            if (!max_percent_own.isEmpty()) {
                builder = builder.addQueryParameter("max_percent_ownership", max_percent_own);
                max_percent_own_str = max_percent_own;
            }
            if (!max_open_loans.isEmpty()) {
                builder = builder.addQueryParameter("max_open_loans", max_open_loans);
                max_percent_own_str = max_open_loans;
            }
            String finalUrl =
                    builder
                            .build()
                            .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    System.out.println("onResponse onClickFilter");
                    String jsonSt = response.body().string();
                    System.out.println("Response onClickFilter: " + jsonSt);
                    if(response.code() ==  200) {
                        Type listType = new TypeToken<List<Loan>>(){}.getType();

                        List<Loan> loans = Constants.GSON_INSTANCE.fromJson(jsonSt, listType);
                        Platform.runLater(() -> {
                            System.out.println(loans);
                            error_label.setText("");
                            error_label.setVisible(false);
                            for (Loan loan : loans) {
                                System.out.println(loan);
                            }
                            filter_loans_observableList.clear();
                            filter_loans_observableList.setAll(loans);
                            scramble_tableview.refresh();
                            amount_invest_for_round = Double.parseDouble(amount_invest_for_round_s);
                        });
                    }

                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
            });
        }
        else{
            Platform.runLater(() -> {
                this.error_label.setText("You Need To Insert Amount For Make Invest!");
                this.error_label.setVisible(true);
                this.error_label.setTextFill(Color.RED);
            });
        }
    }

    @FXML
    protected void onClickPayAllSelectedLoans(ActionEvent event)
    {
        if (isRewindMode.getValue()){
            return;
        }
        for (PaymentView paymentView : paymentViews.keySet()) {
            System.out.println("paymentView :" + paymentView);
            payPayment(paymentView.getLoanId(), paymentView.getLenderName(), paymentView.getUpcomingPayment()+"");
            CheckBox checkBox = paymentViews.get(paymentView);
            checkBox.setSelected(false);
            StringBuilder notifictionMessage = new StringBuilder();
            notifictionMessage.append("Current Time Unit: ");
            notifictionMessage.append(current_TimeUnit);
            notifictionMessage.append("\n");
            notifictionMessage.append("Loan Id: ");
            notifictionMessage.append(paymentView.getLoanId());
            notifictionMessage.append("\n");
            notifictionMessage.append("Lender Name: ");
            notifictionMessage.append(paymentView.getLenderName());
            notifictionMessage.append("\n");
            notifictionMessage.append("Loan Status: ");
            notifictionMessage.append(paymentView.getLoanStatus());
            notifictionMessage.append("\n");
            notifictionMessage.append("Total Invest: ");
            notifictionMessage.append(paymentView.getTotalInvest());
            notifictionMessage.append("\n");
            notifictionMessage.append("Currently Payed: ");
            notifictionMessage.append(paymentView.getUpcomingPayment());
            notifictionMessage.append("\n");
            notifictionMessage.append("-------------------------------\n");
            addNotification(ABS_Client.current_customer.getName(), notifictionMessage.toString());
        }
        paymentViews.clear();
        pay_btn.setDisable(true);
    }

    protected void finishLoan(String loan_id)
    {
        if (isRewindMode.getValue()){
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.FINISH_LOAN)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .addQueryParameter("loan_id", loan_id)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse finishLoan");
                String jsonSt = response.body().string();
                System.out.println("Response finishLoan: " + jsonSt);
                if(response.code() ==  200) {
                    Platform.runLater(() -> {
                        dashboard_error_label.setText("");
                        dashboard_error_label.setVisible(false);
                    });
                }
                else{
                    HashMap<String,String> mapfromJson = GSON_INSTANCE.fromJson(jsonSt, HashMap.class);

                    for (Map.Entry<String, String> stringStringEntry : mapfromJson.entrySet()) {
                        System.out.println(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error!");
                            alert.setHeaderText(stringStringEntry.getKey());
                            alert.setContentText(stringStringEntry.getValue());
                            alert.showAndWait();

                        });
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    protected void putLoanOnSale(String loan_id)
    {
        if (isRewindMode.getValue()){
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.SALE_LOAN)
                .newBuilder()
                .addQueryParameter("loan_id", loan_id)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse investMoneyInLoan");
                String jsonSt = response.body().string();
                System.out.println("Response investMoneyInLoan: " + jsonSt);
                if(response.code() ==  200) {}
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }



    public void startYazRefresher(){
        timeUnitRefresher = new TimeUnitRefresher(
                this::updateYazTimeLabel);

        timeUnit_Timer = new Timer();
        timeUnit_Timer.schedule(timeUnitRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startNotificationsRefresher(){
        notificationsRefresher = new NotificationsRefresher(
                this::updateNotificationsView);

       notificationsRefresh_Timer = new Timer();
        notificationsRefresh_Timer.schedule(notificationsRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);

    }

    public void startBorrowerRefresher(){
        borrowerRefresher = new BorrowerInfoRefresher(
                this::updateBorrowerView);

        borrowerRefresh_Timer = new Timer();
        borrowerRefresh_Timer.schedule(borrowerRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startPaymentsRefresher(){
        paymentsRefresher = new PaymentsRefresher(this::updatePaymentsView);

        paymentsRefresh_Timer = new Timer();
        paymentsRefresh_Timer.schedule(paymentsRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startBalanceRefresher()
    {
        balanceRefresher = new BalanceRefresher(this::updateBalanceRefresh);

        balanceRefresh_Timer = new Timer();
        balanceRefresh_Timer.schedule(balanceRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startTransacionsRefresher()
    {
        transactionsRefresher = new TransactionsRefresher(this::updateTransactionsRefresh);

        transactionsRefresh_Timer = new Timer();
        transactionsRefresh_Timer.schedule(transactionsRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startLoansForSaleRefresher()
    {
        loansForSaleRefresher = new LoansForSaleRefresher(this::updateLoansForSaleView);

        loansForSaleRefresh_Timer = new Timer();
        loansForSaleRefresh_Timer.schedule(loansForSaleRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startLenderRefresher(){
        lenderRefresher = new LenderInfoRefresher(this::updateLender);

        lenderRefresh_Timer = new Timer();
        lenderRefresh_Timer.schedule(lenderRefresher, Constants.REFRESH_RATE, Constants.REFRESH_RATE);
    }

    public void startRewindRefresher(){
        rewindRefresher_Timer = new Timer();
        rewindRefresher = new IsRewindModeRefresher(this::updateRewindMode, unused -> rewindRefresher_Timer.cancel());


        rewindRefresher_Timer.schedule(rewindRefresher,Constants.REFRESH_RATE, Constants.REFRESH_RATE);

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

    private <Data> void addCheckBoxToTable(TableColumn<Data, Void> cblBtn, BiConsumer<Data,CheckBox> consumer) {

        javafx.util.Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new javafx.util.Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
            @Override
            public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
                final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

                    private final CheckBox checkbox = new CheckBox();
                    {
                        checkbox.setOnAction((ActionEvent event) -> {
                            Data data = getTableView().getItems().get(getIndex());
                            consumer.accept(data,checkbox);
                            System.out.println("checkbox selected for selectedData: " + data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(checkbox);
                        }
                    }
                };
                return cell;
            }
        };
        cblBtn.setCellFactory(cellFactory);
    }


    protected void payPayment(String loan_id, String lender_name, String payment)
    {
        if (isRewindMode.getValue()){
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.PAY_PAYMENT)
                .newBuilder()
                .addQueryParameter("username", ABS_Client.current_customer.getName())
                .addQueryParameter("loan_id", loan_id)
                .addQueryParameter("lender_name", lender_name)
                .addQueryParameter("money_to_pay", payment)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure payPayment");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse updatePaymentsView");
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response updatePaymentsView: " + jsonArrayOfUsersNames);
                PaymentView[] paymentsFromJson = new PaymentView[0];
                if(!jsonArrayOfUsersNames.isEmpty()) {
                    paymentsFromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, PaymentView[].class);
                    for (PaymentView paymentView : paymentsFromJson) {
                        System.out.println(paymentView);
                    }
                }
                PaymentView[] finalPaymentsFromJson = paymentsFromJson;
                Platform.runLater(() -> {
                    loans_payment_observableList.clear();
                    loans_payment_observableList.setAll(finalPaymentsFromJson);
                    payment_tableview.refresh();

                    updateBalanceView("update", "0");
                });
            }
        });
        this.payment_tableview.refresh();

    }

    protected void addNotification(String client_name, String notification)
    {
        if (isRewindMode.getValue()){
            return;
        }
        String finalUrl = HttpUrl
                .parse(Constants.ADD_NOTIFICATION)
                .newBuilder()
                .addQueryParameter("username", client_name)
                .addQueryParameter("notification", notification)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("onFailure addNotification");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("onResponse updateLoansInfoView");
                String jsonArrayOfUsersNames = response.body().string();
                System.out.println("Response updateLoansInfoView: " + jsonArrayOfUsersNames);
            }
        });
    }

    @FXML
    void uncheckAllCheckBoxes(ActionEvent event){
        if (isRewindMode.getValue()){
            return;
        }
        for (CheckBox checkBox : this.paymentViews.values()) {
            checkBox.setSelected(false);
        }
        this.paymentViews.clear();
    }


}

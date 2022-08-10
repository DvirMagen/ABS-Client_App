package customer_utills;

//import com.google.gson.Gson;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Constants {

    // global constants
//    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
//    public final static String JHON_DOE = "<Anonymous>";
//    public final static int REFRESH_RATE = 2000;
//    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

//    // fxml locations
    public final static String CUSTOMER_LOAD_FILE_PAGE_FXML_RESOURCE_LOCATION = "/javafx/scenes/loadFile/CustomerLoadFileScene.fxml";
    public final static String CUSTOMER_LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/javafx/scenes/login/CustomerLoginScene.fxml";
    public final static String CUSTOMER_DASHBOARD_PAGE_FXML_RESOURCE_LOCATION = "/javafx/scenes/dashboard/CustomerDashboardScene.fxml";
    public final static String CUSTOMER_NEW_LOAN_WINDOW_FXML_RESOURCE_LOCATION = "/javafx/scenes/newLoan/NewLoanWindow.fxml";
    public static final String POPUP_LOADING_TASKBAR_WINDOW_PATH = "/javafx/scenes/Popup/TaskBarScramble.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/web_app";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public static final String LOGOUT = FULL_SERVER_PATH + "/logout";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH +"/clientLoginShortResponse";
    public final static String LOAD_XML = FULL_SERVER_PATH + "/client/loadxml";
    public final static String LOAN = FULL_SERVER_PATH + "/loans";
    public final static String FILTER = FULL_SERVER_PATH + "/filter";
    public final static String YAZ_TIME = FULL_SERVER_PATH + "/";
    public final static String CATEGORIES = FULL_SERVER_PATH + "/categories";

    public final static String BALANCE = FULL_SERVER_PATH + "/balance";

    public final static String MOVEMENTS = FULL_SERVER_PATH + "/movements";
    public final static String INVEST_LOAN = FULL_SERVER_PATH + "/invest_loan";
    public final static String UPDATE_BALANCE = FULL_SERVER_PATH + "/update_balance";
    public final static String UPDATE_SCRAMBLE = FULL_SERVER_PATH + "/update_scramble";
    public final static String INVESTED_LOANS = FULL_SERVER_PATH + "/invested_loans";
    public final static String PAY_PAYMENT = FULL_SERVER_PATH + "/pay_payment";
    public final static String NOTIFICATIONS = FULL_SERVER_PATH + "/notifications";
    public final static String ADD_NOTIFICATION = FULL_SERVER_PATH + "/add_notification";
    public final static String PAYMENTS = FULL_SERVER_PATH + "/payments";
    public final static String FINISH_LOAN = FULL_SERVER_PATH + "/finish_loan";
    public final static String LOANS_FOR_SALE = FULL_SERVER_PATH + "/loans_for_sale";
    public final static String SALE_LOAN = FULL_SERVER_PATH + "/sale_loan";
    public final static String BUY_LOAN = FULL_SERVER_PATH + "/buy_loan";
    public final static String IS_REWIND = FULL_SERVER_PATH + "/rewind_mode";

    public final static String CREATE_NEW_LOAN = FULL_SERVER_PATH + "/create_new_loan";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    public final static int REFRESH_RATE = 500;

    // GSON instance
    public final static Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(Loan.Lender.class, new JsonDeserializer<Loan.Lender>() {
        @Override
        public Loan.Lender deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Gson().fromJson(jsonElement.getAsString(), new TypeToken<Loan.Lender>(){}.getType());
        }
    }).create();
}

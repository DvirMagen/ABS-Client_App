package javafx.scenes.dashboard;

import customer_utills.Constants;
import customer_utills.PaymentView;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static customer_utills.Constants.GSON_INSTANCE;

public class PaymentsRefresher extends TimerTask {

    private final Consumer<PaymentView[]> paymentsConsumer;
    public PaymentsRefresher( Consumer<PaymentView[]> loansInfoConsumer) {
        this.paymentsConsumer = loansInfoConsumer;
    }

    @Override
    public void run() {
        if(ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
            String finalUrl = HttpUrl
                    .parse(Constants.PAYMENTS)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure PaymentsRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    System.out.println("onResponse updateLenderInfoView");
                    String jsonArrayOfUsersNames = response.body().string();
//                    System.out.println("Response updateLenderInfoView: " + jsonArrayOfUsersNames);
                    PaymentView[] paymentViews = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, PaymentView[].class);
//                    System.out.println("paymentViews: " + Arrays.toString(paymentViews));
                    Platform.runLater(() -> paymentsConsumer.accept(paymentViews));
                }
            });
        }
    }

}

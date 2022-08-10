package javafx.scenes.dashboard;

import customer_utills.Constants;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static customer_utills.Constants.GSON_INSTANCE;

public class BalanceRefresher extends TimerTask {
    private final Consumer<Double> balanceConsumer;
    public BalanceRefresher( Consumer<Double> timeUnitConsumer) {
        this.balanceConsumer = timeUnitConsumer;
    }

    @Override
    public void run() {
        if (ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
            String finalUrl = HttpUrl
                    .parse(Constants.UPDATE_BALANCE)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .addQueryParameter("action", "update")
                    .addQueryParameter("amount", "0")
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure BalanceRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    System.out.println("onResponse");

                    String jsonArrayOfUsersNames = response.body().string();
//                    System.out.println("Response updateBalanceView: " + jsonArrayOfUsersNames);
                    if (response.code() == 200) {
                        HashMap<String, Object> mapfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, HashMap.class);
                        double balance = (double) mapfromJson.get("balance");
//                        System.out.println("Response updateBalanceView balance: " + balance);
                        Platform.runLater(() -> balanceConsumer.accept(balance));

                    } else if (response.code() == 400) {
                        HashMap<String, String> mapfromJson = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, HashMap.class);

                        for (Map.Entry<String, String> stringStringEntry : mapfromJson.entrySet()) {
                            System.out.println(stringStringEntry.getKey() + " , " + stringStringEntry.getValue());

                        }
                    }
                }
            });
        }
    }
}

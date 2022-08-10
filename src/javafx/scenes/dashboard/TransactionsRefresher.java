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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

import static customer_utills.Constants.GSON_INSTANCE;

public class TransactionsRefresher extends TimerTask {
    private final Consumer<List<String>> transactionsConsumer;
    public TransactionsRefresher( Consumer<List<String>> transactionsConsumer) {
        this.transactionsConsumer = transactionsConsumer;
    }
    @Override
    public void run() {
        if(ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
//            System.out.println("updateMovementView");
            String finalUrl = HttpUrl
                    .parse(Constants.MOVEMENTS)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure TransactionsRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    System.out.println("onResponse Movements");
                    String jsonArrayOfUsersNames = response.body().string();
//                    System.out.println("Response Movements: " + jsonArrayOfUsersNames);
                    Map<String, Object> map = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Map.class);
                    List<String> movements = (List<String>) map.getOrDefault("movements", new ArrayList<String>());
                    Platform.runLater(() -> transactionsConsumer.accept(movements));
                }
            });
        }
    }

}

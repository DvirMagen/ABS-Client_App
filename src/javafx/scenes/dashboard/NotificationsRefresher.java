package javafx.scenes.dashboard;

import customer_utills.Constants;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class NotificationsRefresher extends TimerTask {

    private final Consumer<String> notificationsConsumer;
    public NotificationsRefresher( Consumer<String> notificationsConsumer) {
        this.notificationsConsumer = notificationsConsumer;
    }

    @Override
    public void run() {
        if(ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
            String finalUrl = HttpUrl
                    .parse(Constants.NOTIFICATIONS)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure NotificationsRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String notifications = response.body().string();
//                    System.out.println("NotificationsRefresher: " + notifications);
                    notificationsConsumer.accept(notifications);
                }
            });
        }
    }
}

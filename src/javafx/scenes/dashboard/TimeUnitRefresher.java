package javafx.scenes.dashboard;

import customer_utills.Constants;
import customer_utills.http.HttpClientUtil;
import javafx.ABS_Client;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TimeUnitRefresher  extends TimerTask {

    private final Consumer<String> timeUnitConsumer;
    public TimeUnitRefresher( Consumer<String> timeUnitConsumer) {
        this.timeUnitConsumer = timeUnitConsumer;
    }

    @Override
    public void run() {
        if(ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
            HttpClientUtil.runAsync(Constants.YAZ_TIME, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("Failure!!!");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String timeUnit = response.body().string();
//                    System.out.println("Time Unit: " + timeUnit);
                    Platform.runLater(() -> timeUnitConsumer.accept(timeUnit));

                }
            });
        }
    }
}

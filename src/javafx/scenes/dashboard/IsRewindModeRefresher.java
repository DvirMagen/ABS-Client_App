package javafx.scenes.dashboard;

import customer_utills.Constants;
import customer_utills.http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Consumer;

public class IsRewindModeRefresher extends TimerTask {
    private final Consumer<Boolean> isRewindModeConsumer;
    private final Consumer<Void> cancel;
    public IsRewindModeRefresher( Consumer<Boolean> isRewindModeConsumer,Consumer<Void> cancel) {
        this.isRewindModeConsumer = isRewindModeConsumer;
        this.cancel = cancel;
    }

    @Override
    public void run() {
            String finalUrl = HttpUrl
                    .parse(Constants.IS_REWIND)
                    .newBuilder()
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure IsRewindModeRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    System.out.println("onResponse updateLoansInfoView");
                    String jsonArrayOfUsersNames = response.body().string();
//                    System.out.println("Response updateLoansInfoView: " + jsonArrayOfUsersNames);
                    Map<String,Boolean> mapfromJson = customer_utills.Constants.GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Map.class);
                    Boolean isRewindMode = mapfromJson.get("RewindMode");
                    Platform.runLater(() -> isRewindModeConsumer.accept(isRewindMode));
                }
            },this.cancel);
    }
}

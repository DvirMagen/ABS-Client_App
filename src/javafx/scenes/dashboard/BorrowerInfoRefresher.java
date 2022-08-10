package javafx.scenes.dashboard;

import customer_utills.Loan;
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

public class BorrowerInfoRefresher extends TimerTask {

    private final Consumer<Loan[]> loansInfoConsumer;
    public BorrowerInfoRefresher( Consumer<Loan[]> loansInfoConsumer) {
        this.loansInfoConsumer = loansInfoConsumer;
    }

    @Override
    public void run() {
        if(ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
            String finalUrl = HttpUrl
                    .parse(customer_utills.Constants.LOAN)
                    .newBuilder()
                    .addQueryParameter("owner", ABS_Client.current_customer.getName())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure BorrowerInfoRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    System.out.println("onResponse updateLoansInfoView");
                    String jsonArrayOfUsersNames = response.body().string();
//                    System.out.println("Response updateLoansInfoView: " + jsonArrayOfUsersNames);
                    Loan[] loansfromJson = customer_utills.Constants.GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Loan[].class);
//                    for (Loan loan : loansfromJson) {
//                        System.out.println(loan);
//                    }
                    Platform.runLater(() -> loansInfoConsumer.accept(loansfromJson));
                }
            });
        }
    }
}

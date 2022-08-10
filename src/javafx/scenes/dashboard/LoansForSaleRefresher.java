package javafx.scenes.dashboard;

import customer_utills.Constants;
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

import static customer_utills.Constants.GSON_INSTANCE;

public class LoansForSaleRefresher extends TimerTask {
    private final Consumer<Loan[]> loansForSaleConsumer;
    public LoansForSaleRefresher( Consumer<Loan[]> loansForSaleConsumer) {
        this.loansForSaleConsumer = loansForSaleConsumer;
    }

    @Override
    public void run() {
        if(ABS_Client.current_customer.getName() != null && !ABS_Client.current_customer.getName().equalsIgnoreCase("null")) {
            String finalUrl = HttpUrl
                    .parse(Constants.LOANS_FOR_SALE)
                    .newBuilder()
                    .addQueryParameter("username", ABS_Client.current_customer.getName())
                    .build()
                    .toString();

            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    System.out.println("onFailure LoansForSaleRefresher");
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    System.out.println("onResponse updateLenderInfoView");
                    String jsonArrayOfUsersNames = response.body().string();
//                    System.out.println("Response updateLenderInfoView: " + jsonArrayOfUsersNames);
                    Loan[] loansfromJson = new Loan[0];
                    if (!jsonArrayOfUsersNames.isEmpty())
                        loansfromJson =GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, Loan[].class);
//                    for (Loan loan : loansfromJson) {
//                        System.out.println(loan);
//                    }
                    Loan[] finalLoansfromJson = loansfromJson;
                    Platform.runLater(() -> loansForSaleConsumer.accept(finalLoansfromJson));
                }
            });
        }
    }

}

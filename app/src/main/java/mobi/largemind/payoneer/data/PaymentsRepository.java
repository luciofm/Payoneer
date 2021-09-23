package mobi.largemind.payoneer.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsRepository {
    private final PaymentsService paymentsService;

    public PaymentsRepository(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    public LiveData<DataState<PaymentsResponse>> fetchPaymentMethods() {
        MutableLiveData<DataState<PaymentsResponse>> liveData = new MutableLiveData<>();
        liveData.setValue(DataState.loading());

        paymentsService.fetchPayments().enqueue(new Callback<PaymentsResponse>() {
            @Override
            public void onResponse(Call<PaymentsResponse> call, Response<PaymentsResponse> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(DataState.data(response.body()));
                } else {
                    liveData.setValue(DataState.error(new IOException()));
                }
            }

            @Override
            public void onFailure(Call<PaymentsResponse> call, Throwable t) {
                liveData.setValue(DataState.error(t));
            }
        });

        return liveData;
    }
}

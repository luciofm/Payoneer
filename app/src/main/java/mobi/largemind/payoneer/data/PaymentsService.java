package mobi.largemind.payoneer.data;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PaymentsService {
    @GET("lists/listresult.json")
    Call<PaymentsResponse> fetchPayments();
}

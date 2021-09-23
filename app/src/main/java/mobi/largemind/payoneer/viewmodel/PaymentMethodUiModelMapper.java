package mobi.largemind.payoneer.viewmodel;

import java.util.ArrayList;
import java.util.List;

import mobi.largemind.payoneer.data.PaymentsResponse;

public class PaymentMethodUiModelMapper {
    public static List<PaymentMethodUiModel> map(PaymentsResponse response) {
        List<PaymentMethodUiModel> list = new ArrayList<>(response.networks.networks.size());
        for (PaymentsResponse.Network network : response.networks.networks) {
            list.add(map(network));
        }
        return list;
    }

    private static PaymentMethodUiModel map(PaymentsResponse.Network network) {
        return new PaymentMethodUiModel(network.code, network.label, network.links.logo);
    }
}

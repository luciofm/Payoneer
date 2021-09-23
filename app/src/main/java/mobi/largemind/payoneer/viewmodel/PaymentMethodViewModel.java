package mobi.largemind.payoneer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import mobi.largemind.payoneer.data.DataState;
import mobi.largemind.payoneer.data.PaymentsRepository;
import mobi.largemind.payoneer.data.PaymentsResponse;

@HiltViewModel
public class PaymentMethodViewModel extends ViewModel {
    private final PaymentsRepository repository;
    private LiveData<UiState<List<PaymentMethodUiModel>>> uiState = null;

    @Inject
    public PaymentMethodViewModel(PaymentsRepository repository) {
        this.repository = repository;
    }

    public LiveData<UiState<List<PaymentMethodUiModel>>> getUiState() {
        ensureUiState();
        return uiState;
    }

    private void ensureUiState() {
        if (uiState == null) {
            uiState = createUiState();
        }
    }

    private LiveData<UiState<List<PaymentMethodUiModel>>> createUiState() {
        return Transformations.map(repository.fetchPaymentMethods(), this::toUiState);
    }

    private UiState<List<PaymentMethodUiModel>> toUiState(DataState<PaymentsResponse> state) {
        switch (state.getState()) {
            case LOADING:
                return UiState.loading();
            case DATA:
                return UiState.loaded(PaymentMethodUiModelMapper.map(state.getData()));
            case ERROR:
                return UiState.error();
            default:
                throw new IllegalStateException("State must be LOADING OR DATA OR ERROR");
        }
    }

    private interface UpdateData {
        List<PaymentMethodUiModel> update(List<PaymentMethodUiModel> data);
    }
}

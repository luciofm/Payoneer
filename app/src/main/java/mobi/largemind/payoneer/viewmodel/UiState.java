package mobi.largemind.payoneer.viewmodel;

import static mobi.largemind.payoneer.util.Preconditions.checkNotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Kind of a Sealed Kotlin class, to expose multiple states of the UiModel.
public class UiState<T> {

    @NonNull
    State state;
    @Nullable
    T data;

    private UiState(@NonNull State state, @Nullable T data) {
        if (state != State.LOADED && data != null) {
            throw new IllegalStateException("Only LOADED have data");
        }
        this.state = state;
        this.data = data;
    }

    public static <T> UiState<T> loading() {
        return new UiState<>(State.LOADING, null);
    }

    public static <T> UiState<T> loaded(T data) {
        return new UiState<T>(State.LOADED, data);
    }

    public static <T> UiState<T> error() {
        return new UiState<>(State.ERROR, null);
    }

    @NonNull
    public State getState() {
        return state;
    }

    @NonNull
    public T getData() {
        checkNotNull(data);
        return data;
    }

    @Override
    public String toString() {
        return "UiState{" +
                "state=" + state +
                ", data=" + data +
                '}';
    }

    public enum State {
        LOADING,
        LOADED,
        ERROR
    }
}

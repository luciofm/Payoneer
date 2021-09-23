package mobi.largemind.payoneer.data;

import static mobi.largemind.payoneer.util.Preconditions.checkCondition;
import static mobi.largemind.payoneer.util.Preconditions.checkNotNull;

import androidx.annotation.NonNull;

import javax.annotation.Nullable;

// Kind of a Sealed Kotlin class, to expose multiple states of a Network call.
public class DataState<T> {

    @NonNull
    private State state;
    @Nullable
    private T data;
    @Nullable
    private Throwable throwable;

    private DataState(@NonNull State state, @Nullable T data, @Nullable Throwable throwable) {
        checkNotNull(state, () -> "STATE should not be null");
        checkCondition(() -> state == State.DATA && data == null, () -> "State.DATA could not have NULL data");
        checkCondition(() -> state != State.ERROR && throwable != null, () -> "Only State.ERROR can have throwable");
        this.state = state;
        this.data = data;
        this.throwable = throwable;
    }

    public static <T> DataState<T> loading() {
        return new DataState<>(State.LOADING, null, null);
    }

    public static <T> DataState<T> data(T data) {
        return new DataState<>(State.DATA, data, null);
    }

    public static <T> DataState<T> error(Throwable throwable) {
        return new DataState<>(State.ERROR, null, throwable);
    }

    @NonNull
    public State getState() {
        return state;
    }

    @NonNull
    public T getData() {
        checkCondition(() -> state != State.DATA, () -> "Can't call getData() on ERROR or LOADING states");
        return data;
    }

    @Nullable
    public Throwable getThrowable() {
        checkCondition(() -> state != State.ERROR, () -> "Can't call getThrowable() on LOADING or DATA states");
        return throwable;
    }

    public enum State {
        LOADING,
        DATA,
        ERROR
    }
}

package mobi.largemind.payoneer;

import org.junit.Test;

import java.io.IOException;

import mobi.largemind.payoneer.data.DataState;

public class DataStateTest {

    @Test(expected = IllegalStateException.class)
    public void ensureDataStateHasNonNullData() {
        DataState.<String>data(null);
    }

    @Test
    public void ensureNullableThrowable() {
        DataState<String> state = DataState.error(null);
        assert state.getThrowable() == null;
    }

    @Test(expected = IllegalStateException.class)
    public void ensureGetDataThrowsOnErrorState() {
        DataState<String> state = DataState.error(null);
        state.getData();
    }

    @Test(expected = IllegalStateException.class)
    public void ensureGetDataThrowsOnLoadingState() {
        DataState<String> state = DataState.loading();
        state.getData();
    }

    @Test
    public void ensureGetDataReturnsDataOnDataState() {
        String data = "test data";
        DataState<String> state = DataState.data("test data");
        String retrievedData = state.getData();
        assert data.contentEquals(retrievedData);
    }

    @Test
    public void ensureGetThrowableReturnsCorrectValueOnErrorState() {
        Throwable exception = new IOException();
        DataState<String> state = DataState.error(exception);
        Throwable retrievedException = state.getThrowable();

        assert retrievedException instanceof IOException;
        assert retrievedException == exception;
    }

    @Test(expected = IllegalStateException.class)
    public void ensureGetThrowableThrowsOnLoadingState() {
        DataState<String> state = DataState.loading();
        state.getThrowable();
    }

    @Test(expected = IllegalStateException.class)
    public void ensureGetThrowableThrowsOnDataState() {
        DataState<String> state = DataState.data("data");
        state.getThrowable();
    }

    @Test
    public void ensureDataDataStateHasCorrectStateType() {
        DataState<String> state = DataState.data("data");
        assert state.getState() == DataState.State.DATA;
    }

    @Test
    public void ensureLoadingDataStateHasCorrectStateType() {
        DataState<String> state = DataState.loading();
        assert state.getState() == DataState.State.LOADING;
    }

    @Test
    public void ensureErrorDataStateHasCorrectStateType() {
        DataState<String> state = DataState.error(null);
        assert state.getState() == DataState.State.ERROR;
    }
}

package mobi.largemind.payoneer.util;

import androidx.annotation.NonNull;

public class Preconditions {
    public static void checkNotNull(Object object) {
        checkNotNull(object, null);
    }

    public static void checkNotNull(Object object, ErrorMessage errorMessage) {
        if (object == null) {
            String message = errorMessage != null ? errorMessage.evaluate() : "Value could not be null";
            throw new NullPointerException(message);
        }
    }

    public static void checkCondition(Condition condition) {
        checkCondition(condition, null);
    }

    public static void checkCondition(Condition condition, ErrorMessage errorMessage) {
        if (condition.evaluate()) {
            String message = errorMessage != null ? errorMessage.evaluate() : "Condition not met";
            throw new IllegalStateException(message);
        }
    }

    public interface Condition {
        boolean evaluate();
    }


    public interface ErrorMessage {
        @NonNull String evaluate();
    }
}

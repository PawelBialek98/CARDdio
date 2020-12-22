package pl.lodz.p.it.cardio.exception;

public class AppTransactionFailureException extends AppBaseException {

    public static final String TRANSACTION_CONCURRENT_MESSAGE_KEY = "auth.message.expired";

    public AppTransactionFailureException(String message) {
        super(message);
    }

    public AppTransactionFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppTransactionFailureException createOptimisticLockingException(Throwable cause){
        return new AppTransactionFailureException(resourceBundle.getString(TRANSACTION_CONCURRENT_MESSAGE_KEY), cause);
    }
}

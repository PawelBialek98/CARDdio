package pl.lodz.p.it.cardio.exception;

public class AppTransactionFailureException extends AppBaseException {
    public AppTransactionFailureException(String message) {
        super(message);
    }

    public AppTransactionFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AppTransactionFailureException createOptimisticLockingException(Throwable cause){
        return new AppTransactionFailureException("messageXDD", cause);
    }
}

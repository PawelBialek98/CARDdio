package pl.lodz.p.it.cardio.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggerInterceptor {
    private static final Logger LOGGER = Logger.getGlobal();

    @Around("within(pl.lodz.p.it.cardio.service.*) || " +
            "within(pl.lodz.p.it.cardio.repositories.*) || " +
            "within(pl.lodz.p.it.cardio.controllers.*)")
    public Object auditMethod(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        String packageName = jp.getSignature().getDeclaringTypeName();
        Object principal;
        try {
            principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException npe) {
            principal = "Scheduler";
        }
        String callerName;
        if (principal instanceof UserDetails) {
            callerName = ((UserDetails) principal).getUsername();
        } else {
            callerName = principal.toString();
        }

        LOGGER.log(Level.INFO,
                "{0} - {1} called by: {2}",
                new Object[]{packageName, methodName, callerName});

        Object obj;
        try{
            obj = jp.proceed();
        } catch (Exception exception) {

            String exceptionInfo = exception.getClass().getName() + ": \"" + exception.getMessage() + "\"";
            Throwable cause = exception.getCause();

            if (cause != null) {
                exceptionInfo += " caused by " + cause.getClass().getName() + ": \"" + cause.getMessage() + "\"";
            }

            LOGGER.log(Level.WARNING,
                    "{0} - {1} called by: {2} has thrown\n{3}",
                    new Object[]{packageName, methodName, callerName, exceptionInfo});

            throw exception;
        }
        LOGGER.log(Level.INFO,
                "{0} - {1} called by: {2} has returned {3}",
                new Object[]{packageName, methodName, callerName, obj});

        return obj;
    }

}

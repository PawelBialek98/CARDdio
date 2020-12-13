package pl.lodz.p.it.cardio.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggerInterceptor {
    private static final Logger LOGGER = Logger.getGlobal();

    @Around("execution(* pl.lodz.p.it.cardio.service.UserServiceImpl.*(..))")
    public Object auditMethod(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        LOGGER.log(Level.INFO, "Call to " + methodName);
        Object obj = jp.proceed();
        LOGGER.log(Level.INFO,"Method called successfully: " + methodName);
        return obj;
    }

}

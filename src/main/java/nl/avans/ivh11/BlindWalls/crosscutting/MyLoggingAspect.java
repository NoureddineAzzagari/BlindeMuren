package nl.avans.ivh11.BlindWalls.crosscutting;

/**
 * Created by thomasdelhez on 08-03-18.
 */
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
//@SuppressWarnings("squid:S00112") //false positive sonarcube

public class MyLoggingAspect {
    @Pointcut("execution(* nl.avans.ivh11.BlindWalls..*(..))") // the pointcut expression
    public void dummyMethod() { // the pointcut signature
    }

    /**
     * Advice wordt uitgevoerd voor het join point
     * @param joinPoint
     */
    @Before("dummyMethod()")
    public void loggingBeforeAdvice(JoinPoint joinPoint) {
        System.out.println("(AOP-myLogger) Executing: "
                + joinPoint.getSignature().getDeclaringTypeName()
                + "."
                + joinPoint.getSignature().getName());
    }

    /**
     * Er wordt geadviseerd zowel voor als na methode invocatie
     */
    @Around("dummyMethod()")
    public Object loggingAroundAdvice(ProceedingJoinPoint pjp)
            throws Throwable {
        System.out.println("(AOP-myLogger) Before execution: "
                + pjp.getSignature().getDeclaringTypeName() + "."
                + pjp.getSignature().getName());
        Object retVal = pjp.proceed();
        System.out.println("(AOP-myLogger) After execution: "
                + pjp.getSignature().getDeclaringTypeName() + "."
                + pjp.getSignature().getName());
        return retVal;
    }
}

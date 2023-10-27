package com.vrerv.springboottemplate.server.common.infra.error;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import static com.vrerv.springboottemplate.server.base.AppStaticConfig.PACKAGE_PREFIX;

@Slf4j
@EnableAspectJAutoProxy
@Aspect
@Component
public class GlobalErrorAspect {
	// the execution of any method defined in the package or a sub-package
	private static final String POINTCUT_EXP = "execution(* " + PACKAGE_PREFIX + ".*.*(..))";

	@Value("${app.debug}")
	private boolean recursive;

	@Pointcut(POINTCUT_EXP)
	private void anyMethodUnderThePackage() {}

	@Around("anyMethodUnderThePackage()")
	public Object adviceErrorWrapping(ProceedingJoinPoint join) throws Throwable {
		try {
			return join.proceed();
		} catch (Throwable t) {
			if (t instanceof AopWrappingException && !recursive) {
				throw t;
			} else {
				List<Object> args = Arrays.asList(join.getArgs());
				throw new AopWrappingException(args, t);
			}
		}
	}

}

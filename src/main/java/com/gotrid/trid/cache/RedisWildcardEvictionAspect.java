package com.gotrid.trid.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Set;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Aspect
public class RedisWildcardEvictionAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(redisWildcardEvict)")
    public Object evictKeys(ProceedingJoinPoint pjp, RedisWildcardEvict redisWildcardEvict) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        EvaluationContext context = new StandardEvaluationContext();

        Object[] args = pjp.getArgs();
        String[] paramNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        String keyPrefix = parser.parseExpression(redisWildcardEvict.keyPrefix()).getValue(context, String.class);
        String cacheName = redisWildcardEvict.cacheName();

        String pattern = cacheName + "::" + keyPrefix + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        return pjp.proceed();
    }
}

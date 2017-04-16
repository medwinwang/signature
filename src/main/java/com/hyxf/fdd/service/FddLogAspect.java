package com.hyxf.fdd.service;

import com.hyxf.fdd.dao.FddLogRepository;
import com.hyxf.fdd.model.FddLog;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

import static com.hyxf.fdd.service.impl.SignServiceImpl.CODE_KEY;
import static com.hyxf.fdd.service.impl.SignServiceImpl.RESULT_KEY;

/**
 * Created by medwin on 2017/4/11.
 */
@Aspect
@Component
public class FddLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(FddLogAspect.class);

    @Autowired
    private FddLogRepository fddLogRepository;

    @Autowired
    private Validator validator;

    @Value("${fdd.smy.contract_id.base}")
    private String smyBase;

    @Pointcut("execution(public * com.hyxf.fdd.web.*.*(..))")
    public void webLog(){}


    @Around("webLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        FddLog log = new FddLog();
        log.setIp(request.getRemoteAddr());
        log.setFunctionName(pjp.getSignature().getName());
        log.setRequestTime(new Date());

        Map map = new LinkedHashMap();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = request.getParameter(paramName);
            map.put(paramName, paramValue);
        }

        BeanUtils.populate(log, map);
        if("signSimple".equals(log.getFunctionName())) {

            Set<ConstraintViolation<FddLog>> set = validator.validate(log);
            if( CollectionUtils.isNotEmpty(set) ){
                for(ConstraintViolation<FddLog> cv : set){
                    JSONObject resultJson = new JSONObject();
                    resultJson.put(RESULT_KEY, "error");
                    resultJson.put(CODE_KEY, "2001");
                    resultJson.put("msg", cv.getMessage());
                    return resultJson.toString();
                }
            }
        }

        fddLogRepository.save(log);

        Object[] args = pjp.getArgs();
        if(StringUtils.isEmpty(log.getContract_id()) && FddLog.SOURCE_SMY.equals(log.getSource())) {
            args[5] = smyBase +  String.format("%07d", log.getId());
        }

        try {

            Object result = pjp.proceed(args);
            if (result instanceof String) {
                log.setResponse((String) result);
            }
            log.setResponeseTime(new Date());
            fddLogRepository.save(log);
            return result;
        }catch (Exception e) {
            logger.error(e.getLocalizedMessage());

            String msg = e.getMessage();
            if(StringUtils.isNotEmpty(msg) && msg.length() > 1024) {
                msg = msg.substring(0, 1000);
            }
            log.setResponse(msg);
            log.setResponeseTime(new Date());
            fddLogRepository.save(log);
            throw  e;
        }

    }

}

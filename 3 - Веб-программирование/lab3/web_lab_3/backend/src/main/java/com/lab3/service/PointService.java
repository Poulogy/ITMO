package com.lab3.service;

import com.lab3.config.InfluxBDConfig;
import com.lab3.module.PointBean;
import com.lab3.util.HitChecker;
import com.lab3.util.JWTUtil;
import jakarta.inject.Inject;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@WebService(
        serviceName = "PointService",
        targetNamespace = "http://lab3.itmo/",
        portName = "PointServicePort"
)
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public class PointService {
    private static final Logger logger = Logger.getLogger(PointService.class.getName());

    @Inject
    private ValidationService validationService;

    @Inject
    private InfluxBDConfig clientBD;

    @Inject
    private JWTUtil jwtUtil; // <-- ДОБАВЛЕНО

    private String getUsernameOrThrow(String token) {
        String username = jwtUtil.validateToken(token);
        if (username == null) {
            logger.warning("Access denied: Invalid or expired token.");
            throw new RuntimeException("Требуется авторизация (недействительный или истекший токен)");
        }
        return username;
    }

    @WebMethod
    public PointBean addPoint(
            @WebParam(name = "x") double x,
            @WebParam(name = "y") double y,
            @WebParam(name = "r") double r,
            @WebParam(name = "token") String token) {

        String username = getUsernameOrThrow(token);
        logger.info(String.format("Adding point: x=%.2f, y=%.2f, r=%.2f for user: %s", x, y, r, username));

        long startTime = System.nanoTime();
        validationService.validatePoint(x, y, r);
        boolean hit = HitChecker.checkHit(x, y, r);
        long scriptTime = System.nanoTime() - startTime;

        PointBean point = new PointBean(x, y, r, hit, null, scriptTime, username);

        clientBD.addPoint(point);

        logger.info("Point added successfully for user: " + username + point);
        return point;
    }

    @WebMethod
    public List<PointBean> getPoints(@WebParam(name = "token") String token) {

        String username = getUsernameOrThrow(token);
        Optional<List<PointBean>> pointsOptional = clientBD.findPointsByUser(username);
        logger.info("Fetching points for username: " + username);

        return pointsOptional.orElseGet(ArrayList::new);
    }
}
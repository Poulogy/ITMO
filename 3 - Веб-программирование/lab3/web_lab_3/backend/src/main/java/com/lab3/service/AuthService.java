package com.lab3.service;

import com.lab3.config.InfluxBDConfig;
import com.lab3.module.User;
import com.lab3.util.JWTUtil;
import com.lab3.util.PasswordUtil;
import jakarta.inject.Inject;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.xml.ws.BindingType;
import jakarta.xml.ws.soap.SOAPBinding;

import java.util.Optional;
import java.util.logging.Logger;


@WebService(
        serviceName = "AuthService",
        targetNamespace = "http://lab3.itmo/",
        portName = "AuthServicePort"
)
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    @Inject
    private InfluxBDConfig clientBD;

    @Inject
    private JWTUtil jwtUtil;

    @WebMethod
    public String register(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) {

        logger.info("Attempting to register user: " + username);

        if (clientBD.findUserByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        String passwordHash = PasswordUtil.hashPassword(password);

        User user = new User(username, passwordHash);

        clientBD.registerUser(user);

        logger.info("User registered successfully: " + username);
        return "Регистрация успешна";
    }

    @WebMethod
    public String login(
            @WebParam(name = "username") String username,
            @WebParam(name = "password") String password) {

        logger.info("Attempting to login user: " + username);

        Optional<User> userOptional = clientBD.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Неверные учетные данные");
        }
        User user = userOptional.get();
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            logger.info("Attempting to login user: " + user.getPasswordHash() + password);
            throw new RuntimeException("Неверные учетные данные, неверный пароль");
        }
        user.setUsername(username);

        String token = jwtUtil.generateToken(username);

        logger.info("User logged in successfully: " + username);
        return token;
    }

    @WebMethod
    public String logout(@WebParam(name = "token") String token) {
        return "Выход выполнен успешно";
    }

}
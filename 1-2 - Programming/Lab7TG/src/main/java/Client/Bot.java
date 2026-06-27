package Client;


import Client.manager.ScriptManager;

import Common.modules.*;
import Common.system.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.*;


public class Bot extends TelegramLongPollingBot {
    final private String BOT_TOKEN = "8328902339:AAGUuG-MpmtYOpgz2Oq31rEiPMaOPauq4xY";
    final private String BOT_NAME = "FlatsCollectionBot";
    /**
     * Получить состояние бота у определённого чата
     */
    public BotState getUserStates(String chI) {
        return userStates.getOrDefault(chI, BotState.Defo);
    }
    /**
     * Bot State у каждого айди
     */
    private final Map<String, BotState> userStates = new HashMap<>();
    /**
     * Пароль у каждого айди
     */
    private final Map<String, String> userPass = new HashMap<>();
    /**
     * Логин у каждого айди
     */
    private final Map<String, String> userLogin = new HashMap<>();
     /**
      * Bot State setter
     */
    public void setBotState(String userId, BotState botState) {
        userStates.put(userId, botState);
    }
    /**
     * port
     */
    int Port;

    /**
     * Порт по умолчанию
     */
    private static int PORT = 65435;
    /**
     * Хост, к которому пытается подключиться клиент.
     */
    private static String HOST = "MNGNZHS14065";
    /**
     * Попытки подключиться к серверу
     */
    private static int attempts = 0;

    /**
     * Канал для обмена данными с сервером.
     */
    private static SocketChannel clientChannel;
    /**
     * Режим переподключения.
     */
    private static boolean reconnectionMode = false;
    /**
     * Получить логин
     */
    public String getLogin(String chatid) {
        return userLogin.getOrDefault(chatid, null);
    }
    /**
     * Получить пароль
     */
    public String getPassword(String chatid) {
        return userPass.getOrDefault(chatid, null);
    }
    /**
     * Задать логин у айди чата
     */
    public void setLogin(String login, String CHATID) {
        userLogin.put(CHATID, login);
    }
    /**
     * Задать пароль у айди чата
     */
    public void setPassword(String password, String CHATID) {
        userPass.put(CHATID, password);
    }

    /**
     * Request
     */
    Request request;
    /**
     * Update
     */
    public Update update;
    public String command;
    public long id;
    public String FileName;

    private Flat flat = new Flat();
    private String name;
    private double area;
    private long numberOfBathrooms;
    private int numberOfRooms;
    private float x;
    private Long y;
    private House house = new House();
    private String HouseName;
    private int year;
    private long numberOfFlatsOnFloor;
    private Transport transport;
    private String transportType;




    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                this.update = update;
                //Извлекаем из объекта сообщение пользователя
                Message inMess = update.getMessage();
                //Достаем из inMess id чата пользователя
                String chatId = inMess.getChatId().toString();
                //Получаем текст сообщения пользователя, отправляем в написанный нами обработчик
                String response = parseMessage(inMess.getText(), chatId);
                //Создаем объект класса SendMessage - наш будущий ответ пользователю
                SendMessage outMess = new SendMessage();
                //Добавляем в наше сообщение id чата, а также наш ответ
                outMess.setChatId(chatId);
                outMess.setText(response);
                //Отправка в чат
                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMess(String message, String userChatId) throws TelegramApiException {
        SendMessage outMess = new SendMessage();
        outMess.setChatId(userChatId);
        outMess.setText(message);
        execute(outMess);
    }
    String TgResponse = ".";
    public String parseMessage(String textMsg, String chId) throws TelegramApiException {
        BotState botState = getUserStates(chId);

        try{
            switch (botState) {
                case Defo:
                    if (textMsg.equals("/start")) {
                        TgResponse = "HI, bot makes flatcollections. Do you want to use default port? [y/n]";
                        setBotState(chId, BotState.Port);
                    } else TgResponse = "Error, pls enter /start";
                    break;
                case Port:
                    switch (textMsg) {
                        case "n", "N":
                            setBotState(chId, BotState.PortNo);
                            TgResponse = "Enter port (1-65535):";
                            break;
                        case "y", "Y":
                            setBotState(chId, BotState.Start);
                            parseMessage("", chId);
                            break;
                        default:
                            TgResponse = "You have entered an invalid value, please try again!";
                            setBotState(chId, BotState.Port);
                    }
                    break;
                case PortNo:
                    try {
                        this.Port = Integer.parseInt(textMsg);
                        if (Port > 0 & Port <= 65535) {
                            PORT = Port;
                            setBotState(chId, BotState.Start);
                            parseMessage("", chId);
                        } else {
                            TgResponse = "Out of range port error, try again!";
                            sendMess("Out of range port error, try again!", chId);
                            setBotState(chId, BotState.Port);
                        }
                    } catch (IllegalArgumentException e) {
                        TgResponse = "Illegal port value, try again!";
                        sendMess("Illegal port value, try again!", chId);
                        setBotState(chId, BotState.Port);
                    }
                    break;
                case Start:
                    clientChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
                    clientChannel.configureBlocking(false);
                    attempts = 0;
                    setBotState(chId, BotState.Account);
                    TgResponse = "Client connected\nDo you have an account? [y/n]";
                    break;
                case Account:
                    if (textMsg.equals("y") || textMsg.equals("Y")) {
                        setBotState(chId, BotState.Login);
                        TgResponse = "Welcome to authorization!\nEnter username (must be more than 7 symbols).";
                    }
                    if (textMsg.equals("n") || textMsg.equals("N")) {
                        setBotState(chId, BotState.LoginIn);
                        TgResponse = "Welcome to registration!\nEnter username (must more than be 7 symbols).";
                    }
                    break;
                case Login:
                    if (textMsg.length() < 7) {
                        TgResponse = "Username is short, try again";
                    }
                    else {
                        setLogin(textMsg, chId);
                        setBotState(chId, BotState.Password);
                        TgResponse = "Enter password (must be more than 5 symbols).";
                    }
                    break;
                case Password:
                    if (textMsg.length() < 5) {
                        TgResponse = "Password too short, try again";
                    }
                    else {
                        setPassword(textMsg, chId);
                        setBotState(chId, BotState.SignInDone);
                        request = new Request(getLogin(chId), textMsg, RequestType.LOGIN);
                        Response res = ClientInit.send(clientChannel, request, chId);
                        sendMess(res.getData(), chId);
                        Client.authorization.validateRegistration(res, chId);
                        parseMessage("start", chId);
                    }
                    break;
                case SignInDone:
                    TgResponse = "Try again! Do you want to use default account? [y/n]";
                    setBotState(chId, BotState.Account);
                    break;
                case LoginIn:
                    if (textMsg.length() < 7) {
                        TgResponse = "Username is short, try again";
                    }
                    else {
                        setLogin(textMsg, chId);
                        setBotState(chId, BotState.PasswordIn);
                        TgResponse = "Enter password (must be 5 symbols).";
                    }
                    break;
                case PasswordIn:
                    if (textMsg.length() < 5) {
                        TgResponse = "Password too short, try again";
                    } else {
                        setPassword(textMsg, chId);
                        setBotState(chId, BotState.Account);
                            request = new Request(getLogin(chId), textMsg, RequestType.REGISTER);
                        Response res = ClientInit.send(clientChannel, request, chId);
                        TgResponse = res.getData() + "\nDo you want to use default account? [y/n]";
                        }

                    break;
                case Enter:
                    Pattern pID = Pattern.compile("/remove_by_id \\d*");
                    Pattern pIDF = Pattern.compile("/update_id \\d*");
                    Pattern pS = Pattern.compile("/execute_script \\S+");

                    Matcher m = pID.matcher(textMsg);
                    Matcher mF = pIDF.matcher(textMsg);
                    Matcher mS = pS.matcher(textMsg);
                    if (textMsg.equals("start")) {
                        TgResponse = "Welcome! Type /help to get list of all commands.";
                    }
                    else if (m.find()) {
                        try{
                            String[] splitInput = textMsg.trim().split(" ");
                            String commandName = splitInput[0].toLowerCase(Locale.ROOT);
                            command = "/remove_by_id";
                            id = Integer.parseInt(splitInput[1].toLowerCase(Locale.ROOT));
                            if (id >= 0){
                                request = new Request(commandName, id, RequestType.COMMAND);
                                Response res = ClientInit.sendCommand(clientChannel, request, chId);
                                TgResponse = res.getData();
                            } else TgResponse = "Error, enter id > 0";
                        } catch (NoSuchElementException e){
                            sendMess("Enter valid Id", chId);
                        } catch (Exception e){
                            sendMess("Enter valid Id" + e.getMessage(), chId);
                        }
                    }
                    else if (textMsg.equals("/add") || textMsg.equals("/add_if_min") ) {
                        LocalDateTime creationDate = LocalDateTime.now();
                        flat.setCreationDate(creationDate);
                        TgResponse = "Please enter your flat name: ";
                        command = textMsg;
                        setBotState(chId, BotState.Name);
                    }
                    else if (mF.find()) {
                        try{
                            String[] splitInput = textMsg.trim().split(" ");
                            command = splitInput[0];
                            id = Integer.parseInt(splitInput[1]);
                            if (id >= 0){
                                LocalDateTime creationDate = LocalDateTime.now();
                                flat.setCreationDate(creationDate);
                                TgResponse = "Please enter your flat name: ";
                                setBotState(chId, BotState.Name);
                            } else TgResponse = "Error, enter id > 0";
                        } catch (NoSuchElementException e){
                            sendMess("Enter valid Id", chId);
                        } catch (Exception e){
                            sendMess("Enter valid Id" + e.getMessage(), chId);
                        }

                    }
                    else if (textMsg.equals("/info") || textMsg.equals("/show") || textMsg.equals("/print_ascending") || textMsg.equals("/head") ||
                            textMsg.equals("/remove_first") || textMsg.equals("/print_unique_house") || textMsg.equals("/help") || textMsg.equals("/clear")){
                        command = textMsg;
                        try{
                            request = new Request(textMsg, RequestType.COMMAND);
                            Response res = ClientInit.sendCommand(clientChannel, request, chId);
                            TgResponse = res.getData();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else if (textMsg.equals("/exit")){
                        setBotState(chId, BotState.Defo);
                        clientChannel.close();
                        TgResponse = "Exitting..., press /start to start again";
                    } else if (mS.find()) {
                        String[] splitInput = textMsg.trim().split(" ");
                        command = splitInput[0];
                        FileName = splitInput[1];
                        new ScriptManager(FileName, chId);
                        TgResponse = "SCRIPT EXECUTION ENDING";
                    }
                    else {
                        TgResponse = "You have entered an invalid command, please try again or type /help to see the list of commands!";
                    }
                    break;
                case Name:
                    try{
                        name = textMsg;
                        flat.setName(name);
                        setBotState(chId, BotState.Area);
                        TgResponse = "Enter flat area [1-539]: ";
                    } catch (Exception e){
                        sendMess("Name is not correct", chId);
                    }
                    break;
                case Area:
                    try{
                        area = Double.parseDouble(textMsg);
                        if (area > 539 || area <= 0) {
                            TgResponse = "Area is out of range, try again";
                        } else {
                            setBotState(chId, BotState.NumberOfBathrooms);
                            TgResponse = "Enter number of bathrooms [1,2,3...]: ";
                            flat.setArea(area);
                        }
                    } catch (Exception e){
                        sendMess("Area is not correct", chId);
                    }
                    break;
                case NumberOfBathrooms:
                    try{
                        numberOfBathrooms = Long.parseLong(textMsg);
                        if (numberOfBathrooms <= 0) {
                            TgResponse = "Number of bathrooms is out of range, try again";
                        } else {
                            setBotState(chId, BotState.NumberOfRooms);
                            TgResponse = "Enter number of rooms: [1-14]";
                            flat.setNumberOfBathrooms(numberOfBathrooms);
                        }
                    } catch (Exception e){
                        sendMess("Number of Bathrooms is not correct", chId);
                    }
                    break;
                case NumberOfRooms:
                    try{
                        numberOfRooms = Integer.parseInt(textMsg);
                        if (numberOfRooms <= 0 || numberOfRooms > 14) {
                            TgResponse = "Number of rooms is out of range, try again";
                        } else {
                            setBotState(chId, BotState.X);
                            TgResponse = "Enter X: [>-360]";
                            flat.setNumberOfRooms(numberOfRooms);
                        }
                    } catch (Exception e){
                        sendMess("Number of rooms is not correct", chId);
                    }
                    break;
                case X:
                    try{
                        x = Float.parseFloat(textMsg);
                        if (x <= -360) {
                            TgResponse = "X is out of range, try again";
                        } else {
                            setBotState(chId, BotState.Y);
                            TgResponse = "Enter Y: [>-118]";
                        }
                    } catch (Exception e){
                        sendMess("x is not correct", chId);
                    }
                    break;
                case Y:
                    try{
                        y = Long.parseLong(textMsg);
                        if (y <= -118) {
                            TgResponse = "Y is out of range, try again";
                        } else {
                            setBotState(chId, BotState.House);

                            Coordinates coordinates = new Coordinates(x, y);
                            flat.setCoordinates(coordinates);
                            TgResponse = "Coordinates was added! x = " + x + " y = " + y + "\nDo you want to add house? [y/n]";
                        }
                    } catch (Exception e){
                        sendMess("Y is not correct", chId);
                    }
                    break;
                case House:
                    if (textMsg.equals("y") || textMsg.equals("Y")) {
                        TgResponse = "Enter house name: ";
                        setBotState(chId, BotState.HouseName);
                    } else if (textMsg.equals("n") || textMsg.equals("N")) {
                        setBotState(chId, BotState.Transport);
                        TgResponse = "Do you want to add transport? [y/n]";
                    } else {
                        TgResponse = "Error, enter y or n";
                    }
                    break;
                case HouseName:
                    try{
                        HouseName = textMsg;
                        house.setName(HouseName);
                        setBotState(chId, BotState.HouseYear);
                        TgResponse = "Enter the year, when house was made: [1-153]";
                    } catch (Exception e){
                        sendMess("Name is not correct", chId);
                    }
                    break;
                case HouseYear:
                    try{
                        year = Integer.parseInt(textMsg);
                        if (153 < year || year <= 0) {
                            TgResponse = "Year is out of range, try again";
                        } else {
                            house.setYear(year);
                            setBotState(chId, BotState.NumberOfFlatsOnFloor);
                            TgResponse = "Enter number of flats on the floor: [1,2,3...]";
                        }
                    } catch (Exception e){
                        sendMess("Year is not correct", chId);
                    }
                    break;
                case NumberOfFlatsOnFloor:
                    try{
                        numberOfFlatsOnFloor = Long.parseLong(textMsg);
                        if (numberOfFlatsOnFloor >= 0) {
                            house.setNumberOfFlatsOnFloor(numberOfFlatsOnFloor);
                            flat.setHouse(house);
                            setBotState(chId, BotState.Transport);
                            TgResponse = "House was added! " + house + "\nDo you want to add transport? [y/n]";
                        } else {
                            TgResponse = "Number of flats on the floor is out of range, try again";
                        }
                    } catch (Exception e){
                        sendMess("Name is not correct", chId);
                    }
                    break;
                case Transport:
                    if (textMsg.equals("y") || textMsg.equals("Y")) {
                        TgResponse = "Enter transport: \n    [FEW,\n" +
                                "    NONE,\n" +
                                "    LITTLE,\n" +
                                "    NORMAL,\n" +
                                "    ENOUGH]";
                        setBotState(chId, BotState.TransportGet);
                    } else if (textMsg.equals("n") || textMsg.equals("N")) {
                        flat.setTransport(null);
                        if (command.equals("/add") || command.equals("/add_if_min")) {
                            request = new Request(command, flat, RequestType.COMMAND);
                            Response res = ClientInit.sendCommand(clientChannel, request, chId);
                            TgResponse = res.getData();
                        } else if (command.equals("/update_id")) {
                            request = new Request(command, id, flat, RequestType.COMMAND);
                            Response res = ClientInit.sendCommand(clientChannel, request, chId);
                            TgResponse = res.getData();
                        } else {
                            TgResponse = "Error";
                        }
                        setBotState(chId, BotState.Enter);
                    } else{
                        TgResponse = "Error, enter y or n: ";
                    }
                    break;
                case TransportGet:
                    try{
                        transportType = textMsg;
                        transport = Transport.valueOf(transportType);
                        flat.setTransport(transport);
                        if (command.equals("/add") || command.equals("/add_if_min")) {
                            request = new Request(command, flat, RequestType.COMMAND);
                            Response res = ClientInit.sendCommand(clientChannel, request, chId);
                            TgResponse = res.getData();
                        } else if (command.equals("/update_id")) {
                            request = new Request(command, id, flat, RequestType.COMMAND);
                            Response res = ClientInit.sendCommand(clientChannel, request, chId);
                            TgResponse = res.getData();
                        } else {
                            TgResponse = "Error " + id + " " + command;
                        }
                        setBotState(chId, BotState.Enter);
                    } catch (Exception e){
                        sendMess("Error, enter right transport name: \n    [FEW,\n" +
                                "    NONE,\n" +
                                "    LITTLE,\n" +
                                "    NORMAL,\n" +
                                "    ENOUGH]", chId);
                    }
                    break;

                default:
                    TgResponse = "Invalid input";
            }
        } catch (UnresolvedAddressException e) {
            sendMess("There is no server with such host, try again", chId);
            setBotState(chId, null);
        } catch (IOException e) {
            sendMess("Server is not available, attempt #" + (attempts + 1), chId);
            reconnectionMode = true;
            if (attempts == 4) {
                sendMess("Reconnection failed. Type /start to try again.", chId);
                setBotState(chId, null);
            }
            attempts++;
            ScriptManager.callStack.clear();
            parseMessage(textMsg, chId);
        } catch (NoSuchElementException e) {
            sendMess("Error exiting...", chId);
            setBotState(chId, null);
            System.exit(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return TgResponse;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
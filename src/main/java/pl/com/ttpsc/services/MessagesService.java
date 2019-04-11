package pl.com.ttpsc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class MessagesService {

    private static MessagesService messagesService;

    private MessagesService (){}

    public static MessagesService getInstance(){
        if (messagesService == null){
            messagesService = new MessagesService();
        }
        return messagesService;
    }

    final static Logger logger = Logger.getLogger(MessagesService.class);

    UserService userService = UserService.getInstance();
    LogonService logonService = LogonService.getInstance();

    static final String GET_DATA_FROM_NEW_MESSAGES = "SELECT Id, IdSender, Message, IdReceiver FROM Messages WHERE Status = 'NEW'";
    static final String INSERT_NEW_MESSAGE = "INSERT INTO Messages (IdSender, IdReceiver, Message, Status) VALUES (?, ?, ?, ?)";
    static final String GET_DATA_TO_REPLAY = "SELECT IdSender FROM Messages WHERE Id = ?";
    static final String GET_ID_FROM_MESSAGES = "SELECT Id FROM Messages WHERE IdReceiver =?";
    static final String DELETE_MESSAGE = "DELETE FROM Messages WHERE Id = ? AND IdReceiver = ?";
    static final String GET_DATA_FROM_MESSAGES_TO_DISPLAY = "SELECT Id, IdSender, Message, Status FROM Messages WHERE IdReceiver = ?";
    static final String UPDATE_MESSAGE_STATUS = "UPDATE Messages SET Status = ? WHERE Id = ?";

    public ResultSet getDataFromMessages () throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_DATA_FROM_NEW_MESSAGES);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public boolean checkingIfThereIsAnyNewMessageForUser (int idUser) throws SQLException {
        boolean checking = false;
        ResultSet resultSet = getDataFromMessages();
        while (resultSet.next()){
            int idUserFromData = resultSet.getInt("IdReceiver");
            if (idUserFromData == idUser){
                checking = true;
            }
        }
        return checking;
    }

    public void manageMessages () throws SQLException {
        logger.debug("Displaying menu with options to manage menu");
        boolean switchGoes = true;

        do {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(GeneralMessages_en.ENTER_DATA_34);
                int number = scanner.nextInt();
                switch (number) {
                    case 1:
                        sendTheMessage();
                        break;
                    case 2:
                        replayTheMessage();
                        break;
                    case 3:
                        deleteTheMessage();
                        break;
                    case 4:
                        displayNewMessages();
                        break;
                    case 5:
                        displayAllMessages();
                        break;
                    case 6:
                        switchGoes = false;
                        break;
                    default:
                        System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                }
            }catch (InputMismatchException e){
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
                logger.error(e.getMessage(), e);
            }
        }while (switchGoes);
    }

    private void displayAllMessages() throws SQLException {
        int idUser = logonService.getIdUserWhoHasLogged();
        ResultSet resultSet = getDataToDisplay(idUser);
        while (resultSet.next()){
            int id = resultSet.getInt("Id");
            int idSender = resultSet.getInt("IdSender");
            String message = resultSet.getString("Message");
            String status = resultSet.getString("Status");
            String from = userService.getUserNameSurnameFromId(idSender);
            String displayNew = "";
            System.out.println(GeneralMessages_en.INFO_STATEMENT_4);
            if (status.equals("NEW")){
                displayNew = "###### " +status +" ######";
                System.out.println("Message number: "+ id+ "\nFrom: "+from+
                        "\nMessage: "+ message+ "\nStatus:" +displayNew+
                        "\n___________________________________________");
            } else {

                System.out.println("Message number: " + id +"\nFrom: "+from+
                        "\nMessage: " + message + "\nStatus:" + status +
                        "\n___________________________________________");
            }
        }
    }

    private void displayNewMessages() throws SQLException {
        int idUser = logonService.getIdUserWhoHasLogged();
        ResultSet resultSet = getDataToDisplay(idUser);
        while (resultSet.next()) {
            int id = resultSet.getInt("Id");
            int idSender = resultSet.getInt("IdSender");
            String message = resultSet.getString("Message");
            String status = resultSet.getString("Status");
            String from = userService.getUserNameSurnameFromId(idSender);
            String displayNew = "";
            if (status.equals("NEW")) {
                System.out.println(GeneralMessages_en.INFO_STATEMENT_4);
                displayNew = "###### " + status + " ######";
                System.out.println("Message number: " + id + "\nFrom: " + from +
                        "\nMessage: " + message + "\nStatus:" + displayNew +
                        "\n___________________________________________");
                updateMessageStatus(id);
            } else {
                System.out.println(GeneralMessages_en.INFO_STATEMENT_5);
            }
        }
    }

    private void deleteTheMessage() throws SQLException {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_39);
            int id = scanner.nextInt();

            int idUser = logonService.getIdUserWhoHasLogged();
            if (checkingIfSuchIdMessageExists(id, idUser)){
                setDeleteMessage(id, idUser);
                checking = false;
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }

        }while (checking);
    }

    private void replayTheMessage() throws SQLException {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_37);
            String message = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_38);
            int idMessage = scanner.nextInt();

            int idSender = logonService.getIdUserWhoHasLogged();
            if (checkingIfSuchIdMessageExists(idMessage, idSender)){
                int idReceiver = getIdReceiverToReplay(idMessage);

                insertNewMessage(idSender, idReceiver, message);
                checking = false;
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
            } else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_3);
            }
        }while (checking);
    }

    private void sendTheMessage() throws SQLException {
        boolean checking = true;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println(GeneralMessages_en.ENTER_DATA_35);
            String receiverName = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_36);
            String receiverSurname = scanner.nextLine();

            System.out.println(GeneralMessages_en.ENTER_DATA_37);
            String message = scanner.nextLine();

            if (userService.checkingIfUserExists(receiverName, receiverSurname)){
                int idSender = logonService.getIdUserWhoHasLogged();
                int idReceiver = userService.getIdFromUser(receiverName, receiverSurname);
                insertNewMessage(idSender, idReceiver, message);
                checking = false;
                System.out.println(GeneralMessages_en.CORRECT_STATEMNET_5);
            }else {
                System.out.println(GeneralMessages_en.WORNING_STATEMENT_2);
            }

        }while (checking);
    }

    public void insertNewMessage (int idSender, int idReceiver,String message) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(INSERT_NEW_MESSAGE);
        preparedStatement.setInt(1, idSender);
        preparedStatement.setInt(2, idReceiver);
        preparedStatement.setString(3, message);
        preparedStatement.setString(4, "NEW");
        preparedStatement.execute();
    }

    public int getIdReceiverToReplay (int id) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_DATA_TO_REPLAY);
        preparedStatement.setInt(1, id);
        int idSender = 0;
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            idSender = resultSet.getInt("idSender");
        }
        return idSender;
    }

    public boolean checkingIfSuchIdMessageExists (int idMessage, int idReceiver) throws SQLException {
        boolean checking = false;
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_ID_FROM_MESSAGES);
        preparedStatement.setInt(1, idReceiver);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int idFromBase = resultSet.getInt("Id");
            if(idFromBase == idMessage){
                checking = true;
            }
        }
        return checking;
    }

    public void setDeleteMessage (int id, int idUser) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(DELETE_MESSAGE);
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, idUser);
        preparedStatement.execute();
    }

    public ResultSet getDataToDisplay (int idUser) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(GET_DATA_FROM_MESSAGES_TO_DISPLAY);
        preparedStatement.setInt(1, idUser);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public void updateMessageStatus (int id) throws SQLException {
        PreparedStatement preparedStatement = MenuService.getInstance().connection.prepareStatement(UPDATE_MESSAGE_STATUS);
        preparedStatement.setString(1, "READ");
        preparedStatement.setInt(2, id);
        preparedStatement.execute();
    }
}

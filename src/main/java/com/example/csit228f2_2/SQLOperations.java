package com.example.csit228f2_2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLOperations {

    private static final String URL = "jdbc:mysql://localhost:3306/javadb";
    private static final String USERNAME = "syjhert";
    private static final String PASSWORD = "syjhert";
    private static Connection getConnection(){
        Connection c = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException(e);
        }
        return c;
    }


    public static void initTables(){
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){
            String createUserTableQuery = "CREATE TABLE IF NOT EXISTS tbluser(" +
                    "userID INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(100) NOT NULL" +
                    ")";
            String createMessageTableQuery = "CREATE TABLE IF NOT EXISTS tblmessage(" +
                    "messageID INT AUTO_INCREMENT PRIMARY KEY," +
                    "userID INT NOT NULL," +
                    "content VARCHAR(200) NOT NULL," +
                    "dateTimeSent DATETIME NOT NULL," +
                    "isEdited INT(1) NOT NULL DEFAULT 0," +
                    "FOREIGN KEY (userID) REFERENCES tbluser(userID) ON UPDATE CASCADE ON DELETE CASCADE" +
                    ")";
            statement.execute(createUserTableQuery);
            statement.execute(createMessageTableQuery);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void insertUser(String username, String password){
        try(Connection c = getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "INSERT INTO tbluser (username, password) VALUES (?, ?)")){

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("User inserted successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateUsername(int userID, String username){
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String sqlUpdate = "UPDATE tbluser SET username='"+username+"' WHERE userID="+userID;
            int rowsUpdated = statement.executeUpdate(sqlUpdate);

            if(rowsUpdated > 0){
                System.out.println("Username updated successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updatePassword(int userID, String password){
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String sqlUpdate = "UPDATE tbluser SET password='"+password+"' WHERE userID="+userID;
            int rowsUpdated = statement.executeUpdate(sqlUpdate);

            if(rowsUpdated > 0){
                System.out.println("Password updated successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteUser(int userID){
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String sqlDelete = "DELETE FROM tbluser WHERE userID="+userID;
            int rowsDeleted = statement.executeUpdate(sqlDelete);

            if(rowsDeleted > 0){
                System.out.println("User deleted successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static int getUserID(String username){  //returns id number or -1 if not exists
        int toReturn = -1;
        try(Connection c = getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM tbluser WHERE username=?")){

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                toReturn = resultSet.getInt("userID");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public static boolean checkPassword(int userID, String password){
        boolean toReturn = false;
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String query = "SELECT * FROM tbluser WHERE userID="+userID;

            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()){
                toReturn = resultSet.getString("password").equals(password);
            }else toReturn = false;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String getUsername(int userID){
        String toReturn = "null";
        try(Connection c = getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM tbluser WHERE userID=?")){

            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                toReturn = resultSet.getString("username");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    //MESSAGE

    public static void insertMessage(Message message){
        try(Connection c = getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "INSERT INTO tblmessage (userID, content, dateTimeSent) VALUES (?, ?, ?)")){

            preparedStatement.setInt(1, message.getUserID());
            preparedStatement.setString(2, message.getContent());
            preparedStatement.setString(3, message.getDateTimeSent());

            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("Message inserted successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateMessageContent(int messageID, String content){
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String sqlUpdate = "UPDATE tblmessage SET content='"+content+"', isEdited=1 WHERE messageID="+messageID;
            int rowsUpdated = statement.executeUpdate(sqlUpdate);

            if(rowsUpdated > 0){
                System.out.println("Message content updated successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteMessage(int messageID){
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String sqlDelete = "DELETE FROM tblmessage WHERE messageID="+messageID;
            int rowsDeleted = statement.executeUpdate(sqlDelete);

            if(rowsDeleted > 0){
                System.out.println("Message deleted successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static List<Message> getMessages(int userID){
        List<Message> messages = new ArrayList<>();
        try(Connection c = getConnection();
            Statement statement = c.createStatement()){

            String query = "SELECT * FROM tblmessage WHERE userID="+userID;

            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                Message m = new Message(
                        resultSet.getInt("messageID"),
                        resultSet.getInt("userID"),
                        resultSet.getString("content"),
                        resultSet.getString("dateTimeSent"),
                        resultSet.getInt("isEdited") == 1
                );
                messages.add(m);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }


//    public static List<String[]> retrieveAllData(){
//        List<String[]> result = new ArrayList<>();
//
//        try(Connection c = getConnection();
//            Statement statement = c.createStatement()){
//
//            String selectQuery = "SELECT * FROM users";
//            ResultSet resultSet = statement.executeQuery(selectQuery);
//
//            while(resultSet.next()){
//                String[] data = new String[3];
//                data[0] = Integer.toString(resultSet.getInt("id"));
//                data[1] = resultSet.getString("name");
//                data[2] = resultSet.getString("email");
//
//                result.add(data);
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//        return result;
//    }
}

package com.example.csit228f2_2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLOperations {
    public static void insertData(String username, String password){
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)")){

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsInserted = preparedStatement.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("Data inserted successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static List<String[]> retrieveALlData(){
        List<String[]> result = new ArrayList<>();

        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()){

            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()){
                String[] data = new String[3];
                data[0] = Integer.toString(resultSet.getInt("id"));
                data[1] = resultSet.getString("name");
                data[2] = resultSet.getString("email");

                result.add(data);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean checkIfDataExists(String username, String password){
        boolean toReturn = true;
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?")){

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                toReturn = true;
            }else{
                toReturn = false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }
}

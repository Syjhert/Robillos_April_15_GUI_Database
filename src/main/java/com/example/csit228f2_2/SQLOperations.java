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
    public static void updateUsername(int id, String username){
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()){

            String sqlUpdate = "UPDATE users SET username='"+username+"' WHERE id="+id;
            int rowsUpdated = statement.executeUpdate(sqlUpdate);

            if(rowsUpdated > 0){
                System.out.println("Data updated successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updatePassword(int id, String password){
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()){

            String sqlUpdate = "UPDATE users SET password='"+password+"' WHERE id="+id;
            int rowsUpdated = statement.executeUpdate(sqlUpdate);

            if(rowsUpdated > 0){
                System.out.println("Data updated successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteData(int id){
        try(Connection c = MySQLConnection.getConnection();
            Statement statement = c.createStatement()){

            String sqlDelete = "DELETE FROM users WHERE id="+id;
            int rowsDeleted = statement.executeUpdate(sqlDelete);

            if(rowsDeleted > 0){
                System.out.println("Data deleted successfully");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static List<String[]> retrieveAllData(){
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

    public static int checkIfDataExists(String username, String password){  //returns id number
        int toReturn = -1;
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?")){

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                toReturn = resultSet.getInt("id");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String getUsername(int id){
        String toReturn = "null";
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(
                    "SELECT * FROM users WHERE id=?")){

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                toReturn = resultSet.getString("username");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return toReturn;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tvst.api;

import static com.tvst.api.Controller.logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author pedja
 */
public class DBUtility
{

    Connection conn;

    public DBUtility(HttpServlet servlet)
    {
        this.conn = (Connection) servlet.getServletContext().getAttribute("DBConnection");
    }

    public User getUser(String email, String password) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement("select id, email, first_name, last_name from user where email=? and password=? limit 1");
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs != null && rs.next())
            {
                return getUser(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
        return null;
    }

    public User getUser(String auth_key) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement("select id, email, first_name, last_name from user where auth_key=? limit 1");
            ps.setString(1, auth_key);
            rs = ps.executeQuery();

            if (rs != null && rs.next())
            {
                return getUser(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
        return null;
    }

    private User getUser(ResultSet rs) throws SQLException
    {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastname(rs.getString("last_name"));
        return user;
    }

    public boolean insertUser(User user) throws ServletException
    {
        PreparedStatement ps = null;
        try
        {
            ps = conn.prepareStatement("insert into user(first_name, last_name, email, password) values (?,?,?,?)");
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastname());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.execute();
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

    public void insertUsersInTx(List<User> users) throws ServletException
    {
        PreparedStatement ps = null;
        try
        {
            conn.setAutoCommit(false);
            for (User user : users)
            {
                ps = conn.prepareStatement("insert into user(first_name, last_name, email, password) values (?,?,?,?)");
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastname());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                ps.executeUpdate();
            }
            conn.commit();
        }
        catch (SQLException e)
        {
            //if we get error user probably exists
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

    public void updateUser(int id, String column, String value) throws ServletException
    {
        PreparedStatement ps = null;
        try
        {
            ps = conn.prepareStatement(String.format("UPDATE user SET %s = ? WHERE id = ?", column));
            ps.setString(1, value);
            ps.setInt(2, id);
            ps.execute();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

    public List<String> getUsersItems(int userId) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            List<String> items = new ArrayList<>();
            ps = conn.prepareStatement(String.format("SELECT item_id FROM items WHERE user_id = ?"));
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs != null)
            {
                while (rs.next())
                {
                    items.add(rs.getString("item_id"));
                }
            }
            return items;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

    public int getUserIdFromSession(String authKey) throws ServletException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = conn.prepareStatement(String.format("SELECT user_id FROM session WHERE auth_key = ? LIMIT 1"));
            ps.setString(1, authKey);
            rs = ps.executeQuery();
            if (rs != null && rs.next())
            {
                return rs.getInt("user_id");
            }
            else
            {
                return -1;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

    public boolean insertSession(String authKey, int userId) throws ServletException
    {
        PreparedStatement ps = null;
        try
        {
            ps = conn.prepareStatement("insert into session(auth_key, user_id) values (?,?)");
            ps.setString(1, authKey);
            ps.setInt(2, userId);
            ps.execute();
            return true;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }

    public void insertItemsInTx(String[] items, int userId) throws ServletException
    {
        PreparedStatement ps = null;
        try
        {
            conn.setAutoCommit(false);
            for (String item : items)
            {
                ps = conn.prepareStatement("insert into items(item_id, user_id) values (?,?)");
                ps.setString(1, item);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
            conn.commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }
    
    public void deleteItemsInTx(String[] items, int userId) throws ServletException
    {
        PreparedStatement ps = null;
        try
        {
            conn.setAutoCommit(false);
            for (String item : items)
            {
                ps = conn.prepareStatement("DELETE FROM items WHERE user_id = ? AND item_id = ?");
                ps.setString(2, item);
                ps.setInt(1, userId);
                ps.executeUpdate();
            }
            conn.commit();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            logger.error("Database connection problem");
            throw new ServletException("DB Connection problem.");
        }
        finally
        {
            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (SQLException e)
            {
                logger.error("SQLException in closing PreparedStatement or ResultSet");
            }
        }
    }
}

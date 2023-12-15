package org.nyu.java.project.reminderregister.dao;

import org.nyu.java.project.reminderregister.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserDao {

    private final DataSource dataSource;


    private Logger logger = LoggerFactory.getLogger(UserDao.class);

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int insertUser(User user) {

        String sql = "INSERT INTO users " +
                "(username, email, password,isEmailVerified,role) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(sql)) {

            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getEmail());
            insertStatement.setString(3, user.getPassword());
            insertStatement.setBoolean(4, user.isEmailVerified());
            insertStatement.setString(5, user.getRoles());

            return insertStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error occurred while inserting record: {}", e.getMessage());
            return -1;
        }

    }

    public int updateUser(User user) {

        String sql = "UPDATE users SET " +
                "username = ?, email = ? , password = ?,otp = ? ,otpRequestedTime = ?,isEmailVerified = ? ,role = ?" +
                "WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement updateStatement = conn.prepareStatement(sql)) {

            updateStatement.setString(1, user.getUsername());
            updateStatement.setString(2, user.getEmail());
            updateStatement.setString(3, user.getPassword());
            updateStatement.setString(4, user.getOtp());
            updateStatement.setLong(5, user.getOtpRequestedTime());
            updateStatement.setBoolean(6, user.isEmailVerified());
            updateStatement.setString(7, user.getRoles());
            updateStatement.setString(8, user.getUsername());

            return updateStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error occurred while updating record: {}", e.getMessage());
            return -1;
        }

    }

    public Optional<User> findUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setOtp(rs.getString("otp"));
                    user.setOtpRequestedTime(rs.getLong("otpRequestedTime"));
                    user.setEmailVerified(rs.getBoolean("isEmailVerified"));
                    user.setRoles(rs.getString("role"));
                    logger.info("The user details are:{}", user);

//                    org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//                            getAuthorities(user));
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles()));

        return authorities;
    }

    public Optional<User> findUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setOtp(rs.getString("otp"));
                    user.setOtpRequestedTime(rs.getLong("otpRequestedTime"));
                    user.setEmailVerified(rs.getBoolean("isEmailVerified"));

                    logger.info("The user details are:{}", user);
                    return Optional.of(user);
                }
            } catch (Exception e) {
                logger.error("Error occurred while retrieving role record: {}", e.getMessage());
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error occurred while retrieving role record: {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


}

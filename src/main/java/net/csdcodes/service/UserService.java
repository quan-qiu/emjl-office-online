package net.csdcodes.service;

import net.csdcodes.model.Asset;
import net.csdcodes.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import net.csdcodes.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> getUser(){
        String sql = "select user_id, orgname,username,ssn,dept, password, email," +
                " enable,created_date,updated_date" +
                " from [dbo].[user] order by user_id asc";
        List users = jdbcTemplate.query(sql,  BeanPropertyRowMapper.newInstance(User.class));

        return users;
    }

    public User getUserById(Integer id){
        String sql = "select * from [dbo].[user] where user_id=?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, BeanPropertyRowMapper.newInstance(User.class));

        return user;
    }

    public List<User> getUserByRole(String role){
        String sql = "select u.username,u.email from [user] u where u.user_id in " +
                "(select ur.user_id from [role] r left join [user_role] ur on r.role_id=ur.role_id " +
                "where r.name=?);";
        List<User> users = jdbcTemplate.query(sql, new Object[]{role}, BeanPropertyRowMapper.newInstance(User.class));

        return users;
    }

    public User getUserBySsn(String userSsn){
        String sql = "select * from [dbo].[user] where ssn=?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{userSsn}, BeanPropertyRowMapper.newInstance(User.class));

        return user;
    }

    public int AddUser(User user){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        int result= jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[user]" +
                                        "(username,ssn,dept, password,email,enable, " +
                                        "created_date,updated_date,orgname)" +
                                        " values(?,?,?,?,?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, user.getUserName());
                        statement.setString(2, user.getSsn());
                        statement.setString(3, user.getDept());
                        statement.setString(4, encryptPassword(user.getPassword()));
                        statement.setString(5, user.getEmail());
                        statement.setInt(6, user.getEnable());
                        statement.setString(7,DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                        statement.setString(8, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                        statement.setString(9, user.getOrgName());
                        return statement;
                    }
                },
                keyHolder);

        //System.out.println(result);
        return keyHolder.getKey().intValue();

    }

    public int updatePassword(String userName, String password){
        int result = jdbcTemplate.update("UPDATE [dbo].[user] set password=? " +
                        "where username=?",
                new Object[]{encryptPassword(password),userName
                });
        return result;
    };

    public int updateUser(User user){
        LocalDateTime now = LocalDateTime.now();

        int result = jdbcTemplate.update("UPDATE [dbo].[user] set orgname=?, username=?, password=?, email=?,enable=?," +
                        "ssn=?,dept=?, " +
                        "updated_date=? " +
                        "where user_id=?",
                new Object[]{user.getOrgName(),user.getUserName(), user.getPassword(), user.getEmail(), user.getEnable(),
                        user.getSsn(),user.getDept(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd",
                        Locale.SIMPLIFIED_CHINESE).format(now),
                        user.getUserId()
                });

        return result;
    }

    public boolean checkUserName(String userName){

        String sql = "select count(*) from [dbo].[user] where username = ?";
        int num = jdbcTemplate.queryForObject(sql, new Object[]{userName}, Integer.class);

        if (num > 0){
            return true;
        }else{
            return false;
        }

    }

    public boolean checkEmail(String email){

        String sql = "select count(*) from [dbo].[user] where email = ?";
        int num = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);

        if (num > 0){
            return true;
        }else{
            return false;
        }
    }

    public User getUserByUsername(String username){
        String sql = "SELECT * FROM [dbo].[user] WHERE username = ?";

        return (User) jdbcTemplate.queryForObject(sql, new Object[]{username},new BeanPropertyRowMapper(User.class));
    };

    public Set<Role> getUserRoles(int user_id){
        Set<Role> roles = null;

        String sql = "select * from role where role.role_id " +
                "in " +
                "(select ur.role_id from [user] u left join user_role ur on u.user_id=ur.user_id " +
                "where u.user_id=?)";
        //System.out.println(sql);
        List<Role> userRoles= jdbcTemplate.query(sql,new Object[]{user_id}, BeanPropertyRowMapper.newInstance(Role.class));

        roles = new HashSet<>(userRoles);
        return roles;
    }

    public String encryptPassword(String password){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        return encoder.encode(password);

    }


}

package net.csdcodes.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(Integer.valueOf(rs.getString("user_id")));
        user.setUsername(rs.getString("username"));
        user.setsRoles(rs.getString("srole"));
        user.setEnable(rs.getInt("enable"));

        return user;
    }
}

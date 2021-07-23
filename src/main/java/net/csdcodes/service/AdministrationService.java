package net.csdcodes.service;

import net.csdcodes.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class AdministrationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Role> getRoles(){
        String sql = "select * from [dbo].[role] order by role_id asc";
        List roles = jdbcTemplate.query(sql,  BeanPropertyRowMapper.newInstance(Role.class));

        return roles;
    }

    public int addRole(Role role){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        int result= jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[role]" +
                                        "(name) " +
                                        " values(?)",
                                Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, role.getName());
                         return statement;
                    }
                },
                keyHolder);

        //System.out.println(result);
        return keyHolder.getKey().intValue();

    }

    public int updateRole(Role role){
        LocalDateTime now = LocalDateTime.now();

        int result = jdbcTemplate.update("UPDATE [dbo].[role] set name=? " +
                        "where role_id=?",
                new Object[]{role.getName(),role.getRoleId()
                });

        return result;
    }

    public Role getRoleById(int id){
        String sql = "select * from [dbo].[role] where role_id=?";
        Role role = jdbcTemplate.queryForObject(sql, new Object[]{id}, BeanPropertyRowMapper.newInstance(Role.class));

        return role;
    }

    public boolean checkRoleName(String roleName){

        String sql = "select count(*) from [dbo].[role] where name = ?";
        int num = jdbcTemplate.queryForObject(sql, new Object[]{roleName}, Integer.class);

        if (num > 0){
            return true;
        }else{
            return false;
        }

    }

    public List<HardwareType> getHardwareTypes(){
        String sql = "select * from [dbo].[hardware_type] order by seq_id asc";
        List hardwareTypes = jdbcTemplate.query(sql,  BeanPropertyRowMapper.newInstance(HardwareType.class));

        return hardwareTypes;
    }

    public int addHardwareType(HardwareType hardwareType){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        int result= jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[hardware_type]" +
                                        "(type,seq_id) " +
                                        " values(?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, hardwareType.getType());
                        statement.setInt(2, hardwareType.getSeqId());
                        return statement;
                    }
                },
                keyHolder);

        //System.out.println(result);
        return keyHolder.getKey().intValue();

    }

    public int updateHardwareType(HardwareType hardwareType){
        LocalDateTime now = LocalDateTime.now();

        int result = jdbcTemplate.update("UPDATE [dbo].[hardware_type] set type=?,seq_id=? " +
                        "where id=?",
                new Object[]{hardwareType.getType(),hardwareType.getSeqId(),hardwareType.getId()});

        return result;
    }

    public HardwareType getHardwareTypeById(int id){
        String sql = "select * from [dbo].[hardware_type] where id=?";
        HardwareType hardwareType = jdbcTemplate.queryForObject(sql, new Object[]{id}, BeanPropertyRowMapper.newInstance(HardwareType.class));

        return hardwareType;
    }

    public boolean checkHardwareTypeName(String hardwareTypeName){

        String sql = "select count(*) from [dbo].[hardware_type] where type = ?";
        int num = jdbcTemplate.queryForObject(sql, new Object[]{hardwareTypeName}, Integer.class);

        if (num > 0){
            return true;
        }else{
            return false;
        }

    }

    //////////////////////
    public List<HardwareStatus> getHardwareStatuses(){
        String sql = "select * from [dbo].[hardware_status] order by seq_id asc";
        List hardwareStatuses = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(HardwareStatus.class));

        return hardwareStatuses;
    }

    public int addHardwareStatus(HardwareStatus hardwareStatus){

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        int result= jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[hardware_status]" +
                                        "(name,seq_id) " +
                                        " values(?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, hardwareStatus.getName());
                        statement.setInt(2, hardwareStatus.getSeqId());
                        return statement;
                    }
                },
                keyHolder);

        //System.out.println(result);
        return keyHolder.getKey().intValue();

    }

    public int updateHardwareStatus(HardwareStatus hardwareStatus){
        LocalDateTime now = LocalDateTime.now();

        int result = jdbcTemplate.update("UPDATE [dbo].[hardware_status] set name=?,seq_id=? " +
                        "where id=?",
                new Object[]{hardwareStatus.getName(),hardwareStatus.getSeqId(),hardwareStatus.getId()});

        return result;
    }

    public HardwareStatus getHardwareStatusById(int id){
        String sql = "select * from [dbo].[hardware_status] where id=?";
        HardwareStatus hardwareStatus = jdbcTemplate.queryForObject(sql, new Object[]{id}, BeanPropertyRowMapper.newInstance(HardwareStatus.class));

        return hardwareStatus;
    }

    public boolean checkHardwareStatusName(String hardwareStatusName){

        String sql = "select count(*) from [dbo].[hardware_status] where name = ?";
        int num = jdbcTemplate.queryForObject(sql, new Object[]{hardwareStatusName}, Integer.class);

        if (num > 0){
            return true;
        }else{
            return false;
        }

    }

    public List<User> getUserRoles(){
        String sql="select u1.username,u1.user_id,u1.enable,t.srole" +
                "  from [user] u1 left join" +
                "  (SELECT  ur1.user_id," +
                "        srole = ( STUFF(( SELECT   concat(',',r.name)" +
                "                          FROM      user_role ur2 left join [role] r on ur2.role_id=r.role_id" +
                "                          WHERE     ur2.user_id = ur1.user_id" +
                "                        FOR" +
                "                          XML PATH('')" +
                "                        ), 1, 1, '') )" +
                " FROM user_role AS ur1" +
                " group by ur1.user_id) t" +
                " on u1.user_id=t.user_id";

        List userRoles = jdbcTemplate.query(sql, new UserRoleRowMapper());

        return userRoles;
    }

    public boolean updateAuthentication(int user_id, List<Role> roles){
        deleteRoleByUserId(user_id);
        return instertUserRoles(user_id, roles);
    }

    private void deleteRoleByUserId(int user_id){
        String sql = "delete from user_role where user_id=?";
         jdbcTemplate.update(sql, new Object[]{user_id});
    }

    private boolean instertUserRoles(int user_id, List<Role> roles){

        for (Role role:roles) {
            int result= jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[user_role]" +
                                            "(user_id, role_id) " +
                                            " values(?,?)");
                            statement.setInt(1, user_id);
                            statement.setInt(2, role.getRoleId());
                            return statement;
                        }
                    });

            if(result != 1){
                return false;
            }
        }

        return true;
    }

    public List<IpAddress> getIpAddresses(){
        String sql = "select * from [dbo].[ip_address] order by ip_address, type asc";
        List ipAddresses = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(IpAddress.class));

        return ipAddresses;
    }

    public Page<IpAddress> getIpAddressByPage(int pageNo, int pageSize){

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);


        String rowCountSql = "select count(*) as row_count from [dbo].[ip_address] ";


        int total = jdbcTemplate.queryForObject(rowCountSql, Integer.class);

        String sql = "select * from [dbo].[ip_address] " +
                "order by ip_address, type asc " +
                "offset ? Rows fetch next ? rows only";


        List<IpAddress> ipAddresses = jdbcTemplate.query(sql, new Object[]{(pageNo-1) * pageSize, pageSize},
                BeanPropertyRowMapper.newInstance(IpAddress.class));

        return new PageImpl<>(ipAddresses,pageable,total);
    }


    public boolean addIpAddress(IpAddress ipAddress){
        LocalDateTime now = LocalDateTime.now();

            int result= jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[ip_address]" +
                                    "(ip_address,type,device,id_password,comment,created_date,updated_date) " +
                                    " values(?,?,?,?,?,?,?)");
                            statement.setString(1, ipAddress.getIpAddress());
                            statement.setString(2, ipAddress.getType());
                            statement.setString(3, ipAddress.getDevice());
                            statement.setString(4, ipAddress.getIdPassword());
                            statement.setString(5, ipAddress.getComment());
                            statement.setString(6, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(7, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            return statement;
                        }
                    });

            if(result != 1){
                return false;
            }

        return true;
    }

    public IpAddress getIpAddressById(int id){
        String sql = "select * from [dbo].[ip_address] where id=?";
        IpAddress ipAddress = jdbcTemplate.queryForObject(sql, new Object[]{id},
                BeanPropertyRowMapper.newInstance(IpAddress.class));

        return ipAddress;
    }

    public int updateIpAddress(int id, IpAddress ipAddress){
        LocalDateTime now = LocalDateTime.now();

        int result = jdbcTemplate.update("UPDATE [dbo].[ip_address] set ip_address=?,type=?," +
                        "device=?,id_password=?,comment=?,updated_date=? " +
                        "where id=?",
                new Object[]{ipAddress.getIpAddress(),ipAddress.getType(),
                        ipAddress.getDevice(),ipAddress.getIdPassword(),ipAddress.getComment(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        id});

        return result;
    }

    public int deleteIpAddress(int id){
        int result = jdbcTemplate.update("DELETE from ip_address where id=?",
                new Object[]{id}
        );

        return result;
    }


}

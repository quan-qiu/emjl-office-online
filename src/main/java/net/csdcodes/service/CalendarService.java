package net.csdcodes.service;

import net.csdcodes.model.Asset;
import net.csdcodes.model.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Service
@Transactional
public class CalendarService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Calendar> getSpecialDay(Integer sYear) {

        String sql = "select * from [dbo].[calendar] " +
                "where year([special_date])=? order by special_date";
        List specialDays = jdbcTemplate.query(sql, new Object[]{sYear}, BeanPropertyRowMapper.newInstance(Calendar.class));

        return specialDays;
    }

    public int save(Calendar calendar) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        int result= jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement("INSERT INTO calendar(special_date, type, flag)" +
                                        " values(?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, simpleDateFormat.format(calendar.getSpecialDate()));
                        statement.setString(2, calendar.getType());
                        statement.setString(3, calendar.getFlag());
                        return statement;
                    }
                },
              keyHolder);

        //System.out.println(result);
        return keyHolder.getKey().intValue();
    }

    public Calendar getCalendarById(int id) throws SQLException{

        String sql = "select * from [dbo].[calendar] where id=? ";

        return (Calendar) jdbcTemplate.queryForObject(sql, new Object[]{id},new BeanPropertyRowMapper(Calendar.class));
    }

    public int deleteSpecialDay(int id){
        int result = jdbcTemplate.update("DELETE from calendar where id=?",
                new Object[]{id}
        );
        System.out.println(result);
        return result;
    }
}

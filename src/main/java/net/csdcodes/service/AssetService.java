package net.csdcodes.service;

import net.csdcodes.model.Asset;
import net.csdcodes.model.HardwareStatus;
import net.csdcodes.model.HardwareType;
import net.csdcodes.model.User;
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
import java.util.List;

@Service
@Transactional
public class AssetService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Asset> getAsset(String username, String type, String status) {

        username = username.equals("*") ? "%%" : username;
        type = type.equals("*") ? "%%" : type;
        status = status.equals("*") ? "%%" : status;

        String sql = "select * from [dbo].[asset] where user_name like ? and type like ? and status like ? order by purchase_date asc";
        List assets = jdbcTemplate.query(sql, new Object[]{"%" + username + "%", type, status}, BeanPropertyRowMapper.newInstance(Asset.class));

        return assets;
    }

    public List<Asset> getAsset(String ssn) {

        String sql = "select * from [dbo].[asset] where ssn = ? order by purchase_date asc";
        List assets = jdbcTemplate.query(sql, new Object[]{ssn}, BeanPropertyRowMapper.newInstance(Asset.class));

        return assets;
    }

    public Page<Asset> getAssetByPage( int pageNo, int pageSize,String username, String type, String status){

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        username = username.equals("*") ? "%%" : username;
        type = type.equals("*") ? "%%" : type;
        status = status.equals("*") ? "%%" : status;

        String rowCountSql = "Select count(*) as row_count " +
                "from [dbo].[asset] " +
                "where user_name like ? and type like ? and status like ? ";


        int total = jdbcTemplate.queryForObject(rowCountSql,
                new Object[]{"%" + username + "%", type, status}, Integer.class);

        String sql = "select * from [dbo].[asset] " +
                "where user_name like ? and type like ? and status like ? " +
                "order by purchase_date asc " +
                "offset ? Rows " +
                "fetch next ? rows only";


        List<Asset> assets = jdbcTemplate.query(sql, new Object[]{"%" + username + "%", type, status, (pageNo-1) * pageSize, pageSize},
                BeanPropertyRowMapper.newInstance(Asset.class));

        return new PageImpl<>(assets,pageable,total);
    }



    public int getAssetCount(String username, String type, String status) {

        username = username.equals("*") ? "%%" : username;
        type = type.equals("*") ? "%%" : type;
        status = status.equals("*") ? "%%" : status;

        String sql = "select count(*) from [dbo].[asset] where user_name like ? and type like ? and status like ?";
        int num = jdbcTemplate.queryForObject(sql, new Object[]{"%" + username + "%", type, status}, Integer.class);

        return num;
    }

    public Asset getAssetById(int id) throws SQLException{

        String sql = "select * from [dbo].[asset] where id=? ";

        return (Asset) jdbcTemplate.queryForObject(sql, new Object[]{id},new BeanPropertyRowMapper(Asset.class));
    }

    public List<HardwareType> getType() {
        String sql = "select id, type,seq_id from hardware_type order by seq_id";
        List types = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(HardwareType.class));

        return types;
    }

    public List<HardwareStatus> getStatus() {
        String sql = "select id, name from hardware_status order by seq_id";
        List status = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(HardwareStatus.class));

        return status;
    }

    public int save(Asset asset) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        int result= jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement statement = con.prepareStatement("INSERT INTO asset(user_name,ssn, dept, asset_type, type, " +
                                        "brand_model, sn, location, purchase_date, comment, asset_id, status)" +
                                        " values(?,?,?,?,?,?,?,?,?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS);
                        statement.setString(1, asset.getUserName());
                        statement.setString(2, asset.getSsn());
                        statement.setString(3, asset.getDept());
                        statement.setString(4, asset.getAssetType());
                        statement.setString(5, asset.getType());
                        statement.setString(6, asset.getBrandModel());
                        statement.setString(7, asset.getSn());
                        statement.setString(8, asset.getLocation());
                        statement.setString(9, simpleDateFormat.format(asset.getPurchaseDate()));
                        statement.setString(10, asset.getComment());
                        statement.setString(11, asset.getAssetId());
                        statement.setString(12, asset.getStatus());
                        return statement;
                    }
                },
                keyHolder);

        //System.out.println(result);
        return keyHolder.getKey().intValue();
    }

    public int update(Asset asset) throws SQLException {

        //System.out.println(asset.toString());
            jdbcTemplate.update("UPDATE asset set user_name=?,ssn=?, dept=?, asset_type=?,type=?," +
                            "brand_model=?,sn=?,location=?,purchase_date=?," +
                            "comment=?, asset_id=?, status=? where id=?",
                    new Object[]{asset.getUserName(),asset.getSsn(), asset.getDept(), asset.getAssetType(), asset.getType(),
                            asset.getBrandModel(), asset.getSn(), asset.getLocation(), asset.getPurchaseDate(),
                            asset.getComment(), asset.getAssetId(), asset.getStatus(), asset.getId()
                    });

        return asset.getId();

    }

    public int deleteAsset(int id){
        int result = jdbcTemplate.update("DELETE from asset where id=?",
                new Object[]{id}
                );

        return result;
    }


}

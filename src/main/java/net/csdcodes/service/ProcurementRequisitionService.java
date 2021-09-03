package net.csdcodes.service;

import com.google.gson.JsonObject;
import net.csdcodes.model.*;
import net.csdcodes.model.ls.PrStatusEnum;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.Float;

@Service
@Transactional
public class ProcurementRequisitionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProcurementRequisitionMain> getPRMainSavedList(String user_ssn) {

        String sql = "select prm.*, isnull(prd1.totalEstCost,0) totalEstCost from [dbo].[pr_main] prm left join" +
                " (select sum(prd.qty * prd.est_cost) totalEstCost,prd.pr_main_id from [dbo].[pr_detail] prd" +
                " group by prd.pr_main_id) prd1" +
                " on prm.id = prd1.pr_main_id" +
                " where prm.apl_user_ssn=? " +
                " and prm.pr_status='SAVED' and prm.pr_pass='' and prm.pr_process='' and prm.submitted=0" +
                " order by prm.id desc";

        List<ProcurementRequisitionMain> prm = jdbcTemplate.query(sql,
                new Object[]{user_ssn},
                BeanPropertyRowMapper.newInstance(ProcurementRequisitionMain.class));

        return prm;

    }

    public ProcurementRequisitionMain getPRMainById(int id) {
        String sql = "SELECT prm.*,isnull(prd1.totalEstCost,0) totalEstCost from [dbo].[pr_main] prm left join " +
                " (select sum(prd.qty * prd.est_cost) totalEstCost,prd.pr_main_id from [dbo].[pr_detail] prd " +
                " group by prd.pr_main_id) prd1 " +
                " on prm.id = prd1.pr_main_id WHERE prm.id = ?";

        return (ProcurementRequisitionMain) jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                new BeanPropertyRowMapper(ProcurementRequisitionMain.class));
    }
    ;

    public int addFlowType(User thisUser, JsonObject flowTypeObject) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        String sql = "INSERT INTO [dbo].[pr_main]" +
                                "(pr_title, apl_user_name, apl_user_ssn, cost_center, " +
                                "apl_dept, pr_no, pr_apl_date, pr_apl_update_date, " +
                                "project_name, pr_status, pr_process, pr_pass,flow_type,erp_code) " +
                                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        statement.setString(1, flowTypeObject.get("title").getAsString());
                        statement.setString(2, thisUser.getUserName());
                        statement.setString(3, thisUser.getSsn());
                        statement.setString(4, flowTypeObject.get("costCenter").getAsString());
                        statement.setString(5, flowTypeObject.get("aplDept").getAsString());
                        statement.setString(6, "");
                        statement.setString(7, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                        statement.setString(8, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                        statement.setString(9, flowTypeObject.get("projectName") == null ? "" : flowTypeObject.get("projectName").getAsString());
                        statement.setString(10, String.valueOf(PrStatusEnum.SAVED));
                        statement.setString(11, "");
                        statement.setString(12, "");
                        statement.setString(13, flowTypeObject.get("flow_type").getAsString());
                        statement.setString(14,flowTypeObject.get("erpCode") == null ? "" : flowTypeObject.get("erpCode").getAsString());

                        return statement;
                    }
                }, keyHolder);

        int id = keyHolder.getKey().intValue();

        return updatePrnoByPrmId(id);
    }

    public int updatePrnoByPrmId(int id) {

        int result = jdbcTemplate.update("UPDATE [dbo].[pr_main] set " +
                        "pr_no=? " +
                        "where id=? ",
                new Object[]{"PR" + String.format("%010d", id), id});

        return result;

    }

    public int updatePRMainById(int id, ProcurementRequisitionMain prm) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(prm);
        System.out.println("PR" + String.format("%010d", prm.getId()));

        int result = jdbcTemplate.update("UPDATE [dbo].[pr_main] set pr_title=?,cost_center=?, " +
                        "apl_dept=?, pr_no=?, project_name=?,pr_apl_update_date=? " +
                        "where id=?",
                new Object[]{prm.getPrTitle()!=null ? prm.getPrTitle() : "",
                        prm.getCostCenter()!=null ? prm.getCostCenter(): "",
                        prm.getAplDept()!=null ? prm.getAplDept() : "",
                        "PR" + String.format("%010d", prm.getId()),
                        prm.getProjectName()!=null ? prm.getProjectName() : "",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        id});

        return result;

    }

    public List<ProcurementRequisitionDetail> getPRDetailsByPRMId(int prmId) {
        String sql = "select *,([qty] * [est_cost]) totalEstCost from [dbo].[pr_detail] " +
                "where pr_main_id=? ";

        List<ProcurementRequisitionDetail> prds = jdbcTemplate.query(sql,
                new Object[]{prmId},
                BeanPropertyRowMapper.newInstance(ProcurementRequisitionDetail.class));

        return prds;
    }

    public ProcurementRequisitionDetail getPRDetailsByPRDId(int prdId) {
        String sql = "select *,([qty] * [est_cost]) totalEstCost from [dbo].[pr_detail] " +
                "where id=? ";


        return (ProcurementRequisitionDetail) jdbcTemplate.queryForObject(sql,
                new Object[]{prdId},
                new BeanPropertyRowMapper(ProcurementRequisitionDetail.class));
    }

    public int createPRDetail(int prmId, ProcurementRequisitionDetail prd) {
        System.out.println(prd.toString());

        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        int result = 0;

        try{
            result = jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement statement = con.prepareStatement("INSERT INTO [dbo].[pr_detail] " +
                                    "(pr_main_id, item_erp_code, item_erp_desc, " +
                                    "item_erp_brand_size, qty, item_erp_unit, " +
                                    "est_cost, created_date, updated_date,target_date,memo) " +
                                    " values(?,?,?,?,?,?,?,?,?,?,?)");
                            statement.setInt(1, prd.getPrMainId());
                            statement.setString(2, prd.getItemErpCode());
                            statement.setString(3, prd.getItemErpDesc());
                            statement.setString(4, prd.getItemErpBrandSize());
                            statement.setFloat(5, prd.getQty());
                            statement.setString(6, prd.getItemErpUnit());
                            statement.setFloat(7, prd.getEstCost());
                            statement.setString(8, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(9, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(10, prd.getTargetDate()!= null ?
                                    simpleDateFormat.format(prd.getTargetDate()): DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(11, prd.getMemo());
                            return statement;
                        }
                    });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        return result;
    }

    ;

    public int updatePRDetailById(int prdId, ProcurementRequisitionDetail prd) {
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        int result = jdbcTemplate.update("UPDATE [dbo].[pr_detail] set" +
                        " item_erp_code=?, " +
                        " item_erp_desc=?," +
                        " item_erp_brand_size=?," +
                        " qty=?," +
                        " item_erp_unit=?," +
                        " est_cost=?," +
                        " updated_date=?," +
                        " target_date=?," +
                        " memo=?" +
                        " where id=?",
                new Object[]{prd.getItemErpCode(), prd.getItemErpDesc(), prd.getItemErpBrandSize(),
                        prd.getQty(), prd.getItemErpUnit(), prd.getEstCost(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        simpleDateFormat.format(prd.getTargetDate()), prd.getMemo(),
                        prdId});

        return result;
    }    ;


    public int deletePRDetailById(int prdId){
        int result = jdbcTemplate.update("DELETE from pr_detail where id=?",
                new Object[]{prdId}
        );

        return result;
    }

    public int deletePRM(int prmId){
        int result = deletePRDByPRMId(prmId);

        if (result >= 0){
            result = deletePRMById(prmId);
        }

        return result;
    }

    private int deletePRDByPRMId(int prmId){
        int result = jdbcTemplate.update("DELETE from pr_detail where pr_main_id=?",
                new Object[]{prmId}
        );

        return result;
    }

    private int deletePRMById(int id){
        int result = jdbcTemplate.update("DELETE from pr_main where id=?",
                new Object[]{id}
        );

        return result;
    }

    public List<User> getUserByRole(String role){
        String sql = "select u.[user_id],u.[username],u.[orgname],u.[ssn] from [user] u " +
                "left join [user_role] ur on u.user_id=ur.user_id " +
                "where ur.role_id in (select role_id from [role] " +
                "where role.name=?)";

        List roleList = jdbcTemplate.query(sql, new Object[]{role}, BeanPropertyRowMapper.newInstance(User.class));
        return roleList;

    }

    public List<ProcurementRequisitionComment> getProcurementRequisitionComment(int prMainId){
        String sql = "select * " +
                "from [pr_comment] p " +
                "where p.[pr_main_id]=? order by pr_comment_id asc";

        System.out.println(sql);
        System.out.println(prMainId);

        List prComments = jdbcTemplate.query(sql, new Object[]{prMainId}, BeanPropertyRowMapper.newInstance(ProcurementRequisitionComment.class));
        return prComments;
    }

    public int deleteProcurementRequisitionCommentByPrmId(int prMainId){
        int result = jdbcTemplate.update( "delete from [pr_comment] where pr_main_id=?",new Object[]{prMainId});
        return result;
    }

    public int updatePrmFinishedStatus(int finished, int prmId){
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        int result = jdbcTemplate.update("UPDATE [dbo].[pr_main] set" +
                        " finished=? , pr_apl_update_date=?" +
                        " where id=?",
                new Object[]{finished,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        prmId});

        return result;
    }

    public int updatePrmApprovedStatus(int approved, int prmId){
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        int result = jdbcTemplate.update("UPDATE [dbo].[pr_main] set" +
                        " approved=? , pr_apl_update_date=?" +
                        " where id=?",
                new Object[]{approved,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        prmId});

        return result;
    }

    public int updatePrmSubmittedStatus( int submitted, int prmId){
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        int result = jdbcTemplate.update("UPDATE [dbo].[pr_main] set" +
                        " submitted=? , pr_apl_update_date=?" +
                        " where id=?",
                new Object[]{submitted,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        prmId});

        return result;
    }



    public int addProcurementRequisitionComment(ProcurementRequisitionComment prc, User thisUser){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        System.out.println(prc.toString());

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        String sql = "INSERT INTO [dbo].[pr_comment] " +
                                "(pr_main_id, userssn, user_org_name, created_date, comment, approved,gate) " +
                                "values(?,?,?,?,?,?,?)";

                        PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        statement.setInt(1, prc.getPrMainId());
                        statement.setString(2, thisUser.getSsn());
                        statement.setString(3, thisUser.getOrgName());
                        statement.setString(4, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                        statement.setString(5, prc.getComment());
                        statement.setInt(6, prc.getApproved());
                        statement.setString(7, prc.getGate());
                        return statement;
                    }
                }, keyHolder);

        return keyHolder.getKey().intValue();
    }


    public List<ProcurementRequisitionMain> getPRMHistory(PRMRequestVariables prmrv){

        //System.out.println(prmrv.toString());

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String  sql = "SELECT TOP 1000 prm.*, isnull(prd1.totalEstCost,0) totalEstCost " +
                " FROM [dbo].[pr_main] prm, " +
                " (select sum(prd.qty * prd.est_cost) totalEstCost,prd.pr_main_id from [dbo].[pr_detail] prd" +
                " group by prd.pr_main_id) prd1 " +
                " where prm.id = prd1.pr_main_id and prm.finished=1 and prm.approved=1 and prm.submitted=1" +
                " and prm.po_code like ? and prm.flow_type like ?";


        if ((prmrv.getPrmStart()==null) && (prmrv.getPrmEnd()==null) ){
            sql = sql + " order by prm.pr_apl_date desc";
            //System.out.println(sql);
            List<ProcurementRequisitionMain> prms = jdbcTemplate.query(sql,
                    new Object[]{
                            //prmrv.getFinished(),prmrv.getApproved(),
                            (prmrv.getPoCode()==null? "%%" : "%" + prmrv.getPoCode() + "%"),
                            (prmrv.getFlowType()==null? "%%" : prmrv.getFlowType())
                    }, BeanPropertyRowMapper.newInstance(ProcurementRequisitionMain.class));
            //System.out.println(prms.size());
            return prms;
        }

        if (!(prmrv.getPrmStart()==null) && !(prmrv.getPrmEnd()==null) ){
            sql = sql + " and pr_apl_date between ? and ? order by prm.pr_apl_date desc";
            //System.out.println(sql);
            List<ProcurementRequisitionMain> prms = jdbcTemplate.query(sql,
                    new Object[]{
                            //prmrv.getFinished(),prmrv.getApproved(),
                            (prmrv.getPoCode()==null? "%%" : "%" + prmrv.getPoCode() + "%"),
                            (prmrv.getFlowType()==null? "%%" : prmrv.getFlowType()),
                            simpleDateFormat.format(prmrv.getPrmStart()),simpleDateFormat.format(prmrv.getPrmEnd())
                    }, BeanPropertyRowMapper.newInstance(ProcurementRequisitionMain.class));
            //System.out.println(prms.size());
            return prms;
        }

        if (!(prmrv.getPrmStart()==null) && (prmrv.getPrmEnd()==null) ){
            sql = sql + " and pr_apl_date >= ? order by prm.pr_apl_date desc";
            //System.out.println(sql);
            List<ProcurementRequisitionMain> prms = jdbcTemplate.query(sql,
                    new Object[]{
                            //prmrv.getFinished(),prmrv.getApproved(),
                            (prmrv.getPoCode()==null? "%%" : "%" + prmrv.getPoCode() + "%"),
                            (prmrv.getFlowType()==null? "%%" : prmrv.getFlowType()),
                            simpleDateFormat.format(prmrv.getPrmStart())
                    }, BeanPropertyRowMapper.newInstance(ProcurementRequisitionMain.class));
            //System.out.println(prms.size());
            return prms;
        }
        if ((prmrv.getPrmStart()==null) && !(prmrv.getPrmEnd()==null) ){
            sql = sql + " and pr_apl_date <= ? order by prm.pr_apl_date desc";
            //System.out.println(sql);
            List<ProcurementRequisitionMain> prms = jdbcTemplate.query(sql,
                    new Object[]{
                            //prmrv.getFinished(),prmrv.getApproved(),
                            (prmrv.getPoCode()==null? "%%" : "%" + prmrv.getPoCode() + "%"),
                            (prmrv.getFlowType()==null? "%%" : prmrv.getFlowType()),
                            simpleDateFormat.format(prmrv.getPrmEnd())
                    }, BeanPropertyRowMapper.newInstance(ProcurementRequisitionMain.class));
            //System.out.println(prms.size());
            return prms;
        }

        return null;
    }

    public List<ProcurementRequisitionMain> PRMSubmittedByUser(String userSsn){
        String  sql = "SELECT TOP 1000 prm.*,isnull(prd1.totalEstCost,0) totalEstCost" +
                " FROM [dbo].[pr_main] prm left join" +
                " (select sum(prd.qty * prd.est_cost) totalEstCost,prd.pr_main_id from [dbo].[pr_detail] prd " +
                " group by prd.pr_main_id) prd1 " +
                " on prm.id = prd1.pr_main_id where prm.apl_user_ssn=? and prm.submitted=1" +
                " order by prm.pr_apl_date desc";
        List<ProcurementRequisitionMain> prms = jdbcTemplate.query(sql,
                new Object[]{userSsn}, BeanPropertyRowMapper.newInstance(ProcurementRequisitionMain.class));

        return prms;

    }

    public float getTotalCostByPrmId(int prmId){
        String sql = "select sum([qty] * [est_cost]) as totalEstCost from [dbo].[pr_detail]" +
                " where pr_main_id=?" +
                " group by pr_main_id;";
        try{
            float estTotalCost = jdbcTemplate.queryForObject(sql,
                    new Object[]{prmId},
                    Float.class);

            return estTotalCost;
        }catch (Exception se){
            System.out.println(se.getMessage());
        }
        return 0;
    }

    public List<Costcenter> getCostcenterDesc(){
        String sql = "select * from [dbo].[costcenter]";
        List ccs = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Costcenter.class));
        return ccs;
    }

    public List<CostType> getCostType(String costcenter){
        String sql = "select * from [dbo].[cost_type] where costcenter=?";
        List cts = jdbcTemplate.query(sql,new Object[]{costcenter},
                BeanPropertyRowMapper.newInstance(CostType.class));
        return cts;
    }

    public List<PrProject> getPrProject(){
        String sql = "select * from [dbo].[pr_project]";
        List pps = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(PrProject.class));
        return pps;
    }

    public int fillPoCodeByPrmId( String poCode, int prmId){
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        int result = jdbcTemplate.update("UPDATE [dbo].[pr_main] set" +
                        " po_code=? , pr_apl_update_date=?" +
                        " where id=?",
                new Object[]{poCode,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        prmId});

        return result;
    }

    public int copyCreatePrmByPrmId(int prmId){

        ProcurementRequisitionMain procurementRequisitionMain = getPRMainById(prmId);

        System.out.println(procurementRequisitionMain);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        int id = 0;

        try {
            jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            String sql = "INSERT INTO [dbo].[pr_main]" +
                                    "(pr_title, apl_user_name, apl_user_ssn, cost_center, " +
                                    "apl_dept, pr_no, pr_apl_date, pr_apl_update_date, " +
                                    "project_name, pr_status, pr_process, pr_pass,flow_type) " +
                                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

                            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                            statement.setString(1, procurementRequisitionMain.getPrTitle());
                            statement.setString(2, procurementRequisitionMain.getAplUserName());
                            statement.setString(3, procurementRequisitionMain.getAplUserSsn());
                            statement.setString(4, procurementRequisitionMain.getCostCenter());
                            statement.setString(5, procurementRequisitionMain.getAplDept());
                            statement.setString(6, "");
                            statement.setString(7, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(8, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(9, procurementRequisitionMain.getProjectName());
                            statement.setString(10, String.valueOf(PrStatusEnum.SAVED));
                            statement.setString(11, "");
                            statement.setString(12, "");
                            statement.setString(13, procurementRequisitionMain.getFlowType());

                            return statement;
                        }
                    }, keyHolder);

            id = keyHolder.getKey().intValue();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return id;

    }

    public int copyCreatePrdByPrmId(int prmId, int newId){

        LocalDateTime now = LocalDateTime.now();

        String sql = "INSERT INTO [dbo].[pr_detail](pr_main_id, item_erp_code, item_erp_desc, " +
                "item_erp_brand_size, qty, item_erp_unit, " +
                "est_cost, created_date, updated_date, target_date, memo) " +
                "select ?, item_erp_code, item_erp_desc, item_erp_brand_size, " +
                "qty, item_erp_unit, est_cost," +
                " ?, ?, ?, " +
                "memo from [dbo].[pr_detail] pd where pd.pr_main_id=?";
        int result = 0;
        try{
            result = jdbcTemplate.update(
                    new PreparedStatementCreator() {
                        @Override
                        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                            PreparedStatement statement = con.prepareStatement(sql);
                            statement.setInt(1, newId);
                            statement.setString(2, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(3, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setString(4, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now));
                            statement.setInt(5, prmId);
                            return statement;
                        }
                    });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return result;
    }

    public int prdFillPoCodeByPrdId( String poCode, int prdId){
        LocalDateTime now = LocalDateTime.now();
        String pattern = "yyyy-MM-dd";

        int result = jdbcTemplate.update("UPDATE [dbo].[pr_detail] set" +
                        " po_code=? , updated_date=?" +
                        " where id=?",
                new Object[]{poCode,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(now),
                        prdId});

        return result;
    }
}

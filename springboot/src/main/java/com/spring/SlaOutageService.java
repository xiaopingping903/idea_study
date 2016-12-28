package com.spring; /**
 * Created by Administrator on 2016/12/26.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class SlaOutageService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 不指定数据源使用默认数据源
     *
     * @return
     * @author SHANHY
     * @create  2016年1月24日
     */
    public List<SlaOutageDTO> getList(){
        String sql = "SELECT ID from t_sla_outage_operation_details";
        return (List<SlaOutageDTO>) jdbcTemplate.query(sql, new RowMapper<SlaOutageDTO>(){

            @Override
            public SlaOutageDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SlaOutageDTO stu = new SlaOutageDTO();
                stu.setId(rs.getInt("ID"));
                return stu;
            }

        });
    }
    /**
     * 指定数据源
     *
     * @return
     * @author SHANHY
     * @create  2016年1月24日
     */
    @TargetDataSource(name="ds1")
    public List<SlaOutageDTO> getListByDs1(){
        String sql = "SELECT ID from death_note";
        return (List<SlaOutageDTO>) jdbcTemplate.query(sql, new RowMapper<SlaOutageDTO>(){

            @Override
            public SlaOutageDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                SlaOutageDTO stu = new SlaOutageDTO();
                stu.setId(rs.getInt("ID"));
                return stu;
            }

        });
    }
}

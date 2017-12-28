package org.xsnake.cloud.xflow.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.xsnake.cloud.xflow.service.api.Page;

/**
 * Oracle实现
 * 
 * @author Jerry.Zhao
 * 
 */
@Configuration
public class DaoTemplate {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int update(String sql) {
		return jdbcTemplate.update(sql);
	}

	public int update(String sql, Object[] args) {
		return jdbcTemplate.update(sql, args);
	}
	
	public int update(String sql,PreparedStatementSetter pss) {
		return jdbcTemplate.update(sql,pss);
	}

	public List<Object> callProcedure(final String sql, final ProcedureParam[] args) {
		CallableStatementCallback<List<Object>> action = new CallableStatementCallback<List<Object>>() {
			@Override
			public List<Object> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				final List<Object> result = new ArrayList<Object>();
				for(int i=1;i<args.length+1;i++){
					ProcedureParam p = args[(i-1)];
					if(p instanceof InProcedureParam){
						cs.setObject(i, p.value);
					}else if(p instanceof OutProcedureParam){
						cs.registerOutParameter(i, p.type);
					}
				}
				cs.execute();
				for(int i=1;i<args.length+1;i++){
					ProcedureParam p = args[(i-1)];
					if(p instanceof OutProcedureParam){
						result.add(cs.getString(i));
					}else{
						result.add(null);
					}
				}
				return result;
			}
		};
		return jdbcTemplate.execute(sql,action);
	}
	
	public static abstract class ProcedureParam{
		public ProcedureParam (int type,Object value){
			this.type = type;
			this.value = value;
		}
		int type;
		Object value;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	public static class InProcedureParam extends ProcedureParam{
		public InProcedureParam(Object value){
			super(-1,value);
		}
	}
	
	public static class OutProcedureParam extends ProcedureParam{
		public OutProcedureParam(int type){
			super(type,null);
		}
	}
	
	private String getSQL(String sql, int start, int end){
		return getMysql(sql, start, end);
	}
	
	@SuppressWarnings("unused")
	private String getOracle(String sql, int start, int end) {
        StringBuffer oracleSql = new StringBuffer();
        oracleSql.append("SELECT * FROM  ( SELECT A.*, ROWNUM RN FROM ( ")
                 .append(sql)
                 .append(" ) A WHERE ROWNUM <= ")
                 .append(end)
                 .append(" ) WHERE RN > ")
                 .append(start);
        return oracleSql.toString();
	}

	private String getMysql(String sql, int start, int end) {
        StringBuffer oracleSql = new StringBuffer();
        oracleSql.append("SELECT * FROM  ( ")
                 .append(sql)
                 .append(" ) _T LIMIT ").append(start) .append(",").append(end);
        return oracleSql.toString();
	}

	
	public <T> List<T> query(String sql,Class<T> clazz){
		return queryForClass(sql,null,clazz);
	}
	
	public <T> List<T> query(String sql, Object[] args , Class<T> clazz){
		return queryForClass(sql,args,clazz);
	}
	
	public <T> List<T> query(String sql,int firstResult, int maxResults,Class<T> clazz){
		 return query(sql,null,firstResult,maxResults,clazz);
	}

	public <T> List<T> query(String sql, Object[] args, int firstResult, int maxResults,Class<T> clazz) {
		return queryForClass(getSQL(sql, firstResult, firstResult + maxResults),args,clazz);
	}
	
	public BigDecimal queryBigDecimal(String sql,Object[] args){
		try{
			return jdbcTemplate.queryForObject(sql, args,BigDecimal.class);
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public BigDecimal queryBigDecimal(String sql){
		return queryBigDecimal(sql,null);
	}
	
	public String queryForString(String sql,Object[] args){
		try{
			return jdbcTemplate.queryForObject(sql, args,String.class);
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public String queryForString(String sql){
		return queryForString(sql,null);
	}
	
	public <T> T queryForObject(String sql,Object[] args,Class<T> clazz){
		try{
			return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<T>(clazz));
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public <T> T queryForObject(String sql,Class<T> clazz){
		return queryForObject(sql, null, clazz);
	}
	
	public <T> Page<T> search(String sql, Object[] args, int currentPage ,int pageSize,Class<T> clazz) {
		try {
			String thql = "select count(1) from (" + sql +") t ";
			Long _count = jdbcTemplate.queryForObject(thql, args, Long.class);
			List<T> results = query(sql, args, (currentPage - 1) * pageSize, pageSize,clazz);
			return new Page<T>(results, currentPage, pageSize, _count.intValue());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private <T> List<T> queryForClass(String resultSQL,Object[] args,Class<T> clazz) {
		RowMapperResultSetExtractor<T> extractor = new RowMapperResultSetExtractor<>(new BeanPropertyRowMapper<>(clazz));
		return jdbcTemplate.query(resultSQL, new ArgumentPreparedStatementSetter(args), extractor);
	}
	
}

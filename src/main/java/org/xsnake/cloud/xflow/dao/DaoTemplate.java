package org.xsnake.cloud.xflow.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
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
	
	public int execute(String sql) {
		return jdbcTemplate.update(sql);
	}

	public int execute(String sql, Object arg) {
		return execute(sql, new Object[] { arg });
	}

	public int execute(String sql, Object[] args) {
		return jdbcTemplate.update(sql, args);
	}
	
	public int execute(String sql,PreparedStatementSetter pss) {
		return jdbcTemplate.update(sql,pss);
	}

	public List<Object> executeCall(final String sql, final ProcedureParam[] args) {
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
		return getOracle(sql, start, end);
	}
	
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
	
	public <T> List<T> query(String sql,Class<T> clazz){
		return queryForClass(sql,null,clazz);
	}
	
	public List<Map<String, Object>> query(String sql){
		return queryForMap(sql,null);
	}
	
	public <T> List<T> query(String sql, Object[] args , Class<T> clazz){
		return queryForClass(sql,args,clazz);
	}
	
	public List<Map<String, Object>> query(String sql, Object[] args){
		return queryForMap(sql,args);
	}
	
	public <T> List<T> query(String sql,int firstResult, int maxResults,Class<T> clazz){
		 return query(sql,null,firstResult,maxResults,clazz);
	}
	
	public List<Map<String, Object>> query(String sql,int firstResult, int maxResults){
		return query(sql,null,firstResult,maxResults);
	}

	public <T> List<T> query(String sql, Object[] args, int firstResult, int maxResults,Class<T> clazz) {
		return queryForClass(getSQL(sql, firstResult, firstResult + maxResults),args,clazz);
	}
	
	public List<Map<String, Object>> query(String sql, Object[] args, int firstResult, int maxResults) {
		return queryForMap(getSQL(sql, firstResult, firstResult + maxResults),args);
	}

	public Long queryLong(String sql,Object arg){
		return queryLong(sql, new Object[]{arg});
	}
	
	public Long queryLong(String sql,Object[] args){
		return jdbcTemplate.queryForObject(sql, args,Long.class);
	}
	
	public Long queryLong(String sql){
		return jdbcTemplate.queryForObject(sql,Long.class);
	}
	
	public BigDecimal queryBigDecimal(String sql,Object arg){
		return queryBigDecimal(sql, new Object[]{arg});
	}
	
	public BigDecimal queryBigDecimal(String sql,Object[] args){
		return jdbcTemplate.queryForObject(sql, args,BigDecimal.class);
	}
	
	public BigDecimal queryBigDecimal(String sql){
		return jdbcTemplate.queryForObject(sql,BigDecimal.class);
	}
	
	public String queryString(String sql,Object arg){
		return jdbcTemplate.queryForObject(sql, new Object[]{arg},String.class);
	}
	
	public String queryString(String sql,Object[] args){
		return jdbcTemplate.queryForObject(sql, args,String.class);
	}
	
	public String queryString(String sql){
		return jdbcTemplate.queryForObject(sql,String.class);
	}
	
	public <T> T queryObject(String sql,Object[] args,Class<T> clazz){
		return jdbcTemplate.queryForObject(sql, args, new BeanPropertyRowMapper<T>(clazz));
	}
	
	public <T> T queryObject(String sql,String arg,Class<T> clazz){
		return queryObject(sql, new Object[]{arg}, clazz);
	}
	
	public <T> T queryObject(String sql,Class<T> clazz){
		return queryObject(sql, (Object[])null, clazz);
	}
	
	public Page<Map<String,Object>> search(String sql, Object[] args, int currentPage ,int pageSize) {
		try {
			String thql = "select count(1) from (" + sql +") t ";
			BigDecimal _count = jdbcTemplate.queryForObject(thql, args, BigDecimal.class);
			int count = _count.intValue();
			List<Map<String,Object>> results = query(sql, args, (currentPage - 1) * pageSize, pageSize);
			return new Page<Map<String,Object>>(results, currentPage, pageSize, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> Page<T> search(String sql, Object[] args, int currentPage ,int pageSize,Class<T> clazz) {
		try {
			String thql = "select count(1) from (" + sql +") t ";
			BigDecimal _count = jdbcTemplate.queryForObject(thql, args, BigDecimal.class);
			int count = _count.intValue();
			List<T> results = query(sql, args, (currentPage - 1) * pageSize, pageSize,clazz);
			return new Page<T>(results, currentPage, pageSize, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected ResultSetExtractor<List<Map<String, Object>>> resultSetExtractor = new ResultSetExtractor<List<Map<String, Object>>>() {
		@Override
		public List<Map<String, Object>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData rsmd = resultSet.getMetaData();
				int count = rsmd.getColumnCount();
				for (int i = 1; i <= count; i++) {
					map.put(rsmd.getColumnName(i), resultSet.getObject(rsmd.getColumnName(i)));
				}
				result.add(map);
			}
			return result;
		}
	};
	
	private List<Map<String, Object>> queryForMap(String resultSQL,Object[] args) {
		return jdbcTemplate.query(resultSQL, new ArgumentPreparedStatementSetter(args), resultSetExtractor);
	}
	
	private <T> List<T> queryForClass(String resultSQL,Object[] args,Class<T> clazz) {
		RowMapperResultSetExtractor<T> extractor = new RowMapperResultSetExtractor<>(new BeanPropertyRowMapper<>(clazz));
		return jdbcTemplate.query(resultSQL, new ArgumentPreparedStatementSetter(args), extractor);
	}
	
}

package org.xsnake.cloud.xflow.service.api;

import java.io.Serializable;
/**
 * 
 * @author Jerry.Zhao
 *
 */
public class PageCondition implements Serializable{

	private static final long serialVersionUID = 1L;

	protected String searchKey;
	
	protected String orderBy;

	protected int page = 1;
	
	private int rows = 20;

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
}

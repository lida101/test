package com.hisense.task.dto;

import java.util.List;

public class DataPage<T> implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_PAGE_SIZE = 10;

	/***
	 * 翻页数据列表内容.
	 */
	List<T> content;
	/***
	 * 当前页.
	 */
	int page;
	/***
	 * 每页行数.
	 */
	int pageSize = DEFAULT_PAGE_SIZE;

	/***
	 * 总页数.
	 */
	int totalPage;
	
	/***
	 * 总行数
	 */
	int totalRows;

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize == 0 ? DEFAULT_PAGE_SIZE : pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setTotalPageByRows(int totalRows) {
		this.totalRows = totalRows;
		if (totalRows == 0) {
			totalPage = 0;
		} else if (totalRows <= pageSize) {
			totalPage = 1;
		} else if (totalRows % pageSize == 0) {
			totalPage = totalRows / pageSize;
		} else {
			totalPage = totalRows / pageSize + 1;
		}
	}

	public int getCurrentPage() {
		if (page > totalPage) {
			return totalPage;
		}
		if (page <= 0) {
			page = 1;
		}
		return page;
	}

}

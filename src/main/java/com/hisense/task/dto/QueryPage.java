package com.hisense.task.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryPage implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***
	 * 当前页.
	 */
	private int page;
	/***
	 * 每页行数.
	 */
	private int pageSize;
	/**
	 * 最大导出条数
	 */
	private int maxResults=500000;
	/***
	 * 排序.
	 */
	private String orderBy;
	/***
	 * 是否导出.
	 */
	private boolean export;
	
	/**
	 * 导出服务类名称
	 */
	private String exportServiceName="";

	/**
	 * 自定义sql过滤条件
	 */

	private String sqlCondition="";
	/**
	 * 分组查询
	 */
	private String groupByStr;

	/**
	 * 聚集函数
	 */
	private String aggregateFuncStr;
	
	/**
	 * 是否进行数据权限校验
	 */
	private boolean isJurisdiction = true;


	private String hql;

	private Map<String, Object> paramMap;

	public boolean isExport() {
		return export;
	}

	public void setExport(boolean export) {
		this.export = export;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxmaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
	public String getSqlCondition() {
		return sqlCondition;
	}

	public void setSqlCondition(String sqlCondition) {
		this.sqlCondition = sqlCondition;
	}

	/***
	 * 查询参数列表.
	 */
	List<QueryParam> queryParamList = new ArrayList<QueryParam>();

	public List<QueryParam> getQueryParamList() {
		return queryParamList;
	}

	public void setQueryParamList(List<QueryParam> queryParamList) {
		this.queryParamList = queryParamList;
	}

	public String getQueryInfo() {
		StringBuffer queryCondition = new StringBuffer();
		queryCondition.append("page=").append(this.page).append(" pageSize=").append(this.pageSize).append(" maxResults=").append(this.maxResults);
		if (this.queryParamList == null) {
			return queryCondition.toString();
		}
		for (QueryParam queryParam : this.queryParamList) {
			if (queryParam.getValue() != null && queryParam.getField() != null && queryParam.getLogic() != null) {
				queryCondition.append(" ").append(queryParam.getField()).append(queryParam.getLogic())
						.append(queryParam.getValue());
			}

		}
		return queryCondition.toString();
	}

	public String getGroupByStr() {
		return groupByStr;
	}

	public void setGroupByStr(String groupByStr) {
		this.groupByStr = groupByStr;
	}

	public String getAggregateFuncStr() {
		return aggregateFuncStr;
	}

	public void setAggregateFuncStr(String aggregateFuncStr) {
		this.aggregateFuncStr = aggregateFuncStr;
	}

	public void setJurisdiction(boolean isJurisdiction) {
		this.isJurisdiction = isJurisdiction;
	}

	public boolean isJurisdiction() {
		return isJurisdiction;
	}

	/**
	 * @return the exportServiceName
	 */
	public String getExportServiceName() {
		return exportServiceName;
	}

	/**
	 * @param exportServiceName the exportServiceName to set
	 */
	public void setExportServiceName(String exportServiceName) {
		this.exportServiceName = exportServiceName;
	}


    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}

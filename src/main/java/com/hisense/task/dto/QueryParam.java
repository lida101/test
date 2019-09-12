package com.hisense.task.dto;

public class QueryParam implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 查询字段.
	 */
	private String field;
	/***
	 * 操作符号.
	 */
	private String logic;
	/**
	 * 查询值.
	 */
	private String value;
	/***
	 * 字段类型.
	 */
	private String type;

    private String startField;

    private String endField;
	
	/**
	 * 页面定义的type与model中类型不一致时使用（如：查询条件时日期，数据库中是string）
	 * 
	 * @author caojun1@hisense.com_20161018
	 */
	private String queryType;

	/**
	 * 页面field定义与model的列定义不一致时使用（如：查询条件中定义两个时间条件 按区间查询某一字段）
	 * 
	 * @author caojun1@hisense.com_20161019
	 */
	private String modelColName;
	
	private String[] values;

	/**
	 * @author caojun1@hisense.com 20160305
	 */
	public QueryParam() {

	}

	/**
	 * @param field
	 * @param type
	 * @param logic
	 * @param value
	 * @author @author caojun1@hisense.com 20160305
	 */
	public QueryParam(String field, String type, String logic, String value) {
		this.setField(field);
		this.setLogic(logic);
		this.setType(type);
		this.setValue(value);
	}
	
    public QueryParam(String field, String type, String logic, String value, String[] values) {
        this.setField(field);
        this.setLogic(logic);
        this.setType(type);
        this.setValue(value);
        this.setValues(values);
    }

	public String getField() {
		if (this.modelColName != null && !"".equals(modelColName)) {
            return this.modelColName;
        }
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	/**
	 * @return the queryType
	 */
	public String getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the modelColName
	 */
	public String getModelColName() {
		return modelColName;
	}

	/**
	 * @param modelColName the modelColName to set
	 */
	public void setModelColName(String modelColName) {
		this.modelColName = modelColName;
	}

    public String getEndField() {
        return endField;
    }

    public void setEndField(String endField) {
        this.endField = endField;
    }

    public String getStartField() {
        return startField;
    }

    public void setStartField(String startField) {
        this.startField = startField;
    }
}

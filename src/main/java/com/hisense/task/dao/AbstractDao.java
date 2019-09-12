package com.hisense.task.dao;

import com.hisense.task.dto.DataPage;
import com.hisense.task.dto.QueryPage;
import org.hibernate.Criteria;
import org.hibernate.Query;

import java.io.Serializable;

/**
 * @Description: 通用DAO接口定义（自定义dao接口继承该接口）
 * @author caojun1@hisense.com
 * @date 2016年1月13日 上午9:00:17
 * @version V1.0
 */
public interface AbstractDao<PK extends Serializable, T> {

	T getByKey(PK key);


	void save(T entity);

	void update(T entity);

	void saveOrUpdate(T entity);

	void persistent(T entity);

	void delete(T entity);

	void flush();
	
    void clear();
    
    void close();

    Query createSQLQuery(String sql);

    Query createQuery(String hsql);

    Criteria createEntityCriteria();

	DataPage<T> queryPageData(QueryPage queryPage);
}

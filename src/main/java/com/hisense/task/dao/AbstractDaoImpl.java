package com.hisense.task.dao;

import com.hisense.task.dto.DataPage;
import com.hisense.task.dto.QueryPage;
import com.hisense.task.dto.QueryParamBind;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <PK>
 * @param <T>
 * @author winner_pan
 */
public abstract class AbstractDaoImpl<PK extends Serializable, T> implements AbstractDao<PK, T> {

    Logger logger = LoggerFactory.getLogger(AbstractDaoImpl.class);

    private final Class<T> persistentClass;

    @SuppressWarnings("unchecked")
    public AbstractDaoImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
    }

    /***
     * 执行Hsql时增加数据权限的过滤.
     *
     * @param hsql
     * @return
     */
    @Override
    public Query createQuery(String hsql) {
        return getSession().createQuery(hsql);
    }

    @Override
    public Criteria createEntityCriteria() {
        Criteria criteria = getSession().createCriteria(persistentClass);
        return criteria;
    }


    @Override
    public SQLQuery createSQLQuery(String sql) {
        return getSession().createSQLQuery(sql);
    }

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }


    @Override
    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        T t = getSession().get(persistentClass, key);
        return t;
    }

    @Override
    public void save(T entity) {
        getSession().save(entity);
    }

    @Override
    public void update(T entity) {
        getSession().update(entity);
    }

    @Override
    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void persistent(T entity) {
        getSession().persist(entity);
    }
    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Override
    public void flush() {
        try {
            Session session = getSession();
            session.flush();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public void clear() {
        try {
            Session session = getSession();
            session.clear();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            Session session = getSession();
            session.close();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public DataPage<T> queryPageData(QueryPage queryPage) {
        DataPage<T> datapage = new DataPage<T>();
        datapage.setPageSize(queryPage.getPageSize());
        datapage.setPage(queryPage.getPage());

        Query query;
        if (!queryPage.isExport()) {
            StringBuffer countSql = new StringBuffer("select count(1) from " + persistentClass.getName());
            if (StringUtils.isNotBlank(queryPage.getSqlCondition())) {
                countSql.append(" where ").append(queryPage.getSqlCondition());
            }
            QueryParamBind.bindExpress(countSql, queryPage.getQueryParamList());
            query = createQuery(countSql.toString());
            query.setReadOnly(true);
            QueryParamBind.setParam(query, queryPage.getQueryParamList());
            List<?> queryList = query.list();
            int totalRows = Integer.valueOf(queryList.size() == 0 ? "0" : queryList.get(0).toString());
            if (totalRows == 0) {
                datapage.setContent(new ArrayList<T>());
                return datapage;
            }
            datapage.setTotalPageByRows(totalRows);
            datapage.setPage(datapage.getCurrentPage());
        }

        StringBuffer selectSql = new StringBuffer("from " + persistentClass.getName());
        if (StringUtils.isNotBlank(queryPage.getSqlCondition())) {
            selectSql.append(" where ").append(queryPage.getSqlCondition());
        }
        QueryParamBind.bindExpress(selectSql, queryPage.getQueryParamList());
        if (StringUtils.isNotEmpty(queryPage.getOrderBy())) {
            selectSql.append(" order by ").append(queryPage.getOrderBy());
        }
        query = createQuery(selectSql.toString());
        QueryParamBind.setParam(query, queryPage.getQueryParamList());
        if (!queryPage.isExport()) {
            query.setFirstResult(datapage.getPageSize() * (datapage.getCurrentPage() - 1));
            query.setMaxResults(datapage.getPageSize());
        } else {
            query.setFirstResult(0);
            query.setMaxResults(queryPage.getMaxResults());
        }
        query.setReadOnly(true);
        datapage.setContent(query.list());
        return datapage;
    }
}
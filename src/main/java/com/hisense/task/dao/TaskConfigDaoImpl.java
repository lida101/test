package com.hisense.task.dao;

import com.hisense.task.dto.QueryModel;
import com.hisense.task.domain.TaskConfig;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("fwScheduleDao")
@SuppressWarnings("unchecked")
public class TaskConfigDaoImpl extends AbstractDaoImpl<Long, TaskConfig> implements TaskConfigDao {


	@Override
	public List<TaskConfig> findAll(QueryModel queryModel) {
		Criteria criteria = createEntityCriteria();
		if (queryModel != null) {
            if (!StringUtils.isBlank(queryModel.getTaskGroup())) {
                criteria.add(Restrictions.eq("taskGroup", queryModel.getTaskGroup()));
            }
            if (!StringUtils.isBlank(queryModel.getTaskName())) {
                criteria.add(Restrictions.like("taskName", "%" + queryModel.getTaskName() + "%"));
            }
			if (!StringUtils.isBlank(queryModel.getIsAuto())) {
				criteria.add(Restrictions.eq("isAuto", queryModel.getIsAuto()));
            }
			if (queryModel.getJobStatus() != null) {
				criteria.add(Restrictions.eq("jobStatus", queryModel.getJobStatus()));
			}
		}
		criteria.addOrder(Order.asc("taskName"));
		return criteria.list();
	}

	@Override
	public List<TaskConfig> findByIsAuto(String isAuto) {
		Criteria criteria = super.createEntityCriteria();
		criteria.add(Restrictions.eq("isAuto", isAuto));
		return criteria.list();
	}

	@Override
	public List<TaskConfig> findByJobStatus(Long jobStatus) {
		Criteria criteria = super.createEntityCriteria();
		criteria.add(Restrictions.eq("jobStatus", jobStatus));
		return criteria.list();
	}

	@Override
	public void saveTask(TaskConfig config) {
		super.saveOrUpdate(config);
	}
}

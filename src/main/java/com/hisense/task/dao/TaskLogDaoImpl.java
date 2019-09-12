package com.hisense.task.dao;

import com.hisense.task.dto.QueryModel;
import com.hisense.task.domain.TaskLog;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jobLogDao")
public class TaskLogDaoImpl extends AbstractDaoImpl<Long, TaskLog> implements TaskLogDao {

    @Override
    public void saveJobLog(TaskLog taskLog) {
        super.saveOrUpdate(taskLog);
    }

    @Override
    public List<TaskLog> findLogList(QueryModel queryModel) {
        StringBuffer sb = new StringBuffer();
        sb.append("from TaskLog a where 1=1 ");
        if (queryModel != null) {
            if (StringUtils.isNotBlank(queryModel.getTaskGroup())) {
                sb.append(" and a.taskConfig.taskGroup = '" + queryModel.getTaskGroup() + "'");
            }
            if (StringUtils.isNotBlank(queryModel.getTaskName())) {
                sb.append(" and a.taskConfig.taskName like '%" + queryModel.getTaskName() + "%'");
            }
            if (StringUtils.isNotBlank(queryModel.getIsSuccess())) {
                sb.append(" and a.isSuccess like '%" + queryModel.getIsSuccess() + "%'");
            }
            if (StringUtils.isNotBlank(queryModel.getRunDate())) {
                sb.append(" and to_char(a.startTime,\'yyyy-MM-dd\')=\'" + queryModel.getRunDate() + "\' ");
            } else {
                sb.append(" and to_char(a.startTime,\'yyyy-MM-dd\')=TO_CHAR(SYSDATE(),\'yyyy-MM-dd\') ");
            }
        } else {
            sb.append(" and TO_CHAR(a.startTime,\'yyyy-MM-dd\')=TO_CHAR(SYSDATE(),\'yyyy-MM-dd\') ");
        }
        sb.append(" order by startTime desc ");
        Query query = createQuery(sb.toString());
        return query.list();
    }

}

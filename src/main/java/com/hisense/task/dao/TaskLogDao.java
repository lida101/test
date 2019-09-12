package com.hisense.task.dao;

import com.hisense.task.dto.QueryModel;
import com.hisense.task.domain.TaskLog;

import java.util.List;

public interface TaskLogDao extends AbstractDao<Long, TaskLog> {

	void saveJobLog(TaskLog taskLog);

	List<TaskLog> findLogList(QueryModel queryModel);
}

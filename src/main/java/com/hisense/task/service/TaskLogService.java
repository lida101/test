package com.hisense.task.service;

import com.hisense.task.dto.DataPage;
import com.hisense.task.dto.QueryModel;
import com.hisense.task.dto.QueryPage;
import com.hisense.task.domain.TaskLog;

import java.util.List;

public interface TaskLogService {
	
	void saveJobLog(TaskLog j);

    DataPage<TaskLog> queryPage(QueryPage page);

    List<TaskLog> findLogList(QueryModel queryModel);
}

package com.hisense.task.service;

import com.hisense.task.dao.TaskLogDao;
import com.hisense.task.dto.DataPage;
import com.hisense.task.dto.QueryModel;
import com.hisense.task.dto.QueryPage;
import com.hisense.task.domain.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("jobLogService")
@Transactional
public class TaskLogServiceImpl implements TaskLogService {
	
	@Autowired
	private TaskLogDao taskLogDao;

	@Override
	public void saveJobLog(TaskLog j) {
		this.taskLogDao.saveJobLog(j);
	}

	@Override
	public DataPage<TaskLog> queryPage(QueryPage page) {
		return this.taskLogDao.queryPageData(page);
	}

	@Override
	public List<TaskLog> findLogList(QueryModel queryModel) {
		return this.taskLogDao.findLogList(queryModel);
	}
}

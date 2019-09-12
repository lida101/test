package com.hisense.task.service;

import com.hisense.task.dto.QueryModel;
import com.hisense.task.domain.TaskConfig;

import java.util.List;

public interface TaskConfigService {

	TaskConfig getById(Long id);
	
	List<TaskConfig> findAll(QueryModel queryModel);
	
	void update(TaskConfig f);

	void init();

	void enableTask(Long taskId);

	void disableTask(Long taskId);

	void deleteTask(Long taskId);

	void saveTask(TaskConfig j);
}

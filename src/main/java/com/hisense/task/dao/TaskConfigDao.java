package com.hisense.task.dao;

import com.hisense.task.dto.QueryModel;
import com.hisense.task.domain.TaskConfig;

import java.util.List;

public interface TaskConfigDao extends AbstractDao<Long, TaskConfig> {

    List<TaskConfig> findAll(QueryModel queryModel);

    List<TaskConfig> findByIsAuto(String isAuto);

    void saveTask(TaskConfig config);

    List<TaskConfig> findByJobStatus(Long jobStatus);
}

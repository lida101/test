package com.hisense.task.service;

import com.hisense.task.dao.TaskConfigDao;
import com.hisense.task.dto.QueryModel;
import com.hisense.task.domain.TaskConfig;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("fwScheduleService")
@Transactional
public class TaskConfigServiceImpl implements TaskConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskConfigServiceImpl.class);

    @Resource
    private TaskConfigDao taskConfigDao;

    @Override
    public void saveTask(TaskConfig j) {
        this.taskConfigDao.saveTask(j);
    }

    @Override
    public TaskConfig getById(Long id) {
        return taskConfigDao.getByKey(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TaskConfig> findAll(QueryModel queryModel) {
        List<TaskConfig> taskConfigs = taskConfigDao.findAll(queryModel);
        Date now = new Date();
        if (taskConfigs != null && !taskConfigs.isEmpty()) {
            for (TaskConfig schedule : taskConfigs) {
                Trigger trigger = ScheduleInfoManager.getJobTrigger(schedule.getId() + "");
                if (trigger == null) {
                    // schedule.setJobStatus(0L);
                } else {
                    // schedule.setJobStatus(1L);
                    Date nextRunTime = trigger.getNextFireTime();
                    if (nextRunTime != null) {
                        Long minutes = (nextRunTime.getTime() - now.getTime()) / 1000 / 60;
                        schedule.setNextRunMinutes(minutes);
                        schedule.setNextRunTimeStr(minutesToStr(minutes));
                    }
                }
            }
            if (queryModel != null  && queryModel.getJobStatus() != null) {
                taskConfigs = taskConfigs.stream().filter(a -> queryModel.getJobStatus().equals(a.getJobStatus())).collect(Collectors.toList());
            }
        }
        return taskConfigs;
    }

    @Override
    public void update(TaskConfig f) {
        taskConfigDao.persistent(f);
    }

    @Override
    public void init() {
        List<TaskConfig> listTaskConfigIsAuto = taskConfigDao.findByIsAuto("Y");
        List<TaskConfig> listTaskConfigIsOpen = taskConfigDao.findByJobStatus(1l);
        List<TaskConfig> listTaskConfig = new ArrayList<>();
        listTaskConfig.addAll(listTaskConfigIsAuto);
        listTaskConfig.addAll(listTaskConfigIsOpen);
        if (listTaskConfig != null && listTaskConfig.size() > 0) {
            for (TaskConfig taskConfig : listTaskConfig) {
//                ScheduleInfoManager.enableCronSchedule(taskConfig);
                enableTask(taskConfig.getId());
            }
        }
    }

    /***
     * 启用定时任务.
     *
     * @param taskId
     *            任务Id.
     */
    @Override
    public void enableTask(Long taskId) {
        TaskConfig taskConfig = taskConfigDao.getByKey(taskId);
        if (taskConfig == null) {
            throw new RuntimeException("该任务ID不存在");
        }
        taskConfig.setJobStatus(1l);
        taskConfigDao.saveTask(taskConfig);
        ScheduleInfoManager.enableCronSchedule(taskConfig);

    }

    /***
     * 停止定时任务.
     *
     * @param taskId
     *            任务Id.
     */
    @Override
    public void disableTask(Long taskId) {
        TaskConfig taskConfig = taskConfigDao.getByKey(taskId);
        if (taskConfig == null) {
            throw new RuntimeException("该任务ID不存在");
        }
        boolean success = ScheduleInfoManager.disableSchedule(taskConfig.getId() + "");
        taskConfig.setJobStatus(0l);
        taskConfigDao.saveTask(taskConfig);
        if (!success) {
            throw new RuntimeException("操作失败");
        }
    }

    /***
     * 刪除定时任务.
     *
     * @param taskId
     *            任务Id.
     */
    @Override
    public void deleteTask(Long taskId) {
        TaskConfig taskConfig = taskConfigDao.getByKey(taskId);
        if (taskConfig == null) {
            throw new RuntimeException("该任务ID不存在");
        }
        taskConfigDao.delete(taskConfig);

    }


    private String minutesToStr(Long minutes) {
        if (minutes < 60) {
            return minutes + "分";
        } else {
            Long hour = minutes / 60;
            minutes = minutes % 60;
            if (hour < 24) {
                return hour + "小时" + (minutes > 0 ? minutes + "分" : "");
            } else {
                Long day = hour / 24;
                hour = hour% 24;
                return day + "天" + (hour > 0 ? hour + "小时" : "") + (minutes > 0 ? minutes + "分" : "");
            }
        }
    }

}

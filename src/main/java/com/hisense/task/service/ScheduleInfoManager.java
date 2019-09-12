package com.hisense.task.service;

import com.hisense.task.domain.TaskConfig;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Description: 计时器工具类
 * <p>
 * <pre>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2013-7-19    lanzhongliang   1.0      1.0 Version
 * </pre>
 */

public class ScheduleInfoManager {

    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    static {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 启动一个自定义的job
     *
     * @param schedule 自定义的job
     * @return 成功则返回true，否则返回false
     */
    public static void enableCronSchedule(TaskConfig schedule) {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(schedule.getId() + "");
            Trigger trigger = scheduler.getTrigger(triggerKey);

            if (null == trigger) {// 如果不存在该trigger则创建一个
                JobDetail jobDetail = JobBuilder.newJob(AsynchroExecuteJobAction.class)
                        .withIdentity(schedule.getId() + "").build();

                JobDataMap paramsMap = new JobDataMap();
                paramsMap.put("taskName", schedule.getTaskName());
                paramsMap.put("taskGroup", schedule.getTaskGroup());
                paramsMap.put("service", schedule.getExecService());
                paramsMap.put("method", schedule.getExecMethod());
                paramsMap.put("params", schedule.getParams());
                paramsMap.put("receivers", schedule.getMailReceiver());
                jobDetail.getJobBuilder().setJobData(paramsMap);

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(schedule.getId() + "")
                        .withSchedule(CronScheduleBuilder.cronSchedule(schedule.getCronExpression()))
                        .build();

                scheduler.scheduleJob(jobDetail, trigger);

            } else { // Trigger已存在，那么更新相应的定时设置
                trigger = TriggerBuilder.newTrigger().
                        withIdentity(schedule.getId() + "").
                        withSchedule(CronScheduleBuilder.cronSchedule(schedule.getCronExpression())).
                        build();

                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Description: 禁用一个job
     *
     * @param jobId 需要被禁用的job的ID
     * @return 成功则返回true，否则返回false\
     */
    public static boolean disableSchedule(String jobId) {
        if ("".equalsIgnoreCase(jobId)) {
            return false;
        }
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            Trigger trigger = getJobTrigger(jobId);
            if (null != trigger) {
                scheduler.deleteJob(new JobKey(jobId));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * Description: 得到job对应的Trigger
     *
     * @param jobId job的ID
     * @return job的Trigger, 如果Trigger不存在则返回null
     */
    public static Trigger getJobTrigger(String jobId) {
        if (StringUtils.isBlank(jobId)) {
            return null;
        }
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = new TriggerKey(jobId);
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断job是否在运行
     * @param jobId job的Id
     * @return
     */
    public static boolean checkExists(String jobId) {
        boolean isExists = false;
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            isExists = scheduler.checkExists(new TriggerKey(jobId));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return isExists;
    }
}
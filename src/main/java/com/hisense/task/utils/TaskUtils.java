package com.hisense.task.utils;

import com.hisense.task.domain.TaskConfig;
import com.hisense.task.domain.TaskLog;
import com.hisense.task.service.TaskConfigService;
import com.hisense.task.service.TaskLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 任务工具类
 *
 * @author TangWenKuo
 * @date 2016年8月8日 下午4:29:13
 */
@SuppressWarnings("unchecked")
@Component
public class TaskUtils {

    private static TaskConfigService scheduleService;

    private static TaskLogService taskLogService;

    private static TaskConfigService taskConfigService;

//    private static String mailReceiver;

    @Autowired
    public void setScheduleService(TaskConfigService scheduleService) {
        TaskUtils.scheduleService = scheduleService;
    }

    @Autowired
    public void setJobLogService(TaskLogService taskLogService) {
        TaskUtils.taskLogService = taskLogService;
    }

    @Autowired
    public void setConfigService(TaskConfigService taskConfigService) {
        TaskUtils.taskConfigService = taskConfigService;
    }

//    @Value("${mail.receiver}")
//    public void setMailReceiver(String mailReceiver) {
//        TaskUtils.mailReceiver = mailReceiver;
//    }


    public static void doTask(Long jobId, boolean isManual) {
        TaskConfig taskConfig = scheduleService.getById(jobId);
//        taskConfig.setJobStatus(1L);
//        taskConfigService.saveTask(taskConfig);
        TaskLog log = new TaskLog();
        log.setTaskConfig(taskConfig);
        log.setStartTime(new Date());
        log.setIsSuccess(isManual ? "手动执行中" : "执行中");
        taskLogService.saveJobLog(log);

        try {
            String params = taskConfig.getParams();
            Object o = ApplicationContextUtils.getBean(taskConfig.getExecService());
            Method m;
            if (StringUtils.isBlank(params)) {
                m = o.getClass().getDeclaredMethod(taskConfig.getExecMethod());
                m.invoke(o);
            } else {
                m = o.getClass().getDeclaredMethod(taskConfig.getExecMethod(), String.class);
                m.invoke(o, params);
            }
            log.setIsSuccess(isManual ? "手动执行成功" : "执行成功");
        } catch (Exception e) {
            Throwable t = e;
            if (e instanceof InvocationTargetException) {
                t = ((InvocationTargetException) e).getTargetException();
            }
            log.setMessage(t.getMessage());
            log.setIsSuccess(isManual ? "手动执行失败" : "执行失败");
//            String receivers = mergeReceivers(mailReceiver, taskConfig.getMailReceiver());
//            if (StringUtils.isNotEmpty(receivers)) {
//                mailService.sendSimpleMail(receivers,
//                        "定时任务--" + log.getTaskName() + "--执行失败",
//                        FrameUtil.getExceptionTrace(t));
//            }
        } finally {
            log.setEndTime(new Date());
            taskLogService.saveJobLog(log);
        }
    }

    private static String mergeReceivers(String mailReceiver, String correspondingReceiver) {
        String receivers;
        if (StringUtils.isBlank(mailReceiver)) {
            receivers = correspondingReceiver;
        } else if (StringUtils.isBlank(correspondingReceiver)) {
            receivers = mailReceiver;
        } else {
            receivers = mailReceiver + "," + correspondingReceiver;
        }
        return receivers;
    }
}

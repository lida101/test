package com.hisense.task.service;

import com.hisense.task.utils.TaskUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.MethodInvokingJob;

/**
 * 
 * 
 * Description:异步定时任务执行类
 * 
 * <pre>
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2013-7-22    lanzhongliang   1.0      1.0 Version
 * </pre>
 */
public class AsynchroExecuteJobAction extends MethodInvokingJob {

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		TaskUtils.doTask(Long.valueOf(context.getJobDetail().getKey().getName()), false);
	}
}
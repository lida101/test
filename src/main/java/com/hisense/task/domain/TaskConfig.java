package com.hisense.task.domain;

import javax.persistence.*;

@Entity
@Table(name = "TASK_CONFIG")
public class TaskConfig {


    private static final long serialVersionUID = 1L;

    @Id
    @Column(name= "TASK_ID", unique = true, nullable = false )
    @SequenceGenerator(name = "my_seq", sequenceName = "sq_task",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
    private Long id;


    /**
     * 任务名称
     */
    @Column(name = "TASK_NAME", nullable = false, length = 120)
    private String taskName;


    /**
     * 任务所属组的名称
     */
    @Column(name = "TASK_GROUP", nullable = false, length = 100)
    private String taskGroup;


    /**
     * 运行时间表达式
     */
    @Column(name = "CRON_EXPRESSION", nullable = false, length = 60)
    private String cronExpression;

    /**
     * 参数
     */
    @Column(name = "PARAMS", length = 300)
    private String params;


    @Column(name = "IS_AUTO")
    private String isAuto;

    /**
     * 任务失败时邮件通知地址
     */
    @Column(name = "mail_receiver")
    private String mailReceiver;

    /**
     * 执行服务名
     */
    @Column(name = "EXEC_SERVICE")
    private String execService;

    /**
     * 执行方法名
     */
    @Column(name = "EXEC_METHOD")
    private String execMethod;

    /**
     * 距下次执行还有多长时间
     //     */
    @Transient
    private String nextRunTimeStr;

    /**
     * 距下次运行还有多少分钟
     //     */
    @Transient
    private Long nextRunMinutes;

    //@Transient
    @Column(name = "JOB_STATUS")
    private Long jobStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getTaskGroup() {
        return this.taskGroup;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return this.params;
    }

    public String getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(String isAuto) {
        this.isAuto = isAuto;
    }

    public String getNextRunTimeStr() {
        return nextRunTimeStr;
    }

    public void setNextRunTimeStr(String nextRunTimeStr) {
        this.nextRunTimeStr = nextRunTimeStr;
    }

    public String getMailReceiver() {
        return mailReceiver;
    }

    public void setMailReceiver(String mailReceiver) {
        this.mailReceiver = mailReceiver;
    }

    public String getExecService() {
        return execService;
    }

    public void setExecService(String execService) {
        this.execService = execService;
    }

    public String getExecMethod() {
        return execMethod;
    }

    public void setExecMethod(String execMethod) {
        this.execMethod = execMethod;
    }

    public Long getNextRunMinutes() {
        return nextRunMinutes;
    }

    public void setNextRunMinutes(Long nextRunMinutes) {
        this.nextRunMinutes = nextRunMinutes;
    }

    public Long getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Long jobStatus) {
        this.jobStatus = jobStatus;
    }
}

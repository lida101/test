package com.hisense.task.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TASK_LOG")
public class TaskLog {
	
	private static final long serialVersionUID = 2760421212021508566L;

	@Id
    @Column(name= "LOG_ID", unique = true, nullable = true )
	@SequenceGenerator(name = "my_seq", sequenceName = "sq_task",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
	private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID")
    private TaskConfig taskConfig;

	@Column(name= "START_TIME")
	private Date startTime;
	
	@Column(name= "END_TIME")
	private Date endTime;
	
	@Column(name= "IS_SUCCESS")
	private String isSuccess;
	
	@Column(name= "MESSAGE")
	private String message;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TaskConfig getTaskConfig() {
		return taskConfig;
	}

	public void setTaskConfig(TaskConfig taskConfig) {
		this.taskConfig = taskConfig;
	}
}

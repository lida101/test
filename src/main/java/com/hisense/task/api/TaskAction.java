package com.hisense.task.api;

import com.hisense.task.dao.TaskConfigDaoImpl;
import com.hisense.task.dto.DataPage;
import com.hisense.task.dto.QueryModel;
import com.hisense.task.dto.QueryPage;
import com.hisense.task.domain.TaskConfig;
import com.hisense.task.domain.TaskLog;
import com.hisense.task.service.ScheduleInfoManager;
import com.hisense.task.service.TaskConfigService;
import com.hisense.task.service.TaskLogService;
import com.hisense.task.utils.BeanUtils;
import com.hisense.task.utils.TaskUtils;
import org.quartz.CronExpression;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class TaskAction {

    @Resource
    private TaskConfigService taskConfigService;

    @Resource
    private TaskLogService taskLogService;

    @Resource
    private TaskConfigDaoImpl taskConfigDao;

    private static final String LOGIN = "login";

    private static final String TASK_LIST = "taskList";

    private static final String TASK_EDIT = "taskEdit";

    private static final String TASK_ADD = "taskAdd";

    private static final String TASK_LOG = "taskLog";

    private static final String TASK_DELETE = "taskDelete";

    private static final String TASK_LOG_QUERY = "taskLogQuery";

    @RequestMapping(value = "/login")
    public String login() {
        return LOGIN;
    }

    /***
     * 翻页查询FwSchedule信息.
     */
    @RequestMapping(value = "/")
    public String getAllFwSchedule(Model model,@ModelAttribute QueryModel queryModel) {
        List<TaskConfig> data = taskConfigService.findAll(queryModel);
        model.addAttribute("tasks", data);
        model.addAttribute("queryModel", queryModel);
        return TASK_LIST;
    }

    /**
     * 修改FwSchedule信息
     */
    @RequestMapping(value = "/edit")
    public String getFwScheduleDto(Model model, @RequestParam("taskId") Long taskId) {
        TaskConfig taskConfig = taskConfigService.getById(taskId);
        model.addAttribute("taskConfig", taskConfig);
        return TASK_EDIT;
    }

    /**
     * 修改FwSchedule信息
     */
    @RequestMapping(value = "/add")
    public String addFwScheduleDto() {
        return TASK_ADD;
    }


    @RequestMapping(value = "/begin", method = RequestMethod.GET)
    public String beginFwSchedule(@RequestParam("taskId") Long taskId) {
        try {
            taskConfigService.enableTask(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/beginAll")
    public String beginFwScheduleAll() {
        try {
            List<TaskConfig> taskConfigs = new ArrayList<>();
            taskConfigs = taskConfigDao.findAll(null);
            for (TaskConfig config : taskConfigs) {
                taskConfigService.enableTask(config.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public String stopFwSchedule(@RequestParam("taskId") Long taskId) {
        try {
            taskConfigService.disableTask(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteFwSchedule(@RequestParam("taskId") Long taskId) {
        try {
            taskConfigService.deleteTask(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/update")
    public String updateFwSchedule(TaskConfig taskConfig) {
        if(!CronExpression.isValidExpression(taskConfig.getCronExpression())) {
            throw new RuntimeException("Cron表达式不合法");
        }
        TaskConfig task = taskConfigService.getById(taskConfig.getId());
        boolean isRunning = ScheduleInfoManager.checkExists(taskConfig.getId() + "");
        if (isRunning) {
            taskConfigService.disableTask(taskConfig.getId());
        }

        //更新任务实体
        BeanUtils.copyNotNullProperties(taskConfig, task);
        taskConfigService.update(task);

        if (isRunning) {
            taskConfigService.enableTask(task.getId());
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/create")
    public String addFwSchedule(TaskConfig taskConfig) {
        if (!CronExpression.isValidExpression(taskConfig.getCronExpression())) {
            throw new RuntimeException("Cron表达式不合法");
        }
        boolean isRunning = ScheduleInfoManager.checkExists(taskConfig.getId() + "");
        if (isRunning) {
            taskConfigService.disableTask(taskConfig.getId());
        }
        taskConfigService.update(taskConfig);
        return "redirect:/";
    }


    /**
     * 执行记录
     */
    @RequestMapping(value = "/taskLog")
    public String taskLog(Model model,
                          @RequestParam("taskId") Long taskId,
                          @RequestParam(value = "page", required = false) Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if(pageNumber == null) {
            pageNumber = 1;
        }
        if(pageSize == null) {
            pageSize = 20;
        }
        QueryPage queryPage = new QueryPage();
        queryPage.setPage(pageNumber);
        queryPage.setPageSize(pageSize);
        queryPage.setSqlCondition(" taskConfig.id = " + taskId + " ");
        queryPage.setOrderBy("startTime desc");
        DataPage<TaskLog> page = taskLogService.queryPage(queryPage);
        model.addAttribute("page", page);
        model.addAttribute("taskId", taskId);
        return TASK_LOG;
    }

    /**
     * 执行记录
     */
    @RequestMapping(value = "/logList")
    public String logList(Model model, @ModelAttribute QueryModel queryModel) {
        List<TaskLog> taskLogList = this.taskLogService.findLogList(queryModel);
        model.addAttribute("list", taskLogList);
        model.addAttribute("queryModel", queryModel);
        return TASK_LOG_QUERY;
    }

    /**
     * 立即执行一次，这个操作完全独立的运行，不影响定时任务的执行，请谨慎使用
     */
    @RequestMapping(value = "/justrun", method = RequestMethod.GET)
    @ResponseBody
    public String runOne(Model model, @RequestParam("taskId") Long taskId) {
        TaskUtils.doTask(taskId, true);
        return "success";
    }


}

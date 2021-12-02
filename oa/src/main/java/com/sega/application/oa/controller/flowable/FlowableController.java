//package com.sega.application.oa.controller.flowable;
//
//import com.alibaba.fastjson.JSONObject;
//import com.sega.application.oa.controller.system.BaseController;
//import com.sega.application.oa.entity.system.OutData;
//import com.sega.application.oa.entity.system.UserEntity;
//import com.sega.application.oa.service.flowable.FlowableService;
//import org.apache.commons.lang3.StringUtils;
//import org.flowable.engine.HistoryService;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.history.HistoricActivityInstance;
//import org.flowable.engine.history.HistoricActivityInstanceQuery;
//import org.flowable.task.api.Task;
//import org.flowable.task.api.history.HistoricTaskInstance;
//import org.flowable.ui.common.model.UserRepresentation;
//import org.flowable.ui.common.security.DefaultPrivileges;
//import org.flowable.variable.api.history.HistoricVariableInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.*;
//
//@RestController
//@RequestMapping("/flowable")
//public class FlowableController  extends BaseController {
//
//    @Autowired
//    private FlowableService flowableService;
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private HistoryService historyService;
//
//    /**
//     * 获取默认的管理员信息
//     * @return
//     */
//    @RequestMapping(value = "/login/rest/account", method = RequestMethod.GET, produces = "application/json")
//    public UserRepresentation getAccount() {
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setId("admin");
//        userRepresentation.setEmail("admin@flowable.org");
//        userRepresentation.setFullName("Administrator");
////        userRepresentation.setLastName("Administrator");
//        userRepresentation.setFirstName("Administrator");
//        List<String> privileges = new ArrayList<>();
//        privileges.add(DefaultPrivileges.ACCESS_MODELER);
//        privileges.add(DefaultPrivileges.ACCESS_IDM);
//        privileges.add(DefaultPrivileges.ACCESS_ADMIN);
//        privileges.add(DefaultPrivileges.ACCESS_TASK);
//        privileges.add(DefaultPrivileges.ACCESS_REST_API);
//        userRepresentation.setPrivileges(privileges);
//        return userRepresentation;
//    }
//
//    /**
//     *
//     * @param procDefId1 流程标识
//     * @param bussinessKey 业务Id
//     * @param map 参数值
//     * @param request
//     * @param response
//     */
//    @RequestMapping("/start")
//    public void start(@RequestParam String cls, String map, @RequestParam String bussinessKey, String state, HttpServletRequest request, HttpServletResponse response){
//        try {
//            if(StringUtils.isBlank(state)){
//                state = 0 + "";
//            }
//            this.outJson(response, flowableService.start(cls, map, bussinessKey, state,getUserBySession(request)));
//        } catch (Exception e) {
//            e.printStackTrace();
//            this.outJson(response, "发生错误启动失败");
//        }
//    }
//
//    /**
//     * 查询任务列表
//     * @param request
//     * @param response
//     */
//    @RequestMapping("/taskList")
//    public void taskList(HttpServletRequest request, HttpServletResponse response){
//        try {
//            List<Task> taskList = taskService.createTaskQuery().list();
//            this.outJson(response, new OutData(true,"查询成功",taskList));
//        } catch (Exception e) {
//            this.outJson(response, new OutData(true,"查询失败"));
//            // TODO: handle exception
//        }
//    }
//
//    /**
//     * 待办事项
//     * @param request
//     * @param response
//     */
//    @RequestMapping("/todoList")
//    public void todoList(Integer page,Integer rows,HttpServletRequest request, HttpServletResponse response){
//        try {
//            UserEntity user = getUserBySession(request);
//            //根据用户获取任务
//            List<Task> taskList = taskService.createTaskQuery().active().
//                    includeProcessVariables().orderByTaskCreateTime().
//                    taskAssignee(user.getUserId() + "").desc().list();
//            //listPage((easyUIPage.getPage() - 1) * easyUIPage.getRows(), easyUIPage.getPage() * easyUIPage.getRows());
//            //根据角色获取任务
//            List<Task> _taskList = taskService.createTaskQuery().active().
//                    includeProcessVariables().orderByTaskCreateTime().
//                    taskCandidateGroup(user.getUserRoleId() + "").desc().list();
//            //listPage((easyUIPage.getPage() - 1) * easyUIPage.getRows(), easyUIPage.getPage() * easyUIPage.getRows());
//            if(taskList.size() == 0){
//                taskList = new ArrayList<>();
//            }
//            taskList.addAll(_taskList);
//
//            List mapList = new ArrayList<>();
//            int start = (page - 1) * rows;
//            for(int i = start ; i < taskList.size() && i < start + rows;i++){
//                Task _task  = taskList.get(i);
//                Map map = new HashMap<>();
//                map.put("taskId", _task.getId());
//                map.put("taskName", _task.getName());
//                map.put("taskDefinitionKey", _task.getTaskDefinitionKey());
//                map.put("createTime", _task.getCreateTime());
//                map.put("description", _task.getDescription());
//                map.put("processVariables", _task.getProcessVariables());
//                map.put("taskLocalVariables", _task.getTaskLocalVariables());
//                map.put("taskFormKey", _task.getFormKey());
//                map.put("pocessInstanceId", _task.getProcessInstanceId());
//                mapList.add(map);
//            }
//            Map map = new HashMap();
//            map.put("data",mapList);
//            map.put("total",taskList.size());
//            this.outJson(response, new OutData(true,"查询失败", map), "yyyy-MM-dd");
//        } catch (Exception e) {
//            this.outJson(response, new OutData(true,"查询失败"));
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 已处理事件
//     * @param request
//     * @param response
//     */
//    @RequestMapping("/alreadySolve")
//    public void alreadySolve(Integer page,Integer rows,HttpServletRequest request, HttpServletResponse response){
//        try {
//            UserEntity user = getUserBySession(request);
//            List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().
//                    taskAssignee(user.getUserId() + "").list();
//            List<HistoricTaskInstance> _taskList = historyService.createHistoricTaskInstanceQuery().includeProcessVariables().
//                    taskCandidateGroup(user.getUserRoleId() + "").list();
//            if(taskList.size() == 0){
//                taskList = new ArrayList<>();
//            }
//            taskList.addAll(_taskList);
//            List mapList = new ArrayList<>();
//            int start = (page - 1) * rows;
//            for(int i = start ; i < taskList.size() && i < start + rows;i++){
//                HistoricTaskInstance _task  = taskList.get(i);
//                Map map = new HashMap<>();
//                map.put("taskId", _task.getId());
//                map.put("taskName", _task.getName());
//                map.put("taskDefinitionKey", _task.getTaskDefinitionKey());
//                map.put("createTime", _task.getCreateTime());
//                map.put("endTime", _task.getEndTime());
//                map.put("description", _task.getDescription());
//                map.put("processVariables", _task.getProcessVariables());
//                map.put("taskLocalVariables", _task.getTaskLocalVariables());
//                map.put("taskFormKey", _task.getFormKey());
//                map.put("pocessInstanceId", _task.getProcessInstanceId());
//                mapList.add(map);
//            }
//            Map map = new HashMap();
//            map.put("data",mapList);
//            map.put("total",taskList.size());
//            this.outJson(response, new OutData(true,"查询成功", map), "yyyy-MM-dd");
//        } catch (Exception e) {
//            this.outJson(response, new OutData(true,"查询失败"));
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 完成任务
//     * @param taskId
//     * @param map
//     * @param request
//     * @param response
//     */
//    @RequestMapping("/completeTask")
//    public void completeTask(String taskId,String procInstId,String comment, String map,HttpServletRequest request, HttpServletResponse response){
//        try {
//            Map<String,Object> mapObj = JSONObject.parseObject(map,Map.class);
//            UserEntity user =  getUserBySession(request);
//            mapObj.put("userId",user.getUserId());
//            mapObj.put("userName",user.getUserName());
//            if(StringUtils.isBlank(comment)){
//                comment = "";
//            }
//            taskService.setVariablesLocal(taskId,mapObj);
//            taskService.addComment(taskId, procInstId, comment);
//            taskService.complete(taskId, mapObj);
//            this.outJson(response, new OutData(true, "任务完成"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            this.outJson(response, new OutData(false, "发生错误"));
//        }
//    }
//
//    /**
//     * 查询流程历史信息
//     * @param procInstId
//     * @param response
//     * @param request
//     */
//    @RequestMapping("/history")
//    public void history(String procInstId, HttpServletResponse response, HttpServletRequest request){
//        List<HistoricActivityInstance> list =  ((HistoricActivityInstanceQuery) ((HistoricActivityInstanceQuery) historyService.createHistoricActivityInstanceQuery().processInstanceId(procInstId)
//                .orderByHistoricActivityInstanceStartTime().asc()).orderByHistoricActivityInstanceEndTime()
//                .asc()).list();
//        List mapList = new ArrayList<>();
//        List<HistoricVariableInstance> var = historyService.createHistoricVariableInstanceQuery()
//                .processInstanceId(procInstId).list();
//        Map _map = new HashMap();
//
//        for (int i = 0; i < var.size(); i++) {
//            HistoricVariableInstance v = var.get(i);
//            _map.put(v.getVariableName(), v.getValue());
//        }
//        for(HistoricActivityInstance histIns : list){
//            Map  map = new HashMap();
//            if(StringUtils.isBlank(histIns.getActivityName())){
//                continue;
//            }
//            if (StringUtils.isNotBlank(histIns.getActivityName()) && StringUtils.isBlank(histIns.getTaskId())){
//                map.put("startTime", histIns.getStartTime());
//                map.put("endTime", histIns.getEndTime());
//                map.put("taskName", histIns.getActivityName());
//                map.put("variables",_map);
//                mapList.add(map);
//                continue;
//            }
//            HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery().taskId(histIns.getTaskId()).singleResult();
//            map.put("taskId", histIns.getTaskId());
//            map.put("taskName", histIns.getActivityName());
//            map.put("startTime", histIns.getStartTime());
//            map.put("endTime", histIns.getEndTime());
//            map.put("processDefinitionId", histIns.getProcessDefinitionId());
//            map.put("processInstanceId", histIns.getProcessInstanceId());
//            map.put("assignee", histIns.getAssignee());
//            map.put("comment", taskService.getTaskComments(histIns.getTaskId()));
//            histIns.getExecutionId();
//            //查询每个任务节点的参数
//            List<HistoricVariableInstance> taskVariables = historyService.createHistoricVariableInstanceQuery().taskId(histIns.getTaskId()).list();
//            Map taskVarMap = new HashMap();
//            for (int i = 0; i < taskVariables.size(); i++) {
//                HistoricVariableInstance v = taskVariables.get(i);
//                taskVarMap.put(v.getVariableName(), v.getValue());
//            }
//            map.put("variables",taskVarMap);
//            mapList.add(map);
//        }
//        Map outMap = new HashMap();
//        outMap.put("data",mapList);
//        outMap.put("total",mapList.size());
//        this.outJson(response, new OutData(true,"查询成功",outMap), "yyyy-MM-dd HH:mm:ss");
//    }
//}

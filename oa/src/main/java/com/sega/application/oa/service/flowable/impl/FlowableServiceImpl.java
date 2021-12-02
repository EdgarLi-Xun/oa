//package com.sega.application.oa.service.flowable.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.sega.application.oa.annotation.WorkFlow;
//import com.sega.application.oa.dao.system.IBaseDao;
//import com.sega.application.oa.dao.system.IUserDao;
//import com.sega.application.oa.entity.system.UserEntity;
//import com.sega.application.oa.service.flowable.FlowableService;
//import com.sega.application.oa.service.system.impl.BaseServiceImpl;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.runtime.ProcessInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class FlowableServiceImpl extends BaseServiceImpl implements FlowableService {
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//
//    @Autowired
//    private IUserDao userDao;
//
//    protected IBaseDao getDao() {
//        return userDao;
//    }
//
//    @Override
//    public Map start(String cls, String map, String bussinessKey, String state, UserEntity userEntity) {
//        try {
//            Class clazz = Class.forName(cls);
//            WorkFlow workFolw = (WorkFlow) clazz.getAnnotation(WorkFlow.class);
//            Map<String, Object> mapObj = JSONObject.parseObject(map, Map.class);
//            mapObj.put("startUserName", userEntity.getUserName());
//            mapObj.put("TaskName", "启动");
//            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(workFolw.procDefKey(), bussinessKey, mapObj);
//            String sql = "update " + workFolw.table()
//                    + " set " + workFolw.procInstColumn() + "=" + processInstance.getId()
//                    + "," + workFolw.statusColumn() + "=" + state
//                    + " where " + workFolw.tableId() + "=" + bussinessKey;
//            userDao.excuteSql(sql);
//            Map _map = new HashMap();
//            _map.put("result",true);
//            _map.put("data","启动成功");
//            return _map;
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            Map _map = new HashMap();
//            _map.put("result",false);
//            _map.put("data","启动失败");
//            return _map;
//        }
//    }
//}

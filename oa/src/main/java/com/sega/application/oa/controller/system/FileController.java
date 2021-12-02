package com.sega.application.oa.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sega.application.oa.service.humanengineering.IAttendanceRecordService;
import com.sega.application.oa.service.system.IFileService;
import com.sega.application.oa.entity.system.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.sega.application.oa.entity.system.OutData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import org.springframework.web.multipart.MultipartFile;

/**
 * 管理控制层
 * @author 孙乾
 * @version 
 * 版本号：1.0.0<br/>
 * 创建日期：2019-12-18 16:30:25<br/>
 * 历史修订：<br/>
 */
@RestController
@CrossOrigin
@RequestMapping("/apis/daily/file")
public class FileController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);
	/**
	 * 注入业务层
	 */	
	@Autowired
	private IFileService fileService;

    @Value("${file.path}")
    private String path;

    @Autowired
    private IAttendanceRecordService attendanceRecordService;
	
	/**
     *新增或更新数据
     * @param file
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public void saveOrUpdate(@RequestBody FileEntity file, HttpServletResponse response, HttpServletRequest request) {
        try {
        	if (file.getFileId() == null) {
                file.setFileCreateDate(new Date());
                file.setFileCreateBy(this.getUserBySession(request).getUserId());
        		fileService.saveEntity(file);
                this.outJson(response, new OutData(true,"新增成功",file), "yyyy-MM-dd");
        	}else{
                fileService.updateEntity(file);
                file.setFileUpdateBy(this.getUserBySession(request).getUserId());
                file.setFileUpdateDate(new Date());
                this.outJson(response, new OutData(true,"更新成功",file), "yyyy-MM-dd");
        	}
        }catch (Exception e){
            log.error(e.getMessage(),e);
            if (file.getFileId() == null) {
                this.outJson(response, new OutData(false,"新增失败"), "yyyy-MM-dd");
            }else{
                this.outJson(response, new OutData(false,"更新失败"), "yyyy-MM-dd");
        	}
        }
    }
    
    /**
	 * 逻辑删除
	 * @param tid
	 * @return
	 */
	@DeleteMapping("/delete/{tid}")
	@ResponseBody
	public void delete(@PathVariable Long tid, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
			FileEntity file = new FileEntity();
            file.setFileId(tid);
            file.setFileDel(1);
            file.setFileUpdateDate(new Date());
            fileService.updateEntity(file);
			this.outJson(response, new OutData(false,"删除成功",file), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}

    /**
	 * 逻辑删除
	 * @param tids
	 * @return
	 */
	@PostMapping("/deleteByIds")
	@ResponseBody
	public void delete( @RequestBody  String tids, HttpServletResponse response, HttpServletRequest request) throws NoSuchFieldException {
		try {
            List<Long> ids = JSONArray.parseArray(tids,Long.class);
            for(Long tid : ids){
                FileEntity file = new FileEntity();
                file.setFileId(tid);
                file.setFileDel(1);
                file.setFileUpdateDate(new Date());
                fileService.updateEntity(file);
            }
			this.outJson(response, new OutData(false,"删除成功"), "yyyy-MM-dd");
		}catch (RuntimeException e){
			this.outJson(response, new OutData(false,"删除失败"), "yyyy-MM-dd");
		}
	}
	
	/**
     * 按条件分页查询数据
     * @param  file
     * @return
     */
    @PostMapping("/queryByPage")
    @ResponseBody
    public void queryByPage(@RequestBody FileEntity file, HttpServletResponse response, HttpServletRequest request) {
        try {

            if (file.getPageNum() == null) {
                file.setPageNum(0);
            }
            if (file.getPageSize() == null) {
               file.setPageSize(20);
            }
            PageHelper.startPage(file.getPageNum(),file.getPageSize());
            List<FileEntity> list = fileService.query(file);
            this.outJson(response,  new OutData(true,"查询成功",new PageInfo<FileEntity>(list)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(true,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 按条件查询数据
     * @param file
     * @return
     */
    @PostMapping("/queryAll")
    @ResponseBody
    public void queryAll(@RequestBody FileEntity file, HttpServletResponse response, HttpServletRequest request) {
        try {
            List<FileEntity> list = fileService.query(file);
            this.outJson(response,  new OutData(true,"查询成功",list), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }
    
    /**
     * 根据ID获取数据
     *
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    @ResponseBody
    public void get(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) {
    	try {
            this.outJson(response,  new OutData(true,"查询成功",fileService.getEntity(id)), "yyyy-MM-dd");
        } catch (RuntimeException e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"查询失败"), "yyyy-MM-dd");
        }
    }

    /**
     * 插入excel获取数据
     *
     */
    @RequestMapping("/import")
    @ResponseBody
    public void importExcel(MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        try {
            String fileSep = System.getProperty("file.separator");
            String[] str1 = file.getOriginalFilename().split("\\.");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String[] date = simpleDateFormat.format(new Date()).split("-");
            File dest = new File(path+fileSep+date[0]+fileSep+date[1]+fileSep+date[2]+fileSep+new Date().getTime()+file.getOriginalFilename());
            File file1 = new File(path+fileSep+date[0]);
            if(!file1.exists()){
                file1.mkdirs();
                File file2 = new File(path+fileSep+date[0]+fileSep+date[1]);
                if(!file2.exists()){
                    file2.mkdirs();
                    File file3 = new File(path+fileSep+date[0]+fileSep+date[1]+fileSep+date[2]);
                    if(!file3.exists()){
                        file3.mkdirs();
                    }
                }
            }
            if(!dest.exists())
            {
                boolean cf = dest.createNewFile();
                if(cf)
                {
                    file.transferTo(dest);//MultipartFile能自动转存文件
                    //other action
                }
            }
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileDel(0);
            fileEntity.setFilePath(dest.getCanonicalPath());
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setFileRealName(new Date().getTime()+file.getOriginalFilename());
            fileEntity.setFileCreateDate(new Date());
            fileEntity.setFileCreateBy(this.getUserBySession(request).getUserId());
            fileEntity.setFileType(str1[str1.length-1]);
            fileService.saveEntity(fileEntity);
            this.outJson(response,  new OutData(true,"插入成功",fileEntity), "yyyy-MM-dd");
        } catch (Exception e){
            log.error(e.getMessage(),e);
            this.outJson(response,  new OutData(false,"插入失败"), "yyyy-MM-dd");
        }
    }
}
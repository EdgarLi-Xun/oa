package com.sega.application.oa.entity.system;

import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author 邱小兵
 * @version 版本号：1.0.0<br/>
 * 创建日期：2017-8-3 20:56:15<br/>
 * 历史修订：<br/>
 */
public class ModelEntity extends BaseEntity {

    /**
     * 子节点列表
     */
    private List<ModelEntity> children;

    /**
     * 节点是否选中
     */
    private Boolean _isChecked = false;

    /**
     * 模块菜单
     */
    private Long modelId;
    /**
     * 模块名称
     */
    private String modelName;
    /**
     * 模块编号 标准长10位，每两位表示一层级
     */
    private String modelCode;

    /**
     * 模块连接
     */
    private String modelUrl;

    /**
     * 模块描述
     */
    private String modelDescribe;
    /**
     * 模块父id
     */
    private Long modelParentId;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     *
     */
    private Date createDate;
    /**
     *
     */
    private Long updateBy;
    /**
     *
     */
    private Date updateDate;
    /**
     * 是否已删除：0：未删除，1:已删除
     */
    private Integer modelDel;

    public ModelEntity() {
    }

    /**
     * 设置
     */
    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取
     */
    public Long getModelId() {
        return this.modelId;
    }

    /**
     * 设置模块名称
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * 获取模块名称
     */
    public String getModelName() {
        return this.modelName;
    }

    /**
     * 设置模块编号 标准长10位，每两位表示一层级
     */
    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    /**
     * 获取模块编号 标准长10位，每两位表示一层级
     */
    public String getModelCode() {
        return this.modelCode;
    }

    /**
     * 设置模块描述
     */
    public void setModelDescribe(String modelDescribe) {
        this.modelDescribe = modelDescribe;
    }

    /**
     * 获取模块描述
     */
    public String getModelDescribe() {
        return this.modelDescribe;
    }

    /**
     * 设置模块父id
     */
    public void setModelParentId(Long modelParentId) {
        this.modelParentId = modelParentId;
    }

    /**
     * 获取模块父id
     */
    public Long getModelParentId() {
        return this.modelParentId;
    }

    /**
     * 设置创建人
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建人
     */
    public Long getCreateBy() {
        return this.createBy;
    }

    /**
     * 设置
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取
     */
    public Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 设置
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取
     */
    public Long getUpdateBy() {
        return this.updateBy;
    }

    /**
     * 设置
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取
     */
    public Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * 设置是否已删除：0：未删除，1:已删除
     */
    public void setModelDel(Integer modelDel) {
        this.modelDel = modelDel;
    }

    /**
     * 获取是否已删除：0：未删除，1:已删除
     */
    public Integer getModelDel() {
        return this.modelDel;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public List<ModelEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ModelEntity> children) {
        this.children = children;
    }

    public Boolean get_isChecked() {
        return _isChecked;
    }

    public void set_isChecked(Boolean _isChecked) {
        this._isChecked = _isChecked;
    }
}
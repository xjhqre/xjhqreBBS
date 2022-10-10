package com.xjhqre.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.DictDataMapper;
import com.xjhqre.admin.service.DictDataService;
import com.xjhqre.common.domain.entity.DictData;
import com.xjhqre.common.utils.DictUtils;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class DictDataServiceImpl implements DictDataService {
    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典数据
     * 
     * @param dictData
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<DictData> listDictData(DictData dictData, Integer pageNum, Integer pageSize) {
        return this.dictDataMapper.listDictData(new Page<>(pageNum, pageSize), dictData);
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     * 
     * @param dictType
     *            字典类型
     * @param dictValue
     *            字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return this.dictDataMapper.selectDictLabel(dictType, dictValue);
    }

    /**
     * 根据字典数据ID查询信息
     * 
     * @param dictCode
     *            字典数据ID
     * @return 字典数据
     */
    @Override
    public DictData selectDictDataById(Long dictCode) {
        return this.dictDataMapper.selectDictDataById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     * 
     * @param dictCodes
     *            需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            // 根据 dictCode 删除对应字典数据
            DictData data = this.selectDictDataById(dictCode);
            this.dictDataMapper.deleteDictDataById(dictCode);
            // 每次删除字典数据后查询对应的字典类型，更新缓存
            List<DictData> dictDatas = this.dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
    }

    /**
     * 新增保存字典数据信息
     * 
     * @param data
     *            字典数据信息
     * @return 结果
     */
    @Override
    public int insertDictData(DictData data) {
        int row = this.dictDataMapper.insertDictData(data);
        if (row > 0) {
            List<DictData> dictDatas = this.dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 修改保存字典数据信息
     * 
     * @param data
     *            字典数据信息
     * @return 结果
     */
    @Override
    public int updateDictData(DictData data) {
        int row = this.dictDataMapper.updateDictData(data);
        if (row > 0) {
            List<DictData> dictDatas = this.dictDataMapper.selectDictDataByType(data.getDictType());
            DictUtils.setDictCache(data.getDictType(), dictDatas);
        }
        return row;
    }
}

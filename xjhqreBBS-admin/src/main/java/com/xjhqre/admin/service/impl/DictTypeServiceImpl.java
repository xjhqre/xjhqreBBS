package com.xjhqre.admin.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.DictData;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.xjhqre.admin.service.DictTypeService;
import com.xjhqre.common.domain.entity.DictType;

/**
 * 字典 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        this.loadingDictCache();
    }

    /**
     * 根据条件分页查询字典类型
     * 
     * @param dictType
     *            字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<DictType> selectDictTypeList(DictType dictType) {
        return this.dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * 根据所有字典类型
     * 
     * @return 字典类型集合信息
     */
    @Override
    public List<DictType> selectDictTypeAll() {
        return this.dictTypeMapper.selectDictTypeAll();
    }

    /**
     * 根据字典类型查询字典数据
     * 
     * @param dictType
     *            字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        List<DictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        dictDatas = this.dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas)) {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId
     *            字典类型ID
     * @return 字典类型
     */
    @Override
    public DictType selectDictTypeById(Long dictId) {
        return this.dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * 根据字典类型查询信息
     * 
     * @param dictType
     *            字典类型
     * @return 字典类型
     */
    @Override
    public DictType selectDictTypeByType(String dictType) {
        return this.dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * 批量删除字典类型信息
     * 
     * @param dictIds
     *            需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            DictType dictType = this.selectDictTypeById(dictId);
            if (this.dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            this.dictTypeMapper.deleteDictTypeById(dictId);
            DictUtils.removeDictCache(dictType.getDictType());
        }
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        DictData dictData = new DictData();
        dictData.setStatus("0");
        Map<String, List<DictData>> dictDataMap = this.dictDataMapper.selectDictDataList(dictData).stream()
            .collect(Collectors.groupingBy(DictData::getDictType));
        for (Map.Entry<String, List<DictData>> entry : dictDataMap.entrySet()) {
            DictUtils.setDictCache(entry.getKey(), entry.getValue().stream()
                .sorted(Comparator.comparing(DictData::getDictSort)).collect(Collectors.toList()));
        }
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        DictUtils.clearDictCache();
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        this.clearDictCache();
        this.loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     * 
     * @param dict
     *            字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(DictType dict) {
        int row = this.dictTypeMapper.insertDictType(dict);
        if (row > 0) {
            DictUtils.setDictCache(dict.getDictType(), null);
        }
        return row;
    }

    /**
     * 修改保存字典类型信息
     * 
     * @param dict
     *            字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(DictType dict) {
        DictType oldDict = this.dictTypeMapper.selectDictTypeById(dict.getDictId());
        this.dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
        int row = this.dictTypeMapper.updateDictType(dict);
        if (row > 0) {
            List<DictData> dictDatas = this.dictDataMapper.selectDictDataByType(dict.getDictType());
            DictUtils.setDictCache(dict.getDictType(), dictDatas);
        }
        return row;
    }

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dict
     *            字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(DictType dict) {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        DictType dictType = this.dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}

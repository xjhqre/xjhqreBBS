package com.xjhqre.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.DictType;

/**
 * 字典表 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface DictTypeMapper {

    /**
     * 分页查询字典类型
     * 
     * @param objectPage
     * @param dictType
     * @return
     */
    IPage<DictType> listDictType(@Param("objectPage") Page<DictType> objectPage, @Param("dictType") DictType dictType);

    /**
     * 根据所有字典类型
     * 
     * @return 字典类型集合信息
     */
    public List<DictType> selectDictTypeAll();

    /**
     * 根据字典类型ID查询信息
     * 
     * @param dictId
     *            字典类型ID
     * @return 字典类型
     */
    public DictType selectDictTypeById(Long dictId);

    /**
     * 根据字典类型查询信息
     * 
     * @param dictType
     *            字典类型
     * @return 字典类型
     */
    public DictType selectDictTypeByType(String dictType);

    /**
     * 通过字典ID删除字典信息
     * 
     * @param dictId
     *            字典ID
     * @return 结果
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * 批量删除字典类型信息
     * 
     * @param dictIds
     *            需要删除的字典ID
     * @return 结果
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 新增字典类型信息
     * 
     * @param dictType
     *            字典类型信息
     * @return 结果
     */
    public int insertDictType(DictType dictType);

    /**
     * 修改字典类型信息
     * 
     * @param dictType
     *            字典类型信息
     * @return 结果
     */
    public int updateDictType(DictType dictType);

    /**
     * 校验字典类型称是否唯一
     * 
     * @param dictType
     *            字典类型
     * @return 结果
     */
    DictType checkDictTypeUnique(String dictType);

}

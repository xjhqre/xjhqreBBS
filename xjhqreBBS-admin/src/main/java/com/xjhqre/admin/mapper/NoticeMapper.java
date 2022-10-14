package com.xjhqre.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.common.domain.admin.Notice;

/**
 * 通知公告表 数据层
 * 
 * @author xjhqre
 */
@Mapper
public interface NoticeMapper {
    /**
     * 查询公告信息
     * 
     * @param noticeId
     *            公告ID
     * @return 公告信息
     */
    Notice selectNoticeById(Long noticeId);

    /**
     * 根据条件分页查询公告信息
     * 
     * @param noticePage
     * @param notice
     * @return
     */
    IPage<Notice> findNotice(@Param("noticePage") Page<Notice> noticePage, @Param("notice") Notice notice);

    /**
     * 查询公告列表
     * 
     * @param notice
     *            公告信息
     * @return 公告集合
     */
    List<Notice> selectNoticeList(Notice notice);

    /**
     * 新增公告
     * 
     * @param notice
     *            公告信息
     * @return 结果
     */
    int insertNotice(Notice notice);

    /**
     * 修改公告
     * 
     * @param notice
     *            公告信息
     * @return 结果
     */
    int updateNotice(Notice notice);

    /**
     * 批量删除公告
     * 
     * @param noticeId
     *            公告ID
     * @return 结果
     */
    int deleteNoticeById(Long noticeId);

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds
     *            需要删除的公告ID
     * @return 结果
     */
    int deleteNoticeByIds(Long[] noticeIds);

}

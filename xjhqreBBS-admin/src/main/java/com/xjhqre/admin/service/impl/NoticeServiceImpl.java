package com.xjhqre.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjhqre.admin.mapper.NoticeMapper;
import com.xjhqre.admin.service.NoticeService;
import com.xjhqre.common.domain.admin.Notice;

/**
 * 公告 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 查询公告信息
     * 
     * @param noticeId
     *            公告ID
     * @return 公告信息
     */
    @Override
    public Notice selectNoticeById(Long noticeId) {
        return this.noticeMapper.selectNoticeById(noticeId);
    }

    @Override
    public IPage<Notice> findNotice(Notice notice, Integer pageNum, Integer pageSize) {

        return this.noticeMapper.findNotice(new Page<Notice>(pageNum, pageSize), notice);
    }

    /**
     * 查询公告列表
     * 
     * @param notice
     *            公告信息
     * @return 公告集合
     */
    @Override
    public List<Notice> selectNoticeList(Notice notice) {
        return this.noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     * 
     * @param notice
     *            公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(Notice notice) {
        return this.noticeMapper.insertNotice(notice);
    }

    /**
     * 修改公告
     * 
     * @param notice
     *            公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(Notice notice) {
        return this.noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     * 
     * @param noticeId
     *            公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId) {
        return this.noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     * 
     * @param noticeIds
     *            需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return this.noticeMapper.deleteNoticeByIds(noticeIds);
    }
}

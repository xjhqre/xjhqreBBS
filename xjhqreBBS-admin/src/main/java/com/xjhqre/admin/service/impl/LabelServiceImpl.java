// package com.xjhqre.admin.service.impl;
//
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.stereotype.Service;
//
// import com.quark.common.base.BaseServiceImpl;
// import com.quark.common.dao.LabelDao;
// import com.quark.common.entity.Label;
// import com.xjhqre.admin.service.LabelService;
//
/// **
// * @Author LHR Create By 2017/9/3
// */
// @Service
// public class LabelServiceImpl extends BaseServiceImpl<LabelDao, Label> implements LabelService {
//
// @Override
// public Page<Label> findByPage(int pageNo, int length) {
// PageRequest pageRequest = new PageRequest(pageNo, length);
// Page<Label> page = repository.findAll(pageRequest);
// return page;
// }
// }

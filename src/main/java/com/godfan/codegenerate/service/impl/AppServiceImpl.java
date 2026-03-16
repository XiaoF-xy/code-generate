package com.godfan.codegenerate.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.godfan.codegenerate.model.entity.App;
import com.godfan.codegenerate.mapper.AppMapper;
import com.godfan.codegenerate.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author GodFan
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}

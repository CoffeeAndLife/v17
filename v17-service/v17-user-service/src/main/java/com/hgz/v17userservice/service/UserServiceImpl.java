package com.hgz.v17userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IUserService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.commons.pojo.ResultBean;
import com.hgz.commons.util.CodeUtils;
import com.hgz.entity.TUser;
import com.hgz.mapper.TUserMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author huangguizhao
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService{

    @Autowired
    private TUserMapper userMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResultBean checkUserNameIsExists(String username) {
        return null;
    }

    @Override
    public ResultBean checkPhoneIsExists(String phone) {
        return null;
    }

    @Override
    public ResultBean checkEmailIsExists(String email) {
        return null;
    }

    @Override
    public ResultBean generateCode(String identification) {
        //1.生成验证码
        String code = CodeUtils.generateCode(6);
        //2.往redis保存一个凭证跟验证码的对应关系 key-value
        redisTemplate.opsForValue().set(identification,code,2, TimeUnit.MINUTES);
        //3.发送消息，给手机发送验证码
        //3.1 调通阿里云提供的短信Demo
        //3.2 发送短信这个功能，整个体系很多系统都可能会用上，变成一个公共的服务
        //3.3 发送消息，异步处理发送短信
        Map<String,String> params = new HashMap<>();
        params.put("identification",identification);
        params.put("code",code);
        rabbitTemplate.convertAndSend("sms-exchange","sms.code",params);

        //此处是不需要发送任何邮件，仅做测试使用
        Map<String,String> params2 = new HashMap<>();
        params2.put("to","2678383176@qq.com");
        params2.put("username","马老师");

        rabbitTemplate.convertAndSend("email-exchange","email.birthday",params2);

        return new ResultBean("200","OK");
    }

    @Override
    public ResultBean checkLogin(TUser user) {
        //1.根据用户输入的账号（手机/邮箱）信息，去查询
        TUser currentUser = userMapper.selectByIdentification(user.getUsername());
        //2.根据查询出来的密码信息，进行比较
        if(currentUser != null){
            if(user.getPassword().equals(currentUser.getPassword())){
                return new ResultBean("200",currentUser.getUsername());
            }
        }
        return new ResultBean("404",null);
    }

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }
}

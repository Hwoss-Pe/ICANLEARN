package com.config;
import com.utils.CronUtil;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 开启定时任务持久化存储到数据库
 */
@Configuration
public class QuartzConfig {
    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";
    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(CronUtil.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(30)  //设置时间周期单位秒
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}

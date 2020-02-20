package com.javablog.smsplatform.monitor.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.javablog.smsplatform.monitor.job.MonitorQueueSizeJob;
import com.javablog.smsplatform.monitor.job.MonitorCustomerFeeJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SimpleJobConfig {

    @Resource
    private ZookeeperRegistryCenter regCenter;

    /**
     * 监控队列大小job
     **/
    @Bean
    public SimpleJob monitorQueueSizeJob() {
        return new MonitorQueueSizeJob();
    }

    /**
     * 监控客户费用job
     **/
    @Bean
    public SimpleJob monitorCustomerFeeJob() {
        return new MonitorCustomerFeeJob();
    }

    /**
     * 将自己实现的job加入调度中执行-----监控队列大小job
     *
     * @param monitorQueueSizeJob
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler monitorQueueSizeScheduler(final SimpleJob monitorQueueSizeJob, @Value("${monitorQueueSizeJob.cron}") final String cron,
                                                  @Value("${monitorQueueSizeJob.shardingTotalCount}") final int shardingTotalCount,
                                                  @Value("${monitorQueueSizeJob.shardingItemParameters}") final String shardingItemParameters) {
        return new SpringJobScheduler(monitorQueueSizeJob, regCenter, getLiteJobConfiguration(monitorQueueSizeJob.getClass(), cron,
                shardingTotalCount, shardingItemParameters));
    }

    /**
     * 将自己实现的job加入调度中执行---监控客户费用job
     *
     * @param monitorCustomerFeeJob
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler monitorCustomerFeeScheduler(final SimpleJob monitorCustomerFeeJob, @Value("${monitorFeeJob.cron}") final String cron,
                                                    @Value("${monitorFeeJob.shardingTotalCount}") final int shardingTotalCount,
                                                    @Value("${monitorFeeJob.shardingItemParameters}") final String shardingItemParameters) {
        return new SpringJobScheduler(monitorCustomerFeeJob, regCenter, getLiteJobConfiguration(monitorCustomerFeeJob.getClass(), cron,
                shardingTotalCount, shardingItemParameters));
    }

    /**
     * 作业的配置
     *
     * @param jobClass
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @return
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron,
                                                         final int shardingTotalCount, final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(
                jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(),
                jobClass.getCanonicalName())).overwrite(true).build();
    }
}

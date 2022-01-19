package com.pad.server.base.configuration;

import com.pad.server.base.services.account.AccountService;
import com.pad.server.base.services.account.AccountServiceImpl;
import com.pad.server.base.services.activitylog.ActivityLogService;
import com.pad.server.base.services.activitylog.ActivityLogServiceImpl;
import com.pad.server.base.services.email.EmailService;
import com.pad.server.base.services.email.EmailServiceImpl;
import com.pad.server.base.services.operator.OperatorService;
import com.pad.server.base.services.operator.OperatorServiceImpl;
import com.pad.server.base.services.sms.SmsService;
import com.pad.server.base.services.sms.SmsServiceImpl;
import com.pad.server.base.services.system.SystemService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Profile("test")
@Configuration
public class AppTestConfig {

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

    @Bean ActivityLogService activityLogService() {
        return new ActivityLogServiceImpl();
    }

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl();
    }

    @Bean
    public OperatorService operatorService() {
        return new OperatorServiceImpl();
    }

    @Bean
    public SmsService smsService() {
        return new SmsServiceImpl();
    }

    @Bean
    @Primary
    public SystemService systemService() {
        return Mockito.mock(SystemService.class);
    }

    @Bean
    public HibernateTemplate hibernateTemplate() {
        return new HibernateTemplate(sessionFactory().getObject());
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan("com.pad.server.base.entities");
        sessionFactory.setHibernateProperties(additionalProperties());
        sessionFactory.setDataSource(dataSource());
        return sessionFactory;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("/schema.sql")
                .addScript("/email_config.sql")
                .addScript("/email_templates.sql")
                .addScript("/sms_config.sql")
                .addScript("/sms_templates.sql")
                .addScript("/operators.sql")
                .build();
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
        return hibernateProperties;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}

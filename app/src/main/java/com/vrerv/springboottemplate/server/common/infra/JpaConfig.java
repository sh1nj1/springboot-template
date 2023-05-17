package com.vrerv.springboottemplate.server.common.infra;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionFactoryImpl;

import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JpaConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        Session session = entityManager.unwrap(Session.class);
        Dialect dialect = ((SessionFactoryImpl) session.getSessionFactory()).getJdbcServices().getDialect();
        log.info("dialect={}", dialect);
    }

}

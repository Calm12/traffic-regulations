package com.calm.pdd.core.config.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.calm.pdd.core.model.repository"})
@EntityScan(basePackages = {"com.calm.pdd.core.model.entity"})
public class PersistenceContext {

}

package com.jutongji.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


/**
 * @author preideas
 *JPA通用业务仓库
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface JpaBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>,JpaSpecificationExecutor<T>
{
}

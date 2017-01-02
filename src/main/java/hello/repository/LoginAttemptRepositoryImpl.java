package hello.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.fasterxml.jackson.databind.JsonNode;

import hello.domain.LoginAttempt;

public class LoginAttemptRepositoryImpl extends SimpleJpaRepository<LoginAttempt, Long>  implements LoginAttemptRepositoryCustom, ApplicationContextAware {

    @SuppressWarnings("unused")
    private EntityManager entityManager;

    @SuppressWarnings("unused")
    private ApplicationContext context;

    @SuppressWarnings("unused")
    private final JpaEntityInformation<LoginAttempt, ?> entityInformation;
    
    
    @Autowired
    public LoginAttemptRepositoryImpl(EntityManager entityManager) {
        super(LoginAttempt.class, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(LoginAttempt.class, entityManager);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

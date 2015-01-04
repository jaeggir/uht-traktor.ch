package ch.uhttraktor.website.rest.service.cron;

import lombok.Getter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class TransactionalTask {

    @Inject
    private PlatformTransactionManager transactionManager;

    @Getter
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    public void init() {
        transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
    }

}
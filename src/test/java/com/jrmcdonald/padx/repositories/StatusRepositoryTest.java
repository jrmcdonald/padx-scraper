package com.jrmcdonald.padx.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import com.jrmcdonald.padx.common.BaseTest;
import com.jrmcdonald.padx.model.Status;
import com.jrmcdonald.padx.model.Status.StatusEnum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * StatusRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class StatusRepositoryTest extends BaseTest {

    @Autowired
    private StatusRepository statusRepository;

    @Test
    public void testPersistAndFindLatestStatus() {
        Status status = new Status(StatusEnum.UPDATING, new Date(), 0L);
        statusRepository.save(status);

        Status status2 = new Status(StatusEnum.READY, new Date(), 100L);
        statusRepository.save(status2);

        Optional<Status> foundStatus = statusRepository.findFirstByOrderByTimestampDesc();
        assertThat(foundStatus).isPresent();
        assertThat(foundStatus.get()).isEqualToComparingFieldByField(status2);
    }
    
}
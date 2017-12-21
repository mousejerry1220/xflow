package org.xsnake.cloud.xflow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xsnake.cloud.xflow.core.register.ActivityRegister;
import org.xsnake.cloud.xflow.core.register.ParticipantHandleRegister;
import org.xsnake.cloud.xflow.dao.DaoTemplate;
import org.xsnake.cloud.xflow.dao.DataSourceFactory;
import org.xsnake.cloud.xflow.dao.repository.ProcessInstanceFormRepository;
import org.xsnake.cloud.xflow.service.api.IDefinitionService;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=Application.class)
@SpringApplicationConfiguration(classes=Application.class)
public class AppTest extends TestCase{

	@Autowired
	ActivityRegister activityRegister;
	
	@Autowired
	ParticipantHandleRegister participantHandleRegister;
	
	@Autowired
	DataSourceFactory dataSourceFactory;

	@Autowired
	DaoTemplate daoTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	IDefinitionService definitionService;
	
	@Autowired
	ProcessInstanceFormRepository processInstanceFormRepository;
	
	@Test
	public void test() throws Exception {
		processInstanceFormRepository.save("a", "abc");
	}

}


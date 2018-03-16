package com.process.demo.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.process.demo.model.Ropa;
import com.process.demo.repository.RopaRepository;

@Service("customService")
@Transactional
public class CustomServiceImpl implements CustomService {

	protected static Logger logger = Logger.getLogger(CustomServiceImpl.class);

	@Override
	public Boolean delete(Ropa ropa)  {
		try {
			logger.info("Enable Delete current ropa");
			return true;
			
		} catch (Exception e) {
			logger.error(e);
			return false;
		}	
	}
	
}
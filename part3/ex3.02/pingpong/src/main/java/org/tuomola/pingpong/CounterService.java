package org.tuomola.pingpong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CounterService {
  @Autowired
  JdbcTemplate jdbcTemplate;

  public CounterService(JdbcTemplate jdbcTemplate)
  {
    this.jdbcTemplate = jdbcTemplate;
    jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS counter ( lastvalue INTEGER )");
    Integer count = jdbcTemplate.queryForObject("SELECT COUNT(lastvalue) FROM counter", Integer.class); 
    if(count == 0) 
      jdbcTemplate.execute("INSERT INTO counter VALUES (0)");
  }

  public int getCounter()
  {
    Integer count = jdbcTemplate.queryForObject("SELECT lastvalue FROM counter", Integer.class); 
    return count.intValue(); 
  }

  public void setCounter(int counter)
  {
    jdbcTemplate.update("UPDATE counter SET lastvalue = ?", counter);
  }
}

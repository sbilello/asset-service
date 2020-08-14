package com.challenge.datastax.s3proxy.configuration;

import java.time.Duration;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

public class EmbeddedCassandraInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(EmbeddedCassandraInitializer.class);
  private static boolean initialized = false;

  protected void startServer() throws Exception {
    if (!initialized) {
      EmbeddedCassandraServerHelper.startEmbeddedCassandra(Duration.ofSeconds(20).toMillis());
      initialized = true;
    }
    CQLDataLoader cqlDataLoader = new CQLDataLoader(EmbeddedCassandraServerHelper.getSession());
    ClassPathCQLDataSet cassandraInit =
        new ClassPathCQLDataSet("cassandra_schema.cql", true, false, "s3_proxy");
    cqlDataLoader.load(cassandraInit);
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    try {
      startServer();
    } catch (Exception exception) {
      logger.error("Not possible to start for {}", exception);
    }
  }

  @Override
  public int getOrder() {
    return 0;
  }
}

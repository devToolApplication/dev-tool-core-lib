package vn.devTool.core.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import vn.devTool.core.constants.RedisKey;
import vn.devTool.core.filter.RequestFilter;
import vn.devTool.core.service.KafkaProducerService;
import vn.devTool.core.utils.CacheUtil;
import vn.devTool.core.utils.MapperUtil;

@Service
public class BaseService {
  @Autowired protected RedisKey redisKey;
  @Autowired protected MongoTemplate mongoTemplate;
  @Autowired protected CacheUtil cacheUtil;
  @Autowired protected MapperUtil mapperUtil;
  @Autowired protected KafkaProducerService kafkaProducerService;

  protected Long getRequestUserId() {
    String userId = RequestFilter.getUserId();
    return userId == null ? null : Long.valueOf(userId);
  }
}

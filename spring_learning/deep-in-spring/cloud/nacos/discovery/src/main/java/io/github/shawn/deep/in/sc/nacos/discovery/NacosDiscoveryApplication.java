package io.github.shawn.deep.in.sc.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class NacosDiscoveryApplication {

  @Autowired private DiscoveryClient discoveryClient;

  private static final boolean HOTSPOT_JVM =
      ClassUtils.isPresent("com.sun.management.OperatingSystemMXBean", null);

  @Value("${spring.application.name}")
  private String serviceId;

  @Autowired private NacosDiscoveryProperties nacosDiscoveryProperties;

  @Autowired private NacosServiceManager nacosServiceManager;

  public static void main(String[] args) {
    SpringApplication.run(NacosDiscoveryApplication.class, args);
  }

  @Scheduled(fixedRate = 5000L, initialDelay = 10L)
  public void upload() throws Exception {
    NamingMaintainService namingMaintainService =
        nacosServiceManager.getNamingMaintainService(nacosDiscoveryProperties.getNacosProperties());
    NamingService namingService = nacosServiceManager.getNamingService();
    List<Instance> instances = namingService.getAllInstances(serviceId);
    if (CollectionUtils.isEmpty(instances)) {
      System.out.println("instances not found");
      return;
    }

    instances.forEach(
        instance -> {
          Map<String, String> metadata = instance.getMetadata();
          metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
          metadata.put("cpu-usage", String.valueOf(getCpuUsage()));
          try {
            namingMaintainService.updateInstance(serviceId, instance);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
    System.out.println("upload success");
  }

  private Integer getCpuUsage() {
    if (HOTSPOT_JVM) {
      OperatingSystemMXBean operatingSystemMXBean =
          (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
      Double usage = operatingSystemMXBean.getProcessCpuLoad() * 100 * 100;
      return usage.intValue();
    } else {
      return 0;
    }
  }
}

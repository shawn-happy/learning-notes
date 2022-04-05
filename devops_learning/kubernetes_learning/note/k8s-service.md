# Service
Service主要用于提供网络服务，通过Service的定义，能够为客户端 应用提供稳定的访问地址(域名或IP地址)和负载均衡功能，以及屏蔽 后端Endpoint的变化，是Kubernetes实现微服务的核心资源。本节对 Service的概念、负载均衡机制、多端口号、外部服务、暴露到集群外、 支持的网络协议、服务发现机制、Headless Service、端点分片和服务拓 扑等内容进行详细说明。

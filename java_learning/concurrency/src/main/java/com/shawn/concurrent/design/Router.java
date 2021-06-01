package com.shawn.concurrent.design;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

// 不变模式 copy on write模式
public final class Router {

	final String ip;

	final Integer port;

	final String iface;

	public Router(String ip, Integer port, String iface) {
		this.ip = ip;
		this.port = port;
		this.iface = iface;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || o instanceof Router) {
			return false;
		}
		Router router = (Router) o;
		return Objects.equals(ip, router.ip) &&
			Objects.equals(port, router.port) &&
			Objects.equals(iface, router.iface);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ip, port, iface);
	}

	class RouteTable {

		ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();

		public Set<Router> get(String iface) {
			return rt.get(iface);
		}

		public void remove(Router router) {
			Set<Router> routers = rt.get(router.iface);
			if (routers != null) {
				routers.remove(router);
			}
		}

		public void add(Router router) {
			Set<Router> routers = rt
				.computeIfAbsent(router.iface, r -> new CopyOnWriteArraySet<>());
			routers.add(router);
		}
	}
}



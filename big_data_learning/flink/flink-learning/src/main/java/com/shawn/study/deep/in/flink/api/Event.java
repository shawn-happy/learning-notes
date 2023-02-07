package com.shawn.study.deep.in.flink.api;

import java.util.Objects;

public class Event {

  private String user;
  private String url;
  private Long timestamp;

  public Event() {}

  public Event(String user, String url, Long timestamp) {
    this.user = user;
    this.url = url;
    this.timestamp = timestamp;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(user, event.user)
        && Objects.equals(url, event.url)
        && Objects.equals(timestamp, event.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, url, timestamp);
  }

  @Override
  public String toString() {
    return "Event{"
        + "user='"
        + user
        + '\''
        + ", url='"
        + url
        + '\''
        + ", timestamp="
        + timestamp
        + '}';
  }
}

package com.shawn.study.deep.in.flink.api.source;

import com.shawn.study.deep.in.flink.api.Event;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class CustomSourceFunction implements SourceFunction<Event> {

  private boolean running = true;

  @Override
  public void run(SourceContext<Event> ctx) throws Exception {
    Random random = new Random();
    String[] users = {"Mary", "Shawn", "Jack", "Alice", "Bob", "Cary"};
    String[] urls = {"./home", "./cart", "./fav", "./prod?id=1", "./prod?id=2"};

    while (running) {
      ctx.collect(
          new Event(
              users[random.nextInt(users.length)],
              urls[random.nextInt(urls.length)],
              Calendar.getInstance().getTimeInMillis()));
      TimeUnit.SECONDS.sleep(1);
    }
  }

  @Override
  public void cancel() {
    running = false;
  }
}

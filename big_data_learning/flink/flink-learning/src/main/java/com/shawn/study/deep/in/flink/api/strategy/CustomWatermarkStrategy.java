package com.shawn.study.deep.in.flink.api.strategy;

import java.time.Duration;
import org.apache.flink.api.common.eventtime.BoundedOutOfOrdernessWatermarks;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkGeneratorSupplier;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;

public class CustomWatermarkStrategy implements WatermarkStrategy<String> {

  private final Duration duration;

  public CustomWatermarkStrategy(Duration duration) {
    this.duration = duration;
  }

  @Override
  public WatermarkGenerator<String> createWatermarkGenerator(
      WatermarkGeneratorSupplier.Context context) {
    return new BoundedOutOfOrdernessWatermarks<>(duration);
  }
}

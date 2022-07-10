package com.shawn.study.deep.in.java.concurrency.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessExecutor {

  private static final long waitForTimeInSecond = Long.getLong("process.executor.wait.for", 1);
  private final String command;
  private final String arguments;
  private final Runtime runtime = Runtime.getRuntime();
  private boolean finished;

  public ProcessExecutor(String processName, String... arguments) {
    StringBuilder argumentsBuilder = new StringBuilder();
    if (arguments != null) {
      for (String arg : arguments) {
        argumentsBuilder.append(" ").append(arg);
      }
    }
    this.arguments = argumentsBuilder.toString();
    this.command = processName + this.arguments;
  }

  public void execute(OutputStream outputStream) throws IOException {
    try {
      execute(outputStream, Long.MAX_VALUE);
    } catch (TimeoutException ignored) {
    }
  }

  public void execute(OutputStream outputStream, long timeoutInMilliSeconds)
      throws IOException, TimeoutException {
    Process process = runtime.exec(command);
    long startTime = System.currentTimeMillis();
    long endTime = -1L;
    InputStream inputStream = process.getInputStream();
    InputStream errorStream = process.getErrorStream();
    int exitValue = -1;
    while (!finished) {
      long costTime = endTime - startTime;
      if (costTime > timeoutInMilliSeconds) {
        finished = true;
        process.destroy();
        String message = String.format("Execution is timeout[%d ms]!", timeoutInMilliSeconds);
        throw new TimeoutException(message);
      }
      try {
        while (inputStream.available() > 0) {
          outputStream.write(inputStream.read());
        }
        while (errorStream.available() > 0) {
          outputStream.write(errorStream.read());
        }
        exitValue = process.exitValue();
        if (exitValue != 0) {
          throw new IOException();
        }
        finished = true;
      } catch (IllegalThreadStateException e) {
        waitFor(waitForTimeInSecond);
        endTime = System.currentTimeMillis();
      }
    }
  }

  private void waitFor(long seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException e) {
      Thread.interrupted();
    }
  }

  public boolean isFinished() {
    return finished;
  }

  public static void main(String[] args) throws Exception {
    ProcessExecutor processExecutor = new ProcessExecutor("java", "-version");
    processExecutor.execute(System.out);
    System.out.println(processExecutor.isFinished());
  }
}

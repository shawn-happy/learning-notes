package com.shawn.study.big.data.hadoop.hdfs;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class HDFSClientTests {

  private static final String HDFS_PATH = "hdfs://172.27.67.41:9000";
  private static final String HDFS_USER = "work";
  private static final String BASE_PATH = "/hdfs-demo/simple-test";

  private FileSystem fileSystem;

  @Before
  public void setup() throws Exception {
    Configuration configuration = new Configuration();
    // 这里我启动的是单节点的 Hadoop,所以副本系数设置为 1,默认值为 3
    configuration.set("dfs.replication", "1");
    fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
  }

  @After
  public void destroy() throws Exception {
    fileSystem.close();
  }

  @Test
  public void mkdir() throws Exception {
    fileSystem.mkdirs(new Path(BASE_PATH));
  }

  @Test
  public void create() throws Exception {
    // 如果文件存在，默认会覆盖, 可以通过第二个参数进行控制。第三个参数可以控制使用缓冲区的大小
    Path path = new Path("/test" + "/hello.txt");
    FSDataOutputStream out = fileSystem.create(path, true, 4096);
    out.write("hello hadoop!\n".getBytes());
    out.write("hello spark!\n".getBytes());
    out.write("hello flink!\n".getBytes());
    // 强制将缓冲区中内容刷出
    out.flush();
    out.close();

    boolean exists = fileSystem.exists(path);
    assertTrue(exists);
  }

  @Test
  public void preview() throws Exception {
    FSDataInputStream inputStream = fileSystem.open(new Path(BASE_PATH + "/hello.txt"));
    String context = inputStreamToString(inputStream);
    log.info(context);
  }

  @Test
  public void rename() throws Exception {
    Path oldPath = new Path(BASE_PATH + "/hello.txt");
    Path newPath = new Path(BASE_PATH + "/hello_hadoop.txt");
    boolean result = fileSystem.rename(oldPath, newPath);
    assertTrue(result);
  }

  @Test
  public void delete() throws Exception {
    /*
     *  第二个参数代表是否递归删除
     *    +  如果 path 是一个目录且递归删除为 true, 则删除该目录及其中所有文件;
     *    +  如果 path 是一个目录但递归删除为 false,则会则抛出异常。
     */
    boolean result = fileSystem.delete(new Path(BASE_PATH), true);
    assertTrue(result);
  }

  @Test
  public void copyFromLocalFile() throws Exception {
    // 如果指定的是目录，则会把目录及其中的文件都复制到指定目录下
    Path src = new Path("/Users/4paradigm/Desktop/ddl_parser.md");
    Path dst = new Path(BASE_PATH);
    fileSystem.copyFromLocalFile(src, dst);
  }

  @Test
  public void copyFromLocalBigFile() throws Exception {

    File file = new File("/Users/4paradigm/Desktop/Hadoop权威指南_.pdf");
    final float fileSize = file.length();
    InputStream in = new BufferedInputStream(new FileInputStream(file));

    FSDataOutputStream out =
        fileSystem.create(
            new Path(BASE_PATH + "/Hadoop_Guide.pdf"),
            new Progressable() {
              long fileCount = 0;

              public void progress() {
                fileCount++;
                // progress 方法每上传大约 64KB 的数据后就会被调用一次
                System.out.println("上传进度：" + (fileCount * 64 * 1024 / fileSize) * 100 + " %");
              }
            });

    IOUtils.copyBytes(in, out, 4096);
  }

  @Test
  public void copyToLocalFile() throws Exception {
    Path src = new Path(BASE_PATH + "Hadoop_Guide.pdf");
    Path dst = new Path("/Users/4paradigm/Desktop/");
    /*
     * 第一个参数控制下载完成后是否删除源文件,默认是 true,即删除;
     * 最后一个参数表示是否将 RawLocalFileSystem 用作本地文件系统;
     * RawLocalFileSystem 默认为 false,通常情况下可以不设置,
     * 但如果你在执行时候抛出 NullPointerException 异常,则代表你的文件系统与程序可能存在不兼容的情况 (window 下常见),
     * 此时可以将 RawLocalFileSystem 设置为 true
     */
    fileSystem.copyToLocalFile(false, src, dst, true);
  }

  @Test
  public void listFiles() throws Exception {
    FileStatus[] statuses = fileSystem.listStatus(new Path("/hdfs-demo"));
    for (FileStatus fileStatus : statuses) {
      // fileStatus 的 toString 方法被重写过，直接打印可以看到所有信息
      System.out.println(fileStatus.toString());
    }
  }

  @Test
  public void listFilesRecursive() throws Exception {
    RemoteIterator<LocatedFileStatus> files = fileSystem.listFiles(new Path("/hdfs-demo"), true);
    while (files.hasNext()) {
      System.out.println(files.next());
    }
  }

  @Test
  public void getFileBlockLocations() throws Exception {
    FileStatus fileStatus = fileSystem.getFileStatus(new Path(BASE_PATH + "Hadoop_Guide.pdf"));
    BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    for (BlockLocation block : blocks) {
      System.out.println(block);
    }
  }

  /**
   * 把输入流转换为指定编码的字符
   *
   * @param inputStream 输入流
   */
  private static String inputStreamToString(InputStream inputStream) {
    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      StringBuilder builder = new StringBuilder();
      String str = "";
      while ((str = reader.readLine()) != null) {
        builder.append(str).append("\n");
      }
      return builder.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

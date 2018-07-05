package com.qtec.homestay.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/12/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LogcatUtil {
  private static final long FILE_SIZE = 6000 * 1000;

  private static boolean running = false;

  private static int fileIndex = 0;

  public static void start(Context context, String dirName, String fileName) {
    String dirPath;
    if (Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
      dirPath = Environment.getExternalStorageDirectory()
          .getAbsolutePath() + File.separator + dirName;

    } else {// 如果SD卡不存在，就保存到本应用的目录下
      dirPath = context.getFilesDir().getAbsolutePath()

          + File.separator + dirName;
    }

    File dir = new File(dirPath);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = createNewFile(fileName, dir);
    if (file == null) return;
    running = true;
    new WriteLogThread(file).start();
  }

  @Nullable
  private static File createNewFile(String fileName, File dir) {
    File file = new File(dir, fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        Log.d("writelog", "read logcat process failed. message: " + e.getMessage());
//        e.printStackTrace();
        return null;
      }
    }
    return file;
  }

  public static void stop() {
    running = false;
  }

  private static class WriteLogThread extends Thread {
    private File file;
    private String fileName;

    public WriteLogThread(File file) {
      this.file = file;
      this.fileName = file.getName();
    }

    @Override
    public void run() {
      FileOutputStream os = null;
      try {
        java.lang.Process p = Runtime.getRuntime().exec("logcat -d");
        final InputStream is = p.getInputStream();
        os = new FileOutputStream(file);
        int len = 0;
        byte[] buf = new byte[256];
        while (-1 != (len = is.read(buf))) {
          os.write(buf, 0, len);
          os.flush();
          if (!running) break;
          if (file.length() >= FILE_SIZE) {
            os.close();
            os = null;
            fileIndex ++ ;
            File parentFile = file.getParentFile();
            file = null;
            file = createNewFile(fileIndex + fileName, parentFile);
            if (file == null) return;
            os = new FileOutputStream(file);
            Thread.sleep(2000);
          }
        }
      } catch (Exception e) {
        Log.d("writelog", "read logcat process failed. message: " + e.getMessage());
      } finally {
        if (null != os) {
          try {
            os.close();
            os = null;
          } catch (IOException e) {
            // Do nothing
          }
        }
      }
    }
  }
}

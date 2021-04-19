package com.news.partybuilding.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.news.partybuilding.base.BaseApplication;
import com.news.partybuilding.config.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {

  /**
   * 压缩图片
   */
  public static String compressPicture(String filePath, int quality) {
    String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
    Bitmap bitmap = resizeImageFromPath(filePath); // 获取一定尺寸的图片
    int degree = readPictureDegree(filePath); // 获取相片拍摄角度
    if (degree != 0) {
      // 旋转照片角度，防止头像横着显示
      bitmap = rotateBitmap(bitmap, degree);
    }
    File outputFile = new File(targetPath);
    try {
      if (!outputFile.exists()) {
        // TODO 创建文件夹可能会失败，需要做处理
        outputFile.getParentFile().mkdirs();
        //outputFile.createNewFile();
      } else {
        // TODO 删除文件可能会失败，需要做处理
        outputFile.delete();
      }
      FileOutputStream out = new FileOutputStream(outputFile);
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return outputFile.getPath();
  }

  /**
   * 根据路径读取图片信息并按比例裁剪为480x800的大小
   * TODO 为什么要缩放为480x800
   */
  private static Bitmap resizeImageFromPath(String filePath) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
    BitmapFactory.decodeFile(filePath, options);
    // 计算缩放比
    options.inSampleSize = calculateInSampleSize(options, 800, 800);
    // 完整解析图片返回bitmap
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(filePath, options);
  }

  /**
   * 计算图片缩放为480x800的缩放比
   */
  private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;
    if (height > reqHeight || width > reqWidth) {
      final int heightRatio = Math.round((float) height / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }
    return inSampleSize;
  }

  /**
   * 获取照片角度
   */
  private static int readPictureDegree(String path) {
    int degree = 0;
    try {
      ExifInterface exifInterface = new ExifInterface(path);
      int orientation = exifInterface.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return degree;
  }

  /**
   * 旋转照片
   */
  private static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
    if (bitmap != null) {
      Matrix matrix = new Matrix();
      matrix.postRotate(degree);
      bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
        bitmap.getHeight(), matrix, true);
      return bitmap;
    }
    return bitmap;
  }

  /**
   * 将base64图片数据转化为bitmap
   */
  public static Bitmap base64ToBitmap(String imgBase64) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 8;
    byte[] decode = Base64.decode(imgBase64, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(decode, 0, decode.length);
  }

  public static Bitmap stringToBitmap(String string) {
    Bitmap bitmap = null;
    try {
      byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
      bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  /**
   * 将图片Url转换成bitmap
   */
  public static Bitmap urlToBitMap(final String url) {

    URL imageurl = null;

    try {
      imageurl = new URL(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    Bitmap bitmap = null;
    try {
      HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
      conn.setDoInput(true);
      conn.connect();
      InputStream is = conn.getInputStream();
      bitmap = BitmapFactory.decodeStream(is);
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return bitmap;
  }

  /**
   * 将图片保存到相册并发送广播通知图片库已更新
   *
   */
  public static void saveImage(Bitmap bitmap) {
    // 效仿支付宝把保存的图片放到~/DCIM/Alipay中
    File imageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
      + "DCIM" + File.separator + Constants.APP_NAME);
    boolean mkdirSuccess = true;
    if (!imageDir.exists()) {
      mkdirSuccess = imageDir.mkdir();
    }
    if (!mkdirSuccess) {
      Log.e("== saveImage", "mkdir FAILED!");
      return;
    }
    String fileName = System.currentTimeMillis() + ".jpg";
    File file = new File(imageDir, fileName);
    Log.i("saveImageFilePath = ", file.getAbsolutePath());
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      // 通过IO流的方式来压缩保存图片
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();

      // 保存图片后发送广播通知图片库已更新
      Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      intent.setData(Uri.fromFile(file));
      BaseApplication.getContext().sendBroadcast(intent);
      UiOperation.toastCenter("图片保存成功");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // 这个地方 拷贝了一下方法 因为showTradingActivity中开启了子线程调用这个方法
  // 子线程调调用Toast会报Can't create handler inside thread that has not calledLooper.prepare()错误
  // 需要加上Looper.prepare(); Looper.loop();这两句话
  // 因为toast需要在activity的主线程才能正常工作
  // 但是别的页面保存图片时不是在子线程中调用的 加上Looper.prepare(); Looper.loop();会报错java.lang.RuntimeException: Only one Looper may be created per thread
  // 除非不使用toast消息提示 可以满足
  public static void saveImage_onNewThread(Bitmap bitmap) {
    // 效仿支付宝把保存的图片放到~/DCIM/Alipay中
    File imageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
      + "DCIM" + File.separator + Constants.APP_NAME);
    boolean mkdirSuccess = true;
    if (!imageDir.exists()) {
      mkdirSuccess = imageDir.mkdir();
    }
    if (!mkdirSuccess) {
      Log.e("== saveImage", "mkdir FAILED!");
      return;
    }
    String fileName = System.currentTimeMillis() + ".jpg";
    File file = new File(imageDir, fileName);
    Log.i("saveImageFilePath = ", file.getAbsolutePath());
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      // 通过IO流的方式来压缩保存图片
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();

      // 保存图片后发送广播通知图片库已更新
      Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      intent.setData(Uri.fromFile(file));
      BaseApplication.getContext().sendBroadcast(intent);
      // 子线程调调用Toast会报Can't create handler inside thread that has not calledLooper.prepare()错误
      // 需要加上Looper.prepare(); Looper.loop();这两句话
      //  因为toast需要在activity的主线程才能正常工作
      Looper.prepare();
      UiOperation.toastCenter("图片保存成功");
      Looper.loop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 该方法是扫描身份证时保存图片的独有方法，因为扫描身份证返回的是字节流 需要转成bitmap 保存到手机里
   * 然后通过接口传到后台
   */
  public static String saveIdCardImage(Bitmap bitmap) {
    // 效仿支付宝把保存的图片放到~/DCIM/Alipay中
    File imageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
      + "DCIM" + File.separator + Constants.APP_NAME);
    boolean mkdirSuccess = true;
    if (!imageDir.exists()) {
      mkdirSuccess = imageDir.mkdir();
    }
    if (!mkdirSuccess) {
      Log.e("== saveImage", "mkdir FAILED!");
      return "";
    }
    String fileName = System.currentTimeMillis() + ".jpg";
    File file = new File(imageDir, fileName);
    Log.i("saveImageFilePath = ", file.getAbsolutePath());
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      // 通过IO流的方式来压缩保存图片
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();

      // 保存图片后发送广播通知图片库已更新
      Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      intent.setData(Uri.fromFile(file));
      BaseApplication.getContext().sendBroadcast(intent);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return imageDir + "/"+ fileName;
  }

  /**
   * 删除扫描身份证留下的照片
   * @param path
   */
  public static void deletePic(String path , Activity activity){
    if(!TextUtils.isEmpty(path)){
      Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
      ContentResolver contentResolver = activity.getContentResolver();//cutPic.this是一个上下文
      String url =  MediaStore.Images.Media.DATA + "='" + path + "'";
      //删除图片
      contentResolver.delete(uri, url, null);
    }
  }

}

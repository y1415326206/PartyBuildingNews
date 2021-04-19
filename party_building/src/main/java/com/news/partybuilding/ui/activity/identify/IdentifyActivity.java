package com.news.partybuilding.ui.activity.identify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.megvii.faceidiol.sdk.manager.IDCardDetectListener;
import com.megvii.faceidiol.sdk.manager.IDCardManager;
import com.megvii.faceidiol.sdk.manager.IDCardResult;
import com.megvii.faceidiol.sdk.manager.UserDetectConfig;
import com.megvii.meglive_sdk.listener.DetectCallback;
import com.megvii.meglive_sdk.listener.PreCallback;
import com.megvii.meglive_sdk.manager.MegLiveManager;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivityIdentifyBinding;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.PersonCenterResponse;
import com.news.partybuilding.utils.ImageUtils;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.utils.faceUtils.GenerateSign;
import com.news.partybuilding.utils.faceUtils.HttpRequestCallBack;
import com.news.partybuilding.utils.faceUtils.HttpRequestManager;
import com.news.partybuilding.viewmodel.IdentifyViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.os.Build.VERSION_CODES.M;

public class IdentifyActivity extends BaseActivity<ActivityIdentifyBinding, IdentifyViewModel> implements View.OnClickListener, IDCardDetectListener, DetectCallback, PreCallback {

  private final String TAG = "IdentifyActivity==";

  // 身份证扫描使用变量
  public static byte[] faceImg = null;  // 身份证正面
  public static byte[] portraitImg = null; // 身份证头像
  public static byte[] emblemImg = null;  // 身份证背面
  private UserDetectConfig config;
  Map<String, String> idCardFilesPath = new HashMap<>();
  private String idCardName;
  private String idCardNumber;
  private String idCardValidDate;
  private String idCardIssueBy;
  private String idCardAddress;
  private String idCardRace;
  private String idCardBirthday;
  private String idCardGender;
  // 用来判断是否扫描过身份证
  private Boolean isAlreadyScanIdCard = false;
  private String identityState;

  // 人脸识别使用变量
  private int mIDCardSide;
  // 人脸识别 使用变量
  // 人脸识别的类型 张张嘴 眨眨眼 摇摇头之类的 不使用底图对比 使用的是办理身份证时的留存照对比
  private static final int ACTION_YY = 1;
  private static final int ACTION_WY = 2;
  private static final int FMP_YY = 3;
  private static final int FMP_WY = 4;
  private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
  private static final int EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE = 101;
  private ProgressDialog mProgressDialog;
  private MegLiveManager megLiveManager;
  private static final String GET_BIZTOKEN_URL = "https://api.megvii.com/faceid/v3/sdk/get_biz_token";
  private static final String VERIFY_URL = "https://api.megvii.com/faceid/v3/sdk/verify";
  private String sign = "";
  private static final String SIGN_VERSION = "hmac_sha1";
  private byte[] imageRef;//底库图片
  private int buttonType;
  private static final String LANGUAGE = "zh";//en
  //  用来存放人脸识别 最佳人脸照片路径
  Map<String, String> bestImageHashMap = new HashMap<>();


  @Override
  protected int getLayoutResId() {
    return R.layout.activity_identify;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(IdentifyViewModel.class);
    binding.setData(viewModel);
  }

  @Override
  protected void init() {
    viewModel.requestPersonCenter(SharePreferenceUtil.getString(Constants.PUBLIC_KEY,""));
    observeViewModel();
    setStatusBar();
    initListener();
    // 初始化扫描 设置
    config = new UserDetectConfig();
  }

  // 观察viewModel中的数据
  private void observeViewModel(){
    viewModel.personalCenterResponse.observe(this, new Observer<PersonCenterResponse>() {
      @Override
      public void onChanged(PersonCenterResponse personCenterResponse) {
        if (personCenterResponse != null){
          viewModel.isIdentifySuccess.postValue(personCenterResponse.getUserCenter().getIdentityState().equals("authentication_successful"));
          viewModel.userCenter.postValue(personCenterResponse.getUserCenter());
        }
      }
    });
  }


  private void initListener() {
    binding.goBack.setOnClickListener(this);
    binding.identify.setOnClickListener(this);
  }

  /**
   * 设置状态栏
   */
  private void setStatusBar() {
    UiUtils.setStatusBar(this);
  }


  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.go_back:
        finish();
        break;
      case R.id.identify:
        requestCameraPerm();
        break;
    }
  }

  //  请求相机权限
  private void requestCameraPerm() {
    // 如果没有相机权限 就申请
    if (checkSelfPermission(Manifest.permission.CAMERA)
      != PackageManager.PERMISSION_GRANTED) {
      //进行权限请求
      requestPermissions(
        new String[]{Manifest.permission.CAMERA},
        100);
    }

    // 如果有相机权限 就去申请存储权限
    if (checkSelfPermission(Manifest.permission.CAMERA)
      == PackageManager.PERMISSION_GRANTED){
      requestWriteExternalPerm();
    }

  }

  // 请求存储权限
  private void requestWriteExternalPerm(){
    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      //进行权限请求
      requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
        EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE);
    }

    // 如果有存储权限就 进行扫描身份证
    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
      //进行扫描身份证
      beginDetect();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
      if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        //拒绝了权限申请
      } else {
        requestWriteExternalPerm();
      }
    }
    else if (requestCode == EXTERNAL_STORAGE_REQ_WRITE_EXTERNAL_STORAGE_CODE) {
      if (grantResults.length < 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
        //拒绝了权限申请
      } else {
        beginDetect();
        //beginDetectScanFace(ACTION_YY);
      }
    }
  }

  // 开始扫描
  private void beginDetect() {
    new Thread(() -> {
      String apiKey = Constants.API_KEY;
      String secret = Constants.API_SECRET;
      long currtTime = System.currentTimeMillis() / 1000;
      long expireTime = System.currentTimeMillis() / 1000 + 60 * 60 * 24;
      String sign = GenerateSign.appSign(apiKey, secret, currtTime, expireTime);
      // 设置屏幕方向为 垂直
      config.setScreenDirection(0);
      // 设置扫描身份证正反面
      config.setCaptureImage(0);
      IDCardManager.getInstance().init(IdentifyActivity.this, sign, "hmac_sha1", config, new IDCardManager.InitCallBack() {
        @Override
        public void initSuccess(String bizToken) {
          IDCardManager.getInstance().setIdCardDetectListener(IdentifyActivity.this);
          IDCardManager.getInstance().startDetect(IdentifyActivity.this, bizToken, "");
        }

        @Override
        public void initFailed(int resultCode, String resultMessage) {

        }
      });
    }).start();
  }

  // 扫描身份证的回调
  @Override
  public void onIdCardDetectFinish(IDCardResult result) {
    Log.i("result===", result.toString());
    faceImg = null;
    portraitImg = null;
    emblemImg = null;
    if (result.getResultCode() == 1002) {
      switch (result.getResultMessage()) {
        case "SUCCESS_ALMOST_FRONTSIDE":
          ToastUtils.show("人像面识别存在质量问题");
          break;
        case "SUCCESS_ALMOST_BACKSIDE":
          ToastUtils.show("国徽面识别存在质量问题");
          break;
        case "SUCCESS_ALMOST_BOTHSIDE":
        case "SUCCESS_ALMOST_BACKSIDE_SINGLE":
        case "SUCCESS_ALMOST_FRONTSIDE_SINGLE":
          ToastUtils.show("识别内容存在质量问题");
          break;
      }
    }
    if (result.getResultCode() == 2001) {
      switch (result.getResultMessage()) {
        case "NO_IDCARD_FRONTSIDE":
          ToastUtils.show("未检测到人像面");
          break;
        case "NO_IDCARD_BACKSIDE":
          ToastUtils.show("未检测到国徽面");
          break;
        case "NO_IDCARD_BOTHSIDE":
        case "NO_IDCARD_BACKSIDE_SINGLE":
        case "NO_IDCARD_FRONTSIDE_SINGLE":
          ToastUtils.show("识别内容存在质量问题");
          break;
      }
    }
    if (result.getResultCode() == 1001) {
      // 姓名 身份证号码
      idCardName = result.getIdCardInfo().getName().getText();
      idCardNumber = result.getIdCardInfo().getIdcardNumber().getText();
      // 身份证有效期
      String idCardDateBegin = result.getIdCardInfo().getValidDateStart().getText();
      String idCardDateEnd = result.getIdCardInfo().getValidDateEnd().getText();
      idCardValidDate = idCardDateBegin + "-" + idCardDateEnd;
      // 签发机关
      idCardIssueBy = result.getIdCardInfo().getIssuedBy().getText();
      // 住址
      idCardAddress = result.getIdCardInfo().getAddress().getText();
      // 民族
      idCardRace = result.getIdCardInfo().getNationality().getText();
      // 生日
      String idCardBirthdayDay = result.getIdCardInfo().getBirthDay().getText();
      String idCardBirthdayYear = result.getIdCardInfo().getBirthYear().getText();
      String idCardBirthdayMonth = result.getIdCardInfo().getBirthMonth().getText();
      idCardBirthday = idCardBirthdayYear + "." + idCardBirthdayMonth + "." + idCardBirthdayDay;
      // 性别
      idCardGender = result.getIdCardInfo().getGender().getText();
      // 身份证正面
      faceImg = result.getIdCardInfo().getImageFrontside();
      byte[] faceImgData = faceImg;
      if (faceImgData != null) {
        Bitmap faceBitmap = BitmapFactory.decodeByteArray(faceImgData, 0, faceImgData.length);
        String faceImagePath = ImageUtils.saveIdCardImage(faceBitmap);
        idCardFilesPath.put("front", faceImagePath);
      }
      // 身份证背面
      emblemImg = result.getIdCardInfo().getImageBackside();
      byte[] emblemImgData = emblemImg;
      if (emblemImgData != null) {
        Bitmap emblemBitmap = BitmapFactory.decodeByteArray(emblemImgData, 0, emblemImgData.length);
        String emblemImagePath = ImageUtils.saveIdCardImage(emblemBitmap);
        idCardFilesPath.put("back", emblemImagePath);
      }
      // 身份证头像
//      portraitImg = result.getIdCardInfo().getImagePortrait();
//      byte[] portraitImgData = portraitImg;
//      if (portraitImgData != null) {
//        Bitmap portraitBitmap = BitmapFactory.decodeByteArray(portraitImgData, 0, portraitImgData.length);
//        String portraitImagePath = ImageUtils.saveIdCardImage(portraitBitmap);
//        idCardFilesPath.put("man",portraitImagePath);
//      }
      uploadIdCardInformation();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    faceImg = null;
    portraitImg = null;
    emblemImg = null;
  }

  // 上传身份证信息
  private void uploadIdCardInformation() {
    new Http(Urls.UPLOAD_ID_CARD_INFORMATION, new HashMap<String, String>() {{
      put("real_name", idCardName);
      put("number", idCardNumber);
      put("gender", idCardGender);
      put("address", idCardAddress);
      put("issued_by", idCardIssueBy);
      put("race", idCardRace);
      put("birthday", idCardBirthday);
      put("valid_date", idCardValidDate);
    }}).postFiles(idCardFilesPath, new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          int code = jsonObject.getInt("code");
          String message = jsonObject.getString("message");
          runOnUiThread(() -> {
            if (code == Constants.SUCCESS_CODE) {
              Log.i(TAG, "onResponse: " + "上传成功");
              // 一旦上传成功 就删掉留在用户手机中的图片
              for (Map.Entry<String, String> entry : idCardFilesPath.entrySet()) {
                ImageUtils.deletePic(entry.getValue(), IdentifyActivity.this);
              }
              // 扫描人脸
              initScanFace();
            } else {
              finish();
            }
            ToastUtils.show(message);
          });
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void OnFailure(String exception) {

      }
    });
  }


  // ----------------------------------
  //  下面是人脸识别使用的方法
  private void initScanFace() {
    mIDCardSide = getIntent().getIntExtra("side", -1);
    megLiveManager = MegLiveManager.getInstance();
    /**
     * 如果build.gradle中的applicationId 与 manifest中package不一致，必须将manifest中package通过
     * 下面方法传入，如果一致可以不调用此方法
     */
    //megLiveManager.setManifestPack(this,"com.cadae");

    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setCancelable(false);

    long currtTime = System.currentTimeMillis() / 1000;
    long expireTime = (System.currentTimeMillis() + 60 * 60 * 100) / 1000;
    sign = GenerateSign.appSign(Constants.API_KEY, Constants.API_SECRET, currtTime, expireTime);

    //请求相机权限
    //requestCameraPermScanFace();
    beginDetectScanFace(ACTION_YY);
  }

  private void beginDetectScanFace(int type) {
    if (type == ACTION_YY) {
      getBizToken("meglive", 1, idCardName, idCardNumber, "", null);
    } else if (type == ACTION_WY) {
      getBizToken("meglive", 0, "", "", UUID.randomUUID().toString(), imageRef);
    } else if (type == FMP_YY) {
      getBizToken("still", 1, "XXX", "xxxxxxxxxxxxxxxx", "", null);
    } else if (type == FMP_WY) {
      getBizToken("still", 0, "", "", UUID.randomUUID().toString(), imageRef);
    }
  }

  private void getBizToken(String livenessType, int comparisonType, String idcardName, String idcardNum, String uuid, byte[] image) {
    mProgressDialog.show();
    HttpRequestManager.getInstance().getBizToken(this, GET_BIZTOKEN_URL, sign, SIGN_VERSION, livenessType, comparisonType, idcardName, idcardNum, uuid, image, new HttpRequestCallBack() {

      @Override
      public void onSuccess(String responseBody) {
        Log.i("3333333", "第一步");
        try {
          JSONObject json = new JSONObject(responseBody);
          String bizToken = json.optString("biz_token");
          Log.i("=========", "onSuccess: " + bizToken);
          megLiveManager.preDetect(IdentifyActivity.this, bizToken, "zh", "https://api.megvii.com", IdentifyActivity.this);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, byte[] responseBody) {
        Log.i("3333333", "第2步");
        Log.i("onFailure", "statusCode=" + statusCode + ",responseBody" + new String(responseBody));
      }
    });
  }

  @Override
  public void onDetectFinish(String token, int errorCode, String errorMessage, String data) {
    Log.i("3333333", "第3步");
    Log.i("========", "errorCode" + errorCode);
    Log.i("========", "errorMessage" + errorMessage);
    if (errorCode == 6000) {
      switch (errorMessage) {
        case "LIVENESS_FAILURE":
          ToastUtils.show("未检测到人脸");
          break;
        case "USER_CANCEL":
          ToastUtils.show("您已取消");
          break;
        case "NO_CAMERA_PERMISSION":
          ToastUtils.show("没有打开相机的权限，请开启权限后重试");
          break;
        case "DEVICE_NOT_SUPPORT":
          ToastUtils.show("无法启动相机，请确认摄像头功能完好");
          break;
        case "FACE_INIT_FAIL":
          ToastUtils.show("无法启动人脸识别，请稍后重试");
          break;
      }
    }
    if (errorCode == 9000) {
      ToastUtils.show("操作超时");
    }
    if (errorCode == 1000) {
      Log.i("========", "errorCode" + errorCode);
      verify(token, data.getBytes());
    }
  }

  @Override
  public void onPreStart() {
    Log.i("3333333", "第4步");
    showDialogDismiss();
  }

  @Override
  public void onPreFinish(String token, int errorCode, String errorMessage) {
    progressDialogDismiss();
    if (errorCode == 1000) {
      megLiveManager.setVerticalDetectionType(MegLiveManager.DETECT_VERITICAL_FRONT);
      megLiveManager.startDetect(this);
    }
  }

  private void verify(String token, byte[] data) {
    showDialogDismiss();
    HttpRequestManager.getInstance().verify(this, VERIFY_URL, sign, SIGN_VERSION, token, data, new HttpRequestCallBack() {
      @Override
      public void onSuccess(String responseBody) {
        try {
          JSONObject jsonObject = new JSONObject(responseBody);
          Log.i("===result", jsonObject.toString(4));
          JSONObject id_card = jsonObject.getJSONObject("verification").getJSONObject("idcard");
          // 可信度
          String confidence = id_card.getString("confidence");
          JSONObject thresholds = id_card.getJSONObject("thresholds");
          // 十万分之一的可信度
          String thresholdsString = thresholds.getString("1e-5");
          // 最佳人脸照片
          String image_best = jsonObject.getJSONObject("images").getString("image_best");
          Log.i("===result", confidence);
          Log.i("===result", thresholdsString);
          Log.i("===result", image_best);
          // 人脸识别的阀值
          //float contrastValue = 75;
          progressDialogDismiss();
          if (Float.parseFloat(confidence) >= Constants.CONTRAST_VALUE && Float.parseFloat(thresholdsString) >= Constants.THRESHOLDS_1E_5) {
            //ToastUtils.show("认证成功");
            // 拿到最佳人脸照片 将base64转成bitmap 并 存到用户手机里
            Bitmap bitmap = ImageUtils.base64ToBitmap(image_best);
            String bestImagePath = ImageUtils.saveIdCardImage(bitmap);
            bestImageHashMap.put("man", bestImagePath);
            // 请求人脸识别成功后的接口
            requestHttpAfterScanFace();
            //finish();
            Log.i("===result", bestImagePath);
          } else {
            ToastUtils.show("认证失败");
          }
        } catch (Exception e) {
          Log.e("exception", "onSuccess: " + Arrays.toString(e.getStackTrace()));
        }
      }

      @Override
      public void onFailure(int statusCode, byte[] responseBody) {
        Log.i("====result", new String(responseBody));
        progressDialogDismiss();
        ToastUtils.show("认证失败");
      }
    });
  }

  private void progressDialogDismiss() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mProgressDialog != null) {
          mProgressDialog.dismiss();
        }
      }
    });
  }

  private void showDialogDismiss() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mProgressDialog != null) {
          mProgressDialog.show();
        }
      }
    });

  }



  // 请求人脸识别之后的接口
  private void requestHttpAfterScanFace() {
    new Http(Urls.AFTER_SCAN_FACE).postFiles(bestImageHashMap, new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          int code = jsonObject.getInt("code");
          String message = jsonObject.getString("message");
          identityState = jsonObject.getJSONObject("data").getString("identity_state");
          runOnUiThread(() -> {
            if (code == Constants.SUCCESS_CODE) {
              Log.i("ResultActivity", "onResponse: " + "上传成功");
              // 一旦上传成功 就删掉留在用户手机中的图片
              for (Map.Entry<String, String> entry : bestImageHashMap.entrySet()) {
                ImageUtils.deletePic(entry.getValue(), IdentifyActivity.this);
              }
            }
            ToastUtils.show(message);
            finish();
          });
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void OnFailure(String exception) {

      }
    });
  }
}
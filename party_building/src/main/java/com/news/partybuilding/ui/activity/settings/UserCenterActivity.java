package com.news.partybuilding.ui.activity.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseActivity;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.ActivityUserCenterBinding;
import com.news.partybuilding.model.CitiesProvinces;
import com.news.partybuilding.model.CitiesProvincesChildren;
import com.news.partybuilding.model.CityJsonBean;
import com.news.partybuilding.model.Country;
import com.news.partybuilding.network.Http;
import com.news.partybuilding.network.Urls;
import com.news.partybuilding.response.CitiesResponse;
import com.news.partybuilding.response.OrganizationResponse;
import com.news.partybuilding.response.PersonCenterResponse;
import com.news.partybuilding.utils.CacheUtils;
import com.news.partybuilding.utils.GetJsonDataUtil;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.utils.SharePreferenceUtil;
import com.news.partybuilding.utils.UiOperation;
import com.news.partybuilding.utils.UiUtils;
import com.news.partybuilding.viewmodel.UserCenterViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.Url;

public class UserCenterActivity extends BaseActivity<ActivityUserCenterBinding, UserCenterViewModel> implements View.OnClickListener {

  // 省
  private List<CitiesProvinces> provincesOptions = new ArrayList<>();
  // 市
  private ArrayList<ArrayList<String>> citiesOptions = new ArrayList<>();
  // 区
  private ArrayList<ArrayList<ArrayList<String>>> countriesOptions = new ArrayList<>();

  private OptionsPickerView pvOptions;

  private int province_id;
  private int city_id;
  private int country_id;
  private String party_organization_id;

  private String province_name;
  private String city_name;
  private String country_name;
  // 党组织名称
  private List<String> organizationItems = new ArrayList<>();
  // 党组织id
  private List<Integer> organizationIds = new ArrayList<>();

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_user_center;
  }

  @Override
  protected void initAndBindViewModel() {
    viewModel = new ViewModelProvider(this).get(UserCenterViewModel.class);
    binding.setData(viewModel);
  }

  @Override
  protected void init() {
    setStatusBar();
    initListener();
    viewModel.requestPersonCenter(SharePreferenceUtil.getString(Constants.PUBLIC_KEY, ""));
    viewModel.personalCenterResponse.observe(this, new Observer<PersonCenterResponse>() {
      @Override
      public void onChanged(PersonCenterResponse personCenterResponse) {
        if (personCenterResponse != null) {
          viewModel.userCenter.postValue(personCenterResponse.getUserCenter());
          if (personCenterResponse.getUserCenter().getGender().isEmpty()) {
            viewModel.gender.postValue("");
          } else if (personCenterResponse.getUserCenter().getGender().equals(Constants.MAN)) {
            viewModel.gender.postValue("男");
          } else {
            viewModel.gender.postValue("女");
          }
          viewModel.provinceCities.postValue(personCenterResponse.getUserCenter().getProvinceName() + " "
            + personCenterResponse.getUserCenter().getCityName() + " " + personCenterResponse.getUserCenter().getCountyName());
        }
      }
    });

  }

  private void initListener() {
    binding.goBack.setOnClickListener(this);
    binding.save.setOnClickListener(this);
    binding.province.setOnClickListener(this);
    binding.organization.setOnClickListener(this);
  }

  /**
   * 设置状态栏
   */
  private void setStatusBar() {
    UiUtils.setStatusBar(this);
  }


  // 弹出地区选择器
  private void showPickerView() {
    pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

      @Override
      public void onOptionsSelect(int options1, int options2, int options3, View v) {
        //返回的分别是三个级别的选中位置
        String opt1tx = provincesOptions.size() > 0 ?
          provincesOptions.get(options1).getPickerViewText() : "";

        String opt2tx = citiesOptions.size() > 0
          && citiesOptions.get(options1).size() > 0 ?
          citiesOptions.get(options1).get(options2) : "";

        String opt3tx = citiesOptions.size() > 0
          && countriesOptions.get(options1).size() > 0
          && countriesOptions.get(options1).get(options2).size() > 0 ?
          countriesOptions.get(options1).get(options2).get(options3) : "";

        // 选中的省名称
        province_name = opt1tx;
        // 选中的市名称
        city_name = opt2tx;
        // 选中的区名称
        country_name = opt3tx;

        viewModel.provinceCities.postValue(opt1tx + " " + opt2tx + " " + opt3tx);
        province_id = provincesOptions.get(options1).getValue();

        for (int i = 0; i < provincesOptions.size(); i++) {//遍历省份
          if (province_id == provincesOptions.get(i).getValue()) {
            List<CitiesProvincesChildren> cityChildren = new ArrayList<>(provincesOptions.get(i).getCitiesProvincesChildrenList());
            for (int j = 0; j < cityChildren.size(); j++) {
              if (city_name.equals(cityChildren.get(j).getLabel())) {
                List<Country> countries = new ArrayList<>(cityChildren.get(j).getCountryList());
                city_id = cityChildren.get(j).getValue();
                for (int k = 0; k < countries.size(); k++) {
                  if (country_name.equals(countries.get(k).getLabel())) {
                    country_id = countries.get(k).getValue();
                  }
                }
              }
            }
          }
        }
        SharePreferenceUtil.setParam("province_id", province_id);
        SharePreferenceUtil.setParam("city_id", city_id);
        SharePreferenceUtil.setParam("country_id", country_id);
        SharePreferenceUtil.setParam("city_name", city_name);
        SharePreferenceUtil.setParam("country_name", country_name);
      }
    })
      .setSubmitText("保存")
      .setSubmitColor(getColor(R.color.colorMainColor))
      .setDividerColor(Color.BLACK)
      .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
      .setContentTextSize(20)
      .build();
    pvOptions.setPicker(provincesOptions, citiesOptions, countriesOptions);//三级选择器
    pvOptions.show();
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.go_back:
        finish();
        break;
      case R.id.save:
        saveInformation();
        break;
      case R.id.province:
        // 文件是否存在
        boolean isFilePresent = CacheUtils.isFilePresent(getApplicationContext(), "province.json");
        UiOperation.showLoading(this);
        if (isFilePresent) {
          initJsonData();
        } else {
          requestProvincesCitiesCountries();
        }
        break;
      case R.id.organization:
        UiOperation.showLoading(this);
        requestPartyOrganization();
        break;
    }
  }


  // 党组织弹窗
  private void showOrganizationPop() {
    pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
      @Override
      public void onOptionsSelect(int options1, int options2, int options3, View v) {
        party_organization_id = String.valueOf(organizationIds.get(options1));
        binding.partyOrganizationText.setText(organizationItems.get(options1));
        SharePreferenceUtil.setParam("party_organization_id", String.valueOf(party_organization_id));
      }
    })
      .setSubmitText("保存")
      .setSubmitColor(getColor(R.color.colorMainColor))
      .setDividerColor(Color.BLACK)
      .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
      .setContentTextSize(20)
      .build();
    pvOptions.setPicker(organizationItems);
    pvOptions.show();
  }


  // 请求党组织
  private void requestPartyOrganization() {
    new Http(Urls.PARTY_ORGANIZATIONS).get(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        OrganizationResponse organizationResponse = new Gson().fromJson(response, OrganizationResponse.class);
        if (organizationResponse.getCode() == Constants.SUCCESS_CODE) {
          if (organizationItems.size() > 0) {
            organizationItems.clear();
          }
          if (organizationIds.size() > 0) {
            organizationIds.clear();
          }
          for (int i = 0; i < organizationResponse.getOrganizationBeanList().size(); i++) {
            organizationItems.add(organizationResponse.getOrganizationBeanList().get(i).getName());
            organizationIds.add(organizationResponse.getOrganizationBeanList().get(i).getId());
          }
          runOnUiThread(() -> {
            showOrganizationPop();
          });
        } else {
          ToastUtils.show(organizationResponse.getMessage());
        }
        UiOperation.closeLoading();
      }

      @Override
      public void OnFailure(String exception) {

      }
    });
  }


  // 保存个人信息
  private void saveInformation() {
    String public_key = SharePreferenceUtil.getString(Constants.PUBLIC_KEY, "");
    String my_nick_name = binding.myNickName.getText().toString();
    String gender = binding.gender.getText().toString();
    int province_id = SharePreferenceUtil.getInt("province_id", 100000);
    int city_id = SharePreferenceUtil.getInt("city_id", 100000);
    int country_id = SharePreferenceUtil.getInt("country_id", 100000);
    String partyOrganizationId = SharePreferenceUtil.getString("party_organization_id","");
    LogUtils.i("============", province_id + " " + city_id + " " + country_id);

    if (public_key.isEmpty()) {
      ToastUtils.show("请先登录");
      return;
    } else if (my_nick_name.isEmpty()) {
      ToastUtils.show("请输入昵称");
      return;
    } else if (!gender.equals("男") && !gender.equals("女")) {
      ToastUtils.show("请输入男女!");
      return;
    } else if (province_id == 100000 || city_id == 100000 || country_id == 100000) {
      ToastUtils.show("请选择所在地");
      return;
    } else if (partyOrganizationId.isEmpty()) {
      ToastUtils.show("请选择党组织");
      return;
    }

    new Http(Urls.EDIT_USER_INFORMATION, new HashMap<String, String>() {{
      put("public_key", public_key);
      put("nickname", my_nick_name);
      if (gender.equals("男")) {
        put("gender", "man");
      } else {
        put("gender", "woman");
      }
      put("province_id", String.valueOf(province_id));
      put("city_id", String.valueOf(city_id));
      put("county_id", String.valueOf(country_id));
      put("party_organization_id", partyOrganizationId);
    }}).post(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        try {
          JSONObject jsonObject = new JSONObject(response);
          String message = jsonObject.getString("message");
          int code = jsonObject.getInt("code");
          runOnUiThread(() -> {
            if (code == Constants.SUCCESS_CODE) {
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


  // 请求省市区接口
  public void requestProvincesCitiesCountries() {
    new Http(Urls.PROVINCES_CITIES_COUNTRIES).get(new Http.ResponseCallBack() {
      @Override
      public void onResponse(String response) {
        CitiesResponse citiesResponse = new Gson().fromJson(response, CitiesResponse.class);
        if (citiesResponse.getCode() == Constants.SUCCESS_CODE) {
          // 将数据缓存到本地
          CacheUtils.createJsonFile(getApplicationContext(), "province.json", response);
          provincesOptions = citiesResponse.getCitiesProvinces();
          for (int i = 0; i < provincesOptions.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < provincesOptions.get(i).getCitiesProvincesChildrenList().size(); c++) {//遍历该省份的所有城市
              String cityName = provincesOptions.get(i).getCitiesProvincesChildrenList().get(c).getLabel();
              cityList.add(cityName);//添加城市
              ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

              //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
              if (provincesOptions.get(i).getCitiesProvincesChildrenList().get(c).getCountryList() == null
                || provincesOptions.get(i).getCitiesProvincesChildrenList().get(c).getCountryList().size() == 0) {
                city_AreaList.add("");
              } else {
                for (int j = 0; j < provincesOptions.get(i).getCitiesProvincesChildrenList().get(c).getCountryList().size(); j++) {
                  city_AreaList.add(provincesOptions.get(i).getCitiesProvincesChildrenList().get(c).getCountryList().get(j).getLabel());
                }
              }
              province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            //添加城市数据
            citiesOptions.add(cityList);
            //添加地区数据
            countriesOptions.add(province_AreaList);
          }
          runOnUiThread(() -> {
            UiOperation.closeLoading();
            showPickerView();
          });
        }
      }

      @Override
      public void OnFailure(String exception) {

      }
    });
  }


  //解析数据
  private void initJsonData() {
    //获取我们缓存的json文件数据
    String JsonData = CacheUtils.readJson(getApplicationContext(), "province.json");
    List<CitiesProvinces> jsonBean = parseDataToCityProvince(JsonData);//用Gson 转成实体
    /**
     * 添加省份数据
     * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
     * PickerView会通过getPickerViewText方法获取字符串显示出来。
     */
    if (provincesOptions.size() > 0) {
      provincesOptions.clear();
    }
    if (citiesOptions.size() > 0) {
      citiesOptions.clear();
    }
    if (countriesOptions.size() > 0) {
      countriesOptions.clear();
    }
    provincesOptions = jsonBean;

    for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
      ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
      ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

      for (int c = 0; c < jsonBean.get(i).getCitiesProvincesChildrenList().size(); c++) {//遍历该省份的所有城市
        String cityName = jsonBean.get(i).getCitiesProvincesChildrenList().get(c).getLabel();
        cityList.add(cityName);//添加城市
        ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
        if (jsonBean.get(i).getCitiesProvincesChildrenList().get(c).getCountryList() == null
          || jsonBean.get(i).getCitiesProvincesChildrenList().get(c).getCountryList().size() == 0) {
          city_AreaList.add("");
        } else {
          for (int j = 0; j < jsonBean.get(i).getCitiesProvincesChildrenList().get(c).getCountryList().size(); j++) {
            city_AreaList.add(jsonBean.get(i).getCitiesProvincesChildrenList().get(c).getCountryList().get(j).getLabel());
          }
        }
        province_AreaList.add(city_AreaList);//添加该省所有地区数据
      }
      //添加城市数据
      citiesOptions.add(cityList);
      //添加地区数据
      countriesOptions.add(province_AreaList);
    }
    UiOperation.closeLoading();
    showPickerView();
  }

  //Gson解析 CitiesProvinces类
  public List<CitiesProvinces> parseDataToCityProvince(String result) {
    List<CitiesProvinces> detail = new ArrayList<>();
    CitiesResponse response = new Gson().fromJson(result, CitiesResponse.class);
    detail = response.getCitiesProvinces();
    return detail;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (pvOptions != null) {
      pvOptions.dismiss();
    }
  }
}
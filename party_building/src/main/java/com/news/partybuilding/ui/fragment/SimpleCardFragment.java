package com.news.partybuilding.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.drakeet.multitype.ItemViewDelegate;
import com.drakeet.multitype.JavaClassLinker;
import com.drakeet.multitype.MultiTypeAdapter;
import com.news.partybuilding.R;
import com.news.partybuilding.base.BaseFragment;
import com.news.partybuilding.config.Constants;
import com.news.partybuilding.databinding.FragmentSimpleCardBinding;
import com.news.partybuilding.model.Article;
import com.news.partybuilding.model.Banner;
import com.news.partybuilding.model.EmptyValue;
import com.news.partybuilding.model.SecondChildren;
import com.news.partybuilding.model.ThirdChildren;
import com.news.partybuilding.response.HomeResponse;
import com.news.partybuilding.ui.adapter.provider.AdvertisementViewProvider;
import com.news.partybuilding.ui.adapter.provider.BannerViewProvider;
import com.news.partybuilding.ui.adapter.provider.FirstArticleViewProvider;
import com.news.partybuilding.ui.adapter.provider.SecondChildrenViewProvider;
import com.news.partybuilding.ui.adapter.provider.ThirdChildrenMoreProvider;
import com.news.partybuilding.ui.adapter.provider.ThirdChildrenViewProvider;
import com.news.partybuilding.utils.LogUtils;
import com.news.partybuilding.viewmodel.SimpleCardViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SimpleCardFragment extends BaseFragment<FragmentSimpleCardBinding, SimpleCardViewModel> {

  private String categoryId;
  private String cityId;

  private MultiTypeAdapter adapter = new MultiTypeAdapter();
  private ArrayList<Object> items = new ArrayList<>();

  // 默认选中的id
  private ArrayList<Integer> defaultCheckedId = new ArrayList<>();

  private ThirdChildrenViewProvider thirdChildrenViewProvider = new ThirdChildrenViewProvider(getContext());

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_simple_card;
  }

  @Override
  protected void initAndBindViewModel() {
    mViewModel = new ViewModelProvider(this).get(SimpleCardViewModel.class);
    mDataBinding.setViewModel(mViewModel);
  }

  @Override
  protected void init() {
    // 初始化recycleView复杂布局
    initMultiTypeRecycle();
    observeData();
  }


  @Override
  public void onResume() {
    super.onResume();
    categoryId = getArguments().getString("categoryId");
    cityId = getArguments().getString("cityId");
    // 请求首页数据
    mViewModel.requestHomeData(categoryId, cityId);
  }

  /**
   * 初始化recycleView复杂布局
   */
  private void initMultiTypeRecycle() {
    // 注册图片管理器
    adapter.register(Banner.class, new BannerViewProvider(getContext()));
    // 注册文章管理器和广告管理器 这里是一个Class对应多个Provider 根据情况注册不同的Provider
    adapter.register(Article.class).to(new FirstArticleViewProvider(getContext()), new AdvertisementViewProvider(getContext())).withJavaClassLinker(new JavaClassLinker<Article>() {
      @NotNull
      @Override
      public Class<? extends ItemViewDelegate<Article, ?>> index(int i, Article article) {
        if (article.getType().equals(Constants.ARTICLE)) {
          return FirstArticleViewProvider.class;
        } else {
          return AdvertisementViewProvider.class;
        }
      }
    });
    // 注册 二级分类管理器
    adapter.register(SecondChildren.class, new SecondChildrenViewProvider());
    // 注册 三级分类管理器
    adapter.register(ThirdChildren.class, thirdChildrenViewProvider);
    adapter.register(EmptyValue.class, new ThirdChildrenMoreProvider());
//    adapter.register(ThirdChildren.class).to(new ThirdChildrenViewProvider(),new ThirdChildrenMoreProvider()).withJavaClassLinker(new JavaClassLinker<ThirdChildren>() {
//      @NotNull
//      @Override
//      public Class<? extends ItemViewDelegate<ThirdChildren, ?>> index(int i, ThirdChildren thirdChildren) {
//        if (thirdChildren.getArticles().size() > 0){
//          return ThirdChildrenMoreProvider.class;
//        }else {
//          return ThirdChildrenViewProvider.class;
//        }
//      }
//    });

    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        if (items.get(position) instanceof ThirdChildren) {
          return 1;
        } else {
          return 4;
        }
      }
    });

    mDataBinding.recyclerView.setLayoutManager(layoutManager);
    adapter.setItems(items);
    mDataBinding.recyclerView.setAdapter(adapter);
  }

  private void observeData(){
    mViewModel.homeResponse.observe(this, new Observer<HomeResponse>() {
      @Override
      public void onChanged(HomeResponse homeResponse) {
        if (items.size() > 0){
          items.clear();
        }
        // banner 数据
        if (homeResponse.getHomeData().isShowBanner()) {
          items.add(homeResponse.getHomeData().getBanner());
        }
        // articles 数据
        if (homeResponse.getHomeData().getArticles() != null && homeResponse.getHomeData().getArticles().size() > 0) {
          items.addAll(homeResponse.getHomeData().getArticles());
        }
        // 二级分类是否为空
        if (homeResponse.getHomeData().getSecondChildrenList() != null) {
          // 遍历二级分类
          for (int i = 0; i < homeResponse.getHomeData().getSecondChildrenList().size(); i++) {
            // 如果有文章 就把二级分类和文章加入item中去
            if (homeResponse.getHomeData().getSecondChildrenList().get(i).getArticles().size() > 0) {
              items.add(homeResponse.getHomeData().getSecondChildrenList().get(i));
              items.addAll(homeResponse.getHomeData().getSecondChildrenList().get(i).getArticles());
            }
            // 如果没有文章 看一下是否有children
            else {
              // 如果有children 就添加二级分类和三级分类标题到 item中去
              if (homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens() != null &&
                homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens().size() > 0) {
                // 默认选中的 三级分类标题的ID
                int checkedId = homeResponse.getHomeData().getSecondChildrenList().get(i).getCheckedChildrenId();
                LogUtils.i("=======================", checkedId + "==================");
                items.add(homeResponse.getHomeData().getSecondChildrenList().get(i));
                // 往thirdChildrenViewProvider中赋值默认选中的Id
//                defaultCheckedId.add(checkedId);
//                thirdChildrenViewProvider.setCheckedId(defaultCheckedId);
                thirdChildrenViewProvider.setCheckId(checkedId);
                items.addAll(homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens());

                // 遍历ThirdChildren的数据 如果等于 checkedId 就初始化一个 查看更多的类 EmptyValue
                for (int j = 0; j < homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens().size(); j++) {
                  if (homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens().get(j).getId() == checkedId) {
                    // 添加一个 查看更多的布局
                    items.add(new EmptyValue(EmptyValue.TYPE_MORE_TITLE, homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens().get(j).getName()));
                    // 将默认的checkedId的文章添加进item
                    items.addAll(homeResponse.getHomeData().getSecondChildrenList().get(i).getChildrens().get(j).getArticles());
                  }
                }


              }
            }
          }
        }
        adapter.notifyDataSetChanged();
      }
    });
  }

}

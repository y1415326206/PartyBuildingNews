package com.news.partybuilding.bindadatper;


public class BindingAdapterUtil {

//  @BindingAdapter("app:onClickView")
//  public static void onClickView(final View view, final View.OnClickListener listener) {
//    RxView.clicks(view)
//      //两秒钟之内只取一个点击事件，防抖操作
//      .throttleFirst(2, TimeUnit.SECONDS)
//      .subscribe(new Consumer<Unit>() {
//        @Override
//        public void accept(Unit unit) throws Throwable {
//          listener.onClick(view);
//        }
//      }).dispose();
//  }
//
//  @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
//  public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
//    if (!TextUtils.isEmpty(url)) {
//      //使用Glide框架加载图片
//      Glide.with(imageView.getContext())
//        .load(url)
//        .placeholder(placeholderRes)
//        .into(imageView);
//    }
//  }
//
////  @BindingAdapter("imageUrl")
////  public static void setImageUrl(ImageView imageView, String url) {
////    Glide.with(imageView.getContext()).load(url).into(imageView);
////  }

}

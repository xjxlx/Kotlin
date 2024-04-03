package com.xjx.kotlin.ui.activity.feature;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.common.utils.LogUtil;
import com.xjx.kotlin.databinding.ActivityConvertEnumBinding;
import com.xjx.kotlin.utils.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConvertEnumActivity extends BaseBindingTitleActivity<ActivityConvertEnumBinding> {
  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    mBinding.btnClick.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            try {
              EnumUtil.convert(Date.class);
            } catch (Exception e) {
              LogUtil.e("convert:" + e.getMessage());
            }
          }
        });
  }

  @NotNull
  @Override
  public ActivityConvertEnumBinding getBinding(
      @NotNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
    return ActivityConvertEnumBinding.inflate(inflater, container, attachToRoot);
  }

  @NotNull
  @Override
  public String getTitleContent() {
    return "转换Enum测试";
  }
}

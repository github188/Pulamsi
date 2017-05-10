/*
Copyright 2015 shizhefei（LuckyJayce）
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.pulamsi.views.LoadView;


import android.view.View;

import com.pulamsi.R;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.LoadView.vary.IVaryViewHelper;
import com.pulamsi.views.LoadView.vary.VaryViewHelper;

import tr.xip.errorview.ErrorView;

/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 *
 * @author daidingkang
 */
public class LoadViewHelper {

    private IVaryViewHelper helper;

    public LoadViewHelper(View view) {
        this(new VaryViewHelper(view));
    }

    public LoadViewHelper(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }

    public void showError(ErrorView.RetryListener onClickListener) {
        View layout = helper.inflate(R.layout.load_error);
        ErrorView mErrorView = (ErrorView) layout.findViewById(R.id.error_view);
        mErrorView.setOnRetryListener(onClickListener);
        helper.showLayout(layout);
    }

    public BlankLayout showEmpty(String errorText) {
        View layout = helper.inflate(R.layout.load_empty);
        BlankLayout blank_layout = (BlankLayout) layout.findViewById(R.id.blank_layout);
        blank_layout.setBlankLayoutInfo(R.drawable.ic_space_order, errorText);
        helper.showLayout(layout);
        return blank_layout;
    }

    public void showLoading() {
        View layout = helper.inflate(R.layout.load_ing);
        helper.showLayout(layout);
    }

    public void restore() {
        helper.restoreView();
    }
}

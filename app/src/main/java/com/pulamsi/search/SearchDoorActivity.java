package com.pulamsi.search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.search.adapter.HotSearchWordListAdapter;
import com.pulamsi.search.adapter.SearchHistoryListAdapter;
import com.pulamsi.search.entity.HotSearchWordItemDO;
import com.pulamsi.search.entity.SearchHistoryItemDO;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.AutoFitViewGroup;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索入口页
 *
 * @author WilliamChik on 2015/8/10.
 */
public class SearchDoorActivity extends BaseActivity
        implements EditText.OnEditorActionListener, TextWatcher, View.OnClickListener, TRecyclerView.OnItemClickListener {

    /**
     * 搜索历史词列表
     */
    private TRecyclerView mHistoryWordList;

    /**
     * 用户输入词的 edit view
     */
    private EditText mUserInputEt;

    /**
     * 清除输入词的按钮
     */
    private ImageView mClearInputBtn;

    /**
     * 历史记录适配器
     */
    private SearchHistoryListAdapter listAdapter;

    private List<SearchHistoryItemDO> searchHistoryItemDOs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        setContentView(R.layout.search_door_activity);
        initUI();
        initData();
    }

    private void initUI() {
        initHeader();
        mTitleHeaderBar.setOnClickListener(this);
        mHistoryWordList = (TRecyclerView) findViewById(R.id.rv_search_door_history_word_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mHistoryWordList.setOnItemClickListener(this);
        listAdapter = new SearchHistoryListAdapter(this);
        mHistoryWordList.setAdapter(listAdapter);
        mHistoryWordList.setLayoutManager(layoutManager);
        initFooterRootView();
    }

    private void initHeader() {
        mTitleHeaderBar.setCustomizedCenterView(R.layout.search_door_header_bar);
        setRightText(getResources().getString(R.string.search_door_right_title_str));
        mTitleHeaderBar.setLeftContainerWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mTitleHeaderBar.setRightContainerWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mClearInputBtn =
                (ImageView) mTitleHeaderBar.getCenterViewContainer().findViewById(R.id.iv_search_door_header_clear_input_btn);
        mClearInputBtn.setOnClickListener(this);
        mUserInputEt = (EditText) mTitleHeaderBar.getCenterViewContainer().findViewById(R.id.et_search_door_header_input_text);
        mUserInputEt.setOnEditorActionListener(this);
        mUserInputEt.addTextChangedListener(this);
    }

    private void initData() {
        try {
            searchHistoryItemDOs = PulamsiApplication.dbUtils.findAll(Selector.from(SearchHistoryItemDO.class).orderBy("id", true).limit(10));
            if (null != searchHistoryItemDOs) {
                int formerItemCount = listAdapter.getItemCount();
                listAdapter.clearDataList();
                listAdapter.notifyItemRangeRemoved(0, formerItemCount);
                listAdapter.addDataList(searchHistoryItemDOs);
                listAdapter.notifyItemRangeInserted(0, searchHistoryItemDOs.size());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化【热门搜索】布局和【清除历史记录】按钮
     */
    private void initFooterRootView() {

        LayoutInflater mInflater = getLayoutInflater();
        View hotSearchWordRootView = mInflater.inflate(R.layout.search_door_hot_search_word_list_footer, null);
        View clearHistoryBtnView = mInflater.inflate(R.layout.search_door_clear_history_footer, null);
        ImageView searchGarbageImage = (ImageView) clearHistoryBtnView.findViewById(R.id.search_garbage_image);
        TextView searchGarbageText = (TextView) clearHistoryBtnView.findViewById(R.id.search_garbage_text);
        searchGarbageImage.setOnClickListener(this);
        searchGarbageText.setOnClickListener(this);
        AutoFitViewGroup hotSearchWordList =
                (AutoFitViewGroup) hotSearchWordRootView.findViewById(R.id.afvg_search_door_hot_search_word_list);
        HotSearchWordListAdapter hotSearchWordListAdapter = new HotSearchWordListAdapter(this);

        LinearLayout layoutFooter = new LinearLayout(this);
        layoutFooter.setOrientation(LinearLayout.VERTICAL);
        layoutFooter.addView(clearHistoryBtnView);
        layoutFooter.addView(hotSearchWordRootView);
        List<HotSearchWordItemDO> list = new ArrayList<HotSearchWordItemDO>();

        HotSearchWordItemDO temp1 = new HotSearchWordItemDO();
        temp1.setHotWordName("面膜");
        list.add(temp1);
        HotSearchWordItemDO temp2 = new HotSearchWordItemDO();
        temp2.setHotWordName("气垫BB");
        list.add(temp2);
        HotSearchWordItemDO temp3 = new HotSearchWordItemDO();
        temp3.setHotWordName("手工皂");
        list.add(temp3);
        HotSearchWordItemDO temp4 = new HotSearchWordItemDO();
        temp4.setHotWordName("指甲油");
        list.add(temp4);
        HotSearchWordItemDO temp5 = new HotSearchWordItemDO();
        temp5.setHotWordName("手霜");
        list.add(temp5);
        HotSearchWordItemDO temp6 = new HotSearchWordItemDO();
        temp6.setHotWordName("唇膏");
        list.add(temp6);

        hotSearchWordListAdapter.addDataList(list);
        hotSearchWordList.setAdapter(hotSearchWordListAdapter);
        mHistoryWordList.addFooterView(layoutFooter);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String userInputStr = mUserInputEt.getText().toString();
        if (userInputStr.length() > 0) {
            mClearInputBtn.setVisibility(View.VISIBLE);
        } else {
            mClearInputBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onClick(View v) {//clearHistory
        int viewId = v.getId();
        if (viewId == R.id.iv_search_door_header_clear_input_btn) {
            mUserInputEt.setText("");
        } else if (viewId == R.id.ly_header_bar_title_wrap) {//点击搜索
            try {
                if (mUserInputEt.getText() != null && mUserInputEt.getText().toString().trim() != null && !""
                        .equals(mUserInputEt.getText().toString().trim())) {
                    SearchHistoryItemDO historyItemDO = new SearchHistoryItemDO();
                    historyItemDO.setHistoryWordStr(mUserInputEt.getText().toString().trim());
                    PulamsiApplication.dbUtils.save(historyItemDO);
                    initData();
                    Intent search = new Intent(SearchDoorActivity.this, SearchListActivity.class);
                    search.putExtra("keyword", mUserInputEt.getText().toString().trim());
                    SearchDoorActivity.this.startActivity(search);
                } else {
                    ToastUtil.showToast("搜索条件不能为空");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        } else if (viewId == R.id.search_garbage_text || viewId == R.id.search_garbage_image) {
            clearHistory();
        }
    }


    @Override
    public void onItemClick(TRecyclerView parent, View view, int position, long id) {
        Intent search = new Intent(SearchDoorActivity.this, SearchListActivity.class);
        search.putExtra("keyword", listAdapter.getItem(position).getHistoryWordStr());
        SearchDoorActivity.this.startActivity(search);
    }


    private void clearHistory() {
        CommonAlertDialog commonAlertDialog =
                new CommonAlertDialog(SearchDoorActivity.this, "是否清除搜索记录", "确认", "取消", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (listAdapter != null) {
                                listAdapter.clearDataList();
                            }
                            if (searchHistoryItemDOs != null && searchHistoryItemDOs.size() != 0) {
                                searchHistoryItemDOs.clear();
                            }
                            listAdapter.notifyDataSetChanged();
                            PulamsiApplication.dbUtils.deleteAll(SearchHistoryItemDO.class);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });
        commonAlertDialog.show();

    }
}

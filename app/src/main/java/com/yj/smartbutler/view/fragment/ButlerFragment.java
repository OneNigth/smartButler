package com.yj.smartbutler.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.yj.smartbutler.R;
import com.yj.smartbutler.adapter.ChatListAdapter;
import com.yj.smartbutler.api.alibaba.ReportAnswerSDK;
import com.yj.smartbutler.constant.MyConstant;
import com.yj.smartbutler.empty.ChatListData;
import com.yj.smartbutler.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yj on 2018/3/29.
 */

public class ButlerFragment extends BaseFragment {
    //找id的
    private View view;
    //聊天主体
    private ListView mChatLv;
    //发送按钮
    private TextView mSendBt;
    //聊天出入框
    private EditText mChatEt;
    //聊天数据
    private List<ChatListData> mChatDatas = new ArrayList<>();
    //聊天适配器
    private ChatListAdapter mChatAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyConstant.CHAT_SEND_HANDLER:
                    //更新数据
                    mChatAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_butler, container, false);
        initView();
        return view;
    }

    private void initView() {
        mChatEt = view.findViewById(R.id.butler_chat_et);
        mSendBt = view.findViewById(R.id.butler_send_btn);
        mChatLv = view.findViewById(R.id.butler_chat_lv);
        mSendBt.setOnClickListener(this);

        ChatListData data = new ChatListData();
        data.content = "你好";
        data.isMe = false;
        data.type = ChatListAdapter.VALUE_LEFT_TEXT;
        mChatDatas.add(data);

        mChatAdapter = new ChatListAdapter(context, mChatDatas);
        mChatLv.setAdapter(mChatAdapter);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.butler_send_btn://发送按钮
                //发送问题
                sendQuestion();
                //清空输入框
                mChatEt.setText("");
                break;


        }
    }

    //发送问题
    private void sendQuestion() {
        //得到输入框内容
        String question = mChatEt.getText().toString().trim();
        //显示提问问题
        showQuestion(question);

        if (question != "")
            ReportAnswerSDK.ReportAnswerHttpTest(question, new ApiCallback() {
                @Override
                public void onFailure(ApiRequest apiRequest, Exception e) {
                    //请求失败
                    LogUtils.d(e.getMessage().toString());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(ApiRequest apiRequest, ApiResponse apiResponse) {
                    //得到返回json
                    String t = new String(apiResponse.getBody(), SdkConstant.CLOUDAPI_ENCODING);
                    LogUtils.i(t);
                    parsingJson(t);//解析
                    //通知线程更新
                    mHandler.sendEmptyMessage(MyConstant.CHAT_SEND_HANDLER);
                }
            });

    }

    //显示问题
    private void showQuestion(String question) {
        ChatListData data = new ChatListData();
        data.content = question;
        data.isMe = true;
        data.type = ChatListAdapter.VALUE_RIGHT_TEXT;
        //添加近数据
        mChatDatas.add(data);
        //发送更新数据请i求
        mHandler.sendEmptyMessage(MyConstant.CHAT_SEND_HANDLER);
    }

    //解析Json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonresult = jsonObject.getJSONObject("result");
            ChatListData data = new ChatListData();

            String content = jsonresult.getString("content");
            String relquestion = jsonresult.getString("relquestion");

            data.isMe = false;
            data.content = content;
            data.relquestion = relquestion;
            data.type = ChatListAdapter.VALUE_LEFT_TEXT;
            mChatDatas.add(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

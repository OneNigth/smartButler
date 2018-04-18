package com.yj.smartbutler.empty;

import java.util.List;

/**
 * Created by yj on 2018/4/16.
 * 功能 快递请求返回数据
 */

public class CouriserInfo {
    /**
     * 公司
     */
    public String company;
    /**
     * 公司缩写
     */
    public String com;
    /**
     * 快递单号
     */
    public String no;
    /**
     * 状态
     */
    public String status;
    /**
     * 物流信息
     */
    public List<CouriserData> infoList;

    /**
     * 错误码
     */
    public int error_code;


}

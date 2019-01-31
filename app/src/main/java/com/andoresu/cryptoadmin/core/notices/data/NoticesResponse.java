package com.andoresu.cryptoadmin.core.notices.data;

import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.List;

public class NoticesResponse extends BaseListResponse implements Serializable {

    public List<Notice> blogs;

}

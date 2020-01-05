package com.andoresu.cryptoadmin.core.setting.data;

import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.List;

public class SettingsResponse extends BaseListResponse implements Serializable {

    public List<Setting> settings;

}
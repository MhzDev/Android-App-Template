package com.mhzdev.apptemplate.Services.API.Response;


import com.mhzdev.apptemplate.Model.GenericModel;
import com.mhzdev.apptemplate.Services.API.BaseResponse;

import java.util.List;

public class GetDataListResponse extends BaseResponse {

    /**
     * Model Array
     */
    public List<GenericModel> dataList;
}

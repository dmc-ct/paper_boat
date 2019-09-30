package com.paper.boat.zrdx.util.interfaces;


import com.paper.boat.zrdx.bean.rest.FilesUploads;
import com.paper.boat.zrdx.network.Result;

import retrofit2.Response;

public interface Uploading {

    void succeed(Result <FilesUploads> filesUploadsResult);

    void defeated(String err, Response <Result <FilesUploads>> resp);
}

package com.paper.boat.zrdx.network;

import com.paper.boat.zrdx.bean.rest.AppUpdate;
import com.paper.boat.zrdx.bean.rest.FilesUploads;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/*接口定义*/
public interface BlogService {

    /*
    * https://docs.open.alipay.com/?
    * app_id=2019092767868374&
    * source=alipay_app_auth&
    * app_auth_code=50a51c79cd4b4783b8e62b2ead968A40
    * */

    /**
     * app更新
     * @param version_no 版本号
     * @param type       类型
     * @return 是否更新
     */
    @POST("checkAppUpdate")
    Call <Result <AppUpdate>> checkAppUpdate(
            @Query("version_no") String version_no
            , @Query("type") String type);

    /**
     * 文件上传
     */
    @Multipart
    @POST("imageUpload")
    Call <Result <FilesUploads>> uploadFile(
            @Header("Authorization") String string,
            @Part MultipartBody.Part file);

}

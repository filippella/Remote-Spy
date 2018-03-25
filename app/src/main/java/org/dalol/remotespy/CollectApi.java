package org.dalol.remotespy;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 04:04.
 */
public interface CollectApi {

    @FormUrlEncoded
    @POST("/habesha-mingle/emailer/")
    Call<ApiResponse> sendData(@Field("toEmail") String email, @Field("subject") String subject, @Field("content") String content);
}

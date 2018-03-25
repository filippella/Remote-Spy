package org.dalol.remotespy.api;

import org.dalol.remotespy.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 14:27.
 */
public interface RegisterTokenApi {

    @FormUrlEncoded
    @POST("/habesha-mingle/token-handler/")
    Call<ApiResponse> registerToken(@Field("deviceId") String deviceId, @Field("token") String token);
}

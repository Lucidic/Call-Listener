package trevathan.com.calllistener.web;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.http.GET;
import trevathan.com.calllistener.entity.BaseResponse;

/**
 * Created by dnorvell on 7/7/15.
 */
public interface WebApi {

    @GET("/")
    Call<BaseResponse> callStarted();

    @GET("/")
    Call<BaseResponse> callEnded();

}

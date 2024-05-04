package com.busik.busik;

import com.busik.busik.Passanger.ApiResponse.AcceptPassanger;
import com.busik.busik.Passanger.ApiResponse.ApplyFlight;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.Passanger.ApiResponse.CancelMyReq;
import com.busik.busik.Passanger.ApiResponse.CancelPassanger;
import com.busik.busik.Passanger.ApiResponse.ChangePassword;
import com.busik.busik.Passanger.ApiResponse.CreateFlight;
import com.busik.busik.Passanger.ApiResponse.DeletePassanger;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.DriverMessage;
import com.busik.busik.Passanger.ApiResponse.DriverReview;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.ApiResponse.GetDriver;
import com.busik.busik.Passanger.ApiResponse.Login;
import com.busik.busik.Passanger.ApiResponse.Me;
import com.busik.busik.Passanger.ApiResponse.MyApplication;
import com.busik.busik.Passanger.ApiResponse.MyFlight;
import com.busik.busik.Passanger.ApiResponse.PassangerInfo;
import com.busik.busik.Passanger.ApiResponse.PassengerMessage;
import com.busik.busik.Passanger.ApiResponse.Review;
import com.busik.busik.Passanger.ApiResponse.ReviewAboutDriver;
import com.busik.busik.Passanger.ApiResponse.ReviewAboutPassanger;
import com.busik.busik.Passanger.ApiResponse.SendMessage;
import com.busik.busik.Passanger.ApiResponse.UpdateFlight;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiConfig {

    //def



    //пасажир
    @POST("passenger/registration")
    Call<Object> register(@Body RequestBody body);

    @POST("passenger/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @POST("flight/flights_list")
    Call<List<Buses>> getAllBuses(@Header("Authorization") String token);

    @POST("flight/flights_list")
    Call<List<Buses>> getAllBusesFilter(@Header("Authorization") String token,@Body RequestBody body);

    @POST("passenger/login")
    Call<Object> login(@Body RequestBody body);

    @POST("user/gmail_login")
    Call<Object> loginGoogle(@Body RequestBody body);

    @POST("user/facebook_login")
    Call<Object> loginFacebook(@Body RequestBody body);

    @POST("passenger/apply_for_flight")
    Call<ApplyFlight> applyFlight(@Header("Authorization") String token,@Body RequestBody body);

    @POST("passenger/leave_review")
    Call<ReviewAboutDriver> leftReviewAboutDriver(@Header("Authorization") String token, @Body RequestBody body);

    @POST("passenger/my_applications")
    Call<List<MyApplication>> myApplication(@Header("Authorization") String token, @Body RequestBody body);

    @PUT("passenger/update_me")
    Call<Me> updateMe(@Header("Authorization") String token, @Body RequestBody body);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/me")
    Call<Me> getMe(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/my_reviews")
    Call<List<Review>> getMyReviews(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/my_flights?flight__archived=False&status__in=[\"approved\", \"waiting\"]")
    Call<List<MyFlight>> getMyFlights(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("country/get_all_cities/{country_id}")
    Call<List<City>> getAllCities(@Path(value = "country_id", encoded = true) int country_id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("country/get_all")
    Call<List<Country>> getAllCountries();

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/my_flights?flight__archived=True&status__in=[\"approved\", \"waiting\"]")
    Call<List<MyFlight>> getMyFlightsArchived(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/my_messages")
    Call<List<PassengerMessage>> getMyMessages(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("flight/flight_detail_full/{trip_id}")
    Call<FullDetail> getFullDetail(@Header("Authorization") String token, @Path(value = "trip_id", encoded = true) int trip_id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/get_driver/{passanger_id}")
    Call<GetDriver> getDriverInfo(@Header("Authorization") String token, @Path(value = "passanger_id", encoded = true) int passanger_id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @DELETE("passenger/my_messages/{message_id}")
    Call<ResponseBody> deleteMessagePassanger(@Header("Authorization") String token, @Path(value = "message_id", encoded = true) int message_id);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/cancel_my_application/{application_id}")
    Call<CancelMyReq> cancelMyReq(@Header("Authorization") String token, @Path(value = "application_id", encoded = true) int application_id);
    //водій


    @POST("driver/registration")
    Call<Object> registerDriver(@Body RequestBody body);

    @POST("tech_message/for_passengers_in_flight")
    Call<SendMessage> sendMessage(@Header("Authorization") String token,@Body RequestBody body);

    @POST("driver/logout")
    Call<ResponseBody> logoutDriver(@Header("Authorization") String token);

    @POST("driver/login")
    Call<Object> loginDriver(@Body RequestBody body);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/me")
    Call<Me> getMeDriver(@Header("Authorization") String token);

    @PUT("driver/update_me")
    Call<Me> updateMeDriver(@Header("Authorization") String token, @Body RequestBody body);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/my_reviews")
    Call<List<DriverReview>> getMyReviewsDriver(@Header("Authorization") String token);

    @POST("driver/create_flight")
    Call<CreateFlight> createFlight(@Header("Authorization") String token,@Body RequestBody body);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/my_flights?archived=False")
    Call<List<DriverFlight>> getDriverFlights(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/my_flights?archived=True")
    Call<List<DriverFlight>> getDriverFlightsArchived(@Header("Authorization") String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("passenger/get_passenger/{passanger_id}")
    Call<PassangerInfo> getPassangerInfo(@Header("Authorization") String token, @Path(value = "passanger_id", encoded = true) int passanger_id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @DELETE("driver/my_messages/{message_id}")
    Call<ResponseBody> deleteMessageDriver(@Header("Authorization") String token, @Path(value = "message_id", encoded = true) int message_id);

    @POST("driver/delete_passenger_from_flight")
    Call<DeletePassanger> deletePassangerFromFlight(@Header("Authorization") String token, @Body RequestBody body);

    @DELETE("driver/delete_flight/{flight_id}")
    Call<DeletePassanger> deleteFlight(@Header("Authorization") String token, @Path(value = "flight_id", encoded = true) int flight_id);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/my_messages")
    Call<List<DriverMessage>> getDriverMessages(@Header("Authorization") String token);

    @POST("user/change_password")
    Call<ChangePassword> changePassword(@Header("Authorization") String token, @Body RequestBody body);

    @POST("driver/leave_review")
    Call<ReviewAboutPassanger> leftReviweAboutPassanger(@Header("Authorization") String token, @Body RequestBody body);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/accept_passenger_application/{application_id}")
    Call<AcceptPassanger> acceptPassanger(@Header("Authorization") String token, @Path(value = "application_id", encoded = true) int application_id);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("driver/cancel_passenger_application/{application_id}")
    Call<CancelPassanger> cancelPassanger(@Header("Authorization") String token, @Path(value = "application_id", encoded = true) int application_id);

    @PUT("driver/update_flight/{flight_id}")
    Call<UpdateFlight> updateFlight(@Header("Authorization") String token, @Body RequestBody body, @Path(value = "flight_id", encoded = true) int flight_id);

}

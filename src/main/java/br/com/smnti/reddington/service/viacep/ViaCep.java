package br.com.smnti.reddington.service.viacep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCep {

    @GET("ws/{cep}/json/")
    Call<ViaCepResponse> findAddress( @Path("cep") String cep );

}

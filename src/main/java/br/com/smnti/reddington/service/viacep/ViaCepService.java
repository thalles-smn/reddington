package br.com.smnti.reddington.service.viacep;

import br.com.smnti.reddington.service.RetrofitBase;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Optional;

@Component
public class ViaCepService extends RetrofitBase<ViaCep> {

    private final String BASE_URL = "https://viacep.com.br/";

    public Optional<ViaCepResponse> findAddress( String zipCode ){
        Call<ViaCepResponse> call = this.getApi(BASE_URL).findAddress(zipCode);

        try {
            Response<ViaCepResponse> response = this.execute(call, "findAddress");

            if(response.raw() != null && response.raw().code() == 200){
                return Optional.ofNullable(response.body());
            }

            return Optional.empty();

        }
        catch( Exception ex ){
            return Optional.empty();
        }
    }

    @Override
    protected String getServiceName() {
        return "ViaCep";
    }
}

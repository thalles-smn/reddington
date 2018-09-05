package br.com.smnti.reddington.service.viacep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepResponse implements Serializable {
    private static final long serialVersionUID = 7810967766787998735L;

    @SerializedName("cep")
    private String zipCode;

    @SerializedName("logradouro")
    private String street;

    @SerializedName("complemento")
    private String complement;

    @SerializedName("bairro")
    private String district;

    @SerializedName("uf")
    private String state;

    @SerializedName("city")
    private String city;

    @SerializedName("ibge")
    private String ibge;
}

package br.com.smnti.reddington.resource.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexRequest implements Serializable {
    private static final long serialVersionUID = -8703896618961323920L;

    private String name;
    private String address;
    private String phone;
    private String cellphone;
    private Boolean active;

}

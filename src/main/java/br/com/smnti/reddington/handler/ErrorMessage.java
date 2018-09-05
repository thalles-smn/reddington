package br.com.smnti.reddington.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = 873876734202368163L;

    private String host;
    private String message;
}

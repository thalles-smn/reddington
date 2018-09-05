package br.com.smnti.reddington.resource.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {
    private static final long serialVersionUID = -9008937503041223746L;

    private String host;
    private List<?> records;
}

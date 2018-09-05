package br.com.smnti.reddington.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address implements Serializable {
    private static final long serialVersionUID = -4769117077900008836L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    @NotNull
    private String street;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    @NotNull
    @Size(min = 8, max = 8)
    private String zipCode;

    @Column(name = "retentative")
    private Integer retentative;

    @PostLoad
    private void postLoad(){
        this.retentative = this.retentative != null ? this.retentative : 0;
    }


}

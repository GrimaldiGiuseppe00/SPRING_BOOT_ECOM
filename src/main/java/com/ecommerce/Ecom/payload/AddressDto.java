package com.ecommerce.Ecom.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long addressId;
    private String buildingName;
    private String street;
    private String city;
    private String state;
    private String country;
    @NotBlank
    @Size(min= 6 , message = "Pincode must contains atleast 6 characters")
    private String pinCode;

}

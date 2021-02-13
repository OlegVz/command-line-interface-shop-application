package com.hybris.shop.dto;

import com.hybris.shop.annotations.ColumnNameAlias;
import lombok.Data;

@Data
public class NewUserDto {

    @ColumnNameAlias(alias = "Email")
    private String email;

    @ColumnNameAlias(alias = "Password")
    private String password;
}

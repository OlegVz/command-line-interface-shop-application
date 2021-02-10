package com.hybris.shop.dto;

import com.hybris.shop.annotations.ColumnNameAlias;
import lombok.Data;

@Data
public class UserDto {

    @ColumnNameAlias(alias = "User ID")
    private Long id;

    @ColumnNameAlias(alias = "Email")
    private String email;
}

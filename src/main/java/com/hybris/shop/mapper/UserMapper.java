package com.hybris.shop.mapper;

import com.hybris.shop.dto.NewUserDto;
import com.hybris.shop.dto.UserDto;
import com.hybris.shop.model.User;
import org.modelmapper.ModelMapper;

import java.util.Objects;

//@Component
public class UserMapper {

    private final ModelMapper modelMapper;

//    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntityFromNewUserDto(NewUserDto newUserDto) {
        return Objects.isNull(newUserDto) ? null : modelMapper.map(newUserDto, User.class);
    }

    public UserDto toUserDtoFromUser(User user) {
        return Objects.isNull(user) ? null : modelMapper.map(user, UserDto.class);
    }
}

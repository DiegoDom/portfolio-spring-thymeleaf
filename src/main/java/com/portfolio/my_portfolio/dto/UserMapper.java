package com.portfolio.my_portfolio.dto;

import com.portfolio.my_portfolio.model.User;

public class UserMapper {
  public static UserDto toDto(User user) {

    if (user == null) {
      return null;
    }

    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEnabled(user.isEnabled());

    return userDto;
  }

  public static User toUser(UserDto userDto) {
    if (userDto == null) {
      return null;
    }

    User user = new User();
    user.setId(userDto.getId());
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());
    user.setEnabled(userDto.isEnabled());

    return user;
  }
}

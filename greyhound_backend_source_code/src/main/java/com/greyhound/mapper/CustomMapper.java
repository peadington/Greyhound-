package com.greyhound.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.greyhound.dto.AdminRequestDto;
import com.greyhound.dto.LoginResponseDto;
import com.greyhound.dto.UserDetailResponseDto;
import com.greyhound.dto.UserListResponseDto;
import com.greyhound.dto.UserRequestDto;
import com.greyhound.model.User;

/**
 * 
 * @author p4logics
 *
 */
@Mapper(componentModel = "spring")
public interface CustomMapper {

	User userRequestDtoToUser(UserRequestDto userRequestDto);

	UserDetailResponseDto userToUserDetailResponseDto(User user);

	User adminRequestDtoToUser(AdminRequestDto userRequestDto);

	LoginResponseDto userToLoginResponseDto(User userDetails);

	List<UserListResponseDto> userListToUserListResponse(List<User> userList);

}
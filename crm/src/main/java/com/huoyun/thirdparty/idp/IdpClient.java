package com.huoyun.thirdparty.idp;

import com.huoyun.exception.BusinessException;
import com.huoyun.thirdparty.idp.impl.CreateUserParam;
import com.huoyun.thirdparty.idp.impl.CreateUserResponse;
import com.huoyun.thirdparty.idp.impl.DeleteUserParam;

public interface IdpClient {

	void changePassword(Long userId, String oldPassword, String newPassword)
			throws BusinessException;

	CreateUserResponse createUser(CreateUserParam createUserParam)
			throws BusinessException;

	void deleteUser(DeleteUserParam deleteUserParam) throws BusinessException;
}

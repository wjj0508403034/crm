package com.huoyun.thirdparty.idp;

import com.huoyun.exception.BusinessException;

public interface IdpClient {

	void changePassword(Long userId, String oldPassword, String newPassword) throws BusinessException;
}

package com.huoyun.user.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huoyun.user.UserInfo;

public class UserInfoImpl implements UserInfo, Serializable{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserInfoImpl.class);

	private static final long serialVersionUID = -4987943232222173181L;

	public static final byte PRM_FULL = 3;
	public static final byte PRM_READONLY = 1;
	public static final byte PRM_NONE = 0;

	private int tenantId;
	private int landscapeId;
	private long id;
	private Long employeeId;
	private boolean isServiceUser;
	private boolean isKeyUser;
	private boolean isSupportUser;

	private Integer mobileInactivityTimeout;
	private String localCurrency;

	private final Map<String, Integer> systemBoAuthorizations = new HashMap<String, Integer>();

	private Set<Long> attachedRoleIds = new HashSet<Long>();

	// Granted permissions for all permission resources
	private Map<String, Integer> permResourcePermissions = new HashMap<String, Integer>();

	private Map<String, String> permissionItemMoreInfo;

	public String getLocalCurrency() {
		return this.localCurrency;
	}

	public void setLocalCurrency(String localCurrency) {
		this.localCurrency = localCurrency;
	}

	public final Integer getMobileInactivityTimeout() {
		return mobileInactivityTimeout;
	}

	public final void setMobileInactivityTimeout(Integer mobileInactivityTimeout) {
		this.mobileInactivityTimeout = mobileInactivityTimeout;
	}

	/**
	 * 0: no permission 1: read permission 3: read/write permission
	 */
	private byte[] permissions;

	private UserInfo realUserInfo;

	public UserInfoImpl(int tenantId, long id, int landscapeId,
			boolean isServiceUser, boolean isSupportUser, boolean isKeyUser,
			Long employeeId) {
		this(tenantId, id, landscapeId, isServiceUser, isSupportUser,
				isKeyUser, employeeId, true);
	}

	private UserInfoImpl(int tenantId, long id, int landscapeId,
			boolean isServiceUser, boolean isSupportUser, boolean isKeyUser,
			Long employeeId, boolean isPackageUser) {
		this.tenantId = tenantId;
		this.id = id;
		this.landscapeId = landscapeId;
		this.employeeId = employeeId;
		this.isServiceUser = isServiceUser;
		this.isSupportUser = isSupportUser;
		this.isKeyUser = isKeyUser;
		if (isPackageUser) {
			this.realUserInfo = new UserInfoImpl(tenantId, id, landscapeId,
					isServiceUser, isSupportUser, isKeyUser, employeeId, false);
		}
	}

	@Override
	public Long getEmployeeId() {
		return employeeId;
	}

	@Override
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public int getLandscapeId() {
		return landscapeId;
	}

	@Override
	public void setLandscapeId(int landscapeId) {
		this.landscapeId = landscapeId;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int getTenantId() {
		return tenantId;
	}

	@Override
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public boolean isServiceUser() {
		return isServiceUser;
	}

	public void setServiceUser(boolean isServiceUser) {
		this.isServiceUser = isServiceUser;
	}

	/**
	 * @return the isSupportUser
	 */
	@Override
	public boolean isSupportUser() {
		return isSupportUser;
	}

	/**
	 * @param isSupportUser
	 *            the isSupportUser to set
	 */
	public void setSupportUser(boolean isSupportUser) {
		this.isSupportUser = isSupportUser;
	}

	@Override
	public boolean isKeyUser() {
		return isKeyUser;
	}

	public void setKeyUser(boolean isKeyUser) {
		this.isKeyUser = isKeyUser;
	}

	@Override
	public UserInfo getRealCurrentUserInfo() {
		return this.realUserInfo;
	}

	private String systemEmail;

	public String getSystemEmail() {
		return systemEmail;
	}

	public void setSystemEmail(String systemEmail) {
		this.systemEmail = systemEmail;
	}

	public Map<String, Integer> getSystemBoAuthorizations() {
		return systemBoAuthorizations;
	}

	public void reloadRoles(Set<Long> roleIds) {
		attachedRoleIds = roleIds;
	}

	public Set<Long> getAttachedRoleIds() {
		return attachedRoleIds;
	}

	public Map<String, Integer> getPermResourcePermissions() {
		return permResourcePermissions;
	}

	public void setPermResourcePermissions(
			Map<String, Integer> permResourcePermissions) {
		this.permResourcePermissions = permResourcePermissions;
	}

	public Map<String, String> getPermissionItemMoreInfo() {
		return permissionItemMoreInfo;
	}

	public void setPermissionItemMoreInfo(
			Map<String, String> permissionItemMoreInfo) {
		this.permissionItemMoreInfo = permissionItemMoreInfo;
	}



}

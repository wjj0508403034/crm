package com.huoyun.saml2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SAMLUserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);
	

	@Override
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
	
}

package com.huoyun.session;

import com.huoyun.business.employee.Employee;

public interface Session {
    public final static String SBO_SESSION_SESS_ATTR = "SBO_SESSION_SESS_ATTR";

	Employee getEmployee();
}

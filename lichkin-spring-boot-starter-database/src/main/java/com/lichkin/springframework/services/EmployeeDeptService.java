package com.lichkin.springframework.services;

import com.lichkin.framework.defines.entities.I_Dept;

/**
 * 员工取部门服务类定义
 * @author SuZhou LichKin Information Technology Co., Ltd.
 */
public interface EmployeeDeptService {

	/**
	 * 按照员工ID和公司ID取部门信息
	 * @param throwException 是否抛出异常
	 * @param employeeId 员工ID
	 * @param compId 公司ID
	 * @return 部门信息
	 */
	public I_Dept findDeptByLoginIdAndCompId(boolean throwException, String employeeId, String compId);

}

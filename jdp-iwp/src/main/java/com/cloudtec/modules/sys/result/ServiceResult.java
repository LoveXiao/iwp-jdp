package com.cloudtec.modules.sys.result;

import com.cloudtec.common.domain.Result;

public class ServiceResult extends Result{
	
	public ServiceResult(){
		super();
		this.setSuccess(false);
	}
}

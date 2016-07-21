package org.apache.shiro.cas.dmp;

import org.apache.shiro.SecurityUtils;


public class ModuleUtil {
	
	/**
	 * 根据集成的shiro框架获取当前登录用户
	 * @return
	 * @throws Exception
	 */
	public static String getCurrUserId() throws Exception{
		String userId = null;
		try{
			Object principal = SecurityUtils.getSubject().getPrincipal();
			 if (principal != null) {
				 userId = principal.toString(); 
			 }else{
				 userId = null;
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
		return userId;
	}

}

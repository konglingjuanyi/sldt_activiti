package org.apache.shiro.cas.dmp;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;



/**
 * shiro权限处理
 * @author 
 *
 */
public class SsoCasRealm extends CasRealm  {
	
	
	//设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("ssoCasRealm");
	}

	/** 
     * 为当前登录的Subject授予角色和权限 
     * @see  该方法的调用时机为需授权资源被访问时 
     * @see  并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache 
     * @see  个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache 
     * @see  比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache 
     */  
    @Override  
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals){  
    	
    	//从 principals获取主身份信息
		//将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
    	//获取当前用户的用户账号
    			String userId = (String) principals.getPrimaryPrincipal();
    			//根据身份信息获取按钮的权限信息 
    			//从数据库获取到权限数据,这里先固定传sm作为系统标志参数  按钮唯一标识暂定为：上级菜单：按钮编号
    		  
    			List<String> rootMp = new ArrayList<String>();
    			for(int i=0;i<5;i++){
    					rootMp.add("rootMenu:00"+i);
    			}
    			setSession("userId",userId);
    			SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    			simpleAuthorizationInfo.addStringPermissions(rootMp);
    			return simpleAuthorizationInfo;
		 
    }  
   
      
       
    /** 
     * 将一些数据放到ShiroSession中,以便于其它地方使用 
     * @see  比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到 
     */  
    private void setSession(Object key, Object value){  
        Subject currentUser = SecurityUtils.getSubject();  
        if(null != currentUser){  
            Session session = currentUser.getSession();  
            System.out.println("Session默认超时时间为[" + session.getTimeout() + "]毫秒");  
            if(null != session){  
                session.setAttribute(key, value);  
            }  
        }  
    }  
    
    //清除缓存
    //当有权限变更的时候的，亦即是用户权限实时运行表T_SM_USER_PRIVLG_RT有数据更新时可以调用下这个方法，来清除下
    //缓存，这样用户可以不用重新登录权限就可以起作用
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}

}

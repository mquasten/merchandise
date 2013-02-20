package de.mq.merchandise.model;

import javax.validation.constraints.Size;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component("login")
@Scope("view")
public class LoginImpl implements Login {

	
	private static final long serialVersionUID = 1L;

	@Size(min=1,  message="{mandatory_field}")

	private String user;
	
	@Size(min=4, max=8 , message="{wrong_password}")
	private String password;
	
	
	public LoginImpl(){
	}
	
	public LoginImpl(final String user , final String password){
		this.user=user;
		this.password=password;
	}
	
	@Override
	public final String getUser() {
		return user;
	}

	public final void setUser(String user) {
		this.user = user;
	}

	
	@Override
	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		this.password = password;
	} 
	
}

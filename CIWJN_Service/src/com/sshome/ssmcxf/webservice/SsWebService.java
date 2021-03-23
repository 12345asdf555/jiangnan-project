package com.sshome.ssmcxf.webservice;

import java.math.BigInteger;

public interface SsWebService {
	
	public BigInteger FindIns_Id(String insname);
	public BigInteger AddWeld(String aweld);
	public Boolean UpdateWeld(String uweld);
	public BigInteger AddJunction(String ajunction);
	public Boolean UpdateJunction(String ujunction);
	public Boolean DeleteJunction(String djunction);
	
}

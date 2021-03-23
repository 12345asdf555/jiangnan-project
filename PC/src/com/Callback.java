package com;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public interface Callback {

	public void taskResult(String str,String connet,ArrayList<String> listarray1,ArrayList<String> listarray2,ArrayList<String> listarray3,HashMap<String, Socket> websocket,String ip1);
	
}

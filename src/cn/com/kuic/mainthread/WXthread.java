package cn.com.kuic.mainthread;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class WXthread {

	public WXthread(String nickname) {
		// TODO Auto-generated constructor stub
		System.out.println("WXÏß³Ì¿ªÆô¡ª¡ª¡ª¡ª¡ª¡ª¡ª¡ª");
		String url=null;
		if(!nickname.equals("")){
			 url ="http://yttest.applinzi.com/test/qf.php?nickname="+nickname;
		}
		if(url!=null){
			URI uri= URI.create(url);
			Desktop dt= Desktop.getDesktop();
			if(dt.isSupported(Desktop.Action.BROWSE)){ 
				try {
					dt.browse(uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		
	}

}

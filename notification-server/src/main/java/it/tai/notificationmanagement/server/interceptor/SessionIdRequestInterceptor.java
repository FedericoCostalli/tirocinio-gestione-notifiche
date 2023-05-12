package it.tai.notificationmanagement.server.interceptor;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Response;


public class SessionIdRequestInterceptor implements okhttp3.Interceptor {

	private Set<String> cookies = new HashSet<String>();
	
	
	public Response intercept(Chain chain) throws IOException {
		 Response originalResponse = chain.proceed(authenticateRequest(chain.request()));
		// System.out.println("YEAH: " + originalResponse.body().string());
		    if (!originalResponse.headers("Set-Cookie").isEmpty()) {
		       cookies.clear();
		    	for (String header : originalResponse.headers("Set-Cookie")) {
		            cookies.add(header.split(";")[0]);
		        }
		    }
		    return originalResponse;
		  }
	
	private okhttp3.Request authenticateRequest(okhttp3.Request request) {
        okhttp3.Request.Builder builder = request.newBuilder();
        
        if (cookies.size()>0) {
        	String cookieValue = "";
        	for (String cookie : cookies) {
        		if (cookieValue.length()>0) {
        			cookieValue += ";";
        		}
        		cookieValue += cookie;
        	}
        	
        	
            builder.addHeader("Cookie", cookieValue);
        } 
        return builder.build();
    }

}

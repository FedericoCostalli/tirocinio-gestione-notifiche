package it.tai.notificationmanagement.service.notification.whatsapp.support;

import feign.Headers;
import feign.RequestLine;
import it.tai.notificationmanagement.service.notification.whatsapp.support.domain.SendStatus;
import it.tai.notificationmanagement.service.notification.whatsapp.support.domain.TextMessageArgument;

public interface WhatsAppAPI {

//	curl -X 'POST' \
	//  'http://localhost:55002/sendText' \
	 // -H 'accept: */*' \
	  //-H 'Content-Type: application/json' \
	  //-d '{
	  //"args": {
	   // "to": "393358233937@c.us",
	   // "content": "Sto facendo delle prove di invio messaggi con la nuova versione del programma"
	 // }
//	}'
	
//	{
//		  "success": false,
//		  "error": {
//		    "name": "SENDTEXT_FAILURE",
//		    "message": "\nNot a contact. Unlock this feature and support open-wa by getting a license: https://get.openwa.dev/l/393283258054\n"
//		  }
//		}
	
	
	@RequestLine("POST /sendText")
	 @Headers({
	      "Content-Type: application/json",
	      "Accept: */*",
	  })
	SendStatus sendMTextMessage( TextMessageArgument body);
}

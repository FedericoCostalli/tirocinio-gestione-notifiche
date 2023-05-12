package it.tai.notificationmanagement.service;

import it.tai.notificationmanagement.domain.AppointmentCheck;
import it.tai.notificationmanagement.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
@Slf4j
@AllArgsConstructor
public class DynamoMessageHistoryRepository implements MessageHistoryRepository {

    final String  tableName = "appointment_check";
    final String  key = "date";
    final String sort_key = "phone_number";
    final String sent = "sent";

    private final DynamoDbClient ddb;

    @Override
    public boolean hasAlreadyBeenSent(AppointmentCheck appointmentCheck) {
        HashMap<String, AttributeValue> keyToGet = new HashMap<String,AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(appointmentCheck.getDate()).build());
        keyToGet.put(sort_key, AttributeValue.builder()
                .s(appointmentCheck.getPhone_number()).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    log.debug("{}: {} ",key1, returnedItem.get(key1).toString());
                    if(key1.equalsIgnoreCase("sent")){
                        return returnedItem.get(key1).bool();
                    }
                }
            } else {
                log.debug("No item found with the key {}", key);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Override
    public void markAsSent(Notification notification) {
        HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();
        LocalDate from_date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = from_date.format(formatter);

        itemValues.put(key, AttributeValue.builder().s(today).build());
        itemValues.put(sort_key, AttributeValue.builder().s(notification.getTarget()).build());
        itemValues.put(sent, AttributeValue.builder().bool(true).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            ddb.putItem(request);
            log.debug("{} was successfully updated", tableName);

        } catch (ResourceNotFoundException e) {
            log.error("Error: The Amazon DynamoDB table \"{}\" can't be found.", tableName);
        } catch (DynamoDbException e) {
            log.error(e.getMessage());

        }
    }

}

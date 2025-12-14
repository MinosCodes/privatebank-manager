package bank;

import com.google.gson.*;
import java.lang.reflect.Type;

public class TransactionSerDer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CLASSNAME", src.getClass().getSimpleName());
        if (src instanceof Transfer t) {
            obj.addProperty("sender", t.getSender());
            obj.addProperty("recipient", t.getRecipient());
        } else if (src instanceof Payment p) {
            obj.addProperty("incomingInterest", p.getIncomingInterest());
            obj.addProperty("outgoingInterest", p.getOutgoingInterest());
        }

        obj.addProperty("date", src.getDate());
        obj.addProperty("amount", src.getAmount());
        obj.addProperty("description", src.getDescription());



        return obj;
    }

    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        String className = obj.get("CLASSNAME").getAsString();

        String date = obj.get("date").getAsString();
        double amount = obj.get("amount").getAsDouble();
        String description = obj.get("description").getAsString();

        return switch (className) {
            case "IncomingTransfer" -> new IncomingTransfer(
                    date,
                    amount,
                    description,
                    obj.get("sender").getAsString(),
                    obj.get("recipient").getAsString()
            );
            case "OutgoingTransfer" -> new OutgoingTransfer(
                    date,
                    amount,
                    description,
                    obj.get("sender").getAsString(),
                    obj.get("recipient").getAsString()
            );
            case "Transfer" -> new Transfer(
                    date,
                    amount,
                    description,
                    obj.get("sender").getAsString(),
                    obj.get("recipient").getAsString()
            );
            case "Payment" -> new Payment(
                    date,
                    amount,
                    description,
                    obj.get("incomingInterest").getAsDouble(),
                    obj.get("outgoingInterest").getAsDouble()
            );
            default -> throw new JsonParseException("Unknown transaction type: " + className);
        };
    }
}

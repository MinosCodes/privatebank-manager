package bank;

import com.google.gson.*;
import java.lang.reflect.Type;

public class TransactionSerDer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("CLASSNAME", src.getClass().getSimpleName());

        JsonObject instance = new JsonObject();
        if (src instanceof Transfer t) {
            instance.addProperty("sender", t.getSender());
            instance.addProperty("recipient", t.getRecipient());
        } else if (src instanceof Payment p) {
            instance.addProperty("incomingInterest", p.getIncomingInterest());
            instance.addProperty("outgoingInterest", p.getOutgoingInterest());
        }

        instance.addProperty("date", src.getDate());
        instance.addProperty("amount", src.getAmount());
        instance.addProperty("description", src.getDescription());

        obj.add("INSTANCE", instance);

        return obj;
    }

    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        String className = obj.get("CLASSNAME").getAsString();

        JsonObject instance = obj.getAsJsonObject("INSTANCE");

        String date = instance.get("date").getAsString();
        double amount = instance.get("amount").getAsDouble();
        String description = instance.get("description").getAsString();

        return switch (className) {
            case "IncomingTransfer" -> new IncomingTransfer(
                    date,
                    amount,
                    description,
                    instance.get("sender").getAsString(),
                    instance.get("recipient").getAsString()
            );
            case "OutgoingTransfer" -> new OutgoingTransfer(
                    date,
                    amount,
                    description,
                    instance.get("sender").getAsString(),
                    instance.get("recipient").getAsString()
            );
            case "Transfer" -> new Transfer(
                    date,
                    amount,
                    description,
                    instance.get("sender").getAsString(),
                    instance.get("recipient").getAsString()
            );
            case "Payment" -> new Payment(
                    date,
                    amount,
                    description,
                    instance.get("incomingInterest").getAsDouble(),
                    instance.get("outgoingInterest").getAsDouble()
            );
            default -> throw new JsonParseException("Unknown transaction type: " + className);
        };
    }
}

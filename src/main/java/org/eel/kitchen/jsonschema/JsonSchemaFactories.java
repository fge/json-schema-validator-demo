package org.eel.kitchen.jsonschema;

import org.eel.kitchen.jsonschema.main.JsonSchemaFactory;
import org.eel.kitchen.jsonschema.metaschema.BuiltinSchemas;
import org.eel.kitchen.jsonschema.metaschema.MetaSchema;
import org.eel.kitchen.jsonschema.schema.AddressingMode;

public final class JsonSchemaFactories
{
    private static final JsonSchemaFactory DRAFTV3_NO_ID;
    private static final JsonSchemaFactory DRAFTV3_WITH_ID;
    private static final JsonSchemaFactory DRAFTV4_NO_ID;
    private static final JsonSchemaFactory DRAFTV4_WITH_ID;

    static {
        final JsonSchemaFactory.Builder builder
            = new JsonSchemaFactory.Builder();

        final MetaSchema draftv4
            = MetaSchema.copyOf(BuiltinSchemas.DRAFTV4_CORE);

        DRAFTV3_NO_ID = builder.build();

        builder.addressingMode(AddressingMode.INLINE);
        DRAFTV3_WITH_ID = builder.build();


        builder.addMetaSchema(draftv4, true);
        DRAFTV4_WITH_ID = builder.build();

        builder.addressingMode(AddressingMode.CANONICAL);
        DRAFTV4_NO_ID = builder.build();
    }

    private JsonSchemaFactories()
    {
    }

    public static JsonSchemaFactory withOptions(final boolean useDraftV4,
        final boolean useId)
    {
        if (useDraftV4)
            return useId ? DRAFTV4_WITH_ID : DRAFTV4_NO_ID;

        return useId ? DRAFTV3_WITH_ID : DRAFTV3_NO_ID;
    }
}

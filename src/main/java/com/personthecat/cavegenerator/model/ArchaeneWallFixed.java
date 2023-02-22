package com.personthecat.cavegenerator.model;

import com.personthecat.cavegenerator.data.CavernSettings;
import com.personthecat.cavegenerator.util.HjsonMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.hjson.JsonValue;

import java.util.List;

import static com.personthecat.cavegenerator.util.CommonMethods.runExF;

@Builder
@FieldNameConstants
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class ArchaeneWallFixed {

    // I have no idea what I'm doing

    /** The name of this feature to be used globally in serialization. */
    private static final String FEATURE_NAME = CavernSettings.Fields.archaeneWallFixed;

    /** The specific blocks to find. */
    List<IBlockState> blocks;

    public static ArchaeneWallFixed fromValue(JsonValue json) {
        if (json.isObject()) {
            return from(json.asObject());
        } else if (json.isArray()) {
            return from(json.asArray());
        }
        throw runExF("Expected object or array: {}", json);
    }

    public static ArchaeneWallFixed from(JsonObject json) {
        final ArchaeneWallFixed.ArchaeneWallFixedBuilder builder = builder();
        return new HjsonMapper(json)
            .mapRequiredStateList(Fields.blocks, FEATURE_NAME, builder::blocks)
            .release(builder::build);
    }

    public static ArchaeneWallFixed from(JsonArray json) {
        final JsonArray blocks = new JsonArray();
        for (JsonValue value : json) {
            if (value.isString()) {
                blocks.add(value);
            } else {
                throw runExF("Expected string: {}", value);
            }
        }
        return from(new JsonObject().add(Fields.blocks, blocks));
    }
}

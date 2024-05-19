package net.glease.autoime;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

@LateMixin
public class AutoIMELateMixin implements ILateMixinLoader {
    @Override
    public String getMixinConfig() {
        return "mixins.autoime.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (loadedMods.contains("betterquesting")) {
            builder.add("MixinPanelTextField");
        }
        if (loadedMods.contains("BiblioCraft")) {
            builder.add("MixinGuiBiblioTextField");
        }
        return builder.build();
    }
}

package io.github.apace100.origins;

import javax.annotation.Nullable;

import com.google.common.reflect.TypeToken;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Default implementation of IForgeRegistryEntry, this is necessary to reduce redundant code.
 * This also enables the registrie's ability to manage delegates. Which are automatically updated
 * if another entry overrides existing ones in the registry.
 */
@SuppressWarnings("unchecked")
public abstract class OriginsRegistryEntry<V extends OriginsRegistryEntry<V>> implements IForgeRegistryEntry<V>
{
    @SuppressWarnings("serial")
    private final TypeToken<V> token = new TypeToken<V>(getClass()){};
    private ResourceLocation registryName = null;

    public final V setRegistryName(String name)
    {
        if (getRegistryName() != null)
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());

        this.registryName = addPrefixIfUnavailable(name);
        return (V)this;
    }

    //Helper functions
    public final V setRegistryName(ResourceLocation name){ return setRegistryName(name.toString()); }

    public final V setRegistryName(String modID, String name){ return setRegistryName(modID + ":" + name); }

    @Nullable
    public final ResourceLocation getRegistryName()
    {
        return registryName != null ? registryName : null;
    }
    
    private ResourceLocation addPrefixIfUnavailable(String name) {
    	int index = name.indexOf(":");
    	if(index == -1) {
    		return new ResourceLocation(OriginsMod.MODID, name);
    	}
    	return new ResourceLocation(name);
    }

    public final Class<V> getRegistryType() { return (Class<V>)token.getRawType(); }
}
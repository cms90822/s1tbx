package com.bc.ceres.glayer;

import com.bc.ceres.binding.ValidationException;
import com.bc.ceres.binding.ValueContainer;
import com.bc.ceres.binding.ValueDescriptor;
import com.bc.ceres.binding.ValueModel;
import com.bc.ceres.binding.accessors.DefaultValueAccessor;
import com.bc.ceres.core.ExtensibleObject;
import com.bc.ceres.core.ServiceRegistry;
import com.bc.ceres.core.ServiceRegistryFactory;

import java.util.ServiceLoader;

// todo - Layer API: the API of this class is confusing and it is hard to implement subclasses. (nf)

// todo - Layer API: carefully javadoc it (nf)
public abstract class LayerType extends ExtensibleObject {

    private static final ServiceRegistry<LayerType> REGISTRY;

    protected LayerType() {
    }

    public abstract String getName();

    // todo - Layer API: it shall be safe for BEAM layers to cast ctx into a ProductSceneViewContext, otherwise there can be no reasonable implementations of this method beside CRS checkings (nf)
    public abstract boolean isValidFor(LayerContext ctx);

    public abstract Layer createLayer(LayerContext ctx, ValueContainer configuration);

    // todo - Layer API: why not use annotations? (nf)
    // todo - Layer API: check IDEA ALT+F7: is this a utility or framework API? Only framework usage is in createLayer(). How must clients use this? (nf)
    // todo - Layer API: how can clients know whether my value model can be serialized or not? when to impl. a converter? (nf)
    public abstract ValueContainer createLayerConfig(LayerContext ctx);

    public static LayerType getLayerType(String layerTypeClassName) {
        return REGISTRY.getService(layerTypeClassName);
    }

    public static <T extends LayerType>  T getLayerType(Class<T> layerTypeClass) {
        return (T) REGISTRY.getService(layerTypeClass.getName());
    }

    static {
        final ServiceRegistry<LayerType> typeServiceRegistry = ServiceRegistryFactory.getInstance().getServiceRegistry(LayerType.class);
        final ServiceLoader<LayerType> serviceLoader = ServiceLoader.load(LayerType.class);
        for (final LayerType layerType : serviceLoader) {
            typeServiceRegistry.addService(layerType);
        }
        REGISTRY = typeServiceRegistry;
    }


}

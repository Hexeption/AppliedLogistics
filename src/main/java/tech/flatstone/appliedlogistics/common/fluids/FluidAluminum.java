package tech.flatstone.appliedlogistics.common.fluids;

import tech.flatstone.appliedlogistics.common.blocks.fluids.BlockFluidAluminum;

public class FluidAluminum extends FluidBase {
    public static FluidAluminum INSTANCE;

    public FluidAluminum() {
        super("aluminum", BlockFluidAluminum.class, true);
        INSTANCE = this;
        this.getFluid().setLuminosity(15);
        this.getFluid().setViscosity(5000);
    }
}
package tech.flatstone.appliedlogistics.common.fluids;

import tech.flatstone.appliedlogistics.common.blocks.fluids.BlockFluidIron;

public class FluidIron extends FluidBase {
    public static FluidIron INSTANCE;

    public FluidIron() {
        super("iron", BlockFluidIron.class, true);
        INSTANCE = this;
        this.getFluid().setLuminosity(15);
        this.getFluid().setViscosity(5000);
    }
}
package net.csdcodes.model;

import net.csdcodes.model.ls.PrFlowEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FlowType {
    private static List<String> flowType = new ArrayList<String>(
            Arrays.asList(
                    PrFlowEnum.RM_ASSEMBLY_MRO.toString(),
                    PrFlowEnum.EXPENSE.toString(),
                    PrFlowEnum.CAPITALIZED_PROJECT_FIXED_ASSET.toString())
    );


    public static List<String> getFlowType() {
        return flowType;
    }
}

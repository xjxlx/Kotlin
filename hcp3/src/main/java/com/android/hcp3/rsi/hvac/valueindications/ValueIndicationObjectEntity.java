package com.android.hcp3.rsi.hvac.valueindications;

import com.android.hcp3.BaseRSIValue;
import com.android.hcp3.rsi.hvac.VcRestrictionReason;
import com.android.hcp3.rsi.hvac.VcSpecialIndicationValue;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueIndicationObject;
import java.time.OffsetTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ValueIndicationObjectEntity extends BaseRSIValue {
  private final List<VcSpecialIndicationValue> specialStateConfiguration;

  private final Float maxValue;

  private final Float minValue;

  private final Float resolution;

  private final VcValueIndicationObjectUnitPercentEnum unitPercent;

  private final VcValueIndicationObjectUnitDensityEnum unitDensity;

  private final VcRestrictionReason restrictionReason;

  private final Float currentValue;

  private final VcSpecialIndicationValue specialState;

  private final OffsetTime timeValue;

  public ValueIndicationObjectEntity(ValueIndicationObject object) {
    super(object);
    this.specialStateConfiguration =
        object
            .getSpecialStateConfiguration()
            .map(
                list ->
                    list.stream()
                        .map(VcSpecialIndicationValue::fromRSI)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.maxValue = object.getMaxValue().orElse(null);
    this.minValue = object.getMinValue().orElse(null);
    this.resolution = object.getResolution().orElse(null);
    this.unitPercent =
        object.getUnitPercent().map(VcValueIndicationObjectUnitPercentEnum::fromRSI).orElse(null);
    this.unitDensity =
        object.getUnitDensity().map(VcValueIndicationObjectUnitDensityEnum::fromRSI).orElse(null);
    this.restrictionReason =
        object.getRestrictionReason().map(VcRestrictionReason::fromRSI).orElse(null);
    this.currentValue = object.getCurrentValue().orElse(null);
    this.specialState =
        object.getSpecialState().map(VcSpecialIndicationValue::fromRSI).orElse(null);
    this.timeValue = object.getTimeValue().orElse(null);
  }
}

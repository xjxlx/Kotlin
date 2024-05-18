package com.android.hcp3.rsi.hvac.valuecontrols;

import com.android.hcp3.BaseRSIValue;
import com.android.hcp3.rsi.hvac.VcRestrictionReason;
import com.android.hcp3.rsi.hvac.VcSpecialValue;
import com.android.hcp3.rsi.hvac.VcTemperatureUnit;
import de.esolutions.fw.rudi.services.rsiglobal.Duration;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.ValueControlObject;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ValueControlObjectEntity extends BaseRSIValue {
  private final List<VcValueControlObjectWeekdaysEnum> weekdays;

  private final List<VcSpecialValue> specialStateConfiguration;

  private final Float maxValue;

  private final Float minValue;

  private final Float resolution;

  private final VcValueControlObjectUnitPercentEnum unitPercent;

  private final VcTemperatureUnit unitTemperature;

  private final VcRestrictionReason restrictionReason;

  private final Float currentValue;

  private final LocalDate dateValue;

  private final Duration durationValue;

  private final VcSpecialValue specialState;

  private final OffsetTime timeValue;

  public ValueControlObjectEntity(ValueControlObject object) {
    super(object);
    this.weekdays =
        object
            .getWeekdays()
            .map(
                list ->
                    list.stream()
                        .map(VcValueControlObjectWeekdaysEnum::fromRSI)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.specialStateConfiguration =
        object
            .getSpecialStateConfiguration()
            .map(list -> list.stream().map(VcSpecialValue::fromRSI).collect(Collectors.toList()))
            .orElse(null);
    this.maxValue = object.getMaxValue().orElse(null);
    this.minValue = object.getMinValue().orElse(null);
    this.resolution = object.getResolution().orElse(null);
    this.unitPercent =
        object.getUnitPercent().map(VcValueControlObjectUnitPercentEnum::fromRSI).orElse(null);
    this.unitTemperature = object.getUnitTemperature().map(VcTemperatureUnit::fromRSI).orElse(null);
    this.restrictionReason =
        object.getRestrictionReason().map(VcRestrictionReason::fromRSI).orElse(null);
    this.currentValue = object.getCurrentValue().orElse(null);
    this.dateValue = object.getDateValue().orElse(null);
    this.durationValue = object.getDurationValue().orElse(null);
    this.specialState = object.getSpecialState().map(VcSpecialValue::fromRSI).orElse(null);
    this.timeValue = object.getTimeValue().orElse(null);
  }
}

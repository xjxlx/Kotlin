package com.android.hcp3.rsi.hvac.generalsettings;

import com.android.hcp3.BaseRSIValue;
import com.android.hcp3.rsi.hvac.airdistributionpresets.AirDistributionPresetObjectEntity;
import com.android.hcp3.rsi.hvac.flavourcartridges.FlavourCartridgeObjectEntity;
import com.android.hcp3.rsi.hvac.switchcontrols.SwitchControlObjectEntity;
import com.android.hcp3.rsi.hvac.switchindications.SwitchIndicationObjectEntity;
import com.android.hcp3.rsi.hvac.valuecontrols.ValueControlObjectEntity;
import com.android.hcp3.rsi.hvac.valueindications.ValueIndicationObjectEntity;
import com.android.hcp3.rsi.hvac.voicecontroldialogues.VoiceControlDialogueObjectEntity;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.GeneralSettingObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GeneralSettingObjectEntity extends BaseRSIValue {
  private final SwitchControlObjectEntity steeringWheelHeatingIntensityToggleButton;

  private final ValueControlObjectEntity airCirculationSensitivity;

  private final SwitchControlObjectEntity airCirculationSmartSystem;

  private final List<AirDistributionPresetObjectEntity> airDistributionPresetList;

  private final List<FlavourCartridgeObjectEntity> flavourCartridgeAvailability;

  private final SwitchControlObjectEntity frontWindowHeaterAutomatic;

  private final Boolean isAirDistributionPresetCombined;

  private final SwitchIndicationObjectEntity premiumAirQualityAirFilter;

  private final SwitchIndicationObjectEntity premiumAirQualityFragrance;

  private final SwitchIndicationObjectEntity premiumAirQualityIonisation;

  private final SwitchControlObjectEntity steeringWheelHeaterCustomerActivation;

  private final SwitchControlObjectEntity steeringWheelHeaterCustomerAutomatic;

  private final SwitchControlObjectEntity steeringWheelHeaterIntelligentLevel;

  private final SwitchControlObjectEntity steeringWheelHeaterIntelligentUsage;

  private final SwitchControlObjectEntity steeringWheelHeaterLinkedToSeatHeater;

  private final SwitchIndicationObjectEntity steeringWheelHeaterState;

  private final ValueControlObjectEntity steeringWheelHeatingIntensity;

  private final List<VoiceControlDialogueObjectEntity> voiceControlDialogueElements;

  private final VcGeneralSettingObjectVoiceControlRestrictionReasonEnum
      voiceControlRestrictionReason;

  private final SwitchControlObjectEntity winterFunctionActivation;

  private final SwitchControlObjectEntity winterFunctionFWHActivation;

  private final SwitchControlObjectEntity winterFunctionMHActivation;

  private final SwitchControlObjectEntity winterFunctionRWHActivation;

  private final SwitchControlObjectEntity winterFunctionSHActivation;

  private final SwitchControlObjectEntity winterFunctionSWHActivation;

  private final FlavourCartridgeObjectEntity activeFlavour;

  private final SwitchControlObjectEntity airCirculationAutomatic;

  private final SwitchControlObjectEntity airCirculationManual;

  private final List<VcGeneralSettingObjectAirCleaningInformationEnum> airCleaningInformation;

  private final SwitchControlObjectEntity airCleaningIonisation;

  private final ValueIndicationObjectEntity airCleaningProgress;

  private final SwitchControlObjectEntity airCleaningState;

  private final SwitchControlObjectEntity behaviorMode;

  private final VcGeneralSettingObjectDriverSideEnum driverSide;

  private final SwitchControlObjectEntity ecoModeAutomatic;

  private final SwitchControlObjectEntity factoryReset;

  private final SwitchControlObjectEntity flavourActivation;

  private final SwitchControlObjectEntity frontWindowHeater;

  private final SwitchControlObjectEntity indirectVentilation;

  private final SwitchControlObjectEntity maxDefrost;

  private final SwitchIndicationObjectEntity performanceLimitation;

  private final SwitchControlObjectEntity premiumAirQuality;

  private final SwitchIndicationObjectEntity premiumAirQualityPcu;

  private final SwitchControlObjectEntity rearControlUnit;

  private final SwitchControlObjectEntity rearControlUnitThirdRow;

  private final SwitchControlObjectEntity rearWindowHeater;

  private final SwitchControlObjectEntity aC;

  private final SwitchControlObjectEntity aCEco;

  private final SwitchControlObjectEntity aCMax;

  private final SwitchControlObjectEntity autoAll;

  private final SwitchControlObjectEntity defrost;

  private final SwitchControlObjectEntity fanOnly;

  private final SwitchControlObjectEntity sync;

  public GeneralSettingObjectEntity(GeneralSettingObject object) {
    super(object);
    this.steeringWheelHeatingIntensityToggleButton =
        object
            .getSteeringWheelHeatingIntensityToggleButton()
            .map(SwitchControlObjectEntity::new)
            .orElse(null);
    this.airCirculationSensitivity =
        object.getAirCirculationSensitivity().map(ValueControlObjectEntity::new).orElse(null);
    this.airCirculationSmartSystem =
        object.getAirCirculationSmartSystem().map(SwitchControlObjectEntity::new).orElse(null);
    this.airDistributionPresetList =
        object
            .getAirDistributionPresetList()
            .map(
                list ->
                    list.stream()
                        .map(AirDistributionPresetObjectEntity::new)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.flavourCartridgeAvailability =
        object
            .getFlavourCartridgeAvailability()
            .map(
                list ->
                    list.stream()
                        .map(FlavourCartridgeObjectEntity::new)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.frontWindowHeaterAutomatic =
        object.getFrontWindowHeaterAutomatic().map(SwitchControlObjectEntity::new).orElse(null);
    this.isAirDistributionPresetCombined = object.getIsAirDistributionPresetCombined().orElse(null);
    this.premiumAirQualityAirFilter =
        object.getPremiumAirQualityAirFilter().map(SwitchIndicationObjectEntity::new).orElse(null);
    this.premiumAirQualityFragrance =
        object.getPremiumAirQualityFragrance().map(SwitchIndicationObjectEntity::new).orElse(null);
    this.premiumAirQualityIonisation =
        object.getPremiumAirQualityIonisation().map(SwitchIndicationObjectEntity::new).orElse(null);
    this.steeringWheelHeaterCustomerActivation =
        object
            .getSteeringWheelHeaterCustomerActivation()
            .map(SwitchControlObjectEntity::new)
            .orElse(null);
    this.steeringWheelHeaterCustomerAutomatic =
        object
            .getSteeringWheelHeaterCustomerAutomatic()
            .map(SwitchControlObjectEntity::new)
            .orElse(null);
    this.steeringWheelHeaterIntelligentLevel =
        object
            .getSteeringWheelHeaterIntelligentLevel()
            .map(SwitchControlObjectEntity::new)
            .orElse(null);
    this.steeringWheelHeaterIntelligentUsage =
        object
            .getSteeringWheelHeaterIntelligentUsage()
            .map(SwitchControlObjectEntity::new)
            .orElse(null);
    this.steeringWheelHeaterLinkedToSeatHeater =
        object
            .getSteeringWheelHeaterLinkedToSeatHeater()
            .map(SwitchControlObjectEntity::new)
            .orElse(null);
    this.steeringWheelHeaterState =
        object.getSteeringWheelHeaterState().map(SwitchIndicationObjectEntity::new).orElse(null);
    this.steeringWheelHeatingIntensity =
        object.getSteeringWheelHeatingIntensity().map(ValueControlObjectEntity::new).orElse(null);
    this.voiceControlDialogueElements =
        object
            .getVoiceControlDialogueElements()
            .map(
                list ->
                    list.stream()
                        .map(VoiceControlDialogueObjectEntity::new)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.voiceControlRestrictionReason =
        object
            .getVoiceControlRestrictionReason()
            .map(VcGeneralSettingObjectVoiceControlRestrictionReasonEnum::fromRSI)
            .orElse(null);
    this.winterFunctionActivation =
        object.getWinterFunctionActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.winterFunctionFWHActivation =
        object.getWinterFunctionFWHActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.winterFunctionMHActivation =
        object.getWinterFunctionMHActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.winterFunctionRWHActivation =
        object.getWinterFunctionRWHActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.winterFunctionSHActivation =
        object.getWinterFunctionSHActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.winterFunctionSWHActivation =
        object.getWinterFunctionSWHActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.activeFlavour =
        object.getActiveFlavour().map(FlavourCartridgeObjectEntity::new).orElse(null);
    this.airCirculationAutomatic =
        object.getAirCirculationAutomatic().map(SwitchControlObjectEntity::new).orElse(null);
    this.airCirculationManual =
        object.getAirCirculationManual().map(SwitchControlObjectEntity::new).orElse(null);
    this.airCleaningInformation =
        object
            .getAirCleaningInformation()
            .map(
                list ->
                    list.stream()
                        .map(VcGeneralSettingObjectAirCleaningInformationEnum::fromRSI)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.airCleaningIonisation =
        object.getAirCleaningIonisation().map(SwitchControlObjectEntity::new).orElse(null);
    this.airCleaningProgress =
        object.getAirCleaningProgress().map(ValueIndicationObjectEntity::new).orElse(null);
    this.airCleaningState =
        object.getAirCleaningState().map(SwitchControlObjectEntity::new).orElse(null);
    this.behaviorMode = object.getBehaviorMode().map(SwitchControlObjectEntity::new).orElse(null);
    this.driverSide =
        object.getDriverSide().map(VcGeneralSettingObjectDriverSideEnum::fromRSI).orElse(null);
    this.ecoModeAutomatic =
        object.getEcoModeAutomatic().map(SwitchControlObjectEntity::new).orElse(null);
    this.factoryReset = object.getFactoryReset().map(SwitchControlObjectEntity::new).orElse(null);
    this.flavourActivation =
        object.getFlavourActivation().map(SwitchControlObjectEntity::new).orElse(null);
    this.frontWindowHeater =
        object.getFrontWindowHeater().map(SwitchControlObjectEntity::new).orElse(null);
    this.indirectVentilation =
        object.getIndirectVentilation().map(SwitchControlObjectEntity::new).orElse(null);
    this.maxDefrost = object.getMaxDefrost().map(SwitchControlObjectEntity::new).orElse(null);
    this.performanceLimitation =
        object.getPerformanceLimitation().map(SwitchIndicationObjectEntity::new).orElse(null);
    this.premiumAirQuality =
        object.getPremiumAirQuality().map(SwitchControlObjectEntity::new).orElse(null);
    this.premiumAirQualityPcu =
        object.getPremiumAirQualityPcu().map(SwitchIndicationObjectEntity::new).orElse(null);
    this.rearControlUnit =
        object.getRearControlUnit().map(SwitchControlObjectEntity::new).orElse(null);
    this.rearControlUnitThirdRow =
        object.getRearControlUnitThirdRow().map(SwitchControlObjectEntity::new).orElse(null);
    this.rearWindowHeater =
        object.getRearWindowHeater().map(SwitchControlObjectEntity::new).orElse(null);
    this.aC = object.getAC().map(SwitchControlObjectEntity::new).orElse(null);
    this.aCEco = object.getACEco().map(SwitchControlObjectEntity::new).orElse(null);
    this.aCMax = object.getACMax().map(SwitchControlObjectEntity::new).orElse(null);
    this.autoAll = object.getAutoAll().map(SwitchControlObjectEntity::new).orElse(null);
    this.defrost = object.getDefrost().map(SwitchControlObjectEntity::new).orElse(null);
    this.fanOnly = object.getFanOnly().map(SwitchControlObjectEntity::new).orElse(null);
    this.sync = object.getSync().map(SwitchControlObjectEntity::new).orElse(null);
  }
}

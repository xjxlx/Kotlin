package com.android.hcp3.rsi.hvac.voicecontroldialogues;

import com.android.hcp3.BaseRSIValue;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.VoiceControlDialogueObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VoiceControlDialogueObjectEntity extends BaseRSIValue {
  private final List<VcVoiceControlDialogueObjectZoneEnum> zone;

  private final VcVoiceControlDialogueObjectClientActionEnum clientAction;

  private final Integer commandValue;

  private final VcVoiceControlDialogueObjectFunctionalFeedbackEnum functionalFeedback;

  private final VcVoiceControlDialogueObjectSwitchValueEnum switchValue;

  private final VcVoiceControlDialogueObjectCommandEnum command;

  public VoiceControlDialogueObjectEntity(VoiceControlDialogueObject object) {
    super(object);
    this.zone =
        object
            .getZone()
            .map(
                list ->
                    list.stream()
                        .map(VcVoiceControlDialogueObjectZoneEnum::fromRSI)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.clientAction =
        object
            .getClientAction()
            .map(VcVoiceControlDialogueObjectClientActionEnum::fromRSI)
            .orElse(null);
    this.commandValue = object.getCommandValue().orElse(null);
    this.functionalFeedback =
        object
            .getFunctionalFeedback()
            .map(VcVoiceControlDialogueObjectFunctionalFeedbackEnum::fromRSI)
            .orElse(null);
    this.switchValue =
        object
            .getSwitchValue()
            .map(VcVoiceControlDialogueObjectSwitchValueEnum::fromRSI)
            .orElse(null);
    this.command =
        object.getCommand().map(VcVoiceControlDialogueObjectCommandEnum::fromRSI).orElse(null);
  }
}

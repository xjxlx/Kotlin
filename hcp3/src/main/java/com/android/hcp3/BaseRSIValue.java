package com.android.hcp3;

import de.esolutions.fw.rudi.services.viwi.XObject;
import java.net.URI;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class BaseRSIValue {
  private final String name;
  @ToString.Exclude private final URI uri;
  @ToString.Exclude private final UUID id;

  protected BaseRSIValue(XObject object) {
    this.name = object.getName();
    this.uri = object.getUri();
    this.id = object.getId();
  }
}

package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.Nullable;
import de.esolutions.fw.rudi.services.RsiService;

public interface ServiceProvider<T extends RsiService> {
  @Nullable
  T getServiceBlocking();
}

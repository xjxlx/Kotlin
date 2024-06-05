package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.esolutions.fw.rudi.services.RsiService;
import java.util.List;
import technology.cariad.vehiclecontrolmanager.ValueCallback;

public abstract class BaseRSIResource<E extends BaseRSIValue> {

  protected BaseRSIResource(
      @Nullable RsiService service,
      @NonNull ServiceProvider<? extends RsiService> serviceProvider) {}

  public void registerValueCallback(@NonNull ValueCallback<List<E>> callback) {}

  public void unregisterValueCallback(@NonNull ValueCallback<List<E>> callback) {}

  @NonNull
  public List<E> getAllValueSync() {
    return null;
  }

  @Nullable
  public E getValueSync(@NonNull String name) {
    return null;
  }
}

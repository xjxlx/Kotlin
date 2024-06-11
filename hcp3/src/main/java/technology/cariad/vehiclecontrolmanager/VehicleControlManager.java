package technology.cariad.vehiclecontrolmanager;

import androidx.annotation.NonNull;
import de.esolutions.android.framework.clients.IAsiAdmin;

/** SDK manager */
public class VehicleControlManager {
  private final RSIManager rsiManager = new RSIManager();
  private IAsiAdmin asiAdmin;

  private VehicleControlManager() {}

  @NonNull
  public static VehicleControlManager getInstance() {
    return Holder.INSTANCE;
  }

  private static class Holder {
    private static final VehicleControlManager INSTANCE = new VehicleControlManager();

    private Holder() {}
  }
}

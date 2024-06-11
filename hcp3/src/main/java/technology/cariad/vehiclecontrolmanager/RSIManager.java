package technology.cariad.vehiclecontrolmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.esolutions.fw.android.rsi.client.rx.IRsiAdmin;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import technology.cariad.vehiclecontrolmanager.rsi.BaseRSIService;

class RSIManager {
  // managers
  private Map<Class<? extends BaseRSIService<?>>, BaseRSIService<?>> mRsiServices;
  @Nullable private IRsiAdmin rsiAdmin;

  protected RSIManager() {}

  private IRsiAdmin checkInit() {
    if (rsiAdmin == null) {
      throw new IllegalStateException("need call init() before");
    }
    return rsiAdmin;
  }

  @NonNull
  private <S extends BaseRSIService<?>> S getService(@NonNull Class<S> clazz) {
    if (mRsiServices.containsKey(clazz)) {
      return (S) mRsiServices.get(clazz);
    } else {
      try {
        Constructor<S> constructor = clazz.getDeclaredConstructor(IRsiAdmin.class);
        constructor.setAccessible(true);
        S manager = constructor.newInstance(rsiAdmin);
        mRsiServices.put(clazz, manager);
        return manager;
      } catch (NoSuchMethodException
          | InvocationTargetException
          | IllegalAccessException
          | InstantiationException e) {
        throw new RuntimeException(e);
      }
    }
  }

  //  public CarGlobalManager getCarGlobalManager() {
  //    checkInit();
  //    return getService(CarGlobalManager.class);
  //  }
}

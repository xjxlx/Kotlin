package technology.cariad.vehiclecontrolmanager;

import androidx.annotation.NonNull;

public interface ValueCallback<T> {
  void onValueChange(@NonNull T t);

  void onError(@NonNull Throwable throwable);
}

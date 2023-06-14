package composite;

import java.util.ArrayList;
import java.util.List;

public class CompositeSwitchable implements Switchable {
  private List<Switchable> switchables = new ArrayList<>();

  public void addSwitchable(Switchable s) {
    switchables.add(s):
  }

  public void turnOn() {
    for (var s : switchables)
      s.turnOn();
  }

  public void turnOff() {
    for (var s : switchables)
      s.turnOff();
  }
}

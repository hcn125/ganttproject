package net.sourceforge.ganttproject.resource;

public class GlobalResourceManager {

  static private GlobalResourceManager globalResourceManager;

  public static GlobalResourceManager getInstance() {
    if(globalResourceManager == null) {
      globalResourceManager = new GlobalResourceManager();
    }
    return globalResourceManager;
  }

  private GlobalResourceManager() {

  }
}

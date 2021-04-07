package net.sourceforge.ganttproject;

import biz.ganttproject.core.option.DefaultColorOption;
import net.sourceforge.ganttproject.gui.options.model.GP1XOptionConverter;

import java.awt.*;

class DefaultTaskColorOption extends DefaultColorOption implements GP1XOptionConverter {
  static public final Color DEFAULT_TASK_COLOR = new Color(140, 182, 206);

  DefaultTaskColorOption() {
    this(DEFAULT_TASK_COLOR);
  }

  DefaultTaskColorOption(Color defaultColor) {
    super("taskDefaultColor", defaultColor);
  }

  @Override
  public String getTagName() {
    return "colors";
  }

  @Override
  public String getAttributeName() {
    return "tasks";
  }

  @Override
  public void loadValue(String legacyValue) {
    loadPersistentValue(legacyValue);
    commit();
  }
}

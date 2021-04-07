package net.sourceforge.ganttproject.resource;

import biz.ganttproject.core.calendar.GanttDaysOff;
import net.sourceforge.ganttproject.CustomProperty;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyHolder;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.task.ResourceAssignment;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public interface HumanResource extends CustomPropertyHolder {
  void delete();

  void setId(int id);

  int getId();

  void setName(String name);

  String getName();

  void setDescription(String description);

  String getDescription();

  void setMail(String email);

  String getMail();

  void setPhone(String phone);

  String getPhone();

  void setRole(Role role);

  Role getRole();

  void addDaysOff(GanttDaysOff gdo);

  DefaultListModel<GanttDaysOff> getDaysOff();

  Object getCustomField(CustomPropertyDefinition def);

  void setCustomField(CustomPropertyDefinition def, Object value);

  ResourceAssignment createAssignment(ResourceAssignment assignmentToTask);

  ResourceAssignment[] getAssignments();

  HumanResource unpluggedClone();

  @Override
  List<CustomProperty> getCustomProperties();

  @Override
  CustomProperty addCustomProperty(CustomPropertyDefinition definition, String valueAsString);

  void resetLoads();

  LoadDistribution getLoadDistribution();

  void swapAssignments(ResourceAssignment a1, ResourceAssignment a2);

  void setStandardPayRate(BigDecimal rate);

  BigDecimal getStandardPayRate();

  double getTotalLoad();

  BigDecimal getTotalCost();

  @Override
  boolean equals(Object obj);

  @Override
  int hashCode();

  @Override
  String toString();
}

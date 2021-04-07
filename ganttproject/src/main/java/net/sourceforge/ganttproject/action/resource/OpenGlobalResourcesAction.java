package net.sourceforge.ganttproject.action.resource;

import biz.ganttproject.core.calendar.GPCalendarCalc;
import biz.ganttproject.core.time.TimeUnitStack;
import net.sourceforge.ganttproject.*;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.document.DocumentManager;
import net.sourceforge.ganttproject.gui.UIConfiguration;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.task.TaskManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class OpenGlobalResourcesAction extends ResourceAction {
  @org.jetbrains.annotations.NotNull
  private final GanttProject project;
  private final UIFacade uiFacade;

  public OpenGlobalResourcesAction(GanttProject project, UIFacade uiFacade) {
    super("View Global Resources", null);
    this.project = project;
    this.uiFacade = uiFacade;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ResourceTreeTableModel model = new ResourceTreeTableModel(project.getHumanResourceManager(),
      project.getTaskManager(), project.getResourceCustomPropertyManager());
    var resourceTreeTable = new ResourceTreeTable(project, model, uiFacade);
    resourceTreeTable.initTreeTable();
    JFrame globalResources = new JFrame("Global Resources");
    globalResources.setSize(700, 600);
    globalResources.setLocationRelativeTo(null);
    globalResources.add(resourceTreeTable);
    globalResources.setVisible(true);

  }

  class GlobalProject implements IGanttProject {

    @Override
    public String getProjectName() {
      return null;
    }

    @Override
    public void setProjectName(String projectName) {

    }

    @Override
    public String getDescription() {
      return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public String getOrganization() {
      return null;
    }

    @Override
    public void setOrganization(String organization) {

    }

    @Override
    public String getWebLink() {
      return null;
    }

    @Override
    public void setWebLink(String webLink) {

    }

    @Override
    public UIConfiguration getUIConfiguration() {
      return null;
    }

    @Override
    public HumanResourceManager getHumanResourceManager() {
      return null;
    }

    @Override
    public RoleManager getRoleManager() {
      return null;
    }

    @Override
    public TaskManager getTaskManager() {
      return null;
    }

    @Override
    public TaskContainmentHierarchyFacade getTaskContainment() {
      return null;
    }

    @Override
    public GPCalendarCalc getActiveCalendar() {
      return null;
    }

    @Override
    public TimeUnitStack getTimeUnitStack() {
      return null;
    }

    @Override
    public void setModified() {

    }

    @Override
    public void setModified(boolean modified) {

    }

    @Override
    public void close() {

    }

    @Override
    public Document getDocument() {
      return null;
    }

    @Override
    public void setDocument(Document document) {

    }

    @Override
    public DocumentManager getDocumentManager() {
      return null;
    }

    @Override
    public void addProjectEventListener(ProjectEventListener listener) {

    }

    @Override
    public void removeProjectEventListener(ProjectEventListener listener) {

    }

    @Override
    public boolean isModified() {
      return false;
    }

    @Override
    public void open(Document document) throws IOException, Document.DocumentException {

    }

    @Override
    public CustomPropertyManager getResourceCustomPropertyManager() {
      return null;
    }

    @Override
    public CustomPropertyManager getTaskCustomColumnManager() {
      return null;
    }

    @Override
    public List<GanttPreviousState> getBaselines() {
      return null;
    }
  }
}

package net.sourceforge.ganttproject.io;

import junit.framework.TestCase;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.OwnedHumanResource;
import net.sourceforge.ganttproject.roles.Role;
import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class TestResourceSaver extends TestCase {
  public void testResourceSaving() throws SAXException {
    // Arrange
    CustomPropertyManager customPropertyManager = mock(CustomPropertyManager.class);
    when(customPropertyManager.getDefinitions()).thenReturn(new ArrayList<>());

    HumanResourceManager manager = mock(HumanResourceManager.class);
    when(manager.getCustomPropertyManager()).thenReturn(customPropertyManager);

    var resources = new ArrayList<HumanResource>();

    var resource = new OwnedHumanResource("TEST_RESOURCE", 1, manager);

    var role = mock(Role.class);
    when(role.getPersistentID()).thenReturn("TEST_PERSISTENT_ID");
    resource.setRole(role);

    resources.add(resource);

    when(manager.getResources()).thenReturn(resources);
    IGanttProject project = mock(IGanttProject.class);
    when(project.getHumanResourceManager()).thenReturn(manager);

    var handler = mock(TransformerHandler.class);

    // Act
    new ResourceSaver().save(project, handler);

    // Assert
    verify(customPropertyManager, atLeast(1)).getDefinitions();
  }
}

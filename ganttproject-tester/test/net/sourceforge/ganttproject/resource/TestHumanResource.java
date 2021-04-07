package net.sourceforge.ganttproject.resource;

import junit.framework.TestCase;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import static org.mockito.Mockito.*;

public class TestHumanResource extends TestCase {
  public void testCopyConstructor() {
    var manager = new DefaultHumanResourceManager(null, new CustomColumnsManager());
    HumanResource humanResource = new OwnedHumanResource("TEST_NAME", 5, manager);
    humanResource.setDescription("TEST_DESCRIPTION");
    humanResource.setRole(mock(Role.class));
    var humanResourceCopy = humanResource.unpluggedClone();
    assertEquals(humanResource.getDescription(), humanResourceCopy.getDescription());
  }
}

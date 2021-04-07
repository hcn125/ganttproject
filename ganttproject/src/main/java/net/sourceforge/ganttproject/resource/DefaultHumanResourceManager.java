/*
GanttProject is an opensource project management tool.
Copyright (C) 2003-2010 Alexandre Thomas, Michael Barmeier, Dmitry Barashev

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.sourceforge.ganttproject.resource;

import biz.ganttproject.core.time.GanttCalendar;
import com.google.common.collect.Lists;
import net.sourceforge.ganttproject.CustomProperty;
import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.undo.GPUndoManager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author barmeier
 */
public class DefaultHumanResourceManager implements HumanResourceManager {

  private List<ResourceView> myViews = new ArrayList<ResourceView>();

  private List<HumanResource> resources = new ArrayList<HumanResource>();

  private int nextFreeId = 0;

  private final Role myDefaultRole;

  private final CustomPropertyManager myCustomPropertyManager;

  private final RoleManager myRoleManager;

  public DefaultHumanResourceManager(Role defaultRole, CustomPropertyManager customPropertyManager) {
    this(defaultRole, customPropertyManager, null);
  }

  public DefaultHumanResourceManager(Role defaultRole, CustomPropertyManager customPropertyManager, RoleManager roleManager) {
    myDefaultRole = defaultRole;
    myCustomPropertyManager = customPropertyManager;
    myRoleManager = roleManager;
  }

  @Override
  public HumanResource newHumanResource() {
    HumanResource result = new OwnedHumanResource("", -1, this);
    result.setRole(myDefaultRole);
    return result;
  }

  @Override
  public ResourceBuilder newResourceBuilder() {
    return new ResourceBuilder() {

      @Override
      public HumanResource build() {
        if (myName == null || myID == null) {
          return null;
        }
        HumanResource result = new OwnedHumanResource(myName, myID, DefaultHumanResourceManager.this);
        Role role = null;
        if (myRole != null && myRoleManager != null) {
          role = myRoleManager.getRole(myRole);
        }
        if (role == null) {
          role = myDefaultRole;
        }
        result.setRole(role);
        result.setPhone(myPhone);
        result.setMail(myEmail);
        result.setStandardPayRate(myStandardRate);
        add(result);
        return result;
      }

    };
  }
  @Override
  public HumanResource create(String name, int i) {
    HumanResource hr = new OwnedHumanResource(name, i, this);
    hr.setRole(myDefaultRole);
    add(hr);
    return hr;
  }

  @Override
  public void add(HumanResource resource) {
    if (resource.getId() == -1) {
      resource.setId(nextFreeId);
    }
    if (resource.getId() >= nextFreeId) {
      nextFreeId = resource.getId() + 1;
    }
    resources.add(resource);
    fireResourceAdded(resource);
  }

  @Override
  public HumanResource getById(int id) {
    // Linear search is not really efficient, but we do not have so many
    // resources !?
    HumanResource pr = null;
    for (int i = 0; i < resources.size(); i++)
      if (resources.get(i).getId() == id) {
        pr = resources.get(i);
        break;
      }
    return pr;
  }

  @Override
  public List<HumanResource> getResources() {
    return resources;
  }

  @Override
  public void remove(HumanResource resource) {
    fireResourcesRemoved(new HumanResource[] { resource });
    resources.remove(resource);
  }

  @Override
  public void remove(HumanResource resource, GPUndoManager myUndoManager) {
    final HumanResource res = resource;
    myUndoManager.undoableEdit("Delete Human OK", new Runnable() {
      @Override
      public void run() {
        fireResourcesRemoved(new HumanResource[] { res });
        resources.remove(res);
      }
    });
  }

  @Override
  public void save(OutputStream target) {
  }

  @Override
  public void clear() {
    fireCleanup();
    resources.clear();
  }

  @Override
  public void addView(ResourceView view) {
    myViews.add(view);
  }

  private void fireResourceAdded(HumanResource resource) {
    ResourceEvent e = new ResourceEvent(this, resource);
    for (Iterator<ResourceView> i = myViews.iterator(); i.hasNext();) {
      ResourceView nextView = i.next();
      nextView.resourceAdded(e);
    }
  }

  @Override
  public void fireResourceChanged(HumanResource resource) {
    ResourceEvent e = new ResourceEvent(this, resource);
    for (Iterator<ResourceView> i = myViews.iterator(); i.hasNext();) {
      ResourceView nextView = i.next();
      nextView.resourceChanged(e);
    }
  }

  private void fireResourcesRemoved(HumanResource[] resources) {
    ResourceEvent e = new ResourceEvent(this, resources);
    for (int i = 0; i < myViews.size(); i++) {
      ResourceView nextView = myViews.get(i);
      nextView.resourcesRemoved(e);
    }
  }

  @Override
  public void fireAssignmentsChanged(HumanResource resource) {
    ResourceEvent e = new ResourceEvent(this, resource);
    for (Iterator<ResourceView> i = myViews.iterator(); i.hasNext();) {
      ResourceView nextView = i.next();
      nextView.resourceAssignmentsChanged(e);
    }
  }

  private void fireCleanup() {
    fireResourcesRemoved(resources.toArray(new HumanResource[resources.size()]));
  }

  /** Move up the resource number index */
  @Override
  public void up(HumanResource hr) {
    int index = resources.indexOf(hr);
    assert index >= 0;
    resources.remove(index);
    resources.add(index - 1, hr);
    fireResourceChanged(hr);
  }

  /** Move down the resource number index */
  @Override
  public void down(HumanResource hr) {
    int index = resources.indexOf(hr);
    assert index >= 0;
    resources.remove(index);
    resources.add(index + 1, hr);
    fireResourceChanged(hr);

  }

  @Override
  public Map<HumanResource, HumanResource> importData(
    HumanResourceManager hrManager,
    HumanResourceMerger merger,
    Map<CustomPropertyDefinition, CustomPropertyDefinition> that2thisCustomDefs) {
    Map<HumanResource, HumanResource> foreign2native = new HashMap<HumanResource, HumanResource>();
    List<HumanResource> foreignResources = hrManager.getResources();
    List<HumanResource> createdResources = Lists.newArrayList();
    for (int i = 0; i < foreignResources.size(); i++) {
      HumanResource foreignHR = foreignResources.get(i);
      HumanResource nativeHR = merger.findNative(foreignHR, this);
      if (nativeHR == null) {
        nativeHR = new OwnedHumanResource(foreignHR.getName(), nextFreeId + createdResources.size(), this);
        nativeHR.setRole(myDefaultRole);
        createdResources.add(nativeHR);
      }
      foreign2native.put(foreignHR, nativeHR);

      for (CustomProperty foreignCP : foreignHR.getCustomProperties()) {
        CustomPropertyDefinition thisDef = that2thisCustomDefs.get(foreignCP.getDefinition());
        if (foreignCP.getValue() != null) {
          nativeHR.addCustomProperty(thisDef, foreignCP.getValueAsString());
        }
      }
    }
    for (HumanResource created : createdResources) {
      add(created);
    }
    merger.merge(foreign2native);
    return foreign2native;
  }

  @Override
  public CustomPropertyManager getCustomPropertyManager() {
    return myCustomPropertyManager;
  }

  static String getValueAsString(Object value) {
    final String result;
    if (value != null) {
      if (value instanceof GanttCalendar) {
        result = ((GanttCalendar) value).toXMLString();
      } else {
        result = String.valueOf(value);
      }
    } else {
      result = null;
    }
    return result;
  }
}

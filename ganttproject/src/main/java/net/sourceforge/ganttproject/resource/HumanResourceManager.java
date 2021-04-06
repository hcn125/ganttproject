/*
GanttProject is an opensource project management tool.
Copyright (C) 2003-2010 Alexandre Thomas, Michael Barmeier, Dmitry Barashev, Emile Dupont-Foisy

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

import net.sourceforge.ganttproject.CustomPropertyDefinition;
import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.undo.GPUndoManager;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface HumanResourceManager {
  HumanResource newHumanResource();

  ResourceBuilder newResourceBuilder();

  HumanResource create(String name, int i);

  void add(HumanResource resource);

  HumanResource getById(int id);

  List<HumanResource> getResources();

  void remove(HumanResource resource);

  void remove(HumanResource resource, GPUndoManager myUndoManager);

  void save(OutputStream target);

  void clear();

  void addView(ResourceView view);

  void fireResourceChanged(HumanResource resource);

  void fireAssignmentsChanged(HumanResource resource);

  void up(HumanResource hr);

  void down(HumanResource hr);

  Map<HumanResource, HumanResource> importData(
    HumanResourceManager hrManager,
    HumanResourceMerger merger,
    Map<CustomPropertyDefinition, CustomPropertyDefinition> that2thisCustomDefs);

  CustomPropertyManager getCustomPropertyManager();
}
